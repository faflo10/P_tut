package p_tut.elethink_interface.Import_Export;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import p_tut.elethink_interface.R;

/**
 * Created by vaxz on 21/05/15.
 */
public class Import_List_Search extends Activity{

    private Button search;
    private EditText words;
    private ImageButton back;
    private String sortedWords;
    private final static String TAG_SEARCH = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_list_search);

        words = (EditText) findViewById( R.id.text_search );
        sortedWords = sortWords( words.getText().toString() );

        search = (Button) findViewById(R.id.button_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortedWords = sortWords( words.getText().toString() );
                Intent intentSearch = new Intent( getApplicationContext(), Import_List_Result.class );
                intentSearch.putExtra(TAG_SEARCH,sortedWords);
                startActivity(intentSearch);
            }
        });

        back = (ImageButton) findViewById(R.id.back_button);
        back.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private String sortWords (String words) {

        String sortedWords="";

        for(int i = 0; i<words.length(); i++ ){

            if( words.charAt(i) == ' '){
                sortedWords += "|";

            }else sortedWords += words.charAt(i);
        }
        return sortedWords;

    }


}
