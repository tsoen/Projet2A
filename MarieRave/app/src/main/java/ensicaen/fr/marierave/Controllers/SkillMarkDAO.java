package ensicaen.fr.marierave.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class SkillMarkDAO extends DAOBase
{
	
	private static final String TABLE_NAME = "tableChildSkillMark";
	private static final String CHILDID = "ChildId";
	private static final String SKILLCODE = "SkillCode";
	private static final String MARK = "Mark";
	
	public SkillMarkDAO(Context context)
	{
		super(context);
	}
	
	public void addSkillMark(Integer childId, String skillCode, String mark)
	{
		ContentValues values = new ContentValues();
		values.put(CHILDID, childId);
		values.put(SKILLCODE, skillCode);
		values.put(MARK, mark);
		
		_database.insert(TABLE_NAME, null, values);
	}
	
	public String getSkillMark(Integer childId, String skillCode)
	{
		Cursor cursor = _database
				.query(TABLE_NAME, new String[]{MARK}, CHILDID + " = ? AND " + SKILLCODE + " = ?", new String[]{childId.toString(), skillCode}, null, null, null, null);
		
		if (cursor != null) {
			if (cursor.getCount() == 0) {
				return "";
			}
			cursor.moveToFirst();
		}
		
		String mark = cursor.getString(0);
		
		cursor.close();
		
		return mark;
	}
	
	public boolean skillMarkExists(Integer childId, String skillCode)
	{
		Cursor cursor = _database
				.query(TABLE_NAME, new String[]{MARK}, CHILDID + " = ? AND " + SKILLCODE + " = ?", new String[]{childId.toString(), skillCode}, null, null, null, null);
		
		if (cursor == null || cursor.getCount() == 0) {
			return false;
		}
		
		cursor.close();
		return true;
	}
	
	public void deleteSkillMark(Integer childId, String skillCode)
	{
		_database.delete(TABLE_NAME, CHILDID + " = ? AND " + SKILLCODE + " = ?", new String[]{childId.toString(), skillCode});
	}
	
	public void updateSkillMark(Integer childId, String skillCode, String mark)
	{
		ContentValues values = new ContentValues();
		values.put(CHILDID, childId);
		values.put(SKILLCODE, skillCode);
		values.put(MARK, mark);
		
		_database.update(TABLE_NAME, values, CHILDID + " = ? AND " + SKILLCODE + " = ?", new String[]{childId.toString(), skillCode});
	}
	
	public int getSkillMarkCount()
	{
		String countQuery = "SELECT * FROM " + TABLE_NAME;
		Cursor cursor = _database.rawQuery(countQuery, null);
		int res = cursor.getCount();
		
		cursor.close();
		
		return res;
	}
}
