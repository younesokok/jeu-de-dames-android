package android.dames;

public class Coordonnees {
	public int x;
	public int y;

	public Coordonnees(int newX, int newY) {
		x = newX;
		y = newY;
	}

	public boolean equals(Coordonnees aTester) {
		if (x == aTester.x && y == aTester.y) {
			return true;
		}
		return false;
	}
}

