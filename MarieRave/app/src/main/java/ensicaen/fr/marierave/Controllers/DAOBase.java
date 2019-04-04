package ensicaen.fr.marierave.Controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DAOBase {
    
    public SQLiteDatabase database;

    private DataBaseHandler handler;
    
    public DAOBase(Context context) {
		this.handler = DataBaseHandler.getInstance(context);
        this.open();
    }
    
    public void open() {

        // Pas besoin de fermer la dernière base puisque getWritableDatabase() s'en charge //
        this.database = handler.getWritableDatabase();
    }

    public void close() {
        this.database.close();
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }
    
    public void clearDatabase()
    {
		this.handler.onUpgrade(database, DataBaseHandler.getVersion(), 100);
    }
}

