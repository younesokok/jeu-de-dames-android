package android.dames;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class PlateauView extends View {

	/* Parametres des cases */
	private int mTailleCase;
	private static int mNbCases = 10;
	private static int mMarge = 0;
	/* Offset du début de damier */
	private Object mXOffset;
	private Object mYOffset;
	/* Tableau de cases */
	private int[][] mTableauCases = new int[10][10]; 


	public PlateauView(Context context, AttributeSet attrs) {
		super(context, attrs);
		/*
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PlateauView);
		mTailleCase = a.getInt(R.styleable.PlateauView_tileSize, 12);
		a.recycle();
		*/
	}

	public PlateauView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Methode appelee lors de l'affichage. Permet d'obtenir la taille de l'ecran.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		int cote = Math.min(w, h);
		mTailleCase = (int) Math.floor((cote-mMarge) / mNbCases);

		mXOffset = ((w - (mTailleCase * mNbCases)) / 2);
		mYOffset = ((h - (mTailleCase * mNbCases)) / 2);

		/*
        clearTiles();
		 */
	}
}
