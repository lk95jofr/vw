package se.knowit.android.vw.servicecenter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShowRouteActivity extends ListActivity {
    private static final String TAG = "ShowRouteActivity";
    
    private ArrayList<String> mRouteList = null;
    private RouteAdapter mAdapter = null;
    
    private String origin;
    private String destination;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.showroute);
        
		Bundle extras = getIntent().getExtras();
		origin = extras.getDouble("mylatitude") + "," + extras.getDouble("mylongitude");
		destination = extras.getDouble("latitude") + "," + extras.getDouble("longitude");
		
		Log.d(TAG, "Origin:  " + origin);
		Log.d(TAG, "Destination: " + destination);
		
        mRouteList = new ArrayList<String>();
        this.mAdapter = new RouteAdapter(this, R.layout.showroutelist, mRouteList);
        setListAdapter(this.mAdapter);
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
        
        if (hasInternet()) {
        	mRouteList.clear();
//        	mRouteList = JsonParser.getRouteFromGoogle("57.786233,14.172363", "57.6590501,14.9625166");
        	mRouteList = JsonParser.getRouteFromGoogle(origin, destination);
            if ((mRouteList != null) && (mRouteList.size() > 0)) {
                for (int i=0; i < mRouteList.size(); i++) {
                	mAdapter.add(mRouteList.get(i));
                }
            }
            
            mAdapter.notifyDataSetChanged();
        }
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
 
	/*
	 * Are we connected to Internet or not
	 */
	private boolean hasInternet() {
		ConnectivityManager con = (ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
	    boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
	    boolean mobile = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
	    
	    return (wifi || mobile);
	}
	
    private class RouteAdapter extends ArrayAdapter<String> {
    	private ArrayList<String> items;

        public RouteAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
        	super(context, textViewResourceId, items);
        	this.items = items;
        }
        
        @Override
        public View getView(int position, View view, ViewGroup parent) {
        	if (view == null) {
        		LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		view = li.inflate(R.layout.showroutelist, null);
        	}
        	
        	String s = items.get(position);
        	if (s != null) {
        		TextView routeDescription = (TextView) view.findViewById(R.id.route_description);
        		
        		if (routeDescription != null) {
        			routeDescription.setText(Html.fromHtml(s));
        		}
        	}
        	
        	return view;
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
}
