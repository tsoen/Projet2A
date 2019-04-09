package ensicaen.fr.marierave.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Model.Skillheader;

public class SkillheaderDAO extends DAOBase
{
	
	private static final String TABLE_NAME = "tableSkillHeader";
	private static final String NAME = "Name";
	private static final String SUBJECT = "Subject";
	
	public SkillheaderDAO(Context context)
	{
		super(context);
	}
	
	public void addSkillheader(Skillheader skillheader)
	{
		if (skillheaderExists(skillheader.getName())) {
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put(NAME, skillheader.getName());
		values.put(SUBJECT, skillheader.getSubject());
		
		_database.insert(TABLE_NAME, null, values);
	}
	
	public boolean skillheaderExists(String name)
	{
		Cursor cursor = _database.query(TABLE_NAME, new String[]{NAME, SUBJECT}, NAME + " = ?", new String[]{name}, null, null, null, null);
		
		if (cursor == null || cursor.getCount() == 0) {
			return false;
		}
		
		cursor.close();
		return true;
	}
	
	public Skillheader getSkillheader(String name)
	{
		Cursor cursor = _database.query(TABLE_NAME, new String[]{NAME, SUBJECT}, NAME + " = ?", new String[]{name}, null, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		Skillheader skillheader = new Skillheader(cursor.getString(0), cursor.getString(1));
		
		cursor.close();
		
		return skillheader;
	}
	
	public List<Skillheader> getAllSkillheaders()
	{
		List<Skillheader> skillheaderList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		Cursor cursor = _database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Skillheader skillheader = new Skillheader(cursor.getString(0), cursor.getString(1));
				
				skillheaderList.add(skillheader);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return skillheaderList;
	}
	
	public List<Skillheader> getSkillheadersInSubject(String subjectName)
	{
		List<Skillheader> skillheaderList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + SUBJECT + " = '" + subjectName + "'";
		
		Cursor cursor = _database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Skillheader skillheader = new Skillheader(cursor.getString(0), cursor.getString(1));
				
				skillheaderList.add(skillheader);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return skillheaderList;
	}
	
	public void deleteSkillheader(String name)
	{
		_database.delete(TABLE_NAME, NAME + " = ?", new String[]{name});
	}

	public void deleteAllSkillheadersInSubject(String subjectName)
	{
		_database.delete(TABLE_NAME, SUBJECT + " = ?", new String[]{subjectName});
	}
	
	public void updateSkillheader(String oldSkillheaderName, Skillheader skillheader)
	{
		if (skillheaderExists(skillheader.getName())) {
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put(NAME, skillheader.getName());
		values.put(SUBJECT, skillheader.getSubject());
		
		_database.update(TABLE_NAME, values, NAME + " = ?", new String[]{oldSkillheaderName});
	}
	
	public int getSkillheaderCount()
	{
		String countQuery = "SELECT * FROM " + TABLE_NAME;
		Cursor cursor = _database.rawQuery(countQuery, null);
		int res = cursor.getCount();
		
		cursor.close();
		
		return res;
	}
}