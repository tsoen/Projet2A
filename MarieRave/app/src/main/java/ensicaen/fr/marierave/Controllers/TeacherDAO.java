package ensicaen.fr.marierave.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Model.Teacher;

public class TeacherDAO extends DAOBase {
	
	private static final String TABLE_NAME = "tableTeacher";
    private static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String FIRSTNAME = "Firstname";
	private static final String IDCONNECTION = "IdConnection";
	private static final String PASSWORD = "Password";
	
	private Context _context;
    
    public TeacherDAO(Context context)
    {
        super(context);
		_context = context;
    }
    
	public void addTeacher(Teacher teacher) {
        ContentValues values = new ContentValues();
		int nextId = getNextId();
		values.put(ID, nextId);
		values.put(NAME, teacher.getName());
        values.put(FIRSTNAME, teacher.getFirstname());
		if (teacher.getName().equals("admin") && teacher.getFirstname().equals("admin")) {
			values.put(IDCONNECTION, teacher.getName());
			values.put(PASSWORD, teacher.getName());
		}
		else {
			values.put(IDCONNECTION, teacher.getName() + nextId);
			values.put(PASSWORD, teacher.getName() + nextId);
		}
		
		_database.insert(TABLE_NAME, null, values);
		
		new TeacherClassroomDAO(_context).addTeacherToClassroom("Ecole", nextId);
	}
    
	public Teacher getTeacher(long id) {
		Cursor cursor = _database.query(TABLE_NAME, new String[]{ID, NAME, FIRSTNAME, IDCONNECTION, PASSWORD},
				ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		Teacher teacher = new Teacher(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
		
		cursor.close();
		
		return teacher;
	}
	
	public Teacher getTeacher(String idConnection)
	{
		Cursor cursor = _database
				.query(TABLE_NAME, new String[]{ID, NAME, FIRSTNAME, IDCONNECTION, PASSWORD}, IDCONNECTION + " = ?", new String[]{idConnection}, null, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		Teacher teacher = new Teacher(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
		
		cursor.close();
		
		return teacher;
	}
	
	public List<Teacher> getAllTeachers() {
		List<Teacher> teacherList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		Cursor cursor = _database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Teacher teacher = new Teacher(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
				
				teacherList.add(teacher);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return teacherList;
	}
	
	public void deleteTeacher(long id) {
		_database.delete(TABLE_NAME, ID + " = ?", new String[]{String.valueOf(id)});
    }
    
	public void updateTeacher(Teacher teacher) {
		ContentValues values = new ContentValues();
		values.put(ID, teacher.getId());
		values.put(NAME, teacher.getName());
		values.put(FIRSTNAME, teacher.getFirstname());
		values.put(IDCONNECTION, teacher.getIdConnection());
		values.put(PASSWORD, teacher.getPassword());
		
		_database.update(TABLE_NAME, values, ID + " = ?", new String[]{String.valueOf(teacher.getId())});
    }
    
	public int getTeacherCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
		Cursor cursor = _database.rawQuery(countQuery, null);
		int res =  cursor.getCount();
		
		cursor.close();
		
		return res;
    }
	
	public int getNextId(){
		return getLastId() + 1;
	}
	
	public int getLastId()
	{
		String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ID + " DESC LIMIT 1;";
		Cursor cursor = _database.rawQuery(query, null);
		
		int next = 0;
		
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			next = cursor.getInt(0);
		}
		
		cursor.close();
		
		return next;
	}
	
	public boolean teacherExists(String idConnection, String password)
	{
		Cursor cursor = _database
				.query(TABLE_NAME, new String[]{ID}, IDCONNECTION + " = ? AND " + PASSWORD + " = ?", new String[]{idConnection, password}, null, null, null, null);
		
		if (cursor == null || cursor.getCount() == 0) {
			return false;
		}
		
		cursor.close();
		return true;
	}
}

