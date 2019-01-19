package ensicaen.fr.marierave.Controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {
    
    public DataBaseHandler(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tableChild (" +
                "Id INTEGER PRIMARY KEY, " +
                "Name TEXT NOT NULL, " +
                "Firstname TEXT NOT NULL, " + "Classroom TEXT, " + "FOREIGN KEY(Classroom) REFERENCES tableClassroom(Name) ON UPDATE CASCADE);");

        db.execSQL("CREATE TABLE tableClassroom (" +
                "Name TEXT PRIMARY KEY);");
	
		db.execSQL("CREATE TABLE tableLevel (" +
				"Id INTEGER PRIMARY KEY, " +
				"Name TEXT NOT NULL);");
		
        db.execSQL("CREATE TABLE tableLevelClassroom (" +
                "ClassroomName TEXT, " +
                "LevelId INTEGER, " + "PRIMARY KEY (ClassroomName,LevelId), " + "FOREIGN KEY(ClassroomName) REFERENCES tableClassroom(Name) ON UPDATE CASCADE ON DELETE" +
				" CASCADE," + "FOREIGN KEY(LevelId) REFERENCES tableLevel(Id) ON UPDATE CASCADE ON DELETE CASCADE);");
	
		db.execSQL("CREATE TABLE tableSubject (" + "Name TEXT PRIMARY KEY);");
	
		db.execSQL("CREATE TABLE tableSkillHeader (" + "Name TEXT PRIMARY KEY, " + "Subject TEXT NOT NULL, " + "FOREIGN KEY(Subject) REFERENCES tableSubject(Name) ON " +
				"UPDATE CASCADE ON DELETE CASCADE);");
	
		db.execSQL("CREATE TABLE tableSkill (" + "Code TEXT PRIMARY KEY, " + "Name TEXT NOT NULL," + "Skillheader TEXT NOT NULL, " + "FOREIGN KEY(Skillheader) " +
				"REFERENCES tableSkillHeader(Name) ON UPDATE CASCADE ON DELETE CASCADE);");
	
		db.execSQL("CREATE TABLE tableChildSkillMark (" + "ChildId INTEGER, " + "SkillCode TEXT, " + "Mark TEXT, " + "PRIMARY KEY(SkillCode,ChildId,Mark), " + "FOREIGN " +
				"KEY(ChildId) REFERENCES tableChild(Id) ON UPDATE CASCADE ON DELETE CASCADE, " + "FOREIGN KEY(SkillCode) REFERENCES tableSkill(Code) ON UPDATE CASCADE " +
				"ON DELETE CASCADE);");
	
		db.execSQL("CREATE TABLE tableChildSkillComment (" + "ChildId INTEGER, " + "SkillCode TEXT, " + "Comment TEXT, " + "PRIMARY KEY(SkillCode,ChildId,Comment), " +
				"FOREIGN " + "KEY(ChildId) REFERENCES tableChild(Id) ON UPDATE CASCADE ON DELETE CASCADE, " + "FOREIGN KEY(SkillCode) REFERENCES tableSkill(Code) ON " +
				"UPDATE CASCADE " + "ON DELETE CASCADE);");
		
		db.execSQL("CREATE TABLE tableTeacher (" +
				"Id INTEGER PRIMARY KEY, " +
				"Name TEXT NOT NULL, " +
				"Firstname TEXT NOT NULL);");
	
		db.execSQL("CREATE TABLE tableTeacherClassroom (" +
				"ClassroomName TEXT, " +
				"TeacherId INTEGER, " + "PRIMARY KEY (ClassroomName,TeacherId)," + "FOREIGN KEY(ClassroomName) REFERENCES tableClassroom(Name) ON UPDATE CASCADE ON " +
				"DELETE CASCADE," + "FOREIGN KEY(TeacherId) REFERENCES tableTeacher(Id) ON UPDATE CASCADE ON DELETE CASCADE);");
	}
	
	@Override
	public void onOpen(SQLiteDatabase db)
	{
		super.onOpen(db);
		db.setForeignKeyConstraintsEnabled(true);
	}
	
	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS tableChildSkillMark;");
		db.execSQL("DROP TABLE IF EXISTS tableChildSkillComment;");
		db.execSQL("DROP TABLE IF EXISTS tableLevelClassroom;");
		db.execSQL("DROP TABLE IF EXISTS tableChild;");
        db.execSQL("DROP TABLE IF EXISTS tableClassroom;");
		db.execSQL("DROP TABLE IF EXISTS tableSubject;");
		db.execSQL("DROP TABLE IF EXISTS tableSkillHeader;");
		db.execSQL("DROP TABLE IF EXISTS tableSkill;");
        db.execSQL("DROP TABLE IF EXISTS tableTeacherClassroom;");
		db.execSQL("DROP TABLE IF EXISTS tableLevel;");
		db.execSQL("DROP TABLE IF EXISTS tableTeacher;");
		
		onCreate(db);
    }
}
