<?php
if (isset($_GET['sens']) && NULL != $_GET['sens'] && '' != $_GET['sens']) {
	include_once('include/Tour.php');

	if ('receive' == $_GET['sens']) {
		$tour = new Tour(array('idPartie' => 1, 'numero' => 3, 'deplacementsPionJoue' => '1:4;4:2', 'pionsManges' => '3:5', 'damesCreees' => ''));
		header("Content-Type: text/xml" );
		echo $tour->getXML();
	}
	else if ('send' == $_GET['sens']) {
		$tour = new Tour($_GET);
		header("Content-Type: text/xml" );
		echo $tour->getXML();
	}
}
?>