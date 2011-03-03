<?php
if (isset($_GET['sens']) && NULL != $_GET['sens'] && '' != $_GET['sens']) {
	if ('receive' == $_GET['sens']) {
		header("Content-Type: text/xml" );
		echo '<?xml version="1.0" encoding="ISO-8859-1"?>'."\n".'
		<tour>'."\n".'
			<idPartie>1</idPartie>'."\n".'
			<numero>3</numero>'."\n".'
			<deplacementsPionJoue>
				<deplacement positionIn="1" positionOut="4" />
				<deplacement positionIn="4" positionOut="2" />
			</deplacementsPionJoue>'."\n".'
			<pionsManges>'."\n".'
				<pion position="3" />'."\n".'
				<pion position="5" />'."\n".'
			</pionsManges>'."\n".'
		</tour>';
	}
}
?>