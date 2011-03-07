<?php
if (isset($_GET['sens']) && NULL != $_GET['sens'] && '' != $_GET['sens']) {
	include_once('include/tools.php');
	include_once('include/Tour.php');

	if ('receive' == $_GET['sens']) {
		if (isset($_GET['action']) && NULL != $_GET['action']) {
			if ('rejoindrePartie' == $_GET['action']) {
				$tour = new Tour(array('joueurs' => cleanPseudo($_GET['pseudo'])));
				$tour->rejoindrePartie();
			}
			else if ('getTourCourant' == $_GET['action']) {
				$tour = new Tour(array('idPartie' => 1, 'numero' => 3, 'joueurs' => 'Thomas:BN', 'deplacementsPionJoue' => '1:4;4:2', 'pionsManges' => '3:5', 'damesCreees' => ''));
			}
			header("Content-Type: text/xml" );
			echo $tour->getXML();
		}
	}
	else if ('send' == $_GET['sens']) {
		if (isset($_GET['action']) && NULL != $_GET['action']) {
			if ('sendPartieFinie' == $_GET['action']) {
				$tour = new Tour($_GET);
			}
			header("Content-Type: text/xml" );
			echo $tour->getXML();
		}
	}
}
?>