package p_tut.elethink_interface.local_database;

import android.content.Context;
import android.database.Cursor;
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
        Cursor result = db.query("sqlite_master", new String[] {"name"}, "type=? AND name=?",
            new String[] {"table","list_listed"},null,null,null );
        int verif = result.getCount();
        /*System.out.println(verif);*/
        if(verif == 0) {
            String strReq = "CREATE TABLE list_listed (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " name TEXT, keyword_1 TEXT, keyword_2 TEXT, keyword_3 TEXT);";
            db.execSQL(strReq);
        }

        /*result = db.query("sqlite_master", new String[] {"name"}, "type=? AND name=?",
                new String[] {"table","list_listed"},null,null,null );
        verif = result.getCount();
        System.out.println(verif);*/
        result.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

