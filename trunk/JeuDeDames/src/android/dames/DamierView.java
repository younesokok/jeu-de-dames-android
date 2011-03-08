package android.dames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
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
	private static final int PION_INDEFINI = 0; /* Pointeur de selection */
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
	private ArrayList<Pion> mPionsNoir = new ArrayList<Pion>();
	private ArrayList<Pion> mPionsBlanc = new ArrayList<Pion>();
	private ArrayList<Pion> mDeplacements = new ArrayList<Pion>();

	/**
	 * Scores des joueurs
	 */
	private int mScoreBlanc;
	private int mScoreNoir;

	/**
	 * Info sur le joueur courrent
	 */
	private int mCouleurJoueur;
	public static final int BLANC = 0;
	public static final int NOIR = 1;
	private static final int OBSERVATEUR = 2;
	private Tour tourCourant;
	
	/**
	 * Texte a afficher
	 */
	private TextView mStatusText;
	private Toast toast;
	private final String tag = "DamierView : ";

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
			setMode(RUNNING);
		}
		// Sinon, on est noir
		else {
			mCouleurJoueur = NOIR;
			mDeplacements.add(new Pion(0, 0));
			setMode(ATTENTE_TOUR_AUTRE_JOUEUR);
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
			case(ATTENTE_TOUR_AUTRE_JOUEUR):{
				// Gestion des règles lors de l'attente - quand ce n'est pas le tour du joueur
				Toast.makeText(getContext(), "Veuillez patienter pendant que\nvotre adversaire joue !", Toast.LENGTH_LONG).show();

				// --- Récupération des informations du serveur
				int numeroAncienTour = tourCourant.getNumero();
				tourCourant = communicationServeur.attendreTourCourant(tourCourant);
				
				// --- Maj du jeu en conséquence
				mDeplacements.clear();
				// Si on a besoin de modifier
				if (tourCourant.getNumero() > numeroAncienTour) {
					// Maj des déplacements
					for (Entry<Integer, Integer> deplacement : tourCourant.getDeplacementsPionJoue().entrySet()) {
						int index = 0;
						if(mCouleurJoueur == BLANC) {
							for (Pion p : mPionsNoir) {
								if (p.getNumeroCase() == deplacement.getKey()) {
									mPionsNoir.get(index).setXYParNumeroCase(deplacement.getValue());
									break;
								}
								index++;
							}
						}
						if(mCouleurJoueur == NOIR) {
							for (Pion p : mPionsBlanc) {
								if (p.getNumeroCase() == deplacement.getKey()) {
									mPionsBlanc.get(index).setXYParNumeroCase(deplacement.getValue());
									break;
								}
								index++;
							}
						}
					}
					// Maj des pions mangés
					for (Integer pionMange : tourCourant.getPionsManges()) {
						int index = 0;
						if(mCouleurJoueur == BLANC) {
							for (Pion p : mPionsNoir) {
								if (p.getNumeroCase() == pionMange) {
									mPionsNoir.remove(index);
									break;
								}
								index++;
							}
						}
						if(mCouleurJoueur == NOIR) {
							for (Pion p : mPionsBlanc) {
								if (p.getNumeroCase() == pionMange) {
									mPionsBlanc.remove(index);
									break;
								}
								index++;
							}
						}
					}
					// Maj des dames
					for (Integer dameCreee : tourCourant.getDamesCreees()) {
						int index = 0;
						if(mCouleurJoueur == BLANC) {
							for (Pion p : mPionsNoir) {
								if (p.getNumeroCase() == dameCreee) {
									mPionsNoir.get(index).setType(DAME_NOIR);
									break;
								}
								index++;
							}
						}
						if(mCouleurJoueur == NOIR) {
							for (Pion p : mPionsBlanc) {
								if (p.getNumeroCase() == dameCreee) {
									mPionsBlanc.get(index).setType(DAME_BLANC);
									break;
								}
								index++;
							}
						}
					}
				}
				
				
				// --- On rend la main au joueur
				Toast.makeText(getContext(), "A vous de jouer !", Toast.LENGTH_LONG).show();
				setEtat(SELECT);
				updateView();
				break;	
			}
			case(SELECT):{

				/* Gestion des règles lors de la selection d'un pion */			
				if(mCouleurJoueur==BLANC) {
					int index = 0;
					for (Pion p : mPionsBlanc) {
						if(p.equalsPosition(mDeplacements.get(mDeplacements.size()-1))){
							mDeplacements.remove(mDeplacements.size()-1);
							mDeplacements.add(new Pion(p.getX(),p.getY()));
							mDeplacements.add(new Pion(p.getX(),p.getY(),p.getType()));
							setEtat(PLAY);
						}
						index++;
					}
					updateView();
					return(true);
				}
				break;	
			}

			case(PLAY):{

				/* Gestion des règles lors du déplacement d'un pion */
				if(mCouleurJoueur==BLANC) {
					/* Si on repose le pion, on peut en choisir un autre */
					if(mDeplacements.get(mDeplacements.size()-1).equalsPosition(mDeplacements.get(mDeplacements.size()-2))) {
						mDeplacements.remove(mDeplacements.size()-1);
						setEtat(SELECT);
					}
					/* On vérifie déjà que le déplacement est diagonal */
					else if(mDeplacements.get(mDeplacements.size()-1).equalsDiag(mDeplacements.get(mDeplacements.size()-2))){
						/* On gere le cas d'un déplacement de 1 en diagonal */
						if(mDeplacements.get(mDeplacements.size()-1).getDistance(mDeplacements.get(mDeplacements.size()-2))==1) {
							/* On verifie si aucun pion blanc n'est présent sur cette case */
							int index = 0;
							for (Pion p : mPionsBlanc) {
								if(p.equalsPosition(mDeplacements.get(mDeplacements.size()-1))){
									/* Sinon on quitte */
									updateView();
									return(true);
								}
								index++;
							}
							/* On verifie si aucun pion noir n'est présent sur cette case */
							index = 0;
							for (Pion p : mPionsNoir) {
								if(p.equalsPosition(mDeplacements.get(mDeplacements.size()-1))){
									/* Sinon on quitte */
									updateView();
									return(true);
								}
								index++;
							}
							/* Si on arrive ici c'est que la case est vide */
							// On positionne le pion dans sa nouvelle case
							mPionsBlanc.add(mDeplacements.get(mDeplacements.size()-1));
							// On l'enleve de sa case précédente
							index = 0;
							for (Pion p : mPionsBlanc) {
								if(p.equalsPosition(mDeplacements.get(mDeplacements.size()-2))){
									mPionsBlanc.remove(index);
									break;
								}
								index++;
							}
							// On permet de continuer le déplacement
							mDeplacements.add(new Pion(mDeplacements.get(mDeplacements.size()-1).getX(),mDeplacements.get(mDeplacements.size()-1).getY()));
							toast = Toast.makeText(getContext(), "Validez votre prise dans le menu !", Toast.LENGTH_SHORT);
							toast.show();
							setEtat(AFFICHE);
							updateGame();
						}	
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
				if(mEtatPrecedent==PLAY) {
					toast = Toast.makeText(getContext(), "Vous devez relacher votre pion !", Toast.LENGTH_LONG);
					toast.show();
					setEtat(mEtatPrecedent);
					return(true);
				}	
				
				// --- Gestion des règles une fois tous les déplacements terminés
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
							}
							index++;
						}
					}
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
							tourCourant.getDamesCreees().add(nouvelleDame.getNumeroCase());
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
							tourCourant.getDamesCreees().add(nouvelleDame.getNumeroCase());
							break;
						}
						index++;
					}
				}
				
				// --- Envoi au serveur
				tourCourant.preparerProchainTour();
				// Ajout des déplacements du pion
				// Note : DeplacementsPionJoue prend en paramètre : positionInitiale -> positionSuivante, ..., positionIntermediaire -> positionFinale 
				int lastCase = -1;
				List<Pion> mDeplacementsInverse = mDeplacements;
				Collections.reverse(mDeplacementsInverse);
				for (Pion pion : mDeplacementsInverse) {
					if (lastCase == -1) {
						lastCase = pion.getNumeroCase();
						continue;
					}
					tourCourant.getDeplacementsPionJoue().put(lastCase, pion.getNumeroCase());
					lastCase = pion.getNumeroCase();
				}
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
		bundle_damier.putInt("mScoreNoir", mScoreNoir);
		bundle_damier.putInt("mScoreBlanc", mScoreBlanc);
		bundle_damier.putSerializable("tourCourant", tourCourant);

		return bundle_damier;
	}

	/**
	 * Methode de restauration du damier après une pause, rotation, etc.
	 * @return bundle_damier
	 */
	public void restoreState(Bundle bundle_damier) {
		setMode(PAUSE);

		mPionsNoir = pionArrayToArrayList(bundle_damier.getIntArray("mPionsNoir"));
		mPionsBlanc = pionArrayToArrayList(bundle_damier.getIntArray("mPionsBlanc"));
		mDeplacements = pionArrayToArrayList(bundle_damier.getIntArray("mSelection"));
		mScoreNoir = bundle_damier.getInt("mScoreNoir");
		mScoreBlanc = bundle_damier.getInt("mScoreBlanc");
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
		if(mEtat==SELECT||mEtat==PLAY) {
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
				mDeplacements.add(new Pion(0, 9));
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
			if (mMode == READY | mMode == LOSE) {
				/*
				 * Au debut ou en fin de partie on relance le jeu
				 */
				initNewGame();
				updateView();
				return (true);
			}
			if (mMode == PAUSE) {
				/*
				 * On continue apres la pause
				 */
				setMode(RUNNING);
				updateView();
				return (true);
			}
			// Si jamais on est dans le mode RUNNING
			if(mMode==RUNNING) {
				// Si on est sur la phase de selection du pion à jouer
				if(mEtat==SELECT||mEtat==PLAY) {
					if (mDeplacements.get(mDeplacements.size()-1).getY()>0){
						mDeplacements.get(mDeplacements.size()-1).setY(mDeplacements.get(mDeplacements.size()-1).getY()-1);
					}
					updateView();
					return(true);
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
					return(true);
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
					return(true);
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
					return(true);
				}
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
			updateGame();
			return true;
		}

		return super.onKeyDown(keyCode, msg);
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
