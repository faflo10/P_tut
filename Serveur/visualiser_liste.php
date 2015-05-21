<?php
 
        /*
         *
         * Objectif : récupéer une liste de mot depuis la base de donnée distante
         * Utilisation de la méthode "post" de HTTP
         *
         */

        // tableau pour la reponse JSON
        $response = array();
         
        require_once("db_connexion.php");
        $bdd=Connect_db();


        if ( !empty($_POST['motsSaisis']) ){

            /*
                $tabNomListe=explode("|",$_SESSION['nomListe']);
                enventuellement gérer le passage d'un tableau de mot
            */


            //chercher la liste envoyé dans la base de donnée
            $query0=$bdd->prepare(
                                    'SELECT * FROM LISTE
                                    WHERE Titre LIKE ?
                                          OR Mot_clef1 LIKE ?
                                          OR Mot_clef2 LIKE ?
                                          OR Mot_clef3 LIKE ?
                                 ');
            
            $query0->execute( array('%'.$_POST['motsSaisis'].'%' , '%'.$_POST['motsSaisis'].'%' , '%'.$_POST['motsSaisis'].'%' ,
                                    '%'.$_POST['motsSaisis'].'%' ) );

            while( $data0=$query0->fetch() ){

                  $response["liste"][]=array(
                                              'idListe' => $data0['Identifiant_liste'],
                                              'titre' => $data0['Titre']
                                            );
            }

            if ( empty($response["liste"]) ){

                $response["valide"] = 0;
                $response["message"] = "Aucune liste correspondant à votre requête n'a été trouvé";

            }else{

                $response["valide"] = 1;
                $response["message"] = "Une ou plusieurs listes ont été trouvées";

            }               

        }else{

            //Champ $_POST['motsSaisis']
            $response["valide"] = 0;
            $response["message"] = "Champ nom liste vide";

        }

        echo ( json_encode($response) );

?>