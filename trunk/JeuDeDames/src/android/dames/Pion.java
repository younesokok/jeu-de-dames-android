package android.dames;

import android.util.Log;

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
	
	public boolean equalsPosition(int numeroCaseATester) {
		if (numeroCaseATester == getNumeroCase()) {
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
		if (Math.abs(x-aTester.x) == Math.abs(y-aTester.y)) {
			return true;
		}
		return false;
	}
	
	public int getDistance(Pion aTester) {
		return (Math.abs(x-aTester.x));
	}
	
	/**
	 * Calcul le numéro de la case où se situe le pion à partir de ses coordonnées
	 * @return Numéro de la case
	 */
	public int getNumeroCase() {
		return y*PlateauView.mNbCasesCote+x;
	}
	
	/**
	 * Calcul (et maj) les coordonnées du pion case à partir de son numéro de case
	 * @param numeroCase Numéro de la case sur lequel est situé le pion
	 */
	public void setXYParNumeroCase(int numeroCase) {
		y = numeroCase%PlateauView.mNbCasesCote;
		x = numeroCase-y/PlateauView.mNbCasesCote;
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

