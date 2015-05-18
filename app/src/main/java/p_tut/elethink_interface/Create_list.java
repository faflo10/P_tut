package p_tut.elethink_interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import p_tut.elethink_interface.local_database.*;


/**
 * Created by faflo10 on 09/04/15.
 */
public class Create_list extends ActionBarActivity {
    private ImageButton back;
    private LocalListDb local;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_list);


        back = (ImageButton) findViewById(R.id.back_button);

        back.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
