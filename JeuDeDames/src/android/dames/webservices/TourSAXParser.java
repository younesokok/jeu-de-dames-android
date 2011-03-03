package android.dames.webservices;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.dames.Tour;

public class TourSAXParser extends DefaultHandler {
	/* --- Attributs --- */
    /**
     * Partie récupérée via le fichier XML
     */
    private Tour tour;
	/**
	 * Buffer permettant de récupérer les données des balises
	 */
	private StringBuffer buffer;

    
    /* --- Constructeurs --- */
	public TourSAXParser() {
		super();
	}
	
	/* --- Méthodes --- */
	/**
     * Evenement recu a chaque fois que l'analyseur rencontre une balise xml ouvrante.
     * @param nameSpaceURI l'url de l'espace de nommage.
     * @param localName le nom local de la balise.
     * @param qName nom de la balise en version 1.0 <code>nameSpaceURI + ":" + localName</code>
     * @param attributs Liste des attributs de la balise courante
     * @throws SAXException si la balise ne correspond pas a ce qui est attendu,
     * comme par exemple non respect d'une dtd.
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
	public void startElement(String nameSpaceURI, String localName, String qName, Attributes attributs) throws SAXException {
		if(qName.equals("tour")) {
			tour = new Tour();
		}
		else {
			buffer = new StringBuffer();
			if(qName.equals("deplacement")){
				try {
					int positionIn = Integer.parseInt(attributs.getValue("positionIn"));
					int positionOut = Integer.parseInt(attributs.getValue("positionOut"));
					tour.getDeplacementsPionJoue().put(positionIn, positionOut);
				} catch(Exception e){
					throw new SAXException(e);
				}
			}
			else if(qName.equals("pion")){
				try {
					int position = Integer.parseInt(attributs.getValue("position"));
					tour.getPionsManges().add(position);
				} catch(Exception e){
					throw new SAXException(e);
				}
			}
			else if(qName.equals("dame")){
				try {
					int position = Integer.parseInt(attributs.getValue("position"));
					tour.getDamesCreees().add(position);
				} catch(Exception e){
					throw new SAXException(e);
				}
			}
		}
	}
	
	/**
     * Evenement recu a chaque fermeture de balise.
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
	public void endElement(String uri, String localName, String qName) {
		if(qName.equals("idPartie")){
			tour.setIdPartie(Integer.parseInt(buffer.toString()));
			buffer = null;
		}
		else if(qName.equals("numero")){
			tour.setNumero(Integer.parseInt(buffer.toString()));
			buffer = null;
		}
	}

	/**
     * Evenement recu a chaque fois que l'analyseur rencontre des caracteres (entre deux balises).
     * @param ch les caracteres proprement dits.
     * @param start le rang du premier caractere a traiter effectivement.
     * @param end le rang du dernier caractere a traiter effectivement
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
	public void characters(char[] ch,int start, int length) throws SAXException {
		String lecture = new String(ch, start, length);
		if(buffer != null) {
			buffer.append(lecture);       
		}
	}

	/* --- Getter, setter --- */
	public void setTour(Tour tour) {
		this.tour = tour;
	}

	public Tour getTour() {
		return tour;
	}
}
