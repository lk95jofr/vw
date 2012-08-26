package se.knowit.android.vw.servicecenter;

import java.util.ArrayList;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class SearchGroup extends ActivityGroup {
    private static final String TAG = "SearchGroup";
    
    // Keep this in a static variable to make it accessible for all the nested activities, lets them manipulate the view
	public static SearchGroup group;

    // Need to keep track of the history if you want the back-button to work properly, don't use this if your activities requires a lot of memory.
	private ArrayList<View> history;

    private static final long minUpdateTime = 1000L;
    private static final float minUpdateDistance = 100.0f;
    
    private static LocationManager locationManager;
    private LocationListener mGpsLocationListener;
    private LocationListener mNetworkLocationListener;
    private static Location mLocation;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.history = new ArrayList<View>();
		group = this;
	}

    @Override
    protected void onResume() {
        super.onResume();

		// Start the root activity within the group and get its view
		View view = getLocalActivityManager().startActivity("SearchActivity", new Intent(this, SearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();

		// Replace the view of this ActivityGroup
		replaceView(view);
		
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		startListening();
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause called");
		
		stopListening();
	}
    
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	public void replaceView(View v) {
		// Adds the old one to history
		history.add(v);
		
		// Changes this Groups View to the new View.
		setContentView(v);
	}

	public void back() {
		if (history.size() > 0) {
			history.remove(history.size()-1);
			if (history.size() > 0) {
				setContentView(history.get(history.size()-1));
			}
		} else {
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SearchGroup.group.back();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	  
    public void startListening() {
    	Log.d(TAG, "Start listening for location updates");
    	
    	if (locationManager != null) {
	        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	        	mGpsLocationListener = new MyLocationListener();
	        	
	        	try {
	        		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minUpdateTime, minUpdateDistance, mGpsLocationListener);
	        	} catch (IllegalArgumentException e) {
	        		Log.w(TAG, e.getMessage(), e);
	        	} catch (RuntimeException e) {
	        		Log.w(TAG, e.getMessage(), e);
	        	}
	        }
	
	        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
	            mNetworkLocationListener = new MyLocationListener();

	            try {
	            	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minUpdateTime, minUpdateDistance, mNetworkLocationListener);
	        	} catch (IllegalArgumentException e) {
	        		Log.w(TAG, e.getMessage(), e);
	        	} catch (RuntimeException e) {
	        		Log.w(TAG, e.getMessage(), e);
	        	}
	        }
    	}
    }

    public void stopListening() {
    	Log.d(TAG, "Stop listening for location updates");
    	
        if (locationManager != null) {
        	if (mGpsLocationListener != null) {
	        	try {
	        		locationManager.removeUpdates(mGpsLocationListener);
	        	} catch (IllegalArgumentException e) {
	        		Log.w(TAG, e.getMessage(), e);
	        	}
        	}
        	
        	if (mNetworkLocationListener != null) {
	        	try {
	        		locationManager.removeUpdates(mNetworkLocationListener);
	        	} catch (IllegalArgumentException e) {
	        		Log.w(TAG, e.getMessage(), e);
	        	}
        	}
        }
    }
    
    public Location getMyLocation() {
    	return mLocation;
    }
    
    public Double getMyLatitude() {
    	return ((mLocation != null) ? mLocation.getLatitude() : null);
    }

    public Double getMyLongitude() {
    	return ((mLocation != null) ? mLocation.getLongitude() : null);
    }

	private class MyLocationListener implements LocationListener {
    	@Override
        public void onLocationChanged(Location location) {
    		mLocation = location;
    		Log.d(TAG, "onLocationChanged, " + mLocation.getLatitude() + ", " + mLocation.getLongitude());
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
