package p_tut.elethink_interface;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import p_tut.elethink_interface.local_database.LocalListDb;

/**
 * Created by faflo10 on 30/03/15.
 */
public class Settings_menu extends ActionBarActivity{
    private ImageButton back;
    private Button wipe;
    private LocalListDb local;
    private SQLiteDatabase db;
    private AlertDialog popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);

        local = new LocalListDb(getBaseContext(),"list.db", null, 1);
        db = local.getWritableDatabase();

        back = (ImageButton) findViewById(R.id.back_button);
        wipe = (Button) findViewById(R.id.wipe_button);
        popup = createDialog();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
            }
        });


    }

    public AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You will loose all your lists !").setTitle("WARNING !");
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.wipe, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //On prend tous les noms de la liste de listes et on en supprime les tables.
                Cursor queryName = db.query("list_listed",new String[]{"name"},null,null,null,null,null,null);
                if(queryName.getCount() == 0) {
                    Toast.makeText(getApplicationContext(),"Nothing to delete",Toast.LENGTH_SHORT).show();
                } else {
                    int name_bd = queryName.getColumnIndex("name");
                    while(queryName.moveToNext()) {
                        String query = "DROP TABLE \"" + queryName.getString(name_bd) + "\";";
                        db.execSQL(query);
                    }
                    queryName.close();
                    //On supprime la table de listes
                    db.execSQL("DROP TABLE list_listed;");
                    //Et on la recréé
                    String strReq = "CREATE TABLE list_listed (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " name TEXT, keyword_1 TEXT, keyword_2 TEXT, keyword_3 TEXT);";
                    db.execSQL(strReq);

                    Toast.makeText(getApplicationContext(),"Save wiped",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.back :
                finish();
                return true;
        }
        return false;
    }

}
