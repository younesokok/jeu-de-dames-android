package android.dames;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
	/* Tableau contenant les cases */
    private Bitmap[] mCasesTab; 

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
	 * Methode de (re)initialisation des cases du damier
	 * @param nbTypeCases
	 */
    public void resetCases(int nbTypeCases) {
    	mCasesTab = new Bitmap[nbTypeCases];
    }
	
	/**
	 * Methode chargeant les différentes cases
	 * @param typeCase
	 * @param sprite
	 */
	public void loadCases(int typeCase, Drawable sprite) {
		// TODO Auto-generated method stub
        Bitmap bitmap = Bitmap.createBitmap(mTailleCase, mTailleCase, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        sprite.setBounds(0, 0, mTailleCase, mTailleCase);
        sprite.draw(canvas);
        
        mCasesTab[typeCase] = bitmap;
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
