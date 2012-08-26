package se.knowit.android.vw.servicecenter;

import android.app.Activity;
import android.os.Bundle;

public class HomeActivity extends Activity {
    private static final String TAG = "HomeActivity";
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
	}
}
