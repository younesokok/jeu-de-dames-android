package android.dames;

public class Pion {
	private int x;
	private int y;
	private int type;
	
	/**
	 * On definit les différents types de cases
	 */
	private static final int PION_INDEFINI = 0; /* Pointeur de selection */
	private static final int PION_BLANC = 3;
	private static final int PION_NOIR = 4;
	private static final int DAME_NOIR = 5;
	private static final int DAME_BLANC = 6;
	private static final int PION_BLANC_SELECTED = 9;
	private static final int PION_NOIR_SELECTED = 10;
	private static final int DAME_NOIR_SELECTED = 11;
	private static final int DAME_BLANC_SELECTED = 12;

	public Pion(int newX, int newY, int newType) {
		x = newX;
		y = newY;
		type = newType;
	}
	
	public Pion(int newX, int newY) {
		x = newX;
		y = newY;
		type = 0;
	}

	public boolean equalsPosition(Pion aTester) {
		if (x == aTester.x && y == aTester.y) {
			return true;
		}
		return false;
	}
	
	public boolean equals(Pion aTester) {
		if (x == aTester.x && y == aTester.y && type == aTester.type) {
			return true;
		}
		return false;
	}
	
	public boolean equalsDiag(Pion aTester) {
		int diffX = x-aTester.x;
		if (diffX == y-aTester.y) {
			return true;
		}
		return false;
	}
	
	public int getDistance(Pion aTester) {
		return (Math.abs(x-aTester.x));
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getType() {
		return type;
	}

	public void setX(int newX) {
		x = newX;
	}

	public void setY(int newY) {
		y = newY;
	}
	
	public void setType(int newType) {
		y = newType;
	}
	
	public String toString(){
		return ("Pion de type : "+type+" en X : "+x+", Y : "+y);
	}
}

