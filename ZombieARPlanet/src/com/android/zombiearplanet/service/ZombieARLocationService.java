package com.android.zombiearplanet.service;

import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.zombiearplanet.application.ZombieARApplication;
import com.android.zombiearplanet.network.RequestQueueManager;
import com.android.zombiearplanet.network.ZombieARRequestArgs;
import com.android.zombiearplanet.network.ZombieClientRequest;
import com.android.zombiearplanet.util.LocationManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ZombieARLocationService extends Service implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener  {
	public final static String TAG = "ZombieARLocationService";
	
	LocationClient locationClient;
	LocationRequest locationRequest;
	
	@Override
	public void onCreate(){
		super.onCreate();
		locationClient = new LocationClient(this, this, this);
		locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(20000);
		locationRequest.setFastestInterval(100);
	}
	
	@Override 
	public void onDestroy(){
		Log.i(TAG, "Disconnecting location service");
		if(locationClient.isConnected()){
			locationClient.removeLocationUpdates(this);
		}
		locationClient.disconnect();
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.i(TAG, "Starting location service");
		locationClient.connect();
		return Service.START_NOT_STICKY;
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.i(TAG, "Updating location " + location.toString());
		double longitude  = location.getLongitude();
		double latitude = location.getLatitude();
		String username = ZombieARApplication.getUsername();		
		if(username.length() != 0 ){
			LocationManager.getInstance().setCurrentLocation(location);
			RequestQueue mRequestQueue = RequestQueueManager.getRequestQueueInstance(getApplicationContext());			
			mRequestQueue.add(new ZombieClientRequest(Method.POST,"http://ec2-54-204-47-139.compute-1.amazonaws.com:8080/ZombieARWebService/client",
													  ZombieARRequestArgs.getUpdateLocationParameter(username,longitude,latitude),
													  locationUpdateRequestListener, locationUpdateErrorListener));
		}
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.e(TAG, "There was a problem connecting to google play services " + result.toString());
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {		
		this.locationClient.requestLocationUpdates(locationRequest, this);		
	}

	@Override
	public void onDisconnected() {
	}
	
	Listener<JSONObject> locationUpdateRequestListener = new Listener<JSONObject>(){
		public void onResponse(JSONObject jsonRoot){
			Log.i(TAG, "Return Data" + jsonRoot);
						
		}
	};
	
	ErrorListener locationUpdateErrorListener = new ErrorListener(){

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e(TAG, "Error " + error.getMessage());
			Toast.makeText(getApplicationContext(), "There was an error trying to login", Toast.LENGTH_SHORT).show();
		}
		
	};

}
