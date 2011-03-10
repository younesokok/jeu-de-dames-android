package android.dames;

import android.widget.Toast;

public class ThreadAttenteTour extends Thread {
	private DamierView damierView;
	private Tour tourCourant;
	
	public ThreadAttenteTour(DamierView damierView) {
		this.damierView = damierView;
		this.tourCourant = damierView.tourCourant;
	}

	@Override
	public void run() {

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
			}
			// Maj des pions mangés
			for (Integer pionMange : tourCourant.getPionsManges()) {
				int index = 0;
				if(damierView.mCouleurJoueur == damierView.BLANC) {
					for (Pion p : damierView.mPionsNoir) {
						if (p.getNumeroCase() == pionMange) {
							damierView.mPionsNoir.remove(index);
							break;
						}
						index++;
					}
				}
				if(damierView.mCouleurJoueur == damierView.NOIR) {
					for (Pion p : damierView.mPionsBlanc) {
						if (p.getNumeroCase() == pionMange) {
							damierView.mPionsBlanc.remove(index);
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


		// --- On rend la main au joueur
		//Toast.makeText(getContext(), "A vous de jouer !", Toast.LENGTH_LONG).show();
		damierView.setEtat(damierView.SELECT);
		//updateView();
	}
}
