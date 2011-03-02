package android.dames;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.TextView;

public class DamierView extends PlateauView {
    
	/**
	 * On definit l'etat du damier
	 */
	private int mMode = READY;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;
    
    /**
     * mPionsXXXX : liste des coordonnes des Pions de couleur XXXX
     * mDamesXXXX : liste des coordonnes des Dames de couleur XXXX
     */
    private ArrayList<Coordonnees> mPionsNoir = new ArrayList<Coordonnees>();
    private ArrayList<Coordonnees> mPionsBlanc = new ArrayList<Coordonnees>();
    private ArrayList<Coordonnees> mDamesNoir = new ArrayList<Coordonnees>();
    private ArrayList<Coordonnees> mDamesBlanc = new ArrayList<Coordonnees>();
    
	public DamierView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DamierView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setTextView(TextView findViewById) {
		// TODO Auto-generated method stub
		
	}

	public void restoreState(Bundle map) {
		// TODO Auto-generated method stub
		
	}

	public void setMode(int ready2) {
		// TODO Auto-generated method stub
		
	}

	public Bundle saveState() {
		// TODO Auto-generated method stub
		return null;
	}

}
