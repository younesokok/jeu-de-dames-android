package android.dames.webservices;

import android.dames.DamierView;
import android.dames.Pion;
import android.dames.PlateauView;
import android.dames.Tour;
import android.os.Handler;
import android.util.Log;

public class ThreadAttenteTour extends Thread {
	private DamierView damierView;
	private Tour tourCourant;
	private Handler mHandler;
	
	public ThreadAttenteTour(DamierView damierView, Handler mHandler) {
		this.damierView = damierView;
		this.tourCourant = damierView.tourCourant;
		this.mHandler = mHandler;
	}

	@Override
	public void run() {
		// --- Maj des infos
		if (damierView.mCouleurJoueur == DamierView.BLANC) {
			damierView.texteJoueurBlanc = PlateauView.JOUEUR_J_ATTENTE_TOUR;
			damierView.texteJoueurNoir = PlateauView.JOUEUR_EN_JEU;
		}
		else {
			damierView.texteJoueurNoir = PlateauView.JOUEUR_J_ATTENTE_TOUR;
			damierView.texteJoueurBlanc = PlateauView.JOUEUR_EN_JEU;
		}
		damierView.texteJoueurBlancCoupPrecedent = "";
		damierView.texteJoueurNoirCoupPrecedent = "";
		
		// --- Récupération du tour de l'adversaire sur le serveur
		int numeroAncienTour = tourCourant.getNumero();
		tourCourant = damierView.communicationServeur.attendreNouveauTour(tourCourant);
		
		// --- Maj du jeu en conséquence
		damierView.mDeplacements.clear();
		// Si on a besoin de modifier
		if (tourCourant.getNumero() > numeroAncienTour) {
			// Maj des déplacements
			int lastDeplacement = -1;
			for (Integer deplacement : tourCourant.getDeplacementsPionJoue()) {
				if (lastDeplacement == -1) {
					lastDeplacement = deplacement;
					continue;
				}
				int index = 0;
				if(damierView.mCouleurJoueur == damierView.BLANC) {
					for (Pion p : damierView.mPionsNoir) {
						if (p.getNumeroCase() == lastDeplacement) {
							damierView.mPionsNoir.get(index).setXYParNumeroCase(deplacement);
							break;
						}
						index++;
					}
				}
				if(damierView.mCouleurJoueur == damierView.NOIR) {
					for (Pion p : damierView.mPionsBlanc) {
						if (p.getNumeroCase() == lastDeplacement) {
							damierView.mPionsBlanc.get(index).setXYParNumeroCase(deplacement);
							break;
						}
						index++;
					}
				}
				lastDeplacement = deplacement;
			}
			// Maj des pions mangés
			for (Integer pionMange : tourCourant.getPionsManges()) {
				int index = 0;
				if(damierView.mCouleurJoueur == damierView.BLANC) {
					for (Pion p : damierView.mPionsBlanc) {
						if (p.getNumeroCase() == pionMange) {
							damierView.mPionsBlanc.remove(index);
							break;
						}
						index++;
					}
				}
				if(damierView.mCouleurJoueur == damierView.NOIR) {
					for (Pion p : damierView.mPionsNoir) {
						if (p.getNumeroCase() == pionMange) {
							damierView.mPionsNoir.remove(index);
							break;
						}
						index++;
					}
				}
			}
			// Maj des dames
			for (Integer dameCreee : tourCourant.getDameCreee()) {
				int index = 0;
				if(damierView.mCouleurJoueur == damierView.BLANC) {
					for (Pion p : damierView.mPionsNoir) {
						if (p.getNumeroCase() == dameCreee) {
							damierView.mPionsNoir.get(index).setType(damierView.DAME_NOIR);
							break;
						}
						index++;
					}
				}
				if(damierView.mCouleurJoueur == damierView.NOIR) {
					for (Pion p : damierView.mPionsBlanc) {
						if (p.getNumeroCase() == dameCreee) {
							damierView.mPionsBlanc.get(index).setType(damierView.DAME_BLANC);
							break;
						}
						index++;
					}
				}
			}
		}
		
		// --- Maj des infos
		// Calcul string coup précédent
		StringBuffer coupPrecedent = new StringBuffer("Coup précédent : ");
		for (Integer deplacement : tourCourant.getDeplacementsPionJoue()) {
			Pion pion = new Pion(0, 0);
			pion.setXYParNumeroCase(deplacement);
			char lettreX = (char) (pion.getX()+65);
			coupPrecedent.append(lettreX+":"+(pion.getY()+1)+";");
		}
		coupPrecedent.deleteCharAt(coupPrecedent.length()-1);
		// Maj infos
		if (damierView.mCouleurJoueur == DamierView.BLANC) {
			damierView.texteJoueurBlanc = PlateauView.JOUEUR_JE_JOUE;
			damierView.texteJoueurNoir = PlateauView.JOUEUR_EN_ATTENTE_TOUR;
			damierView.texteJoueurBlancCoupPrecedent = "";
			damierView.texteJoueurNoirCoupPrecedent = coupPrecedent.toString();
		}
		else {
			damierView.texteJoueurNoir = PlateauView.JOUEUR_JE_JOUE;
			damierView.texteJoueurBlanc = PlateauView.JOUEUR_EN_ATTENTE_TOUR;
			damierView.texteJoueurBlancCoupPrecedent = coupPrecedent.toString();
			damierView.texteJoueurNoirCoupPrecedent = "";
		}

		// -- Ajout de ce tour au damier
		damierView.tourCourant = this.tourCourant;

		// --- On rend la main au joueur
		damierView.setEtat(damierView.SELECT);
		
		mHandler.post(damierView.mUpdateView);
	}
}
