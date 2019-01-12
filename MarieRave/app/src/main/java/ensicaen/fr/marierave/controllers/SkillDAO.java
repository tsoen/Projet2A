package ensicaen.fr.marierave.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ensicaen.fr.marierave.Model.Child;
import ensicaen.fr.marierave.Model.Skill;


public class SkillDAO extends DAOBase {
	
	private static final String TABLE_NAME = "tableSkill";
    private static final String CODE = "Code";
    private static final String NAME = "Name";

    public SkillDAO(Context context)
    {
        super(context);
    }
    
	public void addSkill(Skill skill) {
        ContentValues values = new ContentValues();
		values.put(CODE, skill.getCode());
		values.put(NAME, skill.getName());
		
        this.database.insert(TABLE_NAME, null, values);
    }
    
	public Skill getSkill(String code) {
		Cursor cursor =  this.database.query(TABLE_NAME, new String[] { CODE, NAME },
				CODE + " = ?", new String[] { code }, null, null, null, null);
		
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		Skill skill = new Skill(cursor.getString(0), cursor.getString(1), "Res");
		
		cursor.close();
		
		return skill;
	}
	
	public List<Skill> getAllSkills() {
		List<Skill> skillList = new ArrayList<>();
		
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		
		Cursor cursor =  this.database.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Skill skill = new Skill(cursor.getString(0), cursor.getString(1), "Res");
				
				skillList.add(skill);
			} while (cursor.moveToNext());
		}
		
		cursor.close();
		
		return skillList;
	}
	
	public void deleteSkill(String code) {
		this.database.delete(TABLE_NAME, CODE + " = ?", new String[] { code });
    }
    
	public void updateSkill(Skill skill) {
		ContentValues values = new ContentValues();
		values.put(CODE, skill.getCode());
		values.put(NAME, skill.getName());
		
		this.database.update(TABLE_NAME, values, CODE  + " = ?", new String[] { skill.getCode() } );
    }
    
	public int getSkillCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor =  this.database.rawQuery(countQuery, null);
		int res =  cursor.getCount();
		
		cursor.close();
		
		return res;
    }
}
