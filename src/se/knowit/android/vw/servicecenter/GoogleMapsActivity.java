package se.knowit.android.vw.servicecenter;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.android1.overlaymanager.ManagedOverlay;
import de.android1.overlaymanager.ManagedOverlayGestureDetector;
import de.android1.overlaymanager.ManagedOverlayItem;
import de.android1.overlaymanager.OverlayManager;
import de.android1.overlaymanager.ZoomEvent;

public class GoogleMapsActivity extends MapActivity implements OnClickListener {
    private static final String TAG = "GoogleMapsActivity";
    
    private ImageButton showRoute;
    private ImageButton showRouteOnMap;
    
    private MapView mapView;
    private MapController mapController;
    private GeoPoint myGeoPoint = null;
    private GeoPoint scGeoPoint;
    private OverlayManager mOverlayManager;
    private ManagedOverlayItem myManagedOverlayItem;
    private ManagedOverlayItem scManagedOverlayItem;
    private BalloonOverlayView myBalloonView;
    private BalloonOverlayView scBalloonView;

	private String name = "";
	private String city = "";
	private String address = "";
	private String phone = "";
	private String url = "";
	private String dist = "";
	private double scLatitude;
	private double scLongitude;
	private double myLatitude;
	private double myLongitude;
	
	private boolean hasFocusOnMe = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.googlemaps);
		
        showRoute = (ImageButton) findViewById(R.id.imagebutton1);
		showRoute.setOnClickListener(this);
		
        showRouteOnMap = (ImageButton) findViewById(R.id.imagebutton2);
        showRouteOnMap.setOnClickListener(this);
		
		Bundle extras = getIntent().getExtras();
		name = extras.getString("name");
		city = extras.getString("city");
		address = extras.getString("address");
		phone = extras.getString("phone");
		url = extras.getString("url");
		dist = extras.getString("dist");
		scLatitude = Double.parseDouble(extras.getString("sclatitude"));
		scLongitude = Double.parseDouble(extras.getString("sclongitude"));
		myLatitude = extras.getDouble("mylatitude");
		myLongitude = extras.getDouble("mylongitude");
		
		Log.d(TAG, "Name:         " + name);
		Log.d(TAG, "City:         " + city);
		Log.d(TAG, "Address:      " + address);
		Log.d(TAG, "Phone:        " + phone);
		Log.d(TAG, "Url:          " + url);
		Log.d(TAG, "Dist:         " + dist);
		Log.d(TAG, "SC Latitude:  " + scLatitude);
		Log.d(TAG, "SC Longitude: " + scLongitude);
		Log.d(TAG, "MY Latitude:  " + myLatitude);
		Log.d(TAG, "MY Longitude: " + myLongitude);
		
		myGeoPoint = new GeoPoint((int) (myLatitude * 1E6), (int) (myLongitude * 1E6));
		scGeoPoint = new GeoPoint((int) (scLatitude * 1E6), (int) (scLongitude * 1E6));
		
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setSatellite(false);
		mapView.setStreetView(true);
		mapView.setBuiltInZoomControls(true);
		
		mapController = mapView.getController();
		mapController.animateTo(scGeoPoint);
		mapController.setZoom(14);
		
		mOverlayManager = new OverlayManager(getApplication(), mapView);
		locationOverlay();
		showBalloon(mapView, myGeoPoint, scGeoPoint, getResources().getString(R.string.my_location), name);
		
		mapView.invalidate();
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
 
	@Override
	public boolean isRouteDisplayed() {
		return false;
	}
	
	public void onClick(View v) {
		if (v == showRoute) {
			Intent i = new Intent(this, ShowRouteActivity.class);
			i.putExtra("latitude", scLatitude);
			i.putExtra("longitude", scLongitude);
			i.putExtra("mylatitude", myLatitude);
			i.putExtra("mylongitude", myLongitude);

			View view = SearchGroup.group.getLocalActivityManager().startActivity("ShowRoute", i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
			SearchGroup.group.replaceView(view);
		} else if (v == showRouteOnMap) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + scLatitude + "," + scLongitude));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	} 
	
    private void showMyDialog() {
        final Dialog dialog = new Dialog(getParent());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.workshop_dialog);
        dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
        
        TextView tvName = (TextView) dialog.findViewById(R.id.dname);
        tvName.setText(name);
        TextView tvPhone = (TextView) dialog.findViewById(R.id.dphn);
        tvPhone.setText(phone);
		Linkify.addLinks(tvPhone, Linkify.ALL);
        TextView tvAddress = (TextView) dialog.findViewById(R.id.daddr_dcity);
        tvAddress.setText(address + ", " + city);
        TextView tvUrl = (TextView) dialog.findViewById(R.id.durl);
        tvUrl.setText(Html.fromHtml("<a href='http://" + url + "'>" + url + "</a>"));
		Linkify.addLinks(tvUrl, Linkify.ALL);
        TextView tvDist = (TextView) dialog.findViewById(R.id.dist);
        tvDist.setText(getResources().getString(R.string.distance) + " " + dist + " " + getResources().getString(R.string.distance_unit));
        
        dialog.show();
    }
    
    private void showBalloon(final MapView mapView, final GeoPoint myPoint, final GeoPoint scPoint, String myText, String scText) {
        boolean isRecycled;
        int viewOffset = 10;
        
        if ((myBalloonView == null) || (scBalloonView == null)) {
            myBalloonView = new BalloonOverlayView(mapView.getContext(), viewOffset);
            scBalloonView = new BalloonOverlayView(mapView.getContext(), viewOffset);
            isRecycled = false;
        } else {
            isRecycled = true;
        }

        myBalloonView.setVisibility(View.GONE);
        myBalloonView.setLabel(myText);
        
        scBalloonView.setVisibility(View.GONE);
        scBalloonView.setLabel(scText);

        MapView.LayoutParams myParams = new MapView.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, myPoint,
                MapView.LayoutParams.BOTTOM_CENTER);
        myParams.mode = MapView.LayoutParams.MODE_MAP;
        
        MapView.LayoutParams scParams = new MapView.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, scPoint,
                MapView.LayoutParams.BOTTOM_CENTER);
        scParams.mode = MapView.LayoutParams.MODE_MAP;

        myBalloonView.setVisibility(View.VISIBLE);
        scBalloonView.setVisibility(View.VISIBLE);

        if (isRecycled) {
            myBalloonView.setLayoutParams(myParams);
            scBalloonView.setLayoutParams(scParams);
        } else {
            mapView.addView(myBalloonView, myParams);
            mapView.addView(scBalloonView, scParams);
        }
    }
    
    private void locationOverlay() {
    	final ManagedOverlay myManagedOverlay = mOverlayManager.createOverlay(getResources().getDrawable(R.drawable.currentposition));
    	final ManagedOverlay scManagedOverlay = mOverlayManager.createOverlay(getResources().getDrawable(R.drawable.marker));
    	
    	myManagedOverlayItem = new ManagedOverlayItem(myGeoPoint, "title", "snippet");
    	myManagedOverlay.add(myManagedOverlayItem);
    	
        scManagedOverlayItem = new ManagedOverlayItem(scGeoPoint, "title", "snippet");
        scManagedOverlay.add(scManagedOverlayItem);

        scManagedOverlay.setOnOverlayGestureListener(new ManagedOverlayGestureDetector.OnOverlayGestureListener() {
        	@Override
            public boolean onDoubleTap(MotionEvent motionEvent,
            						   ManagedOverlay managedOverlay,
            						   GeoPoint geoPoint,
            						   ManagedOverlayItem managedOverlayItem) {
        		mapController.zoomIn();
        		
        		if (hasFocusOnMe) {
        			mapController.animateTo(myGeoPoint);
        		} else {
        			mapController.animateTo(scGeoPoint);
        		}
                
                return true;
            }

            @Override
            public void onLongPress(MotionEvent arg0, ManagedOverlay arg1) {
            }

            @Override
            public void onLongPressFinished(MotionEvent motionEvent,
                                            ManagedOverlay managedOverlay,
                                            GeoPoint geoPoint,
                                            ManagedOverlayItem managedOverlayItem) {
        		if (!hasFocusOnMe) {
        			mapController.animateTo(myGeoPoint);
        			hasFocusOnMe = true;
        		} else {
        			mapController.animateTo(scGeoPoint);
        			hasFocusOnMe = false;
        		}
            }

            @Override
            public boolean onScrolled(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3, ManagedOverlay arg4) {
                return false;
            }

            @Override
            public boolean onSingleTap(MotionEvent motionEvent, 
                                       ManagedOverlay managedOverlay,
                                       GeoPoint geoPoint,
                                       ManagedOverlayItem managedOverlayItem) {
            	showMyDialog();
            	
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent zoomEvent, ManagedOverlay managedOverlay) {
                return false;
            }

        });
        
        mOverlayManager.populate();
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
