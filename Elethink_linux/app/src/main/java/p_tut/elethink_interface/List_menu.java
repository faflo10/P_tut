package p_tut.elethink_interface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import p_tut.elethink_interface.Import_Export.Import_Export_List;

/**
 * Created by faflo10 on 12/03/2015.
 */
public class List_menu extends ActionBarActivity {

    ImageButton back;
    Button manage_list, create_list, distant_server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listes_menu);

        back = (ImageButton) findViewById(R.id.back_button);
        manage_list = (Button) findViewById(R.id.manage_list_button);
        create_list = (Button) findViewById(R.id.create_list_button);
        distant_server = (Button) findViewById(R.id.server);

        back.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick (View v){
                finish();
            }
        });

        manage_list.setOnClickListener(new Button.OnClickListener() {
            public void onClick (View v) {
                Intent manage = new Intent(List_menu.this, Manage_list.class);
                startActivity(manage);
            }
        });

        create_list.setOnClickListener(new Button.OnClickListener() {
            public void onClick (View v) {
                Intent creation = new Intent(List_menu.this, Create_list.class);
                startActivity(creation);
            }
        });

        distant_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent server = new Intent(getApplicationContext(), Import_Export_List.class);
                startActivity(server);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.manage_list :
                Intent manage = new Intent(List_menu.this, Manage_list.class);
                startActivity(manage);
                return true;
            case R.id.create_list :
                Intent creation = new Intent(List_menu.this, Create_list.class);
                startActivity(creation);
                return true;
            case R.id.back :
                finish();
                return true;
        }
        return false;
    }
}
