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
	private Map<Integer, Integer> deplacementsPionJoue;
	private List<Integer> pionsManges;
	private List<Integer> damesCreees;
	
	/* --- Constructeurs --- */
	public Tour() {
		deplacementsPionJoue = new HashMap<Integer, Integer>();
		pionsManges = new ArrayList<Integer>();
		damesCreees = new ArrayList<Integer>();
	}
	public Tour(int idPartie, int numero, Map<Integer, Integer> deplacementsPionJoue, List<Integer> pionsManges, List<Integer> damesCreees) {
		this.idPartie = idPartie;
		this.numero = numero;
		this.deplacementsPionJoue = deplacementsPionJoue;
		this.pionsManges = pionsManges;
		this.damesCreees = damesCreees;
	}
	
	/* --- Méthodes --- */
	
	/* --- Getter, setter --- */
	public String toString() {
		StringBuffer sb = new StringBuffer("Tour "+numero+" de la partie "+idPartie+" : \n");
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
	
	public void setIdPartie(int idPartie) { this.idPartie = idPartie; }
	public int getIdPartie() { return idPartie; }
	public void setNumero(int numero) { this.numero = numero; }
	public void incrNumero() { this.numero++; }
	public int getNumero() { return numero; }
	public void setDeplacementsPionJoue(Map<Integer, Integer> deplacementsPionJoue) { this.deplacementsPionJoue = deplacementsPionJoue; }
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
}
