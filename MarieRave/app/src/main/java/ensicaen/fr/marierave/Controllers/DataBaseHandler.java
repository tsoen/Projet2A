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
                "Firstname TEXT NOT NULL, " +
                "Classroom TEXT);");

        db.execSQL("CREATE TABLE tableClassroom (" +
                "Name TEXT PRIMARY KEY);");
	
		db.execSQL("CREATE TABLE tableLevel (" +
				"Id INTEGER PRIMARY KEY, " +
				"Name TEXT NOT NULL);");
		
        db.execSQL("CREATE TABLE tableLevelClassroom (" +
                "ClassroomName TEXT, " +
                "LevelId INTEGER, " +
                "PRIMARY KEY (ClassroomName,LevelId));");
	
		db.execSQL("CREATE TABLE tableSkill (" +
				"Code String PRIMARY KEY, " +
				"Name TEXT);");
	
		db.execSQL("CREATE TABLE tableTeacher (" +
				"Id INTEGER PRIMARY KEY, " +
				"Name TEXT NOT NULL, " +
				"Firstname TEXT NOT NULL);");
	
		db.execSQL("CREATE TABLE tableTeacherClassroom (" +
				"ClassroomName TEXT, " +
				"TeacherId INTEGER, " +
				"PRIMARY KEY (ClassroomName,TeacherId));");
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tableChild;");
        db.execSQL("DROP TABLE IF EXISTS tableClassroom;");
        db.execSQL("DROP TABLE IF EXISTS tableLevel;");
        db.execSQL("DROP TABLE IF EXISTS tableLevelClassroom;");
        db.execSQL("DROP TABLE IF EXISTS tableSkill;");
        db.execSQL("DROP TABLE IF EXISTS tableTeacher;");
        db.execSQL("DROP TABLE IF EXISTS tableTeacherClassroom;");
		
        onCreate(db);
    }
}
