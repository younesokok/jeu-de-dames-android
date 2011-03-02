package android.dames;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class JeuDeDames extends Activity {
	
	//private String URL = "http://192.168.1.42/Projet/Android/JeuDeDames/Serveur/index.php";  
	private String URL = "http://www.google.fr";  
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
				String query = "?data=Olivier"; //txtSearch.getText().toString();
				callWebService(query);
			}
        });
    }
    
    public void callWebService(String q){
    	HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(URL);
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
    }
}