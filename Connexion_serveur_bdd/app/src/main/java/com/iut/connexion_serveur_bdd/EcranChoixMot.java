package com.iut.connexion_serveur_bdd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by vaxz on 15/05/15.
 */
public class EcranChoixMot extends Activity {

    Button boutonChoixMot;
    EditText textMot;
    String motsSaisis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_mot);

        boutonChoixMot = (Button) findViewById(R.id.bouttonEnvoiMot);

        //rechercher liste
        boutonChoixMot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //effectuer la recherche de liste correspondant aux mots souhaités
                Intent rechercheListe = new Intent(getApplicationContext(), EcranRechercheListe.class);
                textMot = (EditText) findViewById(R.id.textMot);
                motsSaisis = textMot.getText().toString();

                rechercheListe.putExtra("IDListe", motsSaisis);
                Log.d("Envoi mots saisis", "Envoi mots saisis");
                startActivity(rechercheListe);

            }
        });


    }
}
