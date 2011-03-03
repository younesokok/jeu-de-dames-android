package android.dames;

import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
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
     * On definit les différents types de cases
     */
    private static final int BLANCHE_VIDE = 1;
    private static final int NOIRE_VIDE = 2;
    
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
		initDamierView();
	}

	public DamierView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initDamierView();
	}

	private void initDamierView() {
		setFocusable(true);
		Resources r = this.getContext().getResources();

		resetCases(6);
		loadCases(BLANCHE_VIDE, r.getDrawable(R.drawable.case_blanche));
		loadCases(NOIRE_VIDE, r.getDrawable(R.drawable.case_noire));
		/*
		loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar));
		*/
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
