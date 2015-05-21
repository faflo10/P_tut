package com.iut.connexion_serveur_bdd;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vaxz on 14/05/15.
 */
public class EcranRechercheListe extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog=null;
    // Creation de l'objet JSONParser
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> resultatsListes;
    // url pour recherche une liste souhaitée
    private static String url = "http://10.0.2.2:80/vaxz/www/visualiser_liste.php";
    // Nom des JSON Node
    private static final String TAG_VALIDE = "valide";
    private static final String TAG_LISTE = "liste";
    private static final String TAG_IDLISTE = "idListe";
    private static final String TAG_TITRE = "titre";
    // Tableau JSON des résultats de la recherche liste
    JSONArray JSONlistes = null;
    //Récupération des mots de recherche saisis par l'uilisateur
    String motsSaisis = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_liste);

        // Hashmap pour la ListView
        resultatsListes = new ArrayList<HashMap<String, String>>();
        // Lancer en arrière-plan la recherche de liste
        new RecherchesListes().execute();
        // Obtenir la listview
        ListView lv = getListView();

        /*
            Après avoir sélectionné une liste unique, on envoie vers EcranRécupérationListe
        */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Obtenir l'identifiant de la liste souhaitée dans le fichier XML activity_affiche_liste_individuellement
                String pid = ( (TextView) view.findViewById(R.id.idListe) ).getText().toString();
                //Démarrer un nouvel intent
                Intent intent0 = new Intent(getApplicationContext(), EcranRecuperationListe.class);
                //Envoyer l'identifiant de la liste à l'activité suivante
                intent0.putExtra(TAG_IDLISTE, pid);
                //Lancer l'activité en attendant un retour
                startActivityForResult(intent0, 100);
            }
        });

    }

    //Reponse depuis l'activité EcranRecuperationListe
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // tester uniquement le code de retour et non le code de la requête
        if (resultCode == 100) {
        /*
            Si le test est ok alors le téléchargement de tous les mots est OK
            Rechargement de l'écran
         */
            Intent intent2 = getIntent();
            finish();
            startActivity(intent2);
        }

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
            pDialog = new ProgressDialog(EcranRechercheListe.this);
            pDialog.setMessage("Recherche de liste en cours");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Obtenir les JSONmots correspondants au mots saisis
         * */
        protected String doInBackground(String... args) {

            //Récupérer les mots saisies par l'utilisateur
            Intent intent3 = getIntent();
            motsSaisis = intent3.getStringExtra("IDListe");
            //Log.d( "MotsSaisis :", motsSaisis);

            // Construire les parametres pour la variable $_SESSION
            List<NameValuePair> parametre = new ArrayList<NameValuePair>();
            parametre.add(new BasicNameValuePair("motsSaisis", motsSaisis));
            // Avoir la chaine de caractère JSON depuis l'url
            Log.d( "RechercheJSONlistes :", jParser.makeHttpRequest(url, "POST", parametre).toString() );
            JSONObject json = jParser.makeHttpRequest(url, "POST", parametre);

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

                        //Création d'une HasMap à chaque itération
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TAG_IDLISTE, idListe);
                        map.put(TAG_TITRE, titre);

                        //ajout de la HashMap à l'ArrayList
                        resultatsListes.add(map);
                    }
                } else {
                    // Pas de résultat dans le tableau JSONmots
                    // Lancer une nouvelle activité EcranChoixMot
                    Intent i = new Intent(getApplicationContext(), EcranChoixMot.class);
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
                            EcranRechercheListe.this, resultatsListes,
                            R.layout.activity_affiche_liste_individuellement,
                            new String[] {TAG_IDLISTE, TAG_TITRE},
                            new int[] { R.id.idListe, R.id.titreListe }
                                                            );
                    // Mise à jour de la listview
                    setListAdapter(adapter);
                }
            });

        }

    }
}
