package p_tut.elethink_interface;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import p_tut.elethink_interface.local_database.LocalListDb;

/**
 * Created by faflo10 on 19/05/15.
 */
public class Fill_list extends ActionBarActivity {
    private ImageButton back;
    private EditText q1;
    private EditText q2;
    private EditText q3;
    private EditText q4;
    private EditText q5;
    private EditText a1;
    private EditText a2;
    private EditText a3;
    private EditText a4;
    private EditText a5;
    private Button add5;
    private Button submit;
    private LocalListDb db;
    private ArrayList<String> questions;
    private ArrayList<String> answers;
    private SQLiteDatabase local;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fill_list);
    }

    public void onStart() {
        super.onStart();
        initComponents();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tabQ[] = {q1,q2,q3,q4,q5};
                EditText tabA[] = {a1,a2,a3,a4,a5};
                boolean ret = checkIfFormOk(tabQ,tabA);
                int i=0;
                if(ret) {
                    while(checkFull(tabQ[i]) && i<tabQ.length) {
                        questions.add(tabQ[i].getText().toString());
                        answers.add(tabA[i].getText().toString());
                        i++;
                    }

                    String query1 = "INSERT INTO list_listed VALUES (null,\""+getIntent().getStringExtra("title")
                            +"\",\""+getIntent().getStringExtra("kw1")+"\",\""+getIntent().getStringExtra("kw2")
                            +"\",\""+getIntent().getStringExtra("kw3")+"\");";
                    local.execSQL(query1);

                    String query2 = "CREATE TABLE " + getIntent().getStringExtra("title") +
                            " (num INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, " +
                            "answer TEXT);";
                    local.execSQL(query2);

                    Toast.makeText(getApplicationContext(),
                            "List created",Toast.LENGTH_LONG).show();

                    //TODO : fill the list created
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Form not correct",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public boolean checkFull(EditText et) {
        if(et.getText().toString().length() != 0) return true;
        return false;
    }

    public boolean checkPairInput(EditText et1, EditText et2) {
        if(checkFull(et1) && checkFull(et2)) {
            return true;
        }
        return false;
    }

    public boolean checkIfFormOk(EditText[] tabQ, EditText[] tabA) {
        int i;
        for(i=0; i<tabQ.length; i++) {
            if(!checkFull(tabQ[i])) break;
        }

        if(i==0) {
            return false;
        } else {
            for (int j = 0; j < i; j++) {
                if (!checkPairInput(tabQ[j], tabA[j])) {
                    return false;
                }
            }
        }

        return true;
    }

    public void initComponents() {
        back = (ImageButton) findViewById(R.id.back_button);
        q1 = (EditText) findViewById(R.id.q1);
        q2 = (EditText) findViewById(R.id.q2);
        q3 = (EditText) findViewById(R.id.q3);
        q4 = (EditText) findViewById(R.id.q4);
        q5 = (EditText) findViewById(R.id.q5);
        a1 = (EditText) findViewById(R.id.a1);
        a2 = (EditText) findViewById(R.id.a2);
        a3 = (EditText) findViewById(R.id.a3);
        a4 = (EditText) findViewById(R.id.a4);
        a5 = (EditText) findViewById(R.id.a5);
        add5 = (Button) findViewById(R.id.five_more);
        submit = (Button) findViewById(R.id.submit);

        questions = new ArrayList<>();
        answers = new ArrayList<>();

        db = new LocalListDb(getBaseContext(),"list.db", null, 1);
        local = db.getWritableDatabase();
    }
}
