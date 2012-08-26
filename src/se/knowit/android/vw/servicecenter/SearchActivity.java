package se.knowit.android.vw.servicecenter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SearchActivity extends ListActivity implements OnEditorActionListener, OnClickListener {
    private static final String TAG = "SearchActivity";
    
    private ProgressDialog mProgressDialog = null;
    private ArrayList<ServiceCenter> mServiceCenterList = null;
    private ServiceCenterAdapter mAdapter = null;
    
    private EditText cityText;
    private Button searchClose;
    private Button searchCountry;
    
    private String scLatitude = null;
    private String scLongitude = null;
    
    private String BRAND = "V";
    
    private boolean hasServiceCenterList = false;
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");
		
        setContentView(R.layout.search);
        
        String s = System.getProperty("background");
        if ("transport".equals(s)) {
        	BRAND ="N";
        }

        mServiceCenterList = new ArrayList<ServiceCenter>();
        this.mAdapter = new ServiceCenterAdapter(this, R.layout.servicecenterlist, mServiceCenterList);
        setListAdapter(this.mAdapter);
        
        cityText = (EditText)findViewById(R.id.EditTextCity);
       	cityText.setOnEditorActionListener(this);
        
		searchClose = (Button) findViewById(R.id.search_close_button);
		searchClose.setOnClickListener(this);
        
		searchCountry = (Button) findViewById(R.id.search_country_button);
		searchCountry.setOnClickListener(this);
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
		
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		
        cityText.setText("");
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
 
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            if ((mServiceCenterList != null) && (!mServiceCenterList.isEmpty())) {
            	if (hasServiceCenterList) {
                    mAdapter.notifyDataSetChanged();
                    for (int i=0; i < mServiceCenterList.size(); i++) {
                    	mAdapter.add(mServiceCenterList.get(i));
                    }
            	}
            }
            
            mProgressDialog.dismiss();
        }
    };
    
    private void showListDialog() {
		final ArrayList<String> list = new ArrayList<String>();
		for (int i=0; i < mServiceCenterList.size(); i++) {
			ServiceCenter sc = mServiceCenterList.get(i);
			list.add(sc.getDName());
		}
		
		final CharSequence[] cs = list.toArray(new CharSequence[list.size()]);

		final AlertDialog.Builder builder = new AlertDialog.Builder(getDialogContext());
		builder.setTitle(R.string.choose_place);
		builder.setItems(cs, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
				for (int i=0; i < mServiceCenterList.size(); i++) {
					ServiceCenter sc = mServiceCenterList.get(i);
					if (sc.getDName().equals(cs[item])) {
						scLatitude = sc.getDlat();
						scLongitude = sc.getDlng();
					}
				}
				
				getServiceCenters();
		    }
		});
		
		AlertDialog alert = builder.create();
		alert.show();
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
    		String city = cityText.getText().toString();
    		if ((city != null) && (!"".equals(city))) {
    			if ((scLatitude == null) || (scLongitude == null)) {
	    			mServiceCenterList = JsonParser.getGeoCoding(BRAND, city);
	    			Log.d(TAG, "Number of servicecenters: " + mServiceCenterList.size());
	    			
	    			if (mServiceCenterList.size() == 1) {
	    				ServiceCenter serviceCenter = mServiceCenterList.get(0);
	    				scLatitude = serviceCenter.getDlat();
	    				scLongitude = serviceCenter.getDlng();
	    				
	    				mServiceCenterList.clear();
	    				mServiceCenterList = JsonParser.getServiceCenterFromServer(BRAND, 10, scLatitude, scLongitude);
	    				
	    	    		hasServiceCenterList = true;
	    			}
    			} else {
    	    		mServiceCenterList = JsonParser.getServiceCenterFromServer(BRAND, 10, scLatitude, scLongitude);
    	    		hasServiceCenterList = true;
    			}
    		}
    	} catch (Exception e) {
    		Log.e(TAG, e.getMessage(), e);
    	}

    	runOnUiThread(returnRes);
    }
	
    private void prepareList() {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(cityText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        
		mServiceCenterList.clear();
		mAdapter.clear();
		hasServiceCenterList = false;
		scLatitude = null;
		scLongitude = null;

        if (hasInternet() && (SearchGroup.group.getMyLocation() != null)) {
        	Runnable viewServiceCenter = new Runnable() {
        		@Override
        		public void run() {
        			Looper.prepare();
        			getServiceCenters();
        		}
        	};
        	
        	Thread viewServiceCenterThread = new Thread(null, viewServiceCenter, "ServiceCenter");
        	viewServiceCenterThread.start();

        	mProgressDialog = ProgressDialog.show(getDialogContext(), "", getResources().getString(R.string.retrieving_data), true);
        } else {
        	Log.i(TAG, getResources().getString(R.string.no_network));
        	Toast.makeText(this, getResources().getString(R.string.no_network), Toast.LENGTH_LONG).show();
        }
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
    
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if ((event != null) && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            prepareList();
			new ShowDialogTask().execute();
        }
        
        return false;
    }
    
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
	     i.putExtra("mylatitude", SearchGroup.group.getMyLatitude());
	     i.putExtra("mylongitude", SearchGroup.group.getMyLongitude());

	     View view = SearchGroup.group.getLocalActivityManager().startActivity("GoogleMapsActivity", i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
	     SearchGroup.group.replaceView(view);
	}
	
	@Override
	public void onClick(View v) {
		if (SearchGroup.group.getMyLocation() != null) {
			Window window = null;
			
			if (v == searchClose) {
				window = SearchGroup.group.getLocalActivityManager().startActivity("ServiceCenterActivity", new Intent(this, ServiceCenterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			} else if (v == searchCountry) {
	//			window = SearchGroup.group.getLocalActivityManager().startActivity("SearchCountryActivity", new Intent(this, SearchCountryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				prepareList();
				new ShowDialogTask().execute();
			}
		
			if (window != null) {
				SearchGroup.group.replaceView(window.getDecorView());
			}
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
        		LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		v = li.inflate(R.layout.servicecenterlist, null);
        	}
        	
        	ServiceCenter o = items.get(position);
        	if (o != null) {
        		TextView name = (TextView) v.findViewById(R.id.dname);
        		TextView phone = (TextView) v.findViewById(R.id.dphn);
        		TextView addr = (TextView) v.findViewById(R.id.daddr_dcity);
        		TextView url = (TextView) v.findViewById(R.id.durl);
        		
        		if (name != null) {
        			name.setText(o.getDName());
        		}
        		
        		if ((phone != null) && (!"".equals(o.getDPhn()))) {
        			phone.setText(o.getDPhn());
        		}
        		
        		if ((addr != null) && (!"".equals(o.getDAddr())) || (!"".equals(o.getDCity()))) {
        			addr.setText(o.getDAddr() + ", " + o.getDCity());
        		}
        		
        		if ((url != null) && (!"".equals(o.getDUrl()))) {
        			url.setText(o.getDUrl());
        		}
        	}
        	
        	return v;
        }
    }
    
    private class ShowDialogTask extends AsyncTask<String, Void, Void> {
    	protected void onPreExecute() {
    	}
    	
    	protected Void doInBackground(final String... args) {
    		do {
    			try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    			}
    		} while (mServiceCenterList.isEmpty());
    		
    		return null;
    	}
    	
    	protected void onPostExecute(final Void unused) {
    		if (!hasServiceCenterList) {
    			showListDialog();
    		}
    	}
    }
}
