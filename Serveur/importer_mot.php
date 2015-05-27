<?php
 
        /*
         *
         * Objectif : récupérer les mots associés à une liste désirée
         * Utilisation de la méthode "get" de HTTP
         *
         */

        // tableau pour la reponse JSON
        $response = array();
         
        require_once("db_connexion.php");
        $bdd=Connect_db();


        if ( !empty($_GET['idListe']) ){
            //ajouter un test si l'identifiant est bien un nombre

            $identifiant= (int) $_GET['idListe'];
                
            //chercher les mots associés à la liste dont l'identifiant a été envoyé
            $query0=$bdd->prepare(
                                    'SELECT * FROM MOT_CLEF
                                    WHERE Identifiant_liste = ?'
                                );
            $query0->execute( array( $identifiant ) );

            while( $data0=$query0->fetch() ){

                    $mots[]=array(
                                     'idListe' => $data0['Identifiant_liste'],
                                     'question' => $data0['Question'],
                                     'reponse' => $data0['Reponse']
                                 );
            }
            $query0->closeCursor();
                 

            if ( empty($mots) ){

                $response["valide"] = 0;
                $response["message"] = "Aucune liste n'a été trouvé avec votre identifiant";

            }else{

                $response["mots"] = $mots;
                $response["valide"] = 1;
                $response["message"] = "Les mots de la liste associé ont été trouvés";

            }            

        }else{

            $response["valide"] = 0;
            $response["message"] = "Champ identifiant liste vide";

        }

        echo ( json_encode($response) );

?>