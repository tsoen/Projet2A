package ensicaen.fr.marierave.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class TeacherClassroomDAO extends DAOBase
{
	private static final String TABLE_NAME = "tableTeacherClassroom";
	private static final String CLASSROOMNAME = "ClassroomName";
	private static final String TEACHERID = "TeacherId";
	
	public TeacherClassroomDAO(Context context)
	{
		super(context);
	}
	
	public void addTeacherToClassroom(String classroomName, Integer teacherId)
	{
		ContentValues values = new ContentValues();
		values.put(CLASSROOMNAME, classroomName);
		values.put(TEACHERID, teacherId);
		
		this.database.insert(TABLE_NAME, null, values);
	}
	
	public List<Integer> getTeachersIdInClassroom(String classroomName)
	{
		Cursor cursor = this.database.query(true, TABLE_NAME, new String[]{TEACHERID}, CLASSROOMNAME + " = ?", new String[]{classroomName}, null, null, null, null);
		
		List<Integer> teachersIdList = new ArrayList<>();
		
		if (cursor != null) {
			if (cursor.getCount() == 0) {
				cursor.close();
				return teachersIdList;
			}
			cursor.moveToFirst();
			
			do {
				teachersIdList.add(cursor.getInt(0));
			} while (cursor.moveToNext());
			
			cursor.close();
		}
		
		return teachersIdList;
	}
	
	public List<Integer> getTeachersIdNotInClassroom(String classroomName)
	{
		String countQuery = "SELECT DISTINCT " + TEACHERID + " FROM " + TABLE_NAME + " WHERE " + TEACHERID + " NOT IN (SELECT " + TEACHERID + " FROM " + TABLE_NAME + " "
				+ "WHERE " + CLASSROOMNAME + " = '" + classroomName + "' )";
		Cursor cursor = this.database.rawQuery(countQuery, null);
		
		List<Integer> teachersIdList = new ArrayList<>();
		
		if (cursor != null) {
			if (cursor.getCount() == 0) {
				cursor.close();
				return teachersIdList;
			}
			cursor.moveToFirst();
			
			do {
				teachersIdList.add(cursor.getInt(0));
			} while (cursor.moveToNext());
			
			cursor.close();
		}
		
		return teachersIdList;
	}

	public List<String> getClassroomsWithThisTeacher(Integer teacherId)
	{
		Cursor cursor = this.database.query(TABLE_NAME, new String[]{CLASSROOMNAME}, TEACHERID + " = ?", new String[]{teacherId.toString()}, null, null, null, null);
		
		List<String> classroomList = new ArrayList<>();
		
		if (cursor != null) {
			if (cursor.getCount() == 0) {
				cursor.close();
				return classroomList;
			}
			cursor.moveToFirst();
			
			do {
				if (!cursor.getString(0).equals("Ecole")) {
					classroomList.add(cursor.getString(0));
				}
			} while (cursor.moveToNext());
			
			cursor.close();
		}
		
		return classroomList;
	}
	
	public void deleteTeacherFromCLassroom(String classroomName, Integer teacherId)
	{
		this.database.delete(TABLE_NAME, CLASSROOMNAME + " = ? AND " + TEACHERID + " = ?", new String[]{classroomName, teacherId.toString()});
	}
}