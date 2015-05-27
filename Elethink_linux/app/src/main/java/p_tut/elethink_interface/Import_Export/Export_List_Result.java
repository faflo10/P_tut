package p_tut.elethink_interface.Import_Export;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import p_tut.elethink_interface.R;
import p_tut.elethink_interface.local_database.LocalListDb;

/**
 * Created by vaxz on 21/05/15.
 */
public class Export_List_Result extends ListActivity {



    private ImageButton back;
    private ProgressDialog pDialog;

    private ListView listesLocales;
    private ArrayList<HashMap<String,String>> resultatsListes;

    String titre, idListe, motclef1, motclef2, motclef3;

    private LocalListDb db;
    private SQLiteDatabase local;

    private static final String URL_AJOUT_LISTE = "http://10.0.2.2:80/vaxz/www/ajout_liste.php";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_IDLISTE = "id";
    private static final String TAG_TITRE = "name";
    private static final String TAG_MOT_CLEF1 = "keyword_1";
    private static final String TAG_MOT_CLEF2 = "keyword_2";
    private static final String TAG_MOT_CLEF3 = "keyword_3";
    private static final String TAG_REPONSE = "answer";
    private static final String TAG_QUESTION = "question";


    public void initComponents(){

        pDialog = new ProgressDialog(Export_List_Result.this);
        resultatsListes = new ArrayList<HashMap<String, String>>();
        listesLocales =getListView();
        back = (ImageButton) findViewById(R.id.back_button);
        db = new LocalListDb(getBaseContext(),"list.db", null, 1);
        local = db.getWritableDatabase();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_list_result);
        initComponents();

        new RechercheListesLocales().execute();

    }

    @Override
    protected void onStart() {
        super.onStart();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listesLocales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idListe = ( (TextView) findViewById(R.id.idListe) ).getText().toString();
                titre = ( (TextView) findViewById(R.id.titreListe) ).getText().toString();
                motclef1 = ( (TextView) findViewById(R.id.mot_clef1) ).getText().toString();
                motclef2 = ( (TextView) findViewById(R.id.mot_clef2) ).getText().toString();
                motclef3 = ( (TextView) findViewById(R.id.mot_clef3) ).getText().toString();
                new Envoiliste().execute();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null) pDialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog != null) pDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onStop();
        if (pDialog != null) pDialog.dismiss();
    }

    private void messInstructionEnCours (String message){
        pDialog.setMessage(message);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    class RechercheListesLocales extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            messInstructionEnCours("Searching local lists");
        }


        @Override
        protected String doInBackground(String... params) {

            int colonneId, colonneTitre, colonneMotClef1, colonneMotClef2, colonneMotClef3;
            String idListe, titre, mclef1, mclef2, mclef3;
            Cursor res0;
            Intent i;

            res0 = local.query("list_listed", new String[]{TAG_IDLISTE, TAG_TITRE, TAG_MOT_CLEF1, TAG_MOT_CLEF2, TAG_MOT_CLEF3}, null,
                          null, null, null, null, null);
            colonneId = res0.getColumnIndex(TAG_IDLISTE);
            colonneTitre = res0.getColumnIndex(TAG_TITRE);
            colonneMotClef1 = res0.getColumnIndex(TAG_MOT_CLEF1);
            colonneMotClef2 = res0.getColumnIndex(TAG_MOT_CLEF2);
            colonneMotClef3 = res0.getColumnIndex(TAG_MOT_CLEF3);

            if(colonneId > -1 && colonneTitre > -1 && colonneMotClef1 > -1 && colonneMotClef1 > -1
               && colonneMotClef2 > -1 && colonneMotClef3 > -1) {
                while ( res0.moveToNext() ){

                    idListe = String.valueOf(res0.getInt(colonneId));
                    titre = res0.getString(colonneTitre);
                    mclef1 = res0.getString(colonneMotClef1);
                    mclef2 = res0.getString(colonneMotClef2);
                    mclef3 = res0.getString(colonneMotClef3);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_IDLISTE, idListe);
                    map.put(TAG_TITRE, titre);
                    map.put(TAG_MOT_CLEF1, mclef1);
                    map.put(TAG_MOT_CLEF2, mclef2);
                    map.put(TAG_MOT_CLEF3, mclef3);

                    resultatsListes.add(map);
                }
                res0.close();

            }else{

                res0.close();
                i = new Intent(getApplicationContext(), Import_Export_List.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ListAdapter adapter = new SimpleAdapter(
                            Export_List_Result.this, resultatsListes,
                            R.layout.activity_export_list_details,
                            new String[] {TAG_IDLISTE, TAG_TITRE, TAG_MOT_CLEF1, TAG_MOT_CLEF2, TAG_MOT_CLEF3},
                            new int[] { R.id.idListe, R.id.titreListe, R.id.mot_clef1, R.id.mot_clef2, R.id.mot_clef3 }
                    );
                    setListAdapter(adapter);

                }
            });
        }
    }

    class Envoiliste extends AsyncTask<String, String, String>{

        JSONObject json=null;
        Intent i = new Intent(getApplicationContext(), Import_Export_List.class);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            messInstructionEnCours("Sending one local list toward the server");
        }

        @Override
        protected String doInBackground(String... params) {

            int colonneQuestion, colonneAnswer;
            String tabListeCarac, tabListe;
            Cursor res0;
            List<NameValuePair> parametre = new ArrayList<NameValuePair>();
            JSONParser jParser = new JSONParser();

            res0 = local.query("\"" + titre + "\"", new String[]{TAG_QUESTION, TAG_REPONSE}, null,
                    null, null, null, null, null);
            colonneQuestion = res0.getColumnIndex(TAG_QUESTION);
            colonneAnswer = res0.getColumnIndex(TAG_REPONSE);

            if( colonneQuestion > -1 && colonneAnswer > -1 ){
                tabListe="";
                for( int k=0; k<res0.getCount(); k++ ){
                    res0.moveToPosition(k);
                    tabListe+= res0.getString(colonneQuestion)+"|"+res0.getString(colonneAnswer);
                    if( k < ( res0.getCount()-1 ) ) tabListe+="|";
                }
                res0.close();

                tabListeCarac = titre + "|" + motclef1 + "|" + motclef2 + "|" + motclef3;


                parametre.add(new BasicNameValuePair("tabListeCarac", tabListeCarac));
                parametre.add(new BasicNameValuePair("tabListe", tabListe));
                json = jParser.makeHttpRequest(URL_AJOUT_LISTE, "POST", parametre);

            }else{

                res0.close();
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String message;
            //commentaire
            pDialog.dismiss();

            try {
                if (json == null) {
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }else{
                    message = json.getString(TAG_MESSAGE);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
