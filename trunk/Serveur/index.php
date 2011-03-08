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
				$tour = new Tour(array('idPartie' => $_GET['idPartie'], 'numero' => $_GET['numero']));
				$tour->getTourCourant();
			}
			header("Content-Type: text/xml" );
			echo $tour->getXML();
		}
	}
	else if ('send' == $_GET['sens']) {
		if (isset($_GET['action']) && NULL != $_GET['action']) {
			if ('sendTourFini' == $_GET['action']) {
				$tour = new Tour($_GET);
			}
			header("Content-Type: text/xml" );
			echo $tour->getXML();
		}
	}
}
?>