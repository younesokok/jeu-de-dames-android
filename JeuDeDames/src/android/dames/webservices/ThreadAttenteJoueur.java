package android.dames.webservices;

import android.dames.DamierView;
import android.dames.PlateauView;
import android.dames.Tour;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class ThreadAttenteJoueur extends Thread {
	private DamierView damierView;
	private Tour tourCourant;
	private Handler mHandler;
    
	public ThreadAttenteJoueur(DamierView damierView, Handler mHandler) {
		this.damierView = damierView;
		this.tourCourant = damierView.tourCourant;
		this.mHandler = mHandler;
	}

	@Override
	public void run() {
		// --- Recherche du tourCourant
		Log.i("Debug", "TourCourant au départ : "+ tourCourant);
		tourCourant = damierView.communicationServeur.attendreAutreJoueur(tourCourant);
		// --- Adversaire arrivée : on rend la main au joueur
		//Toast.makeText(damierView.getContext(), "A vous de jouer !", Toast.LENGTH_LONG).show();
		damierView.tourCourant = tourCourant;
		
		// --- Maj des infos
		if (damierView.mCouleurJoueur == DamierView.BLANC) {
			damierView.texteJoueurBlanc = PlateauView.JOUEUR_JE_JOUE;
			damierView.texteJoueurNoir = PlateauView.JOUEUR_EN_ATTENTE_TOUR;
		}
		Log.i("Debug", "TourCourant à l'arrivée : "+tourCourant);
		damierView.setEtat(damierView.SELECT);
		
		mHandler.post(damierView.mUpdateView);
	}
}
