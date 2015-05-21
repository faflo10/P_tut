package com.iut.connexion_serveur_bdd;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vaxz on 15/05/15.
 */
public class EcranRecuperationListe extends ListActivity{

    // Progress Dialog
    private ProgressDialog pDialog=null;
    // Creation de l'objet JSONParser
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> listeRécupération;
    // url pour recupérer une liste souhaitée
    private static String url = "http://10.0.2.2:80/vaxz/www/importer_mot.php";
    // Nom des JSON Node
    private static final String TAG_VALIDE = "valide";
    private static final String TAG_MOT = "mots";
    private static final String TAG_IDLISTE = "idListe";
    private static final String TAG_REPONSE = "reponse";
    private static final String TAG_QUESTION = "question";
    // Tableau JSON des résultats de la recherche liste
    JSONArray JSONmots = null;
    //Récupération des mots de recherche saisis par l'uilisateur
    String IDListe = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_liste);

        // Hashmap pour la ListView
        listeRécupération = new ArrayList<HashMap<String, String>>();
        // Lancer en arrière-plan la recherche de liste
        new RecuperationListe().execute();

    }



    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class RecuperationListe extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EcranRecuperationListe.this);
            pDialog.setMessage("Récupération de liste souhaitée");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Obtenir les JSONmots correspondants au mots saisis
         * */
        protected String doInBackground(String... args) {

            //Récupérer l'identifiant de la liste souhaité par l'utilisateur
            Intent intent4 = getIntent();
            IDListe = intent4.getStringExtra(TAG_IDLISTE);
            Log.d(TAG_IDLISTE+" : ", IDListe);

            // Construire les parametres pour la variable $_SESSION
            List<NameValuePair> parametre = new ArrayList<NameValuePair>();
            parametre.add(new BasicNameValuePair("idListe", IDListe));
            // Avoir la chaine de caractère JSON depuis l'url
            Log.i( "RechercheJSONlistes :", jParser.makeHttpRequest(url, "GET", parametre).toString() );
            JSONObject json = jParser.makeHttpRequest(url, "GET", parametre);

            //Vérifier le log cat pour la réponse JSON
            //Log.d( "RechercheJSONlistes :", json.toString() );

            if (json!=null) Log.d( "RechercheJSONmots :", json.toString() );
            else {

                Log.d( "RechercheJSONmots :", "JSON NULL");
                return null;

            }

            try {
                // Vérifier le TAG_VALIDE
                int succes = json.getInt(TAG_VALIDE);

                if (succes == 1) {
                    // Présence des mots dans JSON
                    // Créer le tableau de mots
                    JSONmots = json.getJSONArray(TAG_MOT);

                    /*
                    Boucle permettant de parcourir le tableau JSONListes
                     */
                    for (int i = 0; i < JSONmots.length(); i++) {

                        JSONObject c = JSONmots.getJSONObject(i);
                        String question = c.getString(TAG_QUESTION);
                        String reponse = c.getString(TAG_REPONSE);

                        //Création d'une HasMap à chaque itération
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TAG_QUESTION, question);
                        map.put(TAG_REPONSE, reponse);

                        //ajout de la HashMap à l'ArrayList
                        listeRécupération.add(map);
                    }
                } else {
                    // Pas de résultat dans le tableau JSONmots
                    // Lancer une nouvelle activité EcranChoixMot
                    Intent i = new Intent(getApplicationContext(), EcranRechercheListe.class);
                    // Fermer toutes les autres activités
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Apres avoir terminé le travail en arrière-plan arrêter le progress dialog
         * **/
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
                            EcranRecuperationListe.this, listeRécupération,
                            R.layout.activity_affiche_pair_question_reponse,
                            new String[] {TAG_QUESTION, TAG_REPONSE},
                            new int[] { R.id.question, R.id.reponse }
                    );
                    // Mise à jour de la listview
                    setListAdapter(adapter);
                }
            });

        }

    }



}
