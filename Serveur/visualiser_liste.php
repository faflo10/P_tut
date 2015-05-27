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

            $tabMotsSaisis=explode("|",$_POST['motsSaisis']);
            


            //chercher les listes souhaités dans la base de donnée
            $query0=$bdd->prepare(
                                    'SELECT * FROM LISTE
                                    WHERE Titre LIKE lower(?)
                                          OR lower(Mot_clef1) LIKE lower(?)
                                          OR lower(Mot_clef2) LIKE lower(?)
                                          OR lower(Mot_clef3) LIKE lower(?)
                                 ');

            $init_tab=0;
            foreach ($tabMotsSaisis as $mot) {

              $query0->execute( array('%'.$mot.'%' , '%'.$mot.'%' , '%'.$mot.'%' ,'%'.$mot.'%' ) );

              while( $data0=$query0->fetch()  ){

                if($init_tab==0){

                    $response["liste"][]=array(
                                                  'idListe' => $data0['Identifiant_liste'],
                                                  'titre' => $data0['Titre'],
                                                  'motClef1' => $data0['Mot_clef1'],
                                                  'motClef2' => $data0['Mot_clef2'],
                                                  'motClef3' => $data0['Mot_clef3']
                                                );
                    $init_tab=1;

                }else{
                //si le tableau est initialisé, il faut éviter d'insérer des doublons dans le tableau "resultat"
                    for( $i=0; $i<count($response["liste"]) && $response["liste"][$i]['titre']!= $data0['Titre']; $i++  );

                    if( $i==count($response["liste"]) ){

                        $response["liste"][]=array(
                                                    'idListe' => $data0['Identifiant_liste'],
                                                    'titre' => $data0['Titre'],
                                                    'motClef1' => $data0['Mot_clef1'],
                                                    'motClef2' => $data0['Mot_clef2'],
                                                    'motClef3' => $data0['Mot_clef3']
                                                  );
                    }
                }

              }
              $query0->closeCursor();              
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