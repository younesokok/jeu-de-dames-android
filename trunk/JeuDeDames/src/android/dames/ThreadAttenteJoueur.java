package android.dames;

import android.util.Log;
import android.widget.Toast;

public class ThreadAttenteJoueur extends Thread {
	private DamierView damierView;
	private Tour tourCourant;
	
	public ThreadAttenteJoueur(DamierView damierView) {
		this.damierView = damierView;
		this.tourCourant = damierView.tourCourant;
	}

	@Override
	public void run() {
		Log.i("Debug", "TourCourant au départ : "+ tourCourant);
		tourCourant = damierView.communicationServeur.attendreAutreJoueur(tourCourant);
		// --- Adversaire arrivée : on rend la main au joueur
		//Toast.makeText(damierView.getContext(), "A vous de jouer !", Toast.LENGTH_LONG).show();
		damierView.tourCourant = tourCourant;
		Log.i("Debug", "TourCourant à l'arrivée : "+tourCourant);
		damierView.setEtat(damierView.SELECT);
		//damierView.updateView();
	}
}
