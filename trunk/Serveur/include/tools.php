<?php

define('EN_COURS', 1);
define('ATTENTE_AUTRE_JOUEUR', 0);
define('BLANC', 1);
define('NOIR', 0);
/**
 * Clean une chaine de caractères pour être un pseudo propre
 * @param string $pseudo Chaine de caractères à cleaner
 * @return string La chaine de caractère rewritée
*/
function cleanPseudo($pseudo)
{
	// Clean caractère
	$pseudo = preg_replace('![/@\'=_ -]!i', '-', $pseudo);
	$pseudo = preg_replace('![&~"#|`^()+{}[\]$£¤*µ%§\!:;\\\.,?°]!i', '', $pseudo);
	
	// Elagage final
	$pseudo = preg_replace('!-(d|l|m|qu|t)-!i', '-', $pseudo);
	$pseudo = preg_replace('!^(d|l|m|qu|t)-!i', '-', $pseudo);
	$pseudo = preg_replace('!-(d|l|m|qu|t)&!i', '-', $pseudo);
	$pseudo = preg_replace('!-{2,}!i', '-', $pseudo);
	$pseudo = preg_replace('!^-!i', '', $pseudo);
	$pseudo = preg_replace('!-$!i', '', $pseudo);

	return $pseudo;
}

function echa($array)
{
	echo '<pre>';
	print_r($array);
	echo '</pre>';
}

?>