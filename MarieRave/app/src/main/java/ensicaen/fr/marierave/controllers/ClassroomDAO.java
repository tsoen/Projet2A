package ensicaen.fr.marierave.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Classroom;


public class ClassroomDAO extends DAOBase {
	
	private static final String TABLE_NAME = "tableClassroom";
    private static final String ID = "Id";
    private static final String NAME = "Name";
    
    public ClassroomDAO(Context context)
    {
        super(context);
    }
    
	public void addChild(Classroom classroom) {
        ContentValues values = new ContentValues();
		values.put(ID, classroom.getId());
		values.put(NAME, classroom.getName());
		
        this.database.insert(TABLE_NAME, null, values);
    }
    
	public Classroom getClassroom(long id) {
		Cursor cursor =  this.database.query(TABLE_NAME, new String[] { ID, NAME },
				ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		//TODO test get(columnName) instead of columnIndex
		Classroom classroom = new Classroom(cursor.getInt(0), cursor.getString(1));
		
		cursor.close();
		
		return classroom;
	}
	
	public List<Classroom> getAllClassrooms() {
		List<Classroom> classroomList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		Cursor cursor =  this.database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Classroom classroom = new Classroom(cursor.getInt(0), cursor.getString(1));
				
				classroomList.add(classroom);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return classroomList;
	}
	
	public void deleteClassroom(long id) {
		this.database.delete(TABLE_NAME, ID + " = ?", new String[] {String.valueOf(id)});
    }
    
	public void updateChild(Classroom classroom) {
		ContentValues values = new ContentValues();
		values.put(ID, classroom.getId());
		values.put(NAME, classroom.getName());
		
		this.database.update(TABLE_NAME, values, ID  + " = ?", new String[] {String.valueOf(classroom.getId())} );
    }
    
	public int getClassroomCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor =  this.database.rawQuery(countQuery, null);
		int res =  cursor.getCount();
		
		cursor.close();
		
		return res;
    }
}

