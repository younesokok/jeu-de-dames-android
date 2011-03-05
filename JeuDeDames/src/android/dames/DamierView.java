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

	// ----------------------- Initialisation -------------------- //

	/**
	 * On definit l'etat du damier
	 */
	private int mMode = READY;
	public static final int PAUSE = 0;
	public static final int READY = 1;
	public static final int RUNNING = 2;
	public static final int LOSE = 3;

	/**
	 * On definit l'etat du damier
	 */
	private int mEtat = SELECT;
	public static final int ATTENTE = 0;
	public static final int SELECT = 1;
	public static final int VALID = 2;
	//	public static final int ... = 3;

	/**
	 * On definit les différents types de cases
	 */
	private static final int CASE_BLANC = 1;
	private static final int CASE_NOIR = 2;
	private static final int PION_BLANC = 3;
	private static final int PION_NOIR = 4;
	private static final int DAME_NOIR = 5;
	private static final int DAME_BLANC = 6;
	private static final int CASE_BLANC_SELECTED = 7;
	private static final int CASE_NOIR_SELECTED = 8;
	private static final int PION_BLANC_SELECTED = 9;
	private static final int PION_NOIR_SELECTED = 10;
	private static final int DAME_NOIR_SELECTED = 11;
	private static final int DAME_BLANC_SELECTED = 12;

	/**
	 * mPionsXXXX : liste des coordonnes des Pions de couleur XXXX
	 * mDamesXXXX : liste des coordonnes des Dames de couleur XXXX
	 */
	private ArrayList<Coordonnees> mPionsNoir = new ArrayList<Coordonnees>();
	private ArrayList<Coordonnees> mPionsBlanc = new ArrayList<Coordonnees>();
	private ArrayList<Coordonnees> mDamesNoir = new ArrayList<Coordonnees>();
	private ArrayList<Coordonnees> mDamesBlanc = new ArrayList<Coordonnees>();
	private ArrayList<Coordonnees> mSelection = new ArrayList<Coordonnees>();

	/**
	 * Scores des joueurs
	 */
	private int mScoreBlanc;
	private int mScoreNoir;

	/**
	 * Texte a afficher
	 */
	private TextView mStatusText;

	/**
	 * Constructeur
	 * @param context
	 * @param attrs
	 */
	public DamierView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDamierView();
	}

	/**
	 * Constructeur
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public DamierView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initDamierView();
	}

	// ----------------------- Modèle -------------------- //

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
		mSelection.add(new Coordonnees(0,9));
	}

	public void setTextView(TextView newTextView) {
		mStatusText = newTextView;
	}

	/**
	 * Methode de sauvegarde du damier lors d'une pause, rotation, etc.
	 * @return bundle_damier
	 */
	public Bundle saveState() {
		Bundle bundle_damier = new Bundle();

		bundle_damier.putIntArray("mPionsNoir", coordArrayListToArray(mPionsNoir));
		bundle_damier.putIntArray("mPionsBlanc", coordArrayListToArray(mPionsBlanc));
		bundle_damier.putIntArray("mDamesNoir", coordArrayListToArray(mDamesNoir));
		bundle_damier.putIntArray("mDamesBlanc", coordArrayListToArray(mDamesBlanc));
		bundle_damier.putIntArray("mSelection", coordArrayListToArray(mSelection));
		bundle_damier.putInt("mScoreNoir", mScoreNoir);
		bundle_damier.putInt("mScoreBlanc", mScoreBlanc);

		return bundle_damier;
	}

	/**
	 * Methode de restauration du damier après une pause, rotation, etc.
	 * @return bundle_damier
	 */
	public void restoreState(Bundle bundle_damier) {
		setMode(PAUSE);

		mPionsNoir = coordArrayToArrayList(bundle_damier.getIntArray("mPionsNoir"));
		mPionsBlanc = coordArrayToArrayList(bundle_damier.getIntArray("mPionsBlanc"));
		mDamesNoir = coordArrayToArrayList(bundle_damier.getIntArray("mDamesNoir"));
		mDamesBlanc = coordArrayToArrayList(bundle_damier.getIntArray("mDamesBlanc"));
		mSelection = coordArrayToArrayList(bundle_damier.getIntArray("mSelection"));
		mScoreNoir = bundle_damier.getInt("mScoreNoir");
		mScoreBlanc = bundle_damier.getInt("mScoreBlanc");
	}

	// ----------------------- Vue -------------------- //

	/**
	 * Execute la VUE du damier
	 */
	private void initDamierView() {
		setFocusable(true);
		Resources r = this.getContext().getResources();

		resetCases(13);
		/* Préchargement des images de cases */
		loadCases(CASE_BLANC, r.getDrawable(R.drawable.case_blanche));
		loadCases(CASE_NOIR, r.getDrawable(R.drawable.case_noire));
		loadCases(PION_BLANC, r.getDrawable(R.drawable.case_pion_blanc));
		loadCases(PION_NOIR, r.getDrawable(R.drawable.case_pion_noir));
		loadCases(DAME_BLANC, r.getDrawable(R.drawable.case_dame_blanc));
		loadCases(DAME_NOIR, r.getDrawable(R.drawable.case_dame_noir));
		/* Les memes mais selectionnées*/
		loadCases(CASE_BLANC_SELECTED, r.getDrawable(R.drawable.case_blanche_selected));
		loadCases(CASE_NOIR_SELECTED, r.getDrawable(R.drawable.case_noire_selected));
		loadCases(PION_BLANC_SELECTED, r.getDrawable(R.drawable.case_pion_blanc_selected));
		loadCases(PION_NOIR_SELECTED, r.getDrawable(R.drawable.case_pion_noir_selected));
		loadCases(DAME_BLANC_SELECTED, r.getDrawable(R.drawable.case_dame_blanc_selected));
		loadCases(DAME_NOIR_SELECTED, r.getDrawable(R.drawable.case_dame_noir_selected));
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
		if(mEtat==SELECT) {
			setActif(mSelection.get(0).getX(), mSelection.get(0).getY());
		}
		invalidate();
	}

	private void updatePionsNoir() {
		int index = 0;
		for (Coordonnees c : mPionsNoir) {
			setCase(PION_NOIR, c.getX(), c.getY());
			index++;
		}		
	}

	private void updatePionsBlanc() {
		int index = 0;
		for (Coordonnees c : mPionsBlanc) {
			setCase(PION_BLANC, c.getX(), c.getY());
			index++;
		}
	}

	private void updateDamesNoir() {
		int index = 0;
		for (Coordonnees c : mDamesNoir) {
			setCase(DAME_NOIR, c.getX(), c.getY());
			index++;
		}		
	}

	private void updateDamesBlanc() {
		int index = 0;
		for (Coordonnees c : mDamesBlanc) {
			setCase(DAME_BLANC, c.getX(), c.getY());
			index++;
		}		
	}

	// ----------------------- Contrôleur -------------------- //

	/**
	 * Gestion des touches
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (mMode == READY | mMode == LOSE) {
				/*
				 * Au debut ou en fin de partie on relance le jeu
				 */
				initNewGame();
				setMode(RUNNING);
				update();
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
			// Si jamais on est dans le mode RUNNING
			if(mMode==RUNNING) {
				if(mEtat==SELECT) {
					if (mSelection.get(mSelection.size()-1).getY()>0){
						mSelection.get(mSelection.size()-1).setY(mSelection.get(mSelection.size()-1).getY()-1);
					}
					update();
					return(true);
				}
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if(mMode==RUNNING) {
				if(mEtat==SELECT) {
					if (mSelection.get(mSelection.size()-1).getY()<mNbCases-1){
						mSelection.get(mSelection.size()-1).setY(mSelection.get(mSelection.size()-1).getY()+1);
					}
					update();
					return(true);
				}
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if(mMode==RUNNING) {
				if(mEtat==SELECT) {
					if (mSelection.get(mSelection.size()-1).getX()>0){
						mSelection.get(mSelection.size()-1).setX(mSelection.get(mSelection.size()-1).getX()-1);
					}
					update();
					return(true);
				}
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if(mMode==RUNNING) {
				if(mEtat==SELECT) {
					if (mSelection.get(mSelection.size()-1).getX()<mNbCases-1){
						mSelection.get(mSelection.size()-1).setX(mSelection.get(mSelection.size()-1).getX()+1);
					}
					update();
					return(true);
				}
			}
		}

		return super.onKeyDown(keyCode, msg);
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

	// ----------------------- Methodes annexes -------------------- //

	/**
	 * Given a ArrayList of coordinates, we need to flatten them into an array of
	 * ints before we can stuff them into a map for flattening and storage.
	 * 
	 * @param cvec : a ArrayList of Coordinate objects
	 * @return : a simple array containing the x/y values of the coordinates
	 * as [x1,y1,x2,y2,x3,y3...]
	 */
	private int[] coordArrayListToArray(ArrayList<Coordonnees> cvec) {
		int count = cvec.size();
		int[] rawArray = new int[count * 2];
		for (int index = 0; index < count; index++) {
			Coordonnees c = cvec.get(index);
			rawArray[2 * index] = c.getX();
			rawArray[2 * index + 1] = c.getY();
		}
		return rawArray;
	}

	/**
	 * Given a flattened array of ordinate pairs, we reconstitute them into a
	 * ArrayList of Coordinate objects
	 * 
	 * @param rawArray : [x1,y1,x2,y2,...]
	 * @return a ArrayList of Coordinates
	 */
	private ArrayList<Coordonnees> coordArrayToArrayList(int[] rawArray) {
		ArrayList<Coordonnees> coordArrayList = new ArrayList<Coordonnees>();

		int coordCount = rawArray.length;
		for (int index = 0; index < coordCount; index += 2) {
			Coordonnees c = new Coordonnees(rawArray[index], rawArray[index + 1]);
			coordArrayList.add(c);
		}
		return coordArrayList;
	}


}
