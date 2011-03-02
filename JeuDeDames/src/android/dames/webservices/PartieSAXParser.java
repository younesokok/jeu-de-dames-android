package android.dames.webservices;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.dames.Tour;

public class PartieSAXParser extends DefaultHandler {
	/* --- Attributs --- */
    /**
     * Partie récupérée via le fichier XML
     */
    private Tour tour;
    /**
     * flags nous indiquant la position du parseur
     */
	private boolean inTour, inIdPartie, inNumeroTour, inIdPionJoue;
	/**
	 * buffer nous permettant de récupérer les données 
	 */
	private StringBuffer buffer;

    
    /* --- Constructeurs --- */
	public PartieSAXParser() {
		super();
	}
	
	/* --- Méthodes --- */
	public void startElement(String uri, String localName, String qName, Attributes attribs) throws SAXException {
		if(qName.equals("partie")) {
			tour = new Tour();
			inTour = true;
		}
		else {
			buffer = new StringBuffer();
			if(qName.equals("id")){
				inIdPartie = true;
			}
			else if(qName.equals("numeroTour")){
				inNumeroTour = true;
			}
			else if(qName.equals("idPionJoue")){
				inIdPionJoue = true;
			}
			else{
				throw new SAXException("Balise "+qName+" inconnue.");
			}
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equals("partie")){
			inTour = false;
		}
		else if(qName.equals("id")){
			tour.setIdPartie(Integer.parseInt(buffer.toString()));
			buffer = null;
			inIdPartie = false;
		}
		else if(qName.equals("numeroTour")){
			tour.setNumeroTour(Integer.parseInt(buffer.toString()));
			buffer = null;
			inNumeroTour = false;
		}
		else if(qName.equals("idPionJoue")){
			tour.setIdPionJoue(Integer.parseInt(buffer.toString()));
			buffer = null;
			inIdPionJoue = false;
		}
		else{
			throw new SAXException("Balise "+qName+" inconnue.");
		}          

	}

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
