package ensicaen.fr.marierave.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class SkillCommentDAO extends DAOBase
{
	private static final String TABLE_NAME = "tableChildSkillComment";
	private static final String CHILDID = "ChildId";
	private static final String SKILLCODE = "SkillCode";
	private static final String COMMENT = "Comment";
	
	public SkillCommentDAO(Context context)
	{
		super(context);
	}
	
	public void addSkillComment(Integer childId, String skillCode, String comment)
	{
		ContentValues values = new ContentValues();
		values.put(CHILDID, childId);
		values.put(SKILLCODE, skillCode);
		values.put(COMMENT, comment);
		
		this.database.insert(TABLE_NAME, null, values);
	}
	
	public String getSkillcomment(Integer childId, String skillCode)
	{
		Cursor cursor = this.database
				.query(TABLE_NAME, new String[]{COMMENT}, CHILDID + " = ? AND " + SKILLCODE + " = ?", new String[]{childId.toString(), skillCode}, null, null, null,
						null);
		
		if (cursor != null) {
			if (cursor.getCount() == 0) {
				return "";
			}
			cursor.moveToFirst();
		}
		
		String comment = cursor.getString(0);
		
		cursor.close();
		
		return comment;
	}
	
	public boolean skillCommentExists(Integer childId, String skillCode)
	{
		Cursor cursor = this.database
				.query(TABLE_NAME, new String[]{COMMENT}, CHILDID + " = ? AND " + SKILLCODE + " = ?", new String[]{childId.toString(), skillCode}, null, null, null,
						null);
		
		if (cursor == null || cursor.getCount() == 0) {
			return false;
		}
		
		cursor.close();
		return true;
	}
	
	public void deleteSkillcomment(Integer childId, String skillCode)
	{
		this.database.delete(TABLE_NAME, CHILDID + " = ? AND " + SKILLCODE + " = ?", new String[]{childId.toString(), skillCode});
	}
	
	public void updateSkillcomment(Integer childId, String skillCode, String comment)
	{
		ContentValues values = new ContentValues();
		values.put(CHILDID, childId);
		values.put(SKILLCODE, skillCode);
		values.put(COMMENT, comment);
		
		this.database.update(TABLE_NAME, values, CHILDID + " = ? AND " + SKILLCODE + " = ?", new String[]{childId.toString(), skillCode});
	}
	
	public int getSkillcommentCount()
	{
		String countQuery = "SELECT * FROM " + TABLE_NAME;
		Cursor cursor = this.database.rawQuery(countQuery, null);
		int res = cursor.getCount();
		
		cursor.close();
		
		return res;
	}
}
