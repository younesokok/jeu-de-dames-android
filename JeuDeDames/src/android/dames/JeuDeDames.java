package android.dames;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.dames.webservices.PartieSAXParser;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class JeuDeDames extends Activity {
	
	private String url = "http://www.jeudedames.la-bnbox.fr";  
	private String result = "";  
	private String deviceId;  
	private final String tag = "JeuDeDames : ";  
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        deviceId = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
        
//        final EditText txtSearch = (EditText) findViewById(R.id.txtSearch);
//        txtSearch.setOnClickListener(new EditText.OnClickListener(){
//        	public void onClick(View v){txtSearch.setText("");}
//    	});

        final Button btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				callWebServicesXML();
			}
        });
    }
    
    /**
     * Retourne l'URL du service Web
     * @param latitude latitude de la position
     * @param longitude longitude de la position
     * @param rayon rayon de recherche des déchèteries
     * @return l'URL du service Web d'Ecoemballages
     */
    private URL buildUrlTour() {
		URL url = null;
		try {
			url = new URL(this.url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
    }
    
    private Tour parserXmlTour(URL url) {
    	// Création du parseur SAX
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = null;
		try {
			sp = spf.newSAXParser();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Récupération du lecteur XML du parseur SAX nouvellement créé
        XMLReader xr = null;
		try {
			xr = sp.getXMLReader();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        /* Create a new ContentHandler and apply it to the XML-Reader*/
        PartieSAXParser parserPartie = new PartieSAXParser();
        xr.setContentHandler(parserPartie);
        
        // Début du parsing du contenu de l'URL
        InputSource in = new InputSource();            
		try {
			// Récupération du contenu de l'URL
            in.setByteStream(url.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        in.setEncoding("ISO-8859-1"); // Changement du charset
        try {
			xr.parse(in); // Parsing
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Fin du parsing
        return parserPartie.getTour();
    }
    
    public void callWebServicesXML() {
    	URL url = buildUrlTour();
    	Tour tourPrecedent = parserXmlTour(url);
    	Log.i(tag, tourPrecedent.toString());
		Toast.makeText(this, tourPrecedent.toString(), Toast.LENGTH_SHORT).show();
    }
    public void callWebServiceREST(String q){
    	HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		request.addHeader("deviceId", deviceId);
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
			result = httpclient.execute(request, handler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		httpclient.getConnectionManager().shutdown();
		Log.i(tag, result);
		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }
}