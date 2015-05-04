package p_tut.elethink_interface.local_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * Created by root on 11/04/15.
 */
public class LocalListDb extends SQLiteOpenHelper {

    public LocalListDb(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strReq = "CREATE TABLE list_listed (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name TEXT, keyword_1 TEXT, keyword_2 TEXT, keyword_3 TEXT)" +
                " IF NOT EXISTS list_listed ;";
        db.execSQL(strReq);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
