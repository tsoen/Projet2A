package ensicaen.fr.marierave.Controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DAOBase {
	
	public SQLiteDatabase _database;

    private DataBaseHandler handler;
    
    public DAOBase(Context context) {
		handler = DataBaseHandler.getInstance(context);
		open();
    }
    
    public void open() {

        // Pas besoin de fermer la dernière base puisque getWritableDatabase() s'en charge //
		_database = handler.getWritableDatabase();
    }

    public void close() {
		_database.close();
    }

    public SQLiteDatabase getDatabase() {
		return _database;
    }
    
    public void clearDatabase()
    {
		handler.onUpgrade(_database, DataBaseHandler.getVersion(), 100);
    }
}

