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
		ContentValues values = new ContentValues();
		values.put(NAME, skillheader.getName());
		values.put(SUBJECT, skillheader.getSubject());
		
		this.database.insert(TABLE_NAME, null, values);
	}
	
	public Skillheader getSkillheader(String name)
	{
		Cursor cursor = this.database.query(TABLE_NAME, new String[]{NAME, SUBJECT}, NAME + " = ?", new String[]{name}, null, null, null, null);
		
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
		
		Cursor cursor = this.database.rawQuery(selectQuery, null);
		
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
		
		Cursor cursor = this.database.rawQuery(selectQuery, null);
		
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
		this.database.delete(TABLE_NAME, NAME + " = ?", new String[]{name});
	}
	
	public void updateSkillheader(Skillheader skillheader)
	{
		ContentValues values = new ContentValues();
		values.put(NAME, skillheader.getName());
		values.put(SUBJECT, skillheader.getSubject());
		
		this.database.update(TABLE_NAME, values, NAME + " = ?", new String[]{skillheader.getName()});
	}
	
	public int getSkillheaderCount()
	{
		String countQuery = "SELECT * FROM " + TABLE_NAME;
		Cursor cursor = this.database.rawQuery(countQuery, null);
		int res = cursor.getCount();
		
		cursor.close();
		
		return res;
	}
}