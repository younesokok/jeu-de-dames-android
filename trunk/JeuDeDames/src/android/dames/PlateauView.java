package android.dames;

import android.content.Context;
import android.content.res.Resources;
import android.dames.webservices.CommucationServeur;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class PlateauView extends View {

	// ----------------------- Initialisation -------------------- //

	/* Parametres des cases */
	private int mTailleCase = 30;
	public static int mNbCasesCote = 10;
	public static int mNbTotalCases = mNbCasesCote*mNbCasesCote;
	private static int mMarge = 0;
	private static int mPadding = 15;
	private static int mTailleBitmapJoueur = 30;
	/* Offset du début de damier */
	private static int mXOffset;
	private static int mYOffset;
	/* Tableau de cases */
	private int[][] mTableauCases = new int[mNbCasesCote][mNbCasesCote]; 
	/* Tableau contenant les representations des différentes types de cases */
	private Bitmap[] mTypeCases; 
	// Affichage dessus et dessous
	public final static String JOUEUR_INCONNU = "Aucun adversaire n'a encore rejoint la partie";
	public final static String JOUEUR_EN_ATTENTE_JOUEUR = "En attente de l'arrivée d'un adversaire";
	public final static String JOUEUR_EN_ATTENTE_TOUR = "En attente du prochain tour";
	public final static String JOUEUR_J_ATTENTE_TOUR = "J'attends mon tour";
	public final static String JOUEUR_EN_JEU = "En train de joueur";
	public final static String JOUEUR_JE_JOUE = "A moi de jouer !";
	public String texteJoueurNoir = JOUEUR_INCONNU;
	public String texteJoueurBlanc = JOUEUR_EN_ATTENTE_JOUEUR;
	public Bitmap bitmapJoueurBlanc;
	public Bitmap bitmapJoueurNoir;
	

	private final Paint mPaint = new Paint();
	
	// --- Paramètres serveur
	private String url = "http://www.jeudedames.la-bnbox.fr/index.php";
	public CommucationServeur communicationServeur;

	public PlateauView(Context context, AttributeSet attrs) {
		super(context, attrs);
		communicationServeur = new CommucationServeur(url);
		
		// Image joueurs
		Resources r = this.getContext().getResources();
		bitmapJoueurBlanc = Bitmap.createBitmap(mTailleBitmapJoueur, mTailleBitmapJoueur, Bitmap.Config.ARGB_8888);
		Canvas canvasTmp = new Canvas(bitmapJoueurBlanc);
		Drawable sprite = r.getDrawable(R.drawable.joueur_blanc_petit);
		sprite.setBounds(0, 0, mTailleBitmapJoueur, mTailleBitmapJoueur);
		sprite.draw(canvasTmp);
		
		bitmapJoueurNoir = Bitmap.createBitmap(mTailleBitmapJoueur, mTailleBitmapJoueur, Bitmap.Config.ARGB_8888);
		canvasTmp = new Canvas(bitmapJoueurNoir);
		sprite = r.getDrawable(R.drawable.joueur_noir_petit);
		sprite.setBounds(0, 0, mTailleBitmapJoueur, mTailleBitmapJoueur);
		sprite.draw(canvasTmp);
	}

	public PlateauView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		communicationServeur = new CommucationServeur(url);
		
		// Image joueurs
		Resources r = this.getContext().getResources();
		bitmapJoueurBlanc = Bitmap.createBitmap(mTailleBitmapJoueur, mTailleBitmapJoueur, Bitmap.Config.ARGB_8888);
		Canvas canvasTmp = new Canvas(bitmapJoueurBlanc);
		Drawable sprite = r.getDrawable(R.drawable.joueur_blanc_petit);
		sprite.setBounds(0, 0, mTailleBitmapJoueur, mTailleBitmapJoueur);
		sprite.draw(canvasTmp);
		
		bitmapJoueurNoir = Bitmap.createBitmap(mTailleBitmapJoueur, mTailleBitmapJoueur, Bitmap.Config.ARGB_8888);
		canvasTmp = new Canvas(bitmapJoueurNoir);
		sprite = r.getDrawable(R.drawable.joueur_noir_petit);
		sprite.setBounds(0, 0, mTailleBitmapJoueur, mTailleBitmapJoueur);
		sprite.draw(canvasTmp);
	}

	/**
	 * Methode de (re)initialisation des cases du damier
	 * @param nbTypeCases
	 */
	public void resetCases(int nbTypeCases) {
		mTypeCases = new Bitmap[nbTypeCases];
	}

	/**
	 * Methode chargeant les différentes cases
	 * @param typeCase
	 * @param sprite
	 */
	public void loadCases(int typeCase, Drawable sprite) {
		Bitmap bitmap = Bitmap.createBitmap(mTailleCase, mTailleCase, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		sprite.setBounds(0, 0, mTailleCase, mTailleCase);
		sprite.draw(canvas);
		mTypeCases[typeCase] = bitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// --- Joueur noir
		mPaint.setColor(Color.WHITE);
		canvas.drawBitmap(bitmapJoueurNoir, mPadding, mPadding, mPaint);
		canvas.drawText(texteJoueurNoir, mPadding+mTailleBitmapJoueur+mPadding, mPadding+mTailleBitmapJoueur/3, mPaint);
		// -- Damier
		for (int x = 0; x < mNbCasesCote; x += 1) {
//			canvas.drawText(""+((char) (x+97)), 
//					mXOffset + x * mTailleCase,
//					mYOffset,
//					mPaint);
			for (int y = 0; y < mNbCasesCote; y += 1) {
				if (mTableauCases[x][y] > 0) {
					canvas.drawBitmap(mTypeCases[mTableauCases[x][y]], 
							mXOffset + x * mTailleCase,
							mYOffset + y * mTailleCase,
							mPaint);
				}
			}
		}
		// --- Joueur blanc
		canvas.drawBitmap(bitmapJoueurBlanc, mPadding, mYOffset+(mTailleCase*mNbCasesCote)+mPadding, mPaint);
		canvas.drawText(texteJoueurBlanc, mPadding+mTailleBitmapJoueur+mPadding, mYOffset+(mTailleCase*mNbCasesCote)+mPadding+mTailleBitmapJoueur/3, mPaint);
	}

	/**
	 * Methode appelee lors de l'affichage. Permet d'obtenir la taille de l'ecran.
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		int cote = Math.min(w, h);
		mTailleCase = (int) Math.floor((cote-mMarge) / mNbCasesCote);

		mXOffset = ((w - (mTailleCase * mNbCasesCote)) / 2);
		mYOffset = ((h - (mTailleCase * mNbCasesCote)) / 2);

		clearCases();
	}

	public void clearCases() {
		for (int x = 0; x < mNbCasesCote; x++) {
			for (int y = 0; y < mNbCasesCote; y++) {
				setCase(((x+y)%2)+1, x, y);
			}
		}
	}

	public void setCase(int type, int x, int y) {
		mTableauCases[x][y] = type;		
	}

	public void setActif(int x, int y) {
		if(mTableauCases[x][y]<7)
			mTableauCases[x][y] = (mTableauCases[x][y])+6;		
	}
}
