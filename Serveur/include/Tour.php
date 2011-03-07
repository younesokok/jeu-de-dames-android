<?php

class Tour
{
	private $idPartie;
	private $numero;
	private $joueurs;
	private $deplacementsPionJoue;
	private $pionsManges;
	private $damesCreees;
	
	public function Tour($params)
	{
		return $this->_construct($params);
	}
	public function _construct($params)
	{
		$this->idPartie = intval(trim(@$params['idPartie']));
		$this->numero = intval(trim(@$params['numero']));
		$this->joueurs = $this->getListeStringUrl(@$params['joueurs']);
		$this->deplacementsPionJoue = $this->getMapUrl(@$params['deplacementsPionJoue']);
		$this->pionsManges = $this->getListeIntUrl(@$params['pionsManges']);
		$this->damesCreees = $this->getListeIntUrl(@$params['damesCreees']);
	}
	
	public function getXML()
	{
		$fichier = 'XML/'.date('Y-m-d', time()).'-partie-'.$this->idPartie.'-tour-'.$this->numero.'.xml';
		if (is_file($fichier)) {
			return file_get_contents($fichier);
		}
		$xml = '<?xml version="1.0" encoding="utf-8"?>'."\n".
		'<tour>'."\n".
			"\t".'<idPartie>'.$this->idPartie.'</idPartie>'."\n".
			"\t".'<numero>'.$this->numero.'</numero>'."\n";
			if (NULL != $this->joueurs && '' != $this->joueurs && count($this->joueurs) > 0) {
				$xml .= "\t".'<joueurs>'."\n";
				foreach($this->joueurs AS $joueur) {
					$xml .= "\t\t".'<joueur pseudo="'.$joueur.'" />'."\n";
				}
				$xml .= "\t".'</joueurs>'."\n";
			}
			if (NULL != $this->deplacementsPionJoue && '' != $this->deplacementsPionJoue && count($this->deplacementsPionJoue) > 0) {
				$xml .= "\t".'<deplacementsPionJoue>'."\n";
				foreach($this->deplacementsPionJoue AS $deplacement) {
					$xml .= "\t\t".'<deplacement positionIn="'.$deplacement[0].'" positionOut="'.$deplacement[1].'" />'."\n";
				}
				$xml .= "\t".'</deplacementsPionJoue>'."\n";
			}
			if (NULL != $this->pionsManges && '' != $this->pionsManges && count($this->pionsManges) > 0) {
				$xml .= "\t".'<pionsManges>'."\n";
				foreach($this->pionsManges AS $pion) {
					$xml .= "\t\t".'<pion position="'.$pion.'" />'."\n";
				}
				$xml .= "\t".'</pionsManges>'."\n";
			}
			if (NULL != $this->damesCreees && '' != $this->damesCreees && count($this->damesCreees) > 0) {
				$xml .= "\t".'<damesCreees>'."\n";
				foreach($this->damesCreees AS $dame) {
					$xml .= "\t\t".'<dame position="'.$dame.'" />'."\n";
				}
				$xml .= "\t".'</damesCreees>'."\n";
			}
		$xml .= '</tour>';
		$fd = file_put_contents($fichier, $xml);
		return $xml;
	}
	
	public function rejoindrePartie()
	{
		$pseudoAAjouter = $this->joueurs[0];
		// --- Parcours des fichiers (d'aujourd'hui) pour trouver une partie disponible
		$pattern = 'XML/'.date('Y-m-d', time()).'-partie-*-tour-*.xml';
		$fichiers = glob($pattern, GLOB_BRACE);
		$dom = new DomDocument();
		$trouve = false;
		foreach($fichiers as $fichier) {
			// Chargement de l'xml
			$dom->load($fichier);
			// Liste des joueurs
			$listeJoueurs = $dom->getElementsByTagName('joueurs')->item(0);
			$joueurs = $listeJoueurs->getElementsByTagName('joueur');
			// --- On a trouvé une partie avec 0 ou 1 seul joueur
			if (NULL != $joueurs &&
				($joueurs->length == 0 || ($joueurs->length == 1 && $joueurs->item(0)->getAttribute('pseudo') != $pseudoAAjouter))) {
				// --- Récupération de la partie
				$this->idPartie = preg_replace('!^.*partie-(\d+)-.*$!', '$1', $fichier);
				$this->numero = 0;
				$this->joueurs[1] = $joueurs->item(0)->getAttribute('pseudo');
				// --- Maj du fichier XML
				// Creation nouvelle balise
				$nouveauJoueur = $dom->createElement('joueur');
				$nouveauJoueur->setAttribute('pseudo', $pseudoAAjouter);
				// Insertion de la nouvelle balise
				
				$listeJoueurs->appendChild($nouveauJoueur);
				// Enregistrement de l'xml
				$nouveauXml = $dom->saveXML();
				if ($nouveauXml) {
					$trouve = true;
					file_put_contents($fichier, $nouveauXml);
					break;
				}
			}
			$dernierFichier = $fichier;
		}
		// --- Aucune partie disponible -> on en créé une
		if (!$trouve) {
			$this->idPartie = preg_replace('!^.*partie-(\d+)-.*$!', '$1', @$dernierFichier)+1;
			$this->numero = 0;
			// Création de l'xml
			$this->getXML();
		}
	}
	
	public static function getListeIntUrl($listeUrl)
	{
		if (NULL == $listeUrl || '' == $listeUrl) {
			return NULL;
		}
		return array_map('intval', array_map('trim', explode(';', $listeUrl)));
	}
	public static function getListeStringUrl($listeUrl)
	{
		if (NULL == $listeUrl || '' == $listeUrl) {
			return NULL;
		}
		return array_map('cleanPseudo', explode(';', $listeUrl));
	}
	public static function getMapUrl($mapUrl)
	{
		if (NULL == $mapUrl || '' == $mapUrl) {
			return NULL;
		}
		$elements = explode(';', $mapUrl);
		$map = array();
		foreach($elements AS $element) {
			$map[] = array_map('intval', array_map('trim', explode(':', $element)));
		}
		return $map;
	}
	
	public function getIdPartie() { return $this->idPartie; }
	public function setIdPartie($idPartie) { $this->idPartie = $idPartie; }
	public function getNumero() { return $this->numero; }
	public function setNumero($numero) { $this->numero = $numero; }
	public function getJoueurs() { return $this->joueurs; }
	public function setJoueurs($joueurs) { $this->joueurs = $joueurs; }
	public function getDeplacementsPionJoue() { return $this->deplacementsPionJoue; }
	public function setDeplacementsPionJoue($deplacementsPionJoue) { $this->deplacementsPionJoue = $deplacementsPionJoue; }
	public function getPionsManges() { return $this->pionsManges; }
	public function setPionsManges($pionsManges) { $this->pionsManges = $pionsManges; }
	public function getDamesCreees() { return $this->damesCreees; }
	public function setDamesCreees($pionsMadamesCreeesnges) { $this->damesCreees = $damesCreees; }
}

?>