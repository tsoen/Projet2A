package ensicaen.fr.marierave.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Model.Subject;

public class SubjectDAO extends DAOBase
{
	
	private static final String TABLE_NAME = "tableSubject";
	private static final String NAME = "Name";
	
	public SubjectDAO(Context context)
	{
		super(context);
	}
	
	public void addSubject(Subject subject)
	{
		ContentValues values = new ContentValues();
		values.put(NAME, subject.getName());
		
		this.database.insert(TABLE_NAME, null, values);
	}
	
	public boolean subjectExists(String name)
	{
		Cursor cursor = this.database.query(TABLE_NAME, new String[]{NAME}, NAME + " = ?", new String[]{name}, null, null, null, null);
		
		if (cursor == null || cursor.getCount() == 0) {
			return false;
		}
		
		cursor.close();
		return true;
	}
	
	public Subject getSubject(String name)
	{
		Cursor cursor = this.database.query(TABLE_NAME, new String[]{NAME}, NAME + " = ?", new String[]{name}, null, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		Subject subject = new Subject(cursor.getString(0));
		
		cursor.close();
		
		return subject;
	}
	
	public List<Subject> getAllSubjects()
	{
		List<Subject> subjectList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		Cursor cursor = this.database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Subject subject = new Subject(cursor.getString(0));
				
				subjectList.add(subject);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return subjectList;
	}
	
	public void deleteSubject(String name)
	{
		this.database.delete(TABLE_NAME, NAME + " = ?", new String[]{name});
	}
	
	public void updateSubject(String oldSubjectName, Subject subject)
	{
		ContentValues values = new ContentValues();
		values.put(NAME, subject.getName());
		
		this.database.update(TABLE_NAME, values, NAME + " = ?", new String[]{oldSubjectName});
	}
	
	public int getSubjectCount()
	{
		String countQuery = "SELECT * FROM " + TABLE_NAME;
		Cursor cursor = this.database.rawQuery(countQuery, null);
		int res = cursor.getCount();
		
		cursor.close();
		
		return res;
	}
}
