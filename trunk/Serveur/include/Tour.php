<?php

class Tour
{
	private $idPartie;
	private $numero;
	private $deplacementsPionJoue;
	private $pionsManges;
	private $damesCreees;
	
	public function Tour($params)
	{
		return $this->_construct($params);
	}
	public function _construct($params)
	{
		$this->idPartie = intval(trim($params['idPartie']));
		$this->numero = intval(trim($params['numero']));
		$this->deplacementsPionJoue = $this->getMapUrl($params['deplacementsPionJoue']);
		$this->pionsManges = $this->getListeUrl($params['pionsManges']);
		$this->damesCreees = $this->getListeUrl($params['damesCreees']);
	}
	
	public function getXML()
	{
		$fichier = 'XML/partie-'.$this->idPartie.'-tour-'.$this->numero.'.xml';
		if (is_file($fichier)) {
			return file_get_contents($fichier);
		}
		$xml = '<?xml version="1.0" encoding="ISO-8859-1"?>'."\n".
		'<tour>'."\n".
			"\t".'<idPartie>'.$this->idPartie.'</idPartie>'."\n".
			"\t".'<numero>'.$this->numero.'</numero>'."\n";
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
		$fd = fopen($fichier, 'w+');
		fwrite($fd, $xml);
		fclose($fd);
		return $xml;
	}
	
	public static function getListeUrl($listeUrl)
	{
		if (NULL == $listeUrl || '' == $listeUrl) {
			return NULL;
		}
		return array_map('intval', array_map('trim', explode(';', $listeUrl)));
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
	public function getDeplacementsPionJoue() { return $this->deplacementsPionJoue; }
	public function setDeplacementsPionJoue($deplacementsPionJoue) { $this->deplacementsPionJoue = $deplacementsPionJoue; }
	public function getPionsManges() { return $this->pionsManges; }
	public function setPionsManges($pionsManges) { $this->pionsManges = $pionsManges; }
	public function getDamesCreees() { return $this->damesCreees; }
	public function setDamesCreees($pionsMadamesCreeesnges) { $this->damesCreees = $damesCreees; }
}

?>