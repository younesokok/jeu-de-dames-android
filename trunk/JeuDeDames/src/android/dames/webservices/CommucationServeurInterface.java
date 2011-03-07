package android.dames.webservices;

import java.net.URL;
import java.util.Map;

import android.dames.Tour;

public interface CommucationServeurInterface {
	/**
	 * Retourne l'URL du service Web
	 * @param url Url sans les paramètres
	 * @param params Liste des paramètrs (name=valeur)
	 * @return l'URL du service Web
	 */
	public URL buildUrl(String url, Map<String, String> params);
	/**
	 * Recherche une partie disponible ou en créé une
	 * @param String pseudo Pseudonyme choisi pour la partie
	 * @return Tour courant
	 */
	public Tour rejoindrePartie(String pseudo);
	/**
	 * Retourne le tour en cours à partir du serveur
	 * @param Tour tour Tour contenant l'id de la partie en cours
	 * @return tour courant
	 */
	public Tour getTourCourant(Tour tour);
	/**
	 * Envoi un nouveau tour terminé au serveur
	 * @param tour Tour terminé
	 * @return Tour courant (si tout s'est bien passé égale au tour terminé)
	 */
	public Tour sendTourFini(Tour tour);
}
