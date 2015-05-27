package p_tut.elethink_interface;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Random;

import p_tut.elethink_interface.local_database.LocalListDb;

/**
 * Created by faflo10 on 26/05/15.
 */
public class Playing extends ActionBarActivity {
    private ImageButton back;
    private TextView mot;
    private Button one;
    private Button two;
    private Button next;
    private ViewSwitcher switch1;
    private LocalListDb db;
    private SQLiteDatabase local;
    private ArrayList<String> questions;
    private ArrayList<String> answers;
    private int value_rand;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_screen);
    }

    protected void onStart() {
        super.onStart();
        setContentView(R.layout.play_screen);
        initComponents();
        setButtons();

        setButtons1();
        Random id_rand = new Random();
        value_rand = id_rand.nextInt(questions.size());
        mot.setText(questions.get(value_rand));
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public void initComponents() {
        back = (ImageButton) findViewById(R.id.back_button);
        mot = (TextView) findViewById(R.id.mot);
        one = (Button) findViewById(R.id.i_know);
        two = (Button) findViewById(R.id.i_dont_know);
        next = (Button) findViewById(R.id.next);
        switch1 = (ViewSwitcher) findViewById(R.id.switch1);


        db = new LocalListDb(getBaseContext(),"list.db", null, 1);
        local = db.getWritableDatabase();

        questions = new ArrayList<>();
        answers = new ArrayList<>();

        Cursor query = local.query(getIntent().getStringExtra("name"), new String[] {"question"},
                null,null,null,null,null);
        int question_t = query.getColumnIndex("question");
        while(query.moveToNext()) {
            questions.add(query.getString(question_t));
        }
        query.close();

        query = local.query(getIntent().getStringExtra("name"), new String[] {"answer"},
                null,null,null,null,null);
        int answer_t = query.getColumnIndex("answer");
        while(query.moveToNext()) {
            answers.add(query.getString(answer_t));
        }
        query.close();
    }

    public void setButtons1() {
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch1.showNext();
                mot.setText(answers.get(value_rand));
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one.setText("Right");
                two.setText("Wrong");
                setButtons2();
                mot.setText(answers.get(value_rand));

            }
        });
    }

    public void setButtons2() {
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random id_rand = new Random();
                if(questions.size() != 1) {
                    int verif = value_rand;
                    do {
                        value_rand = id_rand.nextInt(questions.size());
                    } while(verif == value_rand);
                } else {
                    value_rand = id_rand.nextInt(1);
                }
                one.setText("I know");
                two.setText("I don't know");
                setButtons1();
                mot.setText(questions.get(value_rand));
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questions.remove(value_rand);
                answers.remove(value_rand);

                if(questions.size() == 0) {
                    Toast.makeText(getApplicationContext(),"Fini",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Random rand = new Random();
                    value_rand = rand.nextInt(questions.size());
                    one.setText("I know");
                    two.setText("I don't know");
                    setButtons1();
                    mot.setText(questions.get(value_rand));
                }
            }
        });
    }

    public void setButtons() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random id_rand = new Random();
                if(questions.size() != 1) {
                    int verif = value_rand;
                    do {
                        value_rand = id_rand.nextInt(questions.size());
                    } while(verif == value_rand);
                } else {
                    value_rand = id_rand.nextInt(1);
                }
                switch1.showPrevious();
                setButtons1();
                mot.setText(questions.get(value_rand));
            }
        });
    }
}
