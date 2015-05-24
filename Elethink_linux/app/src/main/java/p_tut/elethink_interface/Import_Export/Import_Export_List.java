package p_tut.elethink_interface.Import_Export;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import p_tut.elethink_interface.R;

/**
 * Created by vaxz on 21/05/15.
 */
public class Import_Export_List extends Activity {

    private Button importListe;
    private Button exportListe;
    private ImageButton back;
    private Intent intentListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export_list);


        back = (ImageButton) findViewById(R.id.back_button);
        back.setOnClickListener(new ImageButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        importListe = (Button) findViewById(R.id.button_import);
        importListe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intentListe = new Intent(getApplicationContext(), Import_List_Search.class);
                startActivity(intentListe);

            }

        });

        exportListe = (Button) findViewById(R.id.button_export);
        exportListe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intentListe = new Intent(getApplicationContext(),Export_List_Result.class);
                startActivity(intentListe);

            }

        });

    }
}
