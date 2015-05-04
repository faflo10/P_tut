package p_tut.elethink_interface;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import p_tut.elethink_interface.local_database.LocalListDb;

/**
 * Created by faflo10 on 09/03/2015.
 */
public class Play_menu extends ActionBarActivity{

    //Attributes of the Play_menu class
    private Button launch;
    private ImageButton back;
    private Spinner spinner;
    private LocalListDb db;

    private Button test;


    //onCreate method, launched at the moment the intent in called
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_menu);

        //back button creation + setOnClickListener
        back = (ImageButton) findViewById(R.id.back_button);
        spinner = (Spinner) findViewById(R.id.spinner_list);
        launch = (Button) findViewById(R.id.button_launch_game);
        test = (Button) findViewById(R.id.test);

        db = new LocalListDb(getBaseContext(),"db_list.db", null, 1);
        SQLiteDatabase local = db.getReadableDatabase();
        final SQLiteDatabase write = db.getWritableDatabase();

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent back_intent = new Intent(Play_menu.this,Start_menu.class);
                startActivity(back_intent);
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write.execSQL("INSERT INTO list_listed VALUES (\"test\", \"test1\", \"test2\", \"test3\"");
            }
        });

        List<String> list = new ArrayList<String>();

        Cursor nb_res = local.query("list_listed", new String[] {"count(id)"}, null, null, null, null, null, null);
        int nb = nb_res.getInt(0);
        int i;
        Cursor res;
        for(i=0; i<nb; i++) {
            res = local.query("list_listed", new String [] {"id","name"}, "id=?",
                    new String[] {String.valueOf(i)}, null, null, null, null);
            list.add(String.valueOf(res.getInt(0)) + " - " + res.getString(1));
        }





        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, list);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int   position, long id) {
                Toast.makeText(getBaseContext(), spinner.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        launch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(Play_menu.this,"Objet selectionne :\n\t" + String.valueOf(spinner.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.back :
                Intent back_intent = new Intent(Play_menu.this, Start_menu.class);
                startActivity(back_intent);
                return true;
        }
        return false;
    }
}
