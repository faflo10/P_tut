package com.iut.connexion_serveur_bdd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/*
Element indipensable pour faire fonctionner correctement cette application:
    ajouter dans le manifest d'android : <uses-permission android:name="android.permission.INTERNET" />
    la machine hôte  à pour adresse ip 10.0.2.2 depuis l'ADV
    récuperer la classe JSONParser

 */


public class EcranPrincipal extends Activity {


    Button boutonRListe;
    Button boutonRecupListe;
    Button boutonAjoutListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecran_principal);

        // Bouttons
        boutonRListe = (Button) findViewById(R.id.rechercheListe);
        boutonRecupListe = (Button) findViewById(R.id.recupereListe);
        boutonAjoutListe = (Button) findViewById(R.id.ajouterListe);

        // Rechercher liste
        boutonRListe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All JSONmots Activity
                Intent intent1 = new Intent(getApplicationContext(), EcranChoixMot.class);
                startActivity(intent1);

            }
        });

        // Récupérer liste
        boutonRecupListe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                /*
                Intent intent2 = new Intent(getApplicationContext(), NewProductActivity.class);
                startActivity(intent2);
                */
            }
        });

        // Ajouter liste
        boutonAjoutListe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                /*
                Intent intent2 = new Intent(getApplicationContext(), NewProductActivity.class);
                startActivity(intent2);
                */
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ecran_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
