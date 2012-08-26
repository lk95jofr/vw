package se.knowit.android.vw.servicecenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParser {
    private static final String TAG = "JsonParser";

	private static String convertStreamToString(InputStream is) {
		/*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
		InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        	Log.e(TAG, e.getMessage(), e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            	Log.w(TAG, e.getMessage(), e);
            }
            
            try {
                isr.close();
            } catch (IOException e) {
            	Log.w(TAG, e.getMessage(), e);
            }
        }
        
        return sb.toString();
    }
	
	public static ArrayList<ServiceCenter> getGeoCoding(String brand, String city) {
		Log.d(TAG, "City: " + city);
		
		ArrayList<ServiceCenter> mServiceCenter = new ArrayList<ServiceCenter>();
		InputStream instream = null;
		
		try {
			StringBuilder url = new StringBuilder();
//			url.append("http://maps.googleapis.com/maps/api/geocode/json?address=Sveg,Sweden&sensor=true");
			url.append("http://maps.googleapis.com/maps/api/geocode/json?address=");
			url.append(city);
			url.append(",Sweden");
			url.append("&sensor=true");
			url.append("&language=sv");
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url.toString()); 
			HttpResponse response = httpclient.execute(httpget);
	
			Log.d(TAG, "Response: " + response.getStatusLine().toString());
			 
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            
            // If the response does not enclose an entity we don't need to close the connection
            if (entity != null) {
                instream = entity.getContent();
                String result = convertStreamToString(instream);
                
                Log.d(TAG, result);
 
                JSONObject jobj = new JSONObject(result);
                String results = jobj.getString("results");
	            JSONArray jarrResults = new JSONArray(results);
	            Log.d(TAG, "Results Size: " + jarrResults.length());
	            for (int i=0; i < jarrResults.length(); i++) {
	            	String geometry = jarrResults.getJSONObject(i).getString("geometry");
	            	JSONObject jarrGeometry = new JSONObject(geometry);
	                String location = jarrGeometry.getString("location");
	            	JSONObject jarrLocation = new JSONObject(location);
	                
	        		ServiceCenter serviceCenter = new ServiceCenter();
	        		serviceCenter.setDName(jarrResults.getJSONObject(i).getString("formatted_address").toString());
	        		serviceCenter.setDPhn("");
	        		serviceCenter.setDAddr("");
	        		serviceCenter.setDCity("");
	        		serviceCenter.setDUrl("");
	        		serviceCenter.setDlat(jarrLocation.getString("lat").toString());
	        		serviceCenter.setDlng(jarrLocation.getString("lng").toString());
	        		serviceCenter.setDist("");
	        		
	        		mServiceCenter.add(serviceCenter);
	            }
            }
		} catch (ClientProtocolException e) {
        	Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
        	Log.e(TAG, e.getMessage(), e);
        } catch (JSONException e) {
        	Log.e(TAG, e.getMessage(), e);
        } finally {
        	if (instream != null) {
	        	try {
	        		instream.close();
	        	} catch (IOException e) {
	            	Log.w(TAG, e.getMessage(), e);
	        	}
        	}
		}
        
        return mServiceCenter;
	}

	public static ArrayList<ServiceCenter> getServiceCenterFromServer(String brand, int nrOfWorkshops, String latitude, String longitude) {
		return getServiceCenterFromServer(brand, nrOfWorkshops, latitude, longitude, null);
	}
	
	public static ArrayList<ServiceCenter> getServiceCenterFromServer(String brand, int nrOfWorkshops, String city) {
		return getServiceCenterFromServer(brand, nrOfWorkshops, null, null, city);
	}
	
	public static ArrayList<ServiceCenter> getServiceCenterFromServer(String brand, int nrOfWorkshops, String latitude, String longitude, String city) {
		Log.d(TAG, "Brand: " + brand + ", Workshops: " + nrOfWorkshops + ", Latitude: " + latitude + ", Longitude: " + longitude + ", City: " + city);
		
		ArrayList<ServiceCenter> mServiceCenter = new ArrayList<ServiceCenter>();
		InputStream instream = null;
        
		try {
//			String url = "http://www.vwgruppen.se/Templates/GMaps/AppJason.aspx?brand=C&no=10&lat=57.786233&lng=14.172363";
//			String url = "http://www.vwgruppen.se/Templates/GMaps/AppJason.aspx?brand=C&no=10&lat=62.21211&lng=14.225590";
			StringBuilder url = new StringBuilder();
			url.append("http://www.vwgruppen.se/Templates/GMaps/AppJason.aspx?brand=");
			url.append(brand);
			if (nrOfWorkshops > 0) {
				url.append("&no=");
				url.append(nrOfWorkshops);
			}
			if ((latitude != null) && (longitude != null)) {
				url.append("&lat=");
				url.append(latitude);
				url.append("&lng=");
				url.append(longitude);
			} else if (city != null) {
				url.append("&loc=");
				url.append(city);
			}
			Log.d(TAG, "URL: " + url);
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url.toString()); 
			HttpResponse response = httpclient.execute(httpget);

			Log.d(TAG, "Response: " + response.getStatusLine().toString());
 
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            
            // If the response does not enclose an entity we don't need to close the connection
            if (entity != null) {
                instream = entity.getContent();
                String result = convertStreamToString(instream);
                
                Log.d(TAG, result);
 
	            JSONArray jarr = new JSONArray(result);
	            Log.d(TAG, "Results Size: " + jarr.length());
	            for (int i=0; i < jarr.length(); i++) {
	        		ServiceCenter serviceCenter = new ServiceCenter();
	        		serviceCenter.setDName(jarr.getJSONObject(i).getString("dname").toString());
	        		serviceCenter.setDPhn(jarr.getJSONObject(i).getString("dphn").toString());
	        		serviceCenter.setDAddr(jarr.getJSONObject(i).getString("daddr").toString());
	        		serviceCenter.setDCity(jarr.getJSONObject(i).getString("dcity").toString());
	        		serviceCenter.setDUrl(jarr.getJSONObject(i).getString("durl").toString());
	        		serviceCenter.setDlat(jarr.getJSONObject(i).getString("lat").toString());
	        		serviceCenter.setDlng(jarr.getJSONObject(i).getString("lng").toString());
	        		serviceCenter.setDist(jarr.getJSONObject(i).getString("dist").toString());
	        		
	        		mServiceCenter.add(serviceCenter);
	            }
            }
        } catch (ClientProtocolException e) {
        	Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
        	Log.e(TAG, e.getMessage(), e);
        } catch (JSONException e) {
        	Log.e(TAG, e.getMessage(), e);
        } finally {
        	if (instream != null) {
	        	try {
	        		instream.close();
	        	} catch (IOException e) {
	            	Log.w(TAG, e.getMessage(), e);
	        	}
        	}
        }
        
        return mServiceCenter;
    }
	
	public static ArrayList<String> getRouteFromGoogle(String origin, String dest) {
		Log.d(TAG, "Origin: " + origin + ", Destination: " + dest);
		
		ArrayList<String> mRouteList = new ArrayList<String>();
        InputStream instream = null;
        
		try {
//			http://maps.googleapis.com/maps/api/directions/json?origin=57.786233,14.172363&destination=57.6590501,14.9625166&language=sv&sensor=true
			StringBuilder url = new StringBuilder();
			url.append("http://maps.googleapis.com/maps/api/directions/json?origin=");
			url.append(origin);
			url.append("&destination=");
			url.append(dest);
			url.append("&language=sv&sensor=true");
			Log.d(TAG, "URL: " + url);
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url.toString()); 
			HttpResponse response = httpclient.execute(httpget);
			Log.d(TAG, response.getStatusLine().toString());
 
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            
            // If the response does not enclose an entity we don't need to close the connection
            if (entity != null) {
                instream = entity.getContent();
                String result = convertStreamToString(instream);
                Log.d(TAG, result);
                
                
	            JSONObject jobj = new JSONObject(result);
	            String routes = jobj.getString("routes");
	            JSONArray jarrRoutes = new JSONArray(routes);
	            String legs = jarrRoutes.getJSONObject(0).getString("legs");
	            JSONArray jarrLegs = new JSONArray(legs);
	            String steps = jarrLegs.getJSONObject(0).getString("steps");
	            JSONArray jarrSteps = new JSONArray(steps);
            	Log.d(TAG, "Results Size: " + jarrSteps.length());
	            for (int i=0; i < jarrSteps.length(); i++) {
	            	String html = jarrSteps.getJSONObject(i).getString("html_instructions");
	            	Log.d(TAG, html);
	            	
	            	mRouteList.add(html);
	            }
            }
        } catch (ClientProtocolException e) {
        	Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
        	Log.e(TAG, e.getMessage(), e);
        } catch (JSONException e) {
        	Log.e(TAG, e.getMessage(), e);
        } finally {
        	if (instream != null) {
	        	try {
	        		instream.close();
	        	} catch (IOException e) {
	            	Log.w(TAG, e.getMessage(), e);
	        	}
        	}
        }
        
        return mRouteList;
    }
}
