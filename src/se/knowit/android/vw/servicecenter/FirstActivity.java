package se.knowit.android.vw.servicecenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class FirstActivity extends Activity implements OnClickListener {
    private static final String TAG = "FirstActivity";
    
    private Button person;
    private Button transport;
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        
        LinearLayout linLay = (LinearLayout) findViewById(R.id.first_root);
        linLay.setBackgroundResource(R.drawable.pers_transport);
        
		person = (Button) findViewById(R.id.pers);
		person.setOnClickListener(this);
        
		transport = (Button) findViewById(R.id.transport);
		transport.setOnClickListener(this);
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

    @Override
    protected void onResume() {
        super.onResume();
    }
    
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
 
	public void onClick(View v) {
		if (v == transport) {
			System.setProperty("background", "transport");
		} else {
			System.setProperty("background", "pers");
		}
		
		Intent i = new Intent(this, StartActivity.class);
		startActivity(i);
	}
}
