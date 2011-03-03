package android.dames;

import android.app.Activity;
import android.content.Intent;
import android.dames.webservices.CommucationServeur;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class JeuDeDames extends Activity {
	private String url = "http://www.jeudedames.la-bnbox.fr";  
	private final String tag = "JeuDeDames : ";
	private CommucationServeur communivationServeur;
	private Tour tourCourant;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		communivationServeur = new CommucationServeur(url);
		
		final Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				if (null == tourCourant) {
					tourCourant = communivationServeur.getTourCourant();
				}
				else {
					tourCourant.incrNumero();
					tourCourant = communivationServeur.sendTourFini(tourCourant);
				}
				Toast.makeText(getApplicationContext(), tourCourant.toString(), Toast.LENGTH_SHORT).show();
			}
		});

		final Button btnDamier = (Button) findViewById(R.id.btnDamier);
		btnDamier.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {

				/**
				 * Lancement de l'activite du jeu de dames
				 */
				Intent intent = new Intent(JeuDeDames.this, JeuDeDamesActivity.class);
				Bundle bundle = new Bundle();
				/* On met ce que l'on veut dans le bundle */
				/*
				bundle.putDouble("PARTYNUM", mPartyNum);
				...
				*/
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
}