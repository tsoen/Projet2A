package ensicaen.fr.marierave.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Model.Skill;

public class SkillDAO extends DAOBase {
	
	private static final String TABLE_NAME = "tableSkill";
    private static final String CODE = "Code";
    private static final String NAME = "Name";
	private static final String SKILLHEADER = "Skillheader";

    public SkillDAO(Context context)
    {
        super(context);
    }
    
	public void addSkill(Skill skill) {
		if (skillExists(skill.getCode())) {
			return;
		}
    	
        ContentValues values = new ContentValues();
		values.put(CODE, skill.getCode());
		values.put(NAME, skill.getName());
		values.put(SKILLHEADER, skill.getSkillheader());
		
		_database.insert(TABLE_NAME, null, values);
    }
	
	public boolean skillExists(String code)
	{
		Cursor cursor = _database.query(TABLE_NAME, new String[]{CODE, NAME, SKILLHEADER}, CODE + " = ?", new String[]{code}, null, null, null, null);
		
		if (cursor == null || cursor.getCount() == 0) {
			return false;
		}
		
		cursor.close();
		return true;
	}
 
	public Skill getSkill(String code) {
		Cursor cursor = _database.query(TABLE_NAME, new String[]{CODE, NAME, SKILLHEADER},
				CODE + " = ?", new String[] { code }, null, null, null, null);
		
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		Skill skill = new Skill(cursor.getString(0), cursor.getString(1), cursor.getString(2));
		
		cursor.close();
		
		return skill;
	}
	
	public List<Skill> getAllSkills() {
		List<Skill> skillList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		Cursor cursor = _database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Skill skill = new Skill(cursor.getString(0), cursor.getString(1), cursor.getString(2));
				
				skillList.add(skill);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return skillList;
	}
	
	public List<Skill> getSkillsInHeader(String skillheaderName)
	{
		List<Skill> skillList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + SKILLHEADER + " = '" + skillheaderName + "'";
		
		Cursor cursor = _database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Skill skill = new Skill(cursor.getString(0), cursor.getString(1), cursor.getString(2));
				
				skillList.add(skill);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return skillList;
	}
	
	public void deleteSkill(String code) {
		_database.delete(TABLE_NAME, CODE + " = ?", new String[]{code});
    }

	public void deleteAllSkillsInHeader(String skillheaderName) {
		_database.delete(TABLE_NAME, SKILLHEADER + " = ?", new String[]{skillheaderName});
	}
	
	public void updateSkill(String oldSkillCode, Skill skill)
	{
		if (skillExists(skill.getCode())) {
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put(CODE, skill.getCode());
		values.put(NAME, skill.getName());
		values.put(SKILLHEADER, skill.getSkillheader());
		
		_database.update(TABLE_NAME, values, CODE + " = ?", new String[]{oldSkillCode});
    }
    
	public int getSkillCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
		Cursor cursor = _database.rawQuery(countQuery, null);
		int res =  cursor.getCount();
		
		cursor.close();
		
		return res;
    }
}
