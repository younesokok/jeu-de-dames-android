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
	 * Retourne le tour en cours à partir du serveur
	 * @return tour courant
	 */
	public Tour getTourCourant();
	/**
	 * Envoi un nouveau tour terminé au serveur
	 * @param tour Tour terminé
	 * @return Tour courant (si tout s'est bien passé égale au tour terminé)
	 */
	public Tour sendTourFini(Tour tour);
}
