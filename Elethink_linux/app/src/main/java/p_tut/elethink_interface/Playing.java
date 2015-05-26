package p_tut.elethink_interface;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import p_tut.elethink_interface.local_database.LocalListDb;

/**
 * Created by faflo10 on 26/05/15.
 */
public class Playing extends ActionBarActivity {
    private ImageButton back;
    private TextView mot;
    private Button i_know;
    private Button i_dont_know;
    private Button right;
    private Button wrong;
    private LocalListDb db;
    private SQLiteDatabase local;
    private int max;
    private ArrayList<String> questions;
    private ArrayList<String> answers;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_screen_1);
    }

    protected void onStart() {
        super.onStart();
        setContentView(R.layout.play_screen_1);

        initComponents();

        String nom = getIntent().getStringExtra("name");

        Cursor query = local.query(nom, new String[] {"question"},null,null,null,null,null);
        int question_t = query.getColumnIndex("question");
        while(query.moveToNext()) {
            questions.add(query.getString(question_t));
        }
        query.close();

        query = local.query(nom, new String[] {"answer"},null,null,null,null,null);
        int answer_t = query.getColumnIndex("answer");
        while(query.moveToNext()) {
            answers.add(query.getString(answer_t));
        }
        query.close();

        Random id_rand = new Random();
        mot.setText(questions.get(id_rand.nextInt(questions.size())));
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public void initComponents() {
        back = (ImageButton) findViewById(R.id.back_button);
        mot = (TextView) findViewById(R.id.mot);
        i_know = (Button) findViewById(R.id.i_know);
        i_dont_know = (Button) findViewById(R.id.i_dont_know);
        right = (Button) findViewById(R.id.right);
        wrong = (Button) findViewById(R.id.wrong);

        db = new LocalListDb(getBaseContext(),"list.db", null, 1);
        local = db.getWritableDatabase();

        questions = new ArrayList<>();
        answers = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
