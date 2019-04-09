package ensicaen.fr.marierave.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Model.Classroom;


public class ClassroomDAO extends DAOBase {
	
	private static final String TABLE_NAME = "tableClassroom";
    private static final String NAME = "Name";
    
    public ClassroomDAO(Context context)
    {
        super(context);
    }
    
	public void addClassroom(Classroom classroom) {
		if (classroomExists(classroom.getName())) {
			return;
		}
    	
        ContentValues values = new ContentValues();
		values.put(NAME, classroom.getName());
		
		_database.insert(TABLE_NAME, null, values);
    }
    
	public Classroom getClassroom(String name) {
		Cursor cursor = _database.query(TABLE_NAME, new String[]{NAME},
				NAME + " = ?", new String[] { name }, null, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		Classroom classroom = new Classroom(cursor.getString(0));
		
		cursor.close();
		
		return classroom;
	}
	
	public List<Classroom> getAllClassrooms() {
		List<Classroom> classroomList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		Cursor cursor = _database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				if (!cursor.getString(0).equals("Ecole")) {
					Classroom classroom = new Classroom(cursor.getString(0));
					classroomList.add(classroom);
				}
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return classroomList;
	}
	
	public void deleteClassroom(String name) {
		_database.delete(TABLE_NAME, NAME + " = ?", new String[]{name});
    }
    
	public void updateClassroom(Classroom classroom) {
		if (classroomExists(classroom.getName())) {
			return;
		}
  
		ContentValues values = new ContentValues();
		values.put(NAME, classroom.getName());
		
		_database.update(TABLE_NAME, values, NAME + " = ?", new String[]{classroom.getName()});
    }
    
	public int getClassroomCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
		Cursor cursor = _database.rawQuery(countQuery, null);
		int res =  cursor.getCount();
		
		cursor.close();
		
		return res;
    }
	
	public boolean classroomExists(String name)
	{
		Cursor cursor = _database.query(TABLE_NAME, new String[]{NAME}, NAME + " = ?", new String[]{name}, null, null, null, null);
		
		boolean exists = cursor.getCount() != 0;
		
		cursor.close();
		
		return exists;
	}
}

