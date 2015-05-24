package p_tut.elethink_interface.Import_Export;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
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
public class Import_List_Result extends ListActivity {

    private ImageButton back;
    // Progress Dialog
    private ProgressDialog pDialog=null;
    // Creation de l'objet JSONParser
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> resultatsListes;
    // urlVisualiserListe pour recherche une liste souhaitée
    private static String urlVisualiserListe = "http://10.0.2.2:80/vaxz/www/visualiser_liste.php";
    private static String urlRecupererListe = "http://10.0.2.2:80/vaxz/www/importer_mot.php";
    // Nom des JSON Node
    private static final String TAG_VALIDE = "valide";
    private static final String TAG_LISTE = "liste";
    private static final String TAG_IDLISTE = "idListe";
    private static final String TAG_TITRE = "titre";
    private static final String TAG_MOT_CLEF1 = "motClef1";
    private static final String TAG_MOT_CLEF2 = "motClef2";
    private static final String TAG_MOT_CLEF3 = "motClef3";
    private static final String TAG_SEARCH = "search";
    private static final String TAG_MOT = "mots";
    private static final String TAG_REPONSE = "reponse";
    private static final String TAG_QUESTION = "question";
    // Tableau JSON des résultats de la recherche liste
    private JSONArray JSONlistes = null;
    private JSONArray JSONmots = null;
    //Récupération des mots de recherche saisis par l'uilisateur
    private String motsSaisis = null;

    String pid, titre, motclef1, motclef2, motclef3;

