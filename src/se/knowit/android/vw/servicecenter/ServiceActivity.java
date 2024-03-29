package se.knowit.android.vw.servicecenter;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ServiceActivity extends Activity {
    private static final String TAG = "ServiceActivity";
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        setContentView(R.layout.service);
        
        WebView webview = (WebView) findViewById(R.id.web_engine_service);  
        
//        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        webview.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;
        
        webview.setWebChromeClient(new WebChromeClient() {
        	public void onProgressChanged(WebView view, int progress) {
        		// Activities and WebViews measure progress with different scales.
        		// The progress meter will automatically disappear when we reach 100%
        		activity.setProgress(progress * 1000);
        	}
        });
        
        webview.setWebViewClient(new WebViewClient() {
        	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        		Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
        	}
        });

	    String s = System.getProperty("background");
	    if ("transport".equals(s)) {
	    	webview.loadUrl("http://www.volkswagentransportbilar.se/se/sv/beratung_und_service/service_ny_testsite.html");
	    } else {
	        webview.loadUrl("http://personbilar.volkswagen.se/sv/koepa_och_koera/service.html");
	    }
	}
}
