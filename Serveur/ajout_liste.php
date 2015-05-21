<?php
 
/*
 *
 * Objectif : ajouter une liste de mot à la base de donnée distante
 * Utilisation de la méthode "post" de HTTP
 *
 */
 
 
// vérification des champs
if ( !empty($_POST['tabListeCarac']) && !empty($_POST['tabListe']) ) {

 /* 
    explosion des tableaux
    tabListeCarac : contient les attributs caractéristiques de la liste : nom, mot-clef
    tabListe : contient les réponses et les questions
 */
    $tabListeCarac=explode( "|", $_POST['tabListeCarac'] );
    $tabListe=explode( "|", $_POST['tabListe'] );
 
    require_once("db_connexion.php");
    $bdd=Connect_db();
    
    // tableau pour la réponse JSON
    $response = array();
 
    
 
    //ajouter la liste envoyé dans la base de donnée
    $query0=$bdd->prepare(' INSERT INTO LISTE( Titre, Mot_clef1, Mot_clef2, Mot_clef3 ) VALUES(?,?,?,?) ');
    
    if ( !( $query0->execute($tabListeCarac) ) ){
        
        $response["valide"] = 0;
        $response["message"] = "L'indexation de la liste n'a pas été effectuée";

    }else{

        //récupérer l'identifiant de la liste nouvellement créée
        $query1=$bdd->prepare('SELECT Identifiant_liste FROM LISTE WHERE ? ');
        $query1->execute( array($tabListeCarac[0]) );
        $cpt=0;
        
        //inserer les mots composants la liste
        for(i=0; i<count($tabListe); i=i+2){

            $query2=$bdd->prepare('INSERT INTO MOT_CLEF(Identifiant_liste, Question, Reponse) VALUES(?,?,?) ');
            $res=$query2->execute( array($data1['Identifiant_liste'],$tabListeCarac[$i],$tabListeCarac[$i+1]) );

            if ($res){
                $cpt++;
            }
        }

        if( !($res*2 == $i) ){

            $response["valide"] = 0;
            $response["message"] = "L'insertion des mots composants la liste n'a pas été effectuée correctement";

        }else{

            $response["valide"] = 1;
            $response["message"] = "Votre liste a été correctement insérer";

        }


    }

    // envoi de la réponse
    echo ( json_encode($response) );
}
?>