    private LocalListDb db;
    private SQLiteDatabase local;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_list_result);

        db = new LocalListDb(getBaseContext(),"list.db", null, 1);
        local = db.getWritableDatabase();

        back = (ImageButton) findViewById(R.id.back_button);
        back.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Hashmap pour la ListView
        resultatsListes = new ArrayList<HashMap<String, String>>();
        // Lancer en arrière-plan la recherche de liste
        new RecherchesListes().execute();
        // Obtenir la listview
        ListView lv = getListView();

        /*
         Après avoir sélectionné une liste unique, on réupère les caractéristique de la liste
        */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Obtenir l'identifiant, le  titre... de la liste souhaitée dans le fichier XML activity_import_list_details
                pid = ((TextView) view.findViewById(R.id.idListe)).getText().toString();
                titre = ((TextView) view.findViewById(R.id.titreListe)).getText().toString();
                motclef1 = ((TextView) view.findViewById(R.id.mot_clef1)).getText().toString();
                motclef2 = ((TextView) view.findViewById(R.id.mot_clef2)).getText().toString();
                motclef3 = ((TextView) view.findViewById(R.id.mot_clef3)).getText().toString();

                new testNewTitle().execute();
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        if(pDialog != null)
            pDialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(pDialog != null)
            pDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onStop();
        if(pDialog != null)
            pDialog.dismiss();
    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class RecherchesListes extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Import_List_Result.this);
            pDialog.setMessage("Searching");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Obtenir les JSONmots correspondants au mots saisis
         * */
        protected String doInBackground(String... args) {

            //Récupérer les mots saisies par l'utilisateur
            motsSaisis = getIntent().getStringExtra(TAG_SEARCH);
            // Construire les parametres pour la variable $_SESSION
            List<NameValuePair> parametre = new ArrayList<NameValuePair>();
            parametre.add(new BasicNameValuePair("motsSaisis", motsSaisis));
            // Avoir la chaine de caractère JSON depuis l'urlVisualiserListe
            Log.d("RechercheJSONlistes :", jParser.makeHttpRequest(urlVisualiserListe, "POST", parametre).toString());
            JSONObject json = jParser.makeHttpRequest(urlVisualiserListe, "POST", parametre);

            //Vérifier le log cat pour la réponse JSON
            if (json!=null) Log.d( "RechercheJSONlistes :", json.toString() );
            else {

                Log.d( "RechercheJSONlistes :", "JSON NULL");
                return null;

            }

            try {
                // Vérifier le TAG_VALIDE
                int succes = json.getInt(TAG_VALIDE);

                if (succes == 1) {
                    // Présence de liste dans JSON
                    // Créer le tableau de listes
                    JSONlistes = json.getJSONArray(TAG_LISTE);

                    /*
                    Boucle permettant de parcourir le tableau JSONListes
                     */
                    for (int i = 0; i < JSONlistes.length(); i++) {

                        JSONObject c = JSONlistes.getJSONObject(i);
                        String idListe = c.getString(TAG_IDLISTE);
                        String titre = c.getString(TAG_TITRE);
                        String mclef1 = c.getString(TAG_MOT_CLEF1);
                        String mclef2 = c.getString(TAG_MOT_CLEF2);
                        String mclef3 = c.getString(TAG_MOT_CLEF3);

                        //Création d'une HasMap à chaque itération
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TAG_IDLISTE, idListe);
                        map.put(TAG_TITRE, titre);
                        map.put(TAG_MOT_CLEF1, mclef1);
                        map.put(TAG_MOT_CLEF2, mclef2);
                        map.put(TAG_MOT_CLEF3, mclef3);

                        //ajout de la HashMap à l'ArrayList
                        resultatsListes.add(map);
                    }
                } else {
                    // Pas de résultat dans le tableau JSONmots
                    // Lancer une nouvelle activité EcranChoixMot
                    Intent i = new Intent(getApplicationContext(), Import_List_Search.class);
                    // Fermer toutes les autres activités
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /*
         Apres avoir terminé le travail en arrière-plan arrêter le progress dialog
         */
        protected void onPostExecute(String file_url) {

            if(pDialog != null)
                pDialog.dismiss();

            // metre à jour l'interface graphique
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Mettre à jour la ListView avec l'ArrayList resultatsListes
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            Import_List_Result.this, resultatsListes,
                            R.layout.activity_import_list_details,
                            new String[] {TAG_IDLISTE, TAG_TITRE, TAG_MOT_CLEF1, TAG_MOT_CLEF2, TAG_MOT_CLEF3},
                            new int[] { R.id.idListe, R.id.titreListe, R.id.mot_clef1, R.id.mot_clef2, R.id.mot_clef3 }
                    );
                    // Mise à jour de la listview
                    setListAdapter(adapter);
                }
            });

        }

    }

    public void newTitleDialog(){

            AlertDialog.Builder builder = new AlertDialog.Builder(Import_List_Result.this);
            builder.setTitle("Veuillez saisir un nouveau titre pour votre liste");

            final EditText input = new EditText(Import_List_Result.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            input.setText(titre);
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    titre = input.getText().toString();
                    new testNewTitle().execute();
                }
            });
            builder.show();
    }

    class testNewTitle extends AsyncTask<String, String, String>{

        boolean modificationTitre;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Import_List_Result.this);
            pDialog.setMessage("Checking list title");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args){

            modificationTitre=false;
            Cursor res0 = local.query("list_listed", new String[]{"id", "name"}, null,
                    null, null, null, null, null);

            int colonneTitre = res0.getColumnIndex("name");
            if(colonneTitre > -1) {
                while ( !modificationTitre && res0.moveToNext()){
                    if (titre.compareToIgnoreCase(res0.getString(colonneTitre)) == 0)
                        modificationTitre = true;
                }
            }
            res0.close();

            return null;
        }

        protected void onPostExecute(String file_url){

            pDialog.dismiss();

            if (modificationTitre){
                newTitleDialog();
            }else{
                new RecherchesMots().execute();
            }
        }
    }


    class RecherchesMots extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Import_List_Result.this);
            pDialog.setMessage("Getting list words");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args){

            // Construire les parametres pour la variable $_SESSION
            List<NameValuePair> parametre = new ArrayList<NameValuePair>();
            parametre.add(new BasicNameValuePair("idListe", pid));
            // Avoir la chaine de caractère JSON depuis l'urlVisualiserListe
            Log.i("RechercheJSONlistes :", jParser.makeHttpRequest(urlRecupererListe, "GET", parametre).toString());
            JSONObject json = jParser.makeHttpRequest(urlRecupererListe, "GET", parametre);

            try {
                // Vérifier le TAG_VALIDE
                int succes = json.getInt(TAG_VALIDE);

                if (succes == 1) {
                    // Présence des mots dans JSON
                    // Créer le tableau de mots
                    JSONmots = json.getJSONArray(TAG_MOT);

                    //ajout dans la base de donnée de la table
                    String query0 = "INSERT INTO list_listed VALUES (null, \""+titre+"\",\""+motclef1
                            +"\",\""+motclef2+"\",\""+motclef3+"\");";
                    local.execSQL(query0);

                    //Récupérer l'identifiant de la liste
                    Cursor res1 = local.query("list_listed", new String [] {"id","name"}, " name = ? ", new String []{titre},
                            null, null, null);
                    //vérifie la Google Doc pour le where
                    res1.moveToNext();
                    int colonneId;
                    if( ( colonneId = res1.getColumnIndex("id") ) > -1 ){
                        int tempId = res1.getInt(colonneId);
                        res1.close();

                        String query1 = "CREATE TABLE \"" +titre +"\" (num INTEGER, question TEXT, "
                                +"answer TEXT, PRIMARY KEY (num, question) );";
                        local.execSQL(query1);

                            /*
                            Boucle permettant de parcourir le tableau JSONListes et de créer
                             */
                        for (int i = 0; i < JSONmots.length(); i++) {

                            JSONObject c = JSONmots.getJSONObject(i);
                            String question = c.getString(TAG_QUESTION);
                            String reponse = c.getString(TAG_REPONSE);

                            String query2 = "INSERT INTO \""+titre+"\" VALUES (\""+tempId+"\",\""+question
                                    +"\",\""+reponse+"\");";
                            local.execSQL(query2);
                        }

                    }

                } else {
                    // Pas de résultat dans le tableau JSONmots
                    // Lancer une nouvelle activité EcranChoixMot
                    Intent i = new Intent(getApplicationContext(), Import_List_Search.class);
                    // Fermer toutes les autres activités
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {

            if(pDialog != null)
                pDialog.dismiss();

            Toast.makeText(getApplicationContext(),"List added", Toast.LENGTH_SHORT).show();
        }

    }

}
