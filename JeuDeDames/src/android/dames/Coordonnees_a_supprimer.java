package android.dames;

public class Coordonnees_a_supprimer {
	private int x;
	private int y;

	public Coordonnees_a_supprimer(int newX, int newY) {
		x = newX;
		y = newY;
	}

	public boolean equals(Coordonnees_a_supprimer aTester) {
		if (x == aTester.x && y == aTester.y) {
			return true;
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int newX) {
		x = newX;
	}

	public void setY(int newY) {
		y = newY;
	}
	
	public String toString(){
		return ("X : "+x+", Y : "+y);
	}
}

