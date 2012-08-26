package se.knowit.android.vw.servicecenter;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class StartActivity extends TabActivity {
    private static final String TAG = "StartActivity";
    
    public TabHost tabHost;
    
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        int background = R.drawable.background;
	    String s = System.getProperty("background");
	    if ("transport".equals(s)) {
	    	background = R.drawable.background_tr;
	    }
	      
	    LinearLayout linLay = (LinearLayout) findViewById(R.id.tab_root);
	    linLay.setBackgroundResource(background);

        Resources res = getResources();
        tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, HomeActivity.class);
        spec = tabHost.newTabSpec("home").setIndicator(null, res.getDrawable(R.drawable.home_tab)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SearchGroup.class);
        spec = tabHost.newTabSpec("search").setIndicator(null, res.getDrawable(R.drawable.search_tab)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ServiceActivity.class);
        spec = tabHost.newTabSpec("service").setIndicator(null, res.getDrawable(R.drawable.service_tab)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, MobGarantiActivity.class);
        spec = tabHost.newTabSpec("mobgaranti").setIndicator(null, res.getDrawable(R.drawable.mobgaranti_tab)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, MoreGroup.class);
        spec = tabHost.newTabSpec("more").setIndicator(null, res.getDrawable(R.drawable.more_tab)).setContent(intent);
        tabHost.addTab(spec);

        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
        	tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#00000000"));
        }
        
        tabHost.setCurrentTab(0);
	}
}
