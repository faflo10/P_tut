package p_tut.elethink_interface;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fill_list);
    }

    protected void onStart() {
        super.onStart();
        setContentView(R.layout.play_screen_1);

        initComponents();
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
