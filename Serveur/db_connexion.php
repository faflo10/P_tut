<?php
 
/**
 * Fonction pour se connecter à la base de donnée
 */
function Connect_db(){

      $host="127.0.0.1"; 
      $user="root";      
      $password="root"; 
      $dbname="Elethink";
  
  try {
       
        $bdd=new PDO('mysql:host='.$host.';dbname='.$dbname.';charset=utf8',$user,$password);

      }catch (Exception $e){

        die('Erreur : '.$e->getMessage());

      }

      return $bdd ;
 }
 
?>