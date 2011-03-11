package android.dames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
	private int mEtatPrecedent = SELECT;
	public static final int AFFICHE = 0;
	public static final int SELECT = 1;
	public static final int PLAY = 2;
	public static final int VALID = 3;
	/**
	 * Variable signifiant que l'on attend que l'autre joueur joue
	 */
	public static final int ATTENTE_TOUR_AUTRE_JOUEUR = 4;
	/**
	 * Variabe signifiant que l'on attend qu'un autre joueur rejoigne la partie
	 */
	public static final int ATTENTE_AUTRE_JOUEUR = 5;
	/**
	 * Variable signifiant que la partie est finie
	 */
	public static final int FINI = 6;
	/**
	 * Variable générique précisant que la partie est en cours
	 * == AFFICHE, SELECT, PLAY, VALID, ATTENTE_TOUR_AUTRE_JOUEUR
	 */
	public static final int EN_COURS = 10;

	/**
	 * On definit les différents types de cases
	 */
	// THOMAS : changé en public pour test thread
	public static final int PION_INDEFINI = 0; /* Pointeur de selection */
	public static final int CASE_BLANC = 1;
	public static final int CASE_NOIR = 2;
	public static final int PION_BLANC = 3;
	public static final int PION_NOIR = 4;
	public static final int DAME_NOIR = 5;
	public static final int DAME_BLANC = 6;
	public static final int CASE_BLANC_SELECTED = 7;
	public static final int CASE_NOIR_SELECTED = 8;
	public static final int PION_BLANC_SELECTED = 9;
	public static final int PION_NOIR_SELECTED = 10;
	public static final int DAME_NOIR_SELECTED = 11;
	public static final int DAME_BLANC_SELECTED = 12;

	/**
	 * mPionsXXXX : liste des coordonnes des Pions de couleur XXXX
	 * mDamesXXXX : liste des coordonnes des Dames de couleur XXXX
	 */
	// THOMAS : changement pour test de thread de private en public
	public ArrayList<Pion> mPionsNoir = new ArrayList<Pion>();
	public ArrayList<Pion> mPionsBlanc = new ArrayList<Pion>();
	public ArrayList<Pion> mDeplacements = new ArrayList<Pion>();

	/**
	 * Scores des joueurs
	 */
	private int mScoreBlanc;
	private int mScoreNoir;

	/**
	 * Info sur le joueur courrent
	 */
	// THOMAS : changement en public pour test thread
	public int mCouleurJoueur;
	public static final int BLANC = 0;
	public static final int NOIR = 1;
	private static final int OBSERVATEUR = 2;
	// THOMAS : en public pour tests thread
	public Tour tourCourant;

	/**
	 * Texte a afficher
	 */
	private TextView mStatusText;
	private Toast toast;
	private final String tag = "DamierView : ";

	// THOMAS : New handler d'evenement (http://developer.android.com/guide/appendix/faq/commontasks.html#threading)
	/*
	 * Handler pour les retours d'info de thread et autre événement
	 */
    final Handler mHandler = new Handler();
    // Create runnable for posting
    final Runnable mUpdateView = new Runnable() {
        public void run() {
    		Toast.makeText(getContext(), "A vous de jouer !", Toast.LENGTH_LONG).show();
            updateView();
        }
    };
    
    
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

	void initNewGame() {
		/* On vide les listes de pions et dames */
		mPionsNoir.clear();
		mPionsBlanc.clear();

		/* On initialise les listes de pions */
		for(int i=0;i<5;i++) {
			for(int j=0;j<4;j++){
				mPionsNoir.add(new Pion((2*i)+(1+j)%2,j, PION_NOIR));
				mPionsBlanc.add(new Pion((2*i)+j%2,9-j, PION_BLANC));
			}
		}
		mScoreBlanc = 0;
		mScoreNoir = 0;
		// Si on attend un autre joueur c'est qu'on est le premier, donc on est blanc
		if (tourCourant.getEtat() == ATTENTE_AUTRE_JOUEUR) {
			mCouleurJoueur = BLANC;
			mDeplacements.add(new Pion(0, (PlateauView.mNbCasesCote-1)));
			setEtat(ATTENTE_AUTRE_JOUEUR);
		}
		// Sinon, on est noir
		else {
			mCouleurJoueur = NOIR;
			mDeplacements.add(new Pion(0, 0));
			setEtat(ATTENTE_TOUR_AUTRE_JOUEUR);
		}
	}

	public void setTextView(TextView newTextView) {
		mStatusText = newTextView;
	}

	/**
	 * On gère les régles ici
	 * @throws InterruptedException 
	 */
	public boolean updateGame(){
		switch(mMode) {
		case(RUNNING):
			switch(mEtat){
			// --- Attente qu'un adversaire rejoigne la partie
			case(ATTENTE_AUTRE_JOUEUR): {
				Log.i(tag, "Mode ATTENTE_AUTRE_JOUEUR ("+mCouleurJoueur+")");
				Toast.makeText(getContext(), "Veuillez patienter en attendant\n l'arrivée de votre adversaire !", Toast.LENGTH_LONG).show();
				// --- Récupération des informations du serveur
				// THOMAS : A mon avis il faut creer la methode ICI !!! et qu'elle fasse des updateView (ou invalidate) periodiquement
				// Encore mieux : Créer un thread qui ne fasse qu'attendre et qui set l'Etat qui c'est ok.
				// --> fait
				Thread attente = new ThreadAttenteJoueur(this,mHandler);
				attente.start();
				// THOMAS : Pourquoi pas d'updateView ici ? 
				updateView();
				break;	
			}
			case(ATTENTE_TOUR_AUTRE_JOUEUR):{
				Log.i(tag, "Mode ATTENTE_TOUR_AUTRE_JOUEUR ("+mCouleurJoueur+")");
				// Gestion des règles lors de l'attente - quand ce n'est pas le tour du joueur
				Toast.makeText(getContext(), "Veuillez patienter pendant que\nvotre adversaire joue !", Toast.LENGTH_LONG).show();

				// --- Récupération des informations du serveur
				// THOMAS : Idem, une thread pour éviter les pb d'attente
				// 
				Thread attente = new ThreadAttenteTour(this,mHandler);
				attente.start();
				
				updateView();
				break;	
			}
			case(SELECT):{

				/* Gestion des règles lors de la selection d'un pion */			
				List<Pion> listePions = null;
				if(mCouleurJoueur==BLANC) {
					listePions = mPionsBlanc;
				}
				else {
					listePions = mPionsNoir;
				}
				int index = 0;
				for (Pion p : listePions) {
					if(p.equalsPosition(mDeplacements.get(mDeplacements.size()-1))){
						mDeplacements.add(new Pion(p.getX(),p.getY(),p.getType()));
						setEtat(PLAY);
					}
					index++;
				}
				updateView();
				return(true);
			}

			case(PLAY):{

				List<Pion> mMesPions ;
				List<Pion> mPionsAdvserves ;
				List<Pion> mTousLesPions = new ArrayList<Pion>();

				/* Gestion des règles lors du déplacement d'un pion */
				if(mCouleurJoueur==BLANC) {
					mMesPions = mPionsBlanc ;
					mPionsAdvserves = mPionsNoir ;
				} else {
					mMesPions = mPionsNoir;
					mPionsAdvserves = mPionsBlanc  ;
				} 
				mTousLesPions.addAll(mPionsBlanc);
				mTousLesPions.addAll(mPionsNoir);
				/* Si on repose le pion, on peut en choisir un autre */
				Pion positionCourante = mDeplacements.get(mDeplacements.size()-1);
				Pion positionPrecedente = mDeplacements.get(mDeplacements.size()-2);
				if(positionCourante.equalsPosition(positionPrecedente)) {
					mDeplacements.remove(mDeplacements.size()-1);
					setEtat(SELECT);
				}
				/* On vérifie déjà que le déplacement est diagonal */
				else if(positionCourante.equalsDiag(positionPrecedente)){
					/* On gere le cas d'un déplacement de 1 en diagonal */
					if(positionCourante.getDistance(positionPrecedente) == 1 || Pion.is1PionPar1Pion(positionCourante, positionPrecedente, mPionsAdvserves)) {
						/* On verifie si aucun pion de mes pions ou un de mon adversaire n'est présent sur cette case */
						int index = 0;
						for (Pion p : mTousLesPions) {
							if(p.equalsPosition(positionCourante)){
								/* Sinon on quitte */
								updateView();
								return(true);
							}
							index++;
						}
						/* Si on arrive ici c'est que la case est vide */
						// On modifie l'emplacement du pion de l'index précédent
						index = 0;
						for (Pion p : mMesPions) {
							if(p.equalsPosition(positionPrecedente)){
								mMesPions.set(index, new Pion(positionCourante));
								break;
							}
							index++;
						}
						// On permet de continuer le déplacement
						//mDeplacements.add(new Pion(positionCourante));

						mDeplacements.add(new Pion(positionCourante.getX(), positionCourante.getY()));
						toast = Toast.makeText(getContext(), "Validez votre prise dans le menu !", Toast.LENGTH_SHORT);
						toast.show();
						// THOMAS : à mon avis il faut ici SELECT et
						//setEtat(AFFICHE);
						setEtat(SELECT);
						// puis faire un updateview (en dessous) par game
						// updateGame();
						// enfin on réaffiche le pointeur et on peut reprendre etc.
					}	
					updateView();
					return(true);
				}
				break;
			}
			/*
			 * Gestion des résultats/prises et génération de la demande à envoyer au serveur !!!
			 */
			case(VALID):{
				int index = 0;
				for (Pion p : mDeplacements) {
					Log.i("Debug","Contenu des déplacements :");
					Log.i("Debug","Déplacement "+index+" : X="+p.getX()+", Y="+p.getY()+" et type="+p.getType());
					index++;
				}
				/* Gestion d'un validation sans déplacement */
				if(mDeplacements.size()<=1) {
					toast = Toast.makeText(getContext(), "Vous devez au moins bouger un pion !", Toast.LENGTH_LONG);
					toast.show();
					setEtat(mEtatPrecedent);
					return(true);
				}	
				/* Gestion d'un pion levé - mais pas joué */
				// TODO
				if(mEtatPrecedent==PLAY) {
					toast = Toast.makeText(getContext(), "Vous devez relacher votre pion !", Toast.LENGTH_LONG);
					toast.show();
					setEtat(mEtatPrecedent);
					return(true);
				}	

				// --- Gestion des règles une fois tous les déplacements terminés
				// Preparation pour l'envoi au serveur
				tourCourant.preparerProchainTour();
				// On enleve les pions/dames pris
				Pion lastDeplacement = null;
				for (Pion deplacement : mDeplacements) {
					if (lastDeplacement == null) {
						lastDeplacement = deplacement;
						continue;
					}
					index = 0;
					if (mCouleurJoueur == BLANC) {
						for (Pion pion : mPionsNoir) {
							// TODO : vérifier la règle de prise d'un pion
							if (pion.equalsDiag(deplacement) && pion.entre2Pions(lastDeplacement, deplacement)) {
								// Ajout à la liste des pions mangés
								tourCourant.getPionsManges().add(pion.getNumeroCase());
								// On le retire de la liste des pions
								mPionsNoir.remove(index);
								break;
							}
							index++;
						}
					}
					if (mCouleurJoueur == NOIR) {
						for (Pion pion : mPionsBlanc) {
							// TODO : vérifier la règle de prise d'un pion
							if (pion.equalsDiag(deplacement) && pion.entre2Pions(lastDeplacement, deplacement)) {
								// Ajout à la liste des pions mangés
								tourCourant.getPionsManges().add(pion.getNumeroCase());
								// On le retire de la liste des pions
								mPionsBlanc.remove(index);
								break;
							}
							index++;
						}
					}
					tourCourant.getDeplacementsPionJoue().add(lastDeplacement.getNumeroCase());
					// On incrémente le déplacement précédent
					lastDeplacement = deplacement;
				}
				// On transforme les pions en dames
				Pion pCourant = mDeplacements.get(mDeplacements.size()-2);
				index = 0;
				if(pCourant.getType()==PION_BLANC && pCourant.getY()==0) {
					for (Pion p : mPionsBlanc) {
						if(p.equalsPosition(pCourant)) {
							Pion nouvelleDame = new Pion(p.getX(),p.getY(),DAME_BLANC);
							mPionsBlanc.set(index, nouvelleDame);
							tourCourant.getDameCreee().add(nouvelleDame.getNumeroCase());
							break;
						}
						index++;
					}
				}
				if(pCourant.getType() == PION_NOIR && pCourant.getY() == (PlateauView.mNbCasesCote-1)) {
					for (Pion p : mPionsNoir) {
						if(p.equalsPosition(pCourant)) {
							Pion nouvelleDame = new Pion(p.getX(),p.getY(),DAME_NOIR);
							mPionsNoir.set(index, nouvelleDame);
							tourCourant.getDameCreee().add(nouvelleDame.getNumeroCase());
							break;
						}
						index++;
					}
				}
				
//				// On MAJ des déplacements du tour courant (il faut inverser)
//				List<Pion> mDeplacementsInverse = mDeplacements;
//				Collections.reverse(mDeplacementsInverse);
//				int i = 0;
//				for (Pion pion : mDeplacementsInverse) {
//					if (i == 0) {
//						i++;
//						continue;
//					}
//					tourCourant.getDeplacementsPionJoue().add(pion.getNumeroCase());
//				}

				
				// --- Envoi au serveur
				Log.i(tag, "Avant d'envoiyer sendTourFini");
				Log.i(tag, tourCourant.toString());
				communicationServeur.sendTourFini(tourCourant);

				// --- Remise en attente
				setEtat(ATTENTE_TOUR_AUTRE_JOUEUR);
				updateGame();
				return(true);
				//break;
			}
			} // Fin SWITCH(mEtat)
		break; //Fin Case RUNNING
		} // Fin SWITCH(mMode)
		/* On arrive le résultat */
		updateView();
		return false;
	}

	/**
	 * Methode de sauvegarde du damier lors d'une pause, rotation, etc.
	 * @return bundle_damier
	 */
	public Bundle saveState() {
		Bundle bundle_damier = new Bundle();

		bundle_damier.putIntArray("mPionsNoir", pionArrayListToArray(mPionsNoir));
		bundle_damier.putIntArray("mPionsBlanc", pionArrayListToArray(mPionsBlanc));
		bundle_damier.putIntArray("mSelection", pionArrayListToArray(mDeplacements));
		bundle_damier.putInt("mEtat", mEtat);
		bundle_damier.putInt("mMode", mMode);
		bundle_damier.putSerializable("tourCourant", tourCourant);

		return bundle_damier;
	}

	/**
	 * Methode de restauration du damier après une pause, rotation, etc.
	 * @return bundle_damier
	 */
	public void restoreState(Bundle bundle_damier) {
		mPionsNoir = pionArrayToArrayList(bundle_damier.getIntArray("mPionsNoir"));
		mPionsBlanc = pionArrayToArrayList(bundle_damier.getIntArray("mPionsBlanc"));
		mDeplacements = pionArrayToArrayList(bundle_damier.getIntArray("mSelection"));
		mMode = bundle_damier.getInt("mMode");
		mEtat = bundle_damier.getInt("mEtat");
		tourCourant = (Tour) bundle_damier.getSerializable("tourCourant");
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
	public void updateView() {
		if (mMode == RUNNING) {
			clearCases();
			updatePionsNoir();
			updatePionsBlanc();
		}
		if (mEtat==SELECT || mEtat==PLAY) {
			clearCases();
			updatePionsNoir();
			updatePionsBlanc();
			updateDeplacements();
		}
		invalidate();
	}

	private void updatePionsNoir() {
		int index = 0;
		for (Pion p : mPionsNoir) {
			setCase(p.getType(), p.getX(), p.getY());
			index++;
		}
	}

	private void updatePionsBlanc() {
		int index = 0;
		for (Pion p : mPionsBlanc) {
			setCase(p.getType(), p.getX(), p.getY());
			index++;
		}
	}

	private void updateDeplacements() {
		/** on ajoute un pointeur s'il y en a pas */
		int index = 0;
		for (Pion p : mDeplacements) {
			if(p.getType()==PION_INDEFINI)
				setActif(p.getX(), p.getY());
			else setCase(p.getType(), p.getX(), p.getY());
			index++;
		}		
	}

	public void setMode(int newMode) {
		int oldMode = mMode;
		mMode = newMode;

		if (newMode == RUNNING & oldMode != RUNNING) {
			mStatusText.setVisibility(View.INVISIBLE);
			updateView();
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

	public void setEtat(int newEtat){
		/* Creation du pointeur de selection */
		if(newEtat==SELECT) {
			if(mDeplacements.size()==0) {
				// Si on est blanc on commence en bas
				if (mCouleurJoueur == BLANC) {
					mDeplacements.add(new Pion(0, (PlateauView.mNbCasesCote-1)));
				}
				// Sinon, en haut
				else {
					mDeplacements.add(new Pion(0, 0));
				}
			}	
		}
		mEtatPrecedent=mEtat;
		mEtat=newEtat;
		Log.i("Debug","Etat : "+mEtat+" - old : "+mEtatPrecedent);
	}

	// ----------------------- Contrôleur -------------------- //

	/**
	 * Gestion des touches
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			// Si jamais on est dans le mode RUNNING
			if(mMode==RUNNING) {
				// Si on est sur la phase de selection du pion à jouer
				if(mEtat==SELECT||mEtat==PLAY) {
					if (mDeplacements.get(mDeplacements.size()-1).getY()>0){
						mDeplacements.get(mDeplacements.size()-1).setY(mDeplacements.get(mDeplacements.size()-1).getY()-1);
					}
					updateView();
				}
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if(mMode==RUNNING) {
				if(mEtat==SELECT||mEtat==PLAY) {
					if (mDeplacements.get(mDeplacements.size()-1).getY()<mNbCasesCote-1){
						mDeplacements.get(mDeplacements.size()-1).setY(mDeplacements.get(mDeplacements.size()-1).getY()+1);
					}
					updateView();
				}
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if(mMode==RUNNING) {
				if(mEtat==SELECT||mEtat==PLAY) {
					if (mDeplacements.get(mDeplacements.size()-1).getX()>0){
						mDeplacements.get(mDeplacements.size()-1).setX(mDeplacements.get(mDeplacements.size()-1).getX()-1);
					}
					updateView();
				}
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if(mMode==RUNNING) {
				if(mEtat==SELECT||mEtat==PLAY) {
					if (mDeplacements.get(mDeplacements.size()-1).getX()<mNbCasesCote-1){
						mDeplacements.get(mDeplacements.size()-1).setX(mDeplacements.get(mDeplacements.size()-1).getX()+1);
					}
					updateView();
				}
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
			updateGame();
		}
		return super.onKeyDown(keyCode, msg);
	}

	
	// TODO : Gérer le tactile
	/* pour gérer le 100% tactile...
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		this.onKeyDown(KeyEvent.KEYCODE_DPAD_UP, null);
		Toast.makeText(getContext(), "X : "+x+" - Y : "+y, Toast.LENGTH_LONG).show();
		return super.onTouchEvent(event);
	}
	*/

	// ----------------------- Methodes annexes -------------------- //

	/**
	 * Given a ArrayList of coordinates, we need to flatten them into an array of
	 * ints before we can stuff them into a map for flattening and storage.
	 * 
	 * @param cvec : a ArrayList of Coordinate objects
	 * @return : a simple array containing the x/y values of the coordinates
	 * as [x1,y1,x2,y2,x3,y3...]
	 */
	private int[] pionArrayListToArray(ArrayList<Pion> cvec) {
		int count = cvec.size();
		int[] rawArray = new int[count * 3];
		for (int index = 0; index < count; index++) {
			Pion c = cvec.get(index);
			rawArray[3 * index] = c.getX();
			rawArray[3 * index + 1] = c.getY();
			rawArray[3 * index + 2] = c.getType();
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
	private ArrayList<Pion> pionArrayToArrayList(int[] rawArray) {
		ArrayList<Pion> pionArrayList = new ArrayList<Pion>();

		int pionCount = rawArray.length;
		for (int index = 0; index < pionCount; index += 3) {
			Pion c = new Pion(rawArray[index], rawArray[index + 1], rawArray[index + 2]);
			pionArrayList.add(c);
		}
		return pionArrayList;
	}

	public void setTourCourant(Tour tourCourant) {
		this.tourCourant = tourCourant;
	}
	public void setTourCourant(Bundle bundle_tourCourant) {
		this.tourCourant = (Tour) bundle_tourCourant.getSerializable("tourCourant");
	}
	public Tour getTourCourant() {
		return tourCourant;
	}
}
