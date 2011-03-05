package android.dames;

import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
    private static final int CASE_NOIR = 1;
    private static final int CASE_BLANC = 2;
    private static final int PION_NOIR = 3;
    private static final int PION_BLANC = 4;
    private static final int DAME_BLANC = 5;
    private static final int DAME_NOIR = 6;
    
	/**
	 * mPionsXXXX : liste des coordonnes des Pions de couleur XXXX
	 * mDamesXXXX : liste des coordonnes des Dames de couleur XXXX
	 */
	private ArrayList<Coordonnees> mPionsNoir = new ArrayList<Coordonnees>();
	private ArrayList<Coordonnees> mPionsBlanc = new ArrayList<Coordonnees>();
	private ArrayList<Coordonnees> mDamesNoir = new ArrayList<Coordonnees>();
	private ArrayList<Coordonnees> mDamesBlanc = new ArrayList<Coordonnees>();
	
	/**
	 * Scores des joueurs
	 */
	private int mScoreBlanc;
	private int mScoreNoir;
	
    /**
     * Texte a afficher
     */
    private TextView mStatusText;
    
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

		resetCases(7);
		Log.i("Debug", "Avant loadCases");
		loadCases(CASE_NOIR, r.getDrawable(R.drawable.case_blanche));
		loadCases(CASE_BLANC, r.getDrawable(R.drawable.case_noire));
		loadCases(PION_NOIR, r.getDrawable(R.drawable.case_pion_blanc));
		loadCases(PION_BLANC, r.getDrawable(R.drawable.case_pion_noir));
		loadCases(DAME_NOIR, r.getDrawable(R.drawable.case_dame_blanc));
		loadCases(DAME_BLANC, r.getDrawable(R.drawable.case_dame_noir));
	}
	
	/**
	 * Lancement du jeu
	 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (mMode == READY | mMode == LOSE) {
                /*
                 * Au debut ou en fin de partie on relance le jeu
                 */
Log.i("Debug", "Appel d'init");
                initNewGame();
Log.i("Debug", "Appel de set Mode RUNNING");
                setMode(RUNNING);
Log.i("Debug", "Appel d'update");
                update();
Log.i("Debug", "Fin d'update");
                return (true);
            }
            if (mMode == PAUSE) {
                /*
                 * On continue apres la pause
                 */
                setMode(RUNNING);
                update();
                return (true);
            }
        }
        return (true);
    }

	
    /**
     * Mets à jour les pions si necessaire.
     */
    public void update() {
        if (mMode == RUNNING) {
        	clearCases();
        	updatePionsNoir();
        	updatePionsBlanc();
        	updateDamesNoir();
        	updateDamesBlanc();
            }
        }

	private void updatePionsNoir() {
        int index = 0;
        for (Coordonnees c : mPionsNoir) {
	        setCase(PION_NOIR, c.x, c.y);
            index++;
        }		
	}
	
	private void updatePionsBlanc() {
        int index = 0;
        for (Coordonnees c : mPionsBlanc) {
	        setCase(PION_BLANC, c.x, c.y);
            index++;
        }
	}
	
	private void updateDamesNoir() {
        int index = 0;
        for (Coordonnees c : mDamesNoir) {
	        setCase(DAME_NOIR, c.x, c.y);
            index++;
        }		
	}
	
	private void updateDamesBlanc() {
        int index = 0;
        for (Coordonnees c : mDamesBlanc) {
	        setCase(DAME_BLANC, c.x, c.y);
            index++;
        }		
	}

	private void initNewGame() {
    	/* On vide les listes de pions et dames */
    	mPionsNoir.clear();
    	mPionsBlanc.clear();
    	mDamesNoir.clear();
    	mDamesBlanc.clear();
    	
    	/* On initialise les listes de pions */
        for(int i=0;i<5;i++) {
            for(int j=0;j<4;j++){
            	mPionsNoir.add(new Coordonnees((2*i)+(1+j)%2,j));
            	mPionsBlanc.add(new Coordonnees((2*i)+j%2,9-j));
            }
        }
        mScoreBlanc = 0;
        mScoreNoir = 0;
    }
    
	public void setTextView(TextView newTextView) {
        mStatusText = newTextView;
	}

	public void setMode(int newMode) {
        int oldMode = mMode;
        mMode = newMode;

        if (newMode == RUNNING & oldMode != RUNNING) {
            mStatusText.setVisibility(View.INVISIBLE);
            update();
            return;
        }

        Resources res = getContext().getResources();
        CharSequence str = "";
        if (newMode == PAUSE) {
            str = res.getText(R.string.mode_pause);
        }
        if (newMode == READY) {
            str = res.getText(R.string.mode_ready);
        }
        if (newMode == LOSE) {
            str = res.getString(R.string.mode_lose_prefix) + mScoreBlanc + "/" + mScoreNoir
                  + res.getString(R.string.mode_lose_suffix);
        }

        mStatusText.setText(str);
        mStatusText.setVisibility(View.VISIBLE);
	}

	public Bundle saveState() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void restoreState(Bundle map) {
		// TODO Auto-generated method stub

	}

}
