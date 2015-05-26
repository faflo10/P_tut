package p_tut.elethink_interface;

import android.content.Intent;
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
                Intent ret = new Intent();
                ret.putExtra("title",getIntent().getStringExtra("title"));
                ret.putExtra("kw1",getIntent().getStringExtra("kw1"));
                ret.putExtra("kw2",getIntent().getStringExtra("kw2"));
                ret.putExtra("kw3",getIntent().getStringExtra("kw3"));
                setResult(RESULT_CANCELED,ret);
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
                if(ret) {                                           //Si le formulaire est bien rempli
                    while(checkFull(tabQ[i]) && i<tabQ.length) {    //On remplit les listes avec les inputs
                        questions.add(tabQ[i].getText().toString());
                        answers.add(tabA[i].getText().toString());
                        i++;
                    }
                    String title_table = getIntent().getStringExtra("title").replaceAll("\\s+$","");
                    String kw1_table = getIntent().getStringExtra("kw1");
                    String kw2_table = getIntent().getStringExtra("kw2");
                    String kw3_table = getIntent().getStringExtra("kw3");

                    //On insere le titre et les mots-clés dans la table de listes
                    String query1 = "INSERT INTO list_listed VALUES (null,\""+
                            title_table
                            +"\",\""+kw1_table+"\",\""+kw2_table
                            +"\",\""+kw3_table+"\");";
                    local.execSQL(query1);

                    //On créé une table pour la nouvelle liste
                    String query2 = "CREATE TABLE \"" + title_table +
                            "\" (num INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, " +
                            "answer TEXT);";
                    local.execSQL(query2);

                    //TODO : A valider
                    for(int j=0; j<questions.size(); j++) {
                        String insert = "INSERT INTO \"" + title_table +
                                "\" VALUES (null,\"" + questions.get(j) +
                                "\",\"" + answers.get(j) + "\");";
                    }

                    Toast.makeText(getApplicationContext(),
                            "List created",Toast.LENGTH_LONG).show();

                    setResult(RESULT_OK);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Form not correct",Toast.LENGTH_LONG).show();
                }
            }
        });

        add5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tabQ[] = {q1,q2,q3,q4,q5};
                EditText tabA[] = {a1,a2,a3,a4,a5};
                if(checkIfFormFull(tabQ,tabA)) {
                    for(int i = 0; i<tabQ.length ; i++) {
                        questions.add(tabQ[i].getText().toString().replaceAll("\\s+$",""));
                        answers.add(tabA[i].getText().toString().replaceAll("\\s+$",""));
                        tabQ[i].setText("");
                        tabA[i].setText("");
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Fill all the inputs before asking for more !",
                            Toast.LENGTH_SHORT).show();
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

    public boolean checkIfFormFull(EditText[] tabQ, EditText[] tabA) {
        for(int i = 0; i<tabQ.length ; i++) {
            if(!checkPairInput(tabQ[i],tabA[i])) return false;
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
// st.replaceAll("\\s+$","")