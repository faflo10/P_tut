package p_tut.elethink_interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by faflo10 on 09/04/15.
 */
public class Manage_list extends ActionBarActivity {
    private ImageButton back;
    private Spinner spin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_list);

        back = (ImageButton) findViewById(R.id.back_button);
        spin = (Spinner) findViewById(R.id.spinner_list);

        back.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                Intent back_intent = new Intent(Manage_list.this, List_menu.class);
                startActivity(back_intent);
            }
        });

        List<String> list = new ArrayList<String>();
        list.add("Coucou");


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
    }
}
