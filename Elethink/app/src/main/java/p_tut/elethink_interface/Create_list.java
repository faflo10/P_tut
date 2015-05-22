package p_tut.elethink_interface;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import p_tut.elethink_interface.local_database.*;


/**
 * Created by faflo10 on 09/04/15.
 */
public class Create_list extends ActionBarActivity {
    private ImageButton back;
    private LocalListDb local;
    private SQLiteDatabase db;
    private EditText name;
    private EditText kw1;
    private EditText kw2;
    private EditText kw3;
    private Button next;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_list);

    }

    public void onStart() {
        super.onStart();
        setContentView(R.layout.create_list);

        initComponents();

        local = new LocalListDb(getBaseContext(),"list.db", null, 1);
        db = local.getWritableDatabase();

        back.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        //TODO Patch the error when inserting spaces in title name;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkTitleField(name)) {             //Verif si le nom existe
                    if(checkKWField(kw1,kw2,kw3)) {     //Verif si les mots-clés existent
                        //Vérification si une liste n'a pas déjà le même nom
                        Cursor result = db.query("sqlite_master", new String[] {"name"}, "type=? AND name=?",
                                new String[] {"table",name.getText().toString().replaceAll("\\s+$","")},null,null,null );
                        int verif = result.getCount();
                        System.out.println(verif);
                        if(verif == 0) {    //Si aucune liste n'a le même nom
                            Intent change = new Intent(Create_list.this, Fill_list.class);
                            change.putExtra("title", name.getText().toString());
                            change.putExtra("kw1", kw1.getText().toString());
                            change.putExtra("kw2", kw2.getText().toString());
                            change.putExtra("kw3", kw3.getText().toString());
                            Toast.makeText(getApplicationContext(), "Heading to the filling of" +
                                    " the list", Toast.LENGTH_SHORT).show();
                            startActivityForResult(change, 42);
                        } else {
                            name.setText("");
                            Toast.makeText(getApplicationContext(),"Nom de liste déjà pris !",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error while filling the keywords",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error while filling the title",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initComponents();

        if(requestCode == 42) {
            if(resultCode == 1) {
                name.setText(getIntent().getStringExtra("title"));
                kw1.setText(getIntent().getStringExtra("kw1"));
                kw2.setText(getIntent().getStringExtra("kw2"));
                kw3.setText(getIntent().getStringExtra("kw3"));
            }
        }
    }

    public boolean checkTitleField(EditText title) {
        String content = title.getText().toString();
        if(content.length() != 0) return true;
        return false;
    }

    public boolean checkKWField(EditText one, EditText two, EditText three) {
        String a = one.getText().toString();
        String b = two.getText().toString();
        String c = three.getText().toString();

        if(a.length() != 0 && b.length() != 0 && c.length() != 0) {
            if(!(a.toLowerCase().equals(b.toLowerCase())) && !(a.toLowerCase().equals(c.toLowerCase()))
                    && !(b.toLowerCase().equals(c.toLowerCase()))) {
                return true;
            }
        }
        return false;
    }

    public void initComponents() {
        back = (ImageButton) findViewById(R.id.back_button);
        name = (EditText) findViewById(R.id.name);
        kw1 = (EditText) findViewById(R.id.kw1);
        kw2 = (EditText) findViewById(R.id.kw2);
        kw3 = (EditText) findViewById(R.id.kw3);
        next = (Button) findViewById(R.id.next);
    }

}
