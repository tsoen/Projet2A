package ensicaen.fr.marierave.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Model.Child;


public class ChildDAO extends DAOBase {
	
	private static final String TABLE_NAME = "tableChild";
    private static final String ID = "Id";
    private static final String NAME = "Name";
    private static final String FIRSTNAME = "Firstname";
    private static final String CLASSROOM = "Classroom";

    public ChildDAO(Context context)
    {
        super(context);
    }
    
	public void addChild(Child child) {
        ContentValues values = new ContentValues();
		values.put(ID, getNextId());
		values.put(NAME, child.getName());
        values.put(FIRSTNAME, child.getFirstname());
        values.put(CLASSROOM, child.getClassroom());
		
		_database.insert(TABLE_NAME, null, values);
    }
    
	public Child getChild(long id) {
		Cursor cursor = _database.query(TABLE_NAME, new String[]{ID, NAME, FIRSTNAME, CLASSROOM},
				ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		Child child = new Child(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
		
		cursor.close();
		
		return child;
	}
	
	public List<Child> getAllChilds() {
		List<Child> childList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		Cursor cursor = _database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Child child = new Child(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
				
				childList.add(child);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return childList;
	}
	
	public List<Child> getAllChildsInClassroom(String classroomName)
	{
		List<Child> childList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + CLASSROOM + " = '" + classroomName + "'";
		
		Cursor cursor = _database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Child child = new Child(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
				childList.add(child);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return childList;
	}
	
	public List<Child> getAllChildsNotInClassroom(String classroomName)
	{
		List<Child> childList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + CLASSROOM + " != '" + classroomName + "'";
		
		Cursor cursor = _database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Child child = new Child(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
				
				childList.add(child);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return childList;
	}
	
	public void deleteChild(long id) {
		_database.delete(TABLE_NAME, ID + " = ?", new String[]{String.valueOf(id)});
    }
    
	public void updateChild(Child child) {
		ContentValues values = new ContentValues();
		values.put(ID, child.getId());
		values.put(NAME, child.getName());
		values.put(FIRSTNAME, child.getFirstname());
		values.put(CLASSROOM, child.getClassroom());
		
		_database.update(TABLE_NAME, values, ID + " = ?", new String[]{String.valueOf(child.getId())});
    }
    
	public int getChildCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
		Cursor cursor = _database.rawQuery(countQuery, null);
		int res =  cursor.getCount();
		
		cursor.close();
		
		return res;
    }
    
    public int getNextId(){
		String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ID + " DESC LIMIT 1;";
		Cursor cursor = _database.rawQuery(query, null);
		
		cursor.moveToFirst();
	
		int next = 1;
	
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			next = cursor.getInt(0);
		}
	
		cursor.close();
	
		return next + 1;
	}
}

