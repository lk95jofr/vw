package se.knowit.android.vw.servicecenter;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class MoreGroup extends ActivityGroup {
    private static final String TAG = "MoreGroup";
    
    // Keep this in a static variable to make it accessible for all the nested activities, lets them manipulate the view
	public static MoreGroup group;

    // Need to keep track of the history if you want the back-button to work properly, don't use this if your activities requires a lot of memory.
	private ArrayList<View> history;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");
		
		this.history = new ArrayList<View>();
		group = this;
	}

    @Override
    protected void onResume() {
        super.onResume();

		// Start the root activity within the group and get its view
		View view = getLocalActivityManager().startActivity("MoreActivity", new Intent(this, MoreActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();

		// Replace the view of this ActivityGroup
		replaceView(view);
    }
    
	public void replaceView(View v) {
		// Adds the old one to history
		history.add(v);
		
		// Changes this Groups View to the new View.
		setContentView(v);
		v.setFocusable(true);
		v.setFocusableInTouchMode(true);
		v.requestFocus();
		v.requestFocusFromTouch();
	}

	public void back() {
		if (history.size() > 0) {
			history.remove(history.size()-1);
			if (history.size() <= 0){
				finish();
			} else {
				setContentView(history.get(history.size()-1));
			}
		} else {
			finish();
		}
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
}
