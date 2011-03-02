package android.dames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Tour {
	/* --- Attributs --- */
	private int idPartie;
	private int numero;
	private Map<Integer, Integer> deplacementsPionsJoue;
	private List<Integer> pionsManges;
	
	/* --- Constructeurs --- */
	public Tour() {
		deplacementsPionsJoue = new HashMap<Integer, Integer>();
		pionsManges = new ArrayList<Integer>();
	}
	
	/* --- Méthodes --- */
	
	/* --- Getter, setter --- */
	public String toString() {
		StringBuffer sb = new StringBuffer("Tour "+numero+" de la partie "+idPartie+" : \n");
		sb.append("Déplacements du pion joué : \n");
		for (Entry<Integer, Integer> deplacement : deplacementsPionsJoue.entrySet()) {
			sb.append("de la case "+deplacement.getKey()+" à la case "+deplacement.getValue()+"\n");
		}
		sb.append("Pions mangés : \n");
		for (Integer pion : pionsManges) {
			sb.append("pion de la case "+pion+"\n");
		}
		return sb.toString();
	}
	
	public void setIdPartie(int idPartie) {
		this.idPartie = idPartie;
	}
	public int getIdPartie() {
		return idPartie;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public int getNumero() {
		return numero;
	}
	public void setDeplacementsPionsJoue(Map<Integer, Integer> deplacementsPionsJoue) {
		this.deplacementsPionsJoue = deplacementsPionsJoue;
	}
	public Map<Integer, Integer> getDeplacementsPionsJoue() {
		return deplacementsPionsJoue;
	}
	public void setPionsManges(List<Integer> pionsManges) {
		this.pionsManges = pionsManges;
	}
	public List<Integer> getPionsManges() {
		return pionsManges;
	}
}
