<?php
		if ( !empty($_POST) ){

		    	$tabListeCarac=array( $_POST['Titre'],$_POST['Mot_clef1'],$_POST['Mot_clef2'],$_POST['Mot_clef3'] );
		    	$tabListe=array( $_POST['Mot1'], $_POST['Mot2'], $_POST['Mot3'], $_POST['Mot4'] );

		    	if( !empty($tabListeCarac) && !empty($tabListe) ){

		    		$_POST['tabListeCarac']=implode( "|", $tabListeCarac );
		    		$_POST['tabListe']=implode( "|", $tabListe );
		    		header('Location: ajout_liste.php');
		    	}
		}else{

?>


<div>
			<form action=# method="post">
				<h3>Caractéristique listes</h3>
				<input placeholder="Titre" name="idListe"></input>
				<input placeholder="Mot_clef1" name="Mot_clef1"></input>
				<input placeholder="Mot_clef2" name="Mot_clef2"></input>
				<input placeholder="Mot_clef3" name="Mot_clef3"></input>

				<h3>Mots</h3>
				<input placeholder="Mot1" name="Mot1"></input>
				<input placeholder="Mot2" name="Mot2"></input>
				<input placeholder="Mot3" name="Mot3"></input>
				<input placeholder="Mot4" name="Mot4"></input>
				<button>Valider</button>
			</form>
</div>

<?php } ?>