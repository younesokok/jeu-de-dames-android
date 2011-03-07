package android.dames;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class JeuDeDamesActivity extends Activity {

	private DamierView mDamierView;
	private static String mBundleKey = "damier-view";

	/**
	 * Methode appelee lors du lancement de l'application, 
	 * d'une rotation ou du retour de pause
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.damier_layout);

        mDamierView = (DamierView) findViewById(R.id.damier);
        mDamierView.setTextView((TextView) findViewById(R.id.text));
      
        if (savedInstanceState != null) {
        	// On vient juste de lancer le jeu
            Bundle bundle_tourCourant = savedInstanceState.getBundle("tourCourant");
            if (bundle_tourCourant != null) {
            	mDamierView.setTourCourant(bundle_tourCourant);
            	mDamierView.setMode(DamierView.READY);
            }
            // On restaure le jeu
            else {
	            Bundle bundle_damier = savedInstanceState.getBundle(mBundleKey);
	            if (bundle_damier != null) {
	            	mDamierView.restoreState(bundle_damier);
	            } else {
	            	mDamierView.setMode(DamierView.PAUSE);
	            }
            }
        }
	}
	
	/**
	 * Methode appelee lors de la mise en pause
	 */
	@Override
	protected void onPause() {
		super.onPause();
		/* On met le jeu en pause */
    	mDamierView.setMode(DamierView.PAUSE);
	}
	
	/**
	 * Methode appelee lors d'une rotation ou d'un pause
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		/* On stocke l'etat du Damier */
        outState.putBundle(mBundleKey, mDamierView.saveState());
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add("Valider");
    	menu.add("Simul retour attente");
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getTitle()=="Valider") {
    		mDamierView.setEtat(mDamierView.VALID);
    		mDamierView.updateGame();
    	}
    	/*
    	if(item.getTitle()=="Simul retour attente") {
    		mDamierView.setEtat(mDamierView.SELECT);
    		mDamierView.updateGame();
    	}
    	*/
    	return super.onOptionsItemSelected(item);
    }
}
