package android.dames;

import android.app.Activity;
import android.os.Bundle;
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

        if (savedInstanceState == null) {
            // We were just launched -- set up a new game
        	mDamierView.setMode(DamierView.READY);
        } else {
            // We are being restored
            Bundle map = savedInstanceState.getBundle(mBundleKey);
            if (map != null) {
            	mDamierView.restoreState(map);
            } else {
            	mDamierView.setMode(DamierView.PAUSE);
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
	 * Methode appelee lors d'une rotation ?!
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		/* On stocke l'etat du Damier */
        outState.putBundle(mBundleKey, mDamierView.saveState());
	}

}
