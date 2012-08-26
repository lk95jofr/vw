package se.knowit.android.vw.servicecenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MoreActivity extends Activity implements OnClickListener {
    private static final String TAG = "MoreActivity";
    
    private Button buttonWeb;
//    private Button symbols;
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");

        setContentView(R.layout.more);
        
		buttonWeb = (Button) findViewById(R.id.web_button);
		buttonWeb.setOnClickListener(this);
        
//		symbols = (Button) findViewById(R.id.symbols_button);
//		symbols.setOnClickListener(this);
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart called");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart called");
	}

    @Override
    protected void onResume() {
        super.onResume();
		Log.d(TAG, "onResume called");
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause called");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop called");
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
		Log.d(TAG, "onDestroy called");
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown");
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MoreGroup.group.back();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
    
	@Override
	public void onClick(View v) {
//		Window window = null;
		
		if (v == buttonWeb) {
		    String s = System.getProperty("background");
		    if ("transport".equals(s)) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.volkswagentransportbilar.se/"));
				startActivity(browserIntent);
		    } else {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://personbilar.volkswagen.se/"));
				startActivity(browserIntent);
		    }
//			window = MoreGroup.group.getLocalActivityManager().startActivity("AudiWebActivity", new Intent(this, AudiWebActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//		} else if (v == symbols) {
//			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.audi.se"));
//			startActivity(browserIntent);
//			window = MoreGroup.group.getLocalActivityManager().startActivity("SearchCountryActivity", new Intent(this, SearchCountryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
		
//		if (window != null) {
//			MoreGroup.group.replaceView(window.getDecorView());
//		}
	}
}
