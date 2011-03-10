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
	private List<Integer> deplacementsPionJoue;
	private List<Integer> pionsManges;
	private List<Integer> dameCreee;
	
	/* --- Constructeurs --- */
	public Tour() {
		idPartie = 0;
		numero = 0;
		etat = DamierView.ATTENTE_AUTRE_JOUEUR;
		joueurs = new HashMap<String, Integer>();
		deplacementsPionJoue = new ArrayList<Integer>();
		pionsManges = new ArrayList<Integer>();
		dameCreee = new ArrayList<Integer>();
	}
	public Tour(int idPartie, int numero, int etat, Map<String, Integer> joueurs, List<Integer> deplacementsPionJoue, List<Integer> pionsManges, List<Integer> dameCreee) {
		this.idPartie = idPartie;
		this.numero = numero;
		this.etat = etat;
		this.joueurs = joueurs;
		this.deplacementsPionJoue = deplacementsPionJoue;
		this.pionsManges = pionsManges;
		this.dameCreee = dameCreee;
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
		for (Integer deplacement : deplacementsPionJoue) {
			sb.append("à la case "+deplacement+"\n");
		}
		sb.append(getStringDeplacementsPionJoue()+"\n");
		sb.append("Pions mangés : \n");
		for (Integer pion : pionsManges) {
			sb.append("pion de la case "+pion+"\n");
		}
		sb.append(getStringPionsManges()+"\n");
		sb.append("Dames créées : \n");
		for (Integer dame : dameCreee) {
			sb.append("dame à la case "+dame+"\n");
		}
		sb.append(getStringDameCreee()+"\n");
		return sb.toString();
	}
	
	public void preparerProchainTour() {
		incrNumero();
		razDeplacementsPionJoue();
		razPionsManges();
		razDameCreee();
	}
	public void setIdPartie(int idPartie) { this.idPartie = idPartie; }
	public int getIdPartie() { return idPartie; }
	public void setNumero(int numero) { this.numero = numero; }
	public void incrNumero() { this.numero = this.numero+1; }
	public int getNumero() { return numero; }
	public void setDeplacementsPionJoue(List<Integer> deplacementsPionJoue) { this.deplacementsPionJoue = deplacementsPionJoue; }
	public void razDeplacementsPionJoue() { deplacementsPionJoue.clear(); }
	public List<Integer> getDeplacementsPionJoue() { return deplacementsPionJoue; }
	public String getStringDeplacementsPionJoue() {
		StringBuffer sb = new StringBuffer();
		if (null != deplacementsPionJoue && deplacementsPionJoue.size() > 0) {
			for (Integer deplacement : deplacementsPionJoue) {
				sb.append(deplacement+";");
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
	public void setDameCreee(List<Integer> dameCreee) { this.dameCreee = dameCreee; }
	public void razDameCreee() { dameCreee.clear(); }
	public List<Integer> getDameCreee() { return dameCreee; }
	public String getStringDameCreee() {
		StringBuffer sb = new StringBuffer();
		if (null != dameCreee && dameCreee.size() > 0) {
			for (Integer dame : dameCreee) {
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
