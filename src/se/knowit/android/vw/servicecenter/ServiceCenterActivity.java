package se.knowit.android.vw.servicecenter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceCenterActivity extends ListActivity {
    private static final String TAG = "ServiceCenterActivity";
    
    private ProgressDialog mProgressDialog = null; 
    private ArrayList<ServiceCenter> mServiceCenterList = null;
    private ServiceCenterAdapter mAdapter = null;
    
    private String BRAND = "V";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");
		
        setContentView(R.layout.servicecenter);
        
        String s = System.getProperty("background");
        if ("transport".equals(s)) {
        	BRAND = "N";
        }
        
        mServiceCenterList = new ArrayList<ServiceCenter>();
        mAdapter = new ServiceCenterAdapter(this, R.layout.servicecenterlist, mServiceCenterList);
        setListAdapter(mAdapter);
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
		
        mServiceCenterList.clear();
        mAdapter.clear();
        
        Log.d(TAG, "onResume, Latitude: " + getMyLatitude() + ", Longitude: " + getMyLongitude());

        if (hasInternet()) {
        	if ((getMyLatitude() != null) && (getMyLongitude() != null)) {
        		Runnable viewServiceCenter = new Runnable() {
        			@Override
        			public void run() {
        				getServiceCenters();
        			}
        		};
				        
        		Thread thread = new Thread(null, viewServiceCenter, "ServiceCenter");
        		thread.start();
				        
        		mProgressDialog = ProgressDialog.show(getDialogContext(), "", getResources().getString(R.string.retrieving_data), true);
        	} else {
        		Toast.makeText(this, getResources().getString(R.string.determining_your_position_error), Toast.LENGTH_LONG).show();
        	}
        } else {
        	Log.i(TAG, getResources().getString(R.string.no_network));
        	Toast.makeText(this, getResources().getString(R.string.no_network), Toast.LENGTH_LONG).show();
        }
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
 
    private Context getDialogContext() {
        Context context;
        if (getParent() != null) {
        	context = getParent();
        } else {
        	context = this;
        }
        
        return context;
    }
    
    private void getServiceCenters() {
    	try {
    		mServiceCenterList = JsonParser.getServiceCenterFromServer(BRAND, 10, Double.toString(getMyLatitude()), Double.toString(getMyLongitude()));
    		Log.d(TAG, "Number of servicecenters: " + mServiceCenterList.size());
    	} catch (Exception e) {
    		Log.e(TAG, e.getMessage(), e);
    	}

    	runOnUiThread(returnRes);
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
    
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            if ((mServiceCenterList != null) && (mServiceCenterList.size() > 0)) {
                mAdapter.notifyDataSetChanged();
                for (int i=0; i < mServiceCenterList.size(); i++) {
                	mAdapter.add(mServiceCenterList.get(i));
                }
            }
            
            mProgressDialog.dismiss();
        }
    };
  
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ServiceCenter servicecenter = (ServiceCenter)this.getListAdapter().getItem(position);
		
		Intent i = new Intent(this, GoogleMapsActivity.class);
		i.putExtra("name", servicecenter.getDName());
		i.putExtra("city", servicecenter.getDCity());
		i.putExtra("address", servicecenter.getDAddr());
		i.putExtra("phone", servicecenter.getDPhn());
		i.putExtra("url", servicecenter.getDUrl());
		i.putExtra("sclatitude", servicecenter.getDlat());
		i.putExtra("sclongitude", servicecenter.getDlng());
		i.putExtra("dist", servicecenter.getDist());
		i.putExtra("mylatitude", getMyLatitude());
		i.putExtra("mylongitude", getMyLongitude());
		
		Log.d(TAG, "onListItemClick, Latitude: " + getMyLatitude() + ", Longitude: " + getMyLongitude());
		
		View view = SearchGroup.group.getLocalActivityManager().startActivity("GoogleMapsActivity", i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		SearchGroup.group.replaceView(view);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SearchGroup.group.back();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	private Double getMyLatitude() {
		return SearchGroup.group.getMyLatitude();
	}

	private Double getMyLongitude() {
		return SearchGroup.group.getMyLongitude();
	}

    private class ServiceCenterAdapter extends ArrayAdapter<ServiceCenter> {
    	private ArrayList<ServiceCenter> items;

        public ServiceCenterAdapter(Context context, int textViewResourceId, ArrayList<ServiceCenter> items) {
        	super(context, textViewResourceId, items);
        	this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	View v = convertView;
        	if (v == null) {
        		LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		v = vi.inflate(R.layout.servicecenterlist, null);
        	}
        	
        	ServiceCenter o = items.get(position);
        	if (o != null) {
        		TextView name = (TextView) v.findViewById(R.id.dname);
        		TextView phone = (TextView) v.findViewById(R.id.dphn);
        		TextView addr = (TextView) v.findViewById(R.id.daddr_dcity);
        		TextView url = (TextView) v.findViewById(R.id.durl);
        		TextView dist = (TextView) v.findViewById(R.id.dist);
        		
        		if (name != null) {
        			name.setText(o.getDName());
        		}
        		
        		if (phone != null) {
        			phone.setText(o.getDPhn());
        		}
        		
        		if (addr != null) {
        			addr.setText(o.getDAddr() + ", " + o.getDCity());
        		}
        		
        		if (url != null) {
        			url.setText(o.getDUrl());
        		}
        		
        		if (dist != null) {
        			dist.setText(getResources().getString(R.string.distance) + " " + o.getDist() + " " + getResources().getString(R.string.distance_unit));
        		}
        	}
        	
        	return v;
        }
    }
}
