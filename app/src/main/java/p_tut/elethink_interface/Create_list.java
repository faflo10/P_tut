package p_tut.elethink_interface;

import android.app.Activity;
import android.content.Intent;
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

        back = (ImageButton) findViewById(R.id.back_button);
        name = (EditText) findViewById(R.id.name);
        kw1 = (EditText) findViewById(R.id.kw1);
        kw2 = (EditText) findViewById(R.id.kw2);
        kw3 = (EditText) findViewById(R.id.kw3);
        next = (Button) findViewById(R.id.next);

        local = new LocalListDb(getBaseContext(),"list.db", null, 1);
        SQLiteDatabase db = local.getWritableDatabase();

        back.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkTitleField(name)) {
                    if(checkKWField(kw1,kw2,kw3)) {
                        Intent change = new Intent(Create_list.this,Fill_list.class);
                        change.putExtra("title",name.getText().toString());
                        change.putExtra("kw1", kw1.getText().toString());
                        change.putExtra("kw2",kw2.getText().toString());
                        change.putExtra("kw3",kw3.getText().toString());
                        Toast.makeText(getApplicationContext(),"Heading to the filling of" +
                                "the list", Toast.LENGTH_LONG).show();
                        startActivity(change);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error while filling the keywords",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error while filling the title",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
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

}
