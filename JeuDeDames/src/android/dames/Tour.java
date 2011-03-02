package android.dames;

public class Tour {
	/* --- Attributs --- */
	private int idPartie;
	private int numeroTour;
	private int idPionJoue;
	
	/* --- Constructeurs --- */
	public Tour() {
		
	}
	
	/* --- Méthodes --- */
	
	/* --- Getter, setter --- */
	public void setIdPionJoue(int idPionJoue) {
		this.idPionJoue = idPionJoue;
	}
	public int getIdPionJoue() {
		return idPionJoue;
	}
	public void setNumeroTour(int numeroTour) {
		this.numeroTour = numeroTour;
	}
	public int getNumeroTour() {
		return numeroTour;
	}
	public void setIdPartie(int idPartie) {
		this.idPartie = idPartie;
	}
	public int getIdPartie() {
		return idPartie;
	}
	public String toString() {
		return "Id partie : "+idPartie+"\n"+
			"Numéro du tour dans la partie : "+numeroTour+"\n"+
			"Id du pion joué : "+idPionJoue;
	}
}
