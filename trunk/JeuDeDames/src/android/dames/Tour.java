package android.dames;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Tour implements Serializable {
	/* --- Attributs --- */
	private int idPartie;
	private int numero;
	private int etat;
	private Map<String, Integer> joueurs;
	private Map<Integer, Integer> deplacementsPionJoue;
	private List<Integer> pionsManges;
	private List<Integer> damesCreees;
	
	/* --- Constructeurs --- */
	public Tour() {
		idPartie = 0;
		numero = 0;
		etat = DamierView.ATTENTE_AUTRE_JOUEUR;
		joueurs = new HashMap<String, Integer>();
		deplacementsPionJoue = new HashMap<Integer, Integer>();
		pionsManges = new ArrayList<Integer>();
		damesCreees = new ArrayList<Integer>();
	}
	public Tour(int idPartie, int numero, int etat, HashMap<String, Integer> joueurs, Map<Integer, Integer> deplacementsPionJoue, List<Integer> pionsManges, List<Integer> damesCreees) {
		this.idPartie = idPartie;
		this.numero = numero;
		this.etat = etat;
		this.joueurs = joueurs;
		this.deplacementsPionJoue = deplacementsPionJoue;
		this.pionsManges = pionsManges;
		this.damesCreees = damesCreees;
	}
	
	/* --- Méthodes --- */
	
	/* --- Getter, setter --- */
	public String toString() {
		StringBuffer sb = new StringBuffer("Tour "+numero+" de la partie "+idPartie+" ("+(etat == DamierView.EN_COURS ? "en cours" : "attente autre joueur")+") : \n");
		sb.append("Joueurs : \n");
		int i = 1;
		for (Entry<String, Integer> joueur : joueurs.entrySet()) {
			sb.append("Joueur "+i+" : "+joueur.getKey()+" ("+(joueur.getValue() == DamierView.BLANC ? "blanc" : "noir")+")\n");
			i++;
		}
		sb.append(getStringJoueurs()+"\n");
		sb.append("Déplacements du pion joué : \n");
		for (Entry<Integer, Integer> deplacement : deplacementsPionJoue.entrySet()) {
			sb.append("de la case "+deplacement.getKey()+" à la case "+deplacement.getValue()+"\n");
		}
		sb.append(getStringDeplacementsPionJoue()+"\n");
		sb.append("Pions mangés : \n");
		for (Integer pion : pionsManges) {
			sb.append("pion de la case "+pion+"\n");
		}
		sb.append(getStringPionsManges()+"\n");
		sb.append("Dames créées : \n");
		for (Integer dame : damesCreees) {
			sb.append("dame à la case "+dame+"\n");
		}
		sb.append(getStringDamesCreees()+"\n");
		return sb.toString();
	}
	
	public void preparerProchainTour() {
		incrNumero();
		razDeplacementsPionJoue();
		razPionsManges();
		razDamesCreees();
	}
	public void setIdPartie(int idPartie) { this.idPartie = idPartie; }
	public int getIdPartie() { return idPartie; }
	public void setNumero(int numero) { this.numero = numero; }
	public void incrNumero() { this.numero++; }
	public int getNumero() { return numero; }
	public void setDeplacementsPionJoue(Map<Integer, Integer> deplacementsPionJoue) { this.deplacementsPionJoue = deplacementsPionJoue; }
	public void razDeplacementsPionJoue() { deplacementsPionJoue.clear(); }
	public Map<Integer, Integer> getDeplacementsPionJoue() { return deplacementsPionJoue; }
	public String getStringDeplacementsPionJoue() {
		StringBuffer sb = new StringBuffer();
		if (null != deplacementsPionJoue && deplacementsPionJoue.size() > 0) {
			for (Entry<Integer, Integer> deplacement : deplacementsPionJoue.entrySet()) {
				sb.append(deplacement.getKey()+":"+deplacement.getValue()+";");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	public void setPionsManges(List<Integer> pionsManges) { this.pionsManges = pionsManges; }
	public void razPionsManges() { pionsManges.clear(); }
	public List<Integer> getPionsManges() { return pionsManges; }
	public String getStringPionsManges() {
		StringBuffer sb = new StringBuffer();
		if (null != pionsManges && pionsManges.size() > 0) {
			for (Integer pion : pionsManges) {
				sb.append(pion+";");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	public void setDamesCreees(List<Integer> damesCreees) { this.damesCreees = damesCreees; }
	public void razDamesCreees() { damesCreees.clear(); }
	public List<Integer> getDamesCreees() { return damesCreees; }
	public String getStringDamesCreees() {
		StringBuffer sb = new StringBuffer();
		if (null != damesCreees && damesCreees.size() > 0) {
			for (Integer dame : damesCreees) {
				sb.append(dame+";");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	public void setJoueurs(HashMap<String, Integer> joueurs) { this.joueurs = joueurs; }
	public void razJoueurs() { joueurs.clear(); }
	public Map<String, Integer> getJoueurs() { return joueurs; }
	public String getStringJoueurs() {
		StringBuffer sb = new StringBuffer();
		if (null != joueurs && joueurs.size() > 0) {
			for (Entry<String, Integer> joueur : joueurs.entrySet()) {
				sb.append(joueur.getKey()+":"+joueur.getValue()+";");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	public void setEtat(int etat) { this.etat = etat; }
	public int getEtat() { return etat; }
}
