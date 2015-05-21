package p_tut.elethink_interface;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

import p_tut.elethink_interface.local_database.LocalListDb;


/**
 * Created by faflo10 on 08/03/2015.
 */
public class Start_menu extends ActionBarActivity {

    private ImageButton back;
    private Button play, list, whereabouts, settings;
    private SQLiteDatabase db;
    private LocalListDb local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);

        //Création de la base de données.
        local = new LocalListDb(getBaseContext(),"list.db",null,1);
        db = local.getWritableDatabase();
        db.setLockingEnabled(false);
        local.onCreate(db);

        back = (ImageButton) findViewById(R.id.back_button);
        play = (Button) findViewById(R.id.button_play);
        list = (Button) findViewById(R.id.button_listes);
        settings = (Button) findViewById(R.id.button_settings);
        whereabouts = (Button) findViewById(R.id.button_whereabouts);

        play.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                Intent play_act = new Intent(Start_menu.this, Play_menu.class);
                startActivity(play_act);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                finish();
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                Intent list_act = new Intent(Start_menu.this, List_menu.class);
                startActivity(list_act);
            }
        });

        settings.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent setting = new Intent(Start_menu.this, Settings_menu.class);
                startActivity(setting);
            }
        });

        whereabouts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent where = new Intent(Start_menu.this, Whereabout_menu.class);
                startActivity(where);
            }
        });
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.new_game :
                Intent new_game = new Intent(Start_menu.this, Play_menu.class);
                startActivity(new_game);
                return true;
            case R.id.list :
                Intent lists = new Intent(Start_menu.this, List_menu.class);
                startActivity(lists);
                return true;
            case R.id.settings :
                Intent setting = new Intent(Start_menu.this, Settings_menu.class);
                startActivity(setting);
                return true;
            case R.id.whereabouts :
                Intent aboutus = new Intent(Start_menu.this, Whereabout_menu.class);
                startActivity(aboutus);
                return true;
            case R.id.back :
                finish();
                return true;
        }
        return false;
    }

}