package ensicaen.fr.marierave.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Teacher;

public class TeacherDAO extends DAOBase {
	
	private static final String TABLE_NAME = "tableTeacher";
    private static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String FIRSTNAME = "Firstname";

    public TeacherDAO(Context context)
    {
        super(context);
    }
    
	public void addTeacher(Teacher teacher) {
        ContentValues values = new ContentValues();
		values.put(ID, teacher.getId());
		values.put(NAME, teacher.getName());
        values.put(FIRSTNAME, teacher.getFirstname());
		
        this.database.insert(TABLE_NAME, null, values);
    }
    
	public Teacher getTeacher(long id) {
		Cursor cursor =  this.database.query(TABLE_NAME, new String[] { ID, NAME, FIRSTNAME },
				ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		Teacher teacher = new Teacher(cursor.getString(1), cursor.getString(2));
		
		cursor.close();
		
		return teacher;
	}
	
	public List<Teacher> getAllTeachers() {
		List<Teacher> teacherList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		Cursor cursor =  this.database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Teacher teacher = new Teacher(cursor.getString(1), cursor.getString(2));
				
				teacherList.add(teacher);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return teacherList;
	}
	
	public void deleteTeacher(long id) {
		this.database.delete(TABLE_NAME, ID + " = ?", new String[] {String.valueOf(id)});
    }
    
	public void updateTeacher(Teacher teacher) {
		ContentValues values = new ContentValues();
		values.put(ID, teacher.getId());
		values.put(NAME, teacher.getName());
		values.put(FIRSTNAME, teacher.getFirstname());
		
		this.database.update(TABLE_NAME, values, ID  + " = ?", new String[] {String.valueOf(teacher.getId())} );
    }
    
	public int getTeacherCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor =  this.database.rawQuery(countQuery, null);
		int res =  cursor.getCount();
		
		cursor.close();
		
		return res;
    }
    
    public int getNextId(){
		String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ID + " DESC LIMIT 1;";
		Cursor cursor =  this.database.rawQuery(query, null);
		
		cursor.moveToFirst();
		
		int next = cursor.getInt(0);
		
		cursor.close();
		
		return next;
	}
}

