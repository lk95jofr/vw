package se.knowit.android.vw.servicecenter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class VWWebActivity extends Activity {
    private static final String TAG = "VWWebActivity";
    
    WebView webview;  
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");
        
        setContentView(R.layout.vwweb);
        
        webview = (WebView) findViewById(R.id.web_engine_vw);  
        
//        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

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
        	
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	view.loadUrl(url);
            	return true;
            }
        });

        Log.d(TAG, "Loading URL");
//        webview.loadUrl("http://mobil.dn.se/");
        webview.loadUrl("http://personbilar.volkswagen.se/");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown");
		
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	if (webview.canGoBack()) {
				Log.d(TAG, "Back");
		        webview.goBack();
		        return true;
	    	}
	    	
	    	MoreGroup.group.back();
			return true;
	    }
	    
	    return super.onKeyDown(keyCode, event);
	}
}
