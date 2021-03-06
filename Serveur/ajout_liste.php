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
        $date = microtime(true);
        
        // tableau pour la réponse JSON
        $response = array();
        //echo ( json_encode($response) ); 
     
        //ajouter la liste envoyé dans la base de donnée
        $query0=$bdd->prepare(' INSERT INTO LISTE( Titre, Date, Mot_clef1, Mot_clef2, Mot_clef3 ) VALUES(?,?,?,?,?) ');

        if( count($tabListe)%2 != 0 ){

            $response["valide"] = 0;
            $response["message"] = "L'indexation de la liste n'a pas été effectuée";
            //echo ( json_encode($response) );

        }else{   
        
                if ( !( $query0->execute( array($tabListeCarac[0], $date, $tabListeCarac[1], $tabListeCarac[2], $tabListeCarac[3]) ) ) ){
                    
                    $response["valide"] = 0;
                    $response["message"] = "L'indexation de la liste n'a pas été effectuée";
                    //echo ( json_encode($response) );

                }else{

                    //récupérer l'identifiant de la liste nouvellement créée
                    $query1=$bdd->prepare('SELECT Identifiant_liste FROM LISTE WHERE Titre = ? AND Date = ?');
                    $query1->execute( array($tabListeCarac[0], $date ) );
                    $data1=$query1->fetch();
                    $identifiantListe=$data1['Identifiant_liste'];
                    $query1->closeCursor();

                    $cpt=0;
                    
                    //inserer les mots composants la liste
                    for($i=0; $i<count($tabListe); $i=$i+2){

                        $query2=$bdd->prepare('INSERT INTO MOT_CLEF(Identifiant_liste, Question, Reponse) VALUES(?,?,?) ');
                        $res=$query2->execute( array($identifiantListe, $tabListe[$i], $tabListe[$i+1]) );

                    }

                    $response["valide"] = 1;
                    $response["message"] = "Votre liste a été correctement insérer";
                }
        }        
    }
        
        echo ( json_encode($response) );
?>