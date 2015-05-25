package p_tut.elethink_interface;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
 * Created by faflo10 on 09/04/15.
 */
public class Manage_list extends ActionBarActivity {
    private ImageButton back;
    private Button erase;
    private Spinner spin;
    private LocalListDb db;
    private SQLiteDatabase local;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_list);

        db = new LocalListDb(getBaseContext(),"list.db", null, 1);
        final SQLiteDatabase local = db.getWritableDatabase();

        back = (ImageButton) findViewById(R.id.back_button);
        erase = (Button) findViewById(R.id.erase);
        spin = (Spinner) findViewById(R.id.spinner_list);

        back.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        List<String> list = new ArrayList<String>();

        //Remplissage du spinner de manière dynamique grâce à la base de données.
        Cursor res = local.query("list_listed", new String [] {"id","name"}, null,
                null, null, null, null, null);

        int id_bd = res.getColumnIndex("id");
        int name_bd = res.getColumnIndex("name");
        while(res.moveToNext()) {
            list.add(Integer.toString(res.getInt(id_bd)) + " - " + res.getString(name_bd));
        }

        res.close();


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, list);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        // Apply the adapter to the spinner
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int   position, long id) {
                Toast.makeText(getBaseContext(), spin.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spin.getSelectedItem() == null) {
                    Toast.makeText(getApplicationContext(),"Choose a list to delete",Toast.LENGTH_SHORT).show();
                } else {
                    String content = spin.getSelectedItem().toString();
                    content = content.substring(0,content.indexOf(" - "));
                    Cursor c_name = local.query("list_listed",new String[] {"name"},"id = ?",
                            new String[] {content},null,null,null);
                    int i_name = c_name.getColumnIndex("name");
                    c_name.moveToNext();
                    String res_name = c_name.getString(i_name);
                    local.execSQL("DROP TABLE '" + res_name+"';");
                    local.execSQL("DELETE FROM list_listed WHERE name='"+res_name+"';");

                    //Bloc permettant de rafraîchir l'activité
                    Intent refresh = getIntent();
                    refresh.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); //Enlève l'animation de refresh
                    finish();
                    startActivity(refresh);
                    overridePendingTransition(0,0); //Enlève mieux l'animation de refresh
                }
            }
        });
    }
}
