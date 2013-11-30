package com.android.zombiearplanet.service;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.zombiearplanet.activity.ZombieViewerActivity;
import com.android.zombiearplanet.application.ZombieARApplication;
import com.android.zombiearplanet.model.HumanModel;
import com.android.zombiearplanet.model.PlayerModel;
import com.android.zombiearplanet.model.ZombieModel;
import com.android.zombiearplanet.network.RequestQueueManager;
import com.android.zombiearplanet.network.ZombieARRequestArgs;
import com.android.zombiearplanet.network.ZombieClientRequest;
import com.android.zombiearplanet.tasks.ZombieClientHandler;
import com.android.zombiearplanet.util.ZombieARDataManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.metaio.sdk.jni.LLACoordinate;
import com.metaio.sdk.jni.Rotation;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ZombieARService extends Service implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener  {
	public final static String TAG = "ZombieARLocationService";
	
	LocationClient locationClient;
	LocationRequest locationRequest;
	ZombieClientHandler handler;
	
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
		this.handler.stopUpdates();
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.i(TAG, "Starting location service");
		locationClient.connect();
		handler = new ZombieClientHandler(new Runnable(){

			@Override
			public void run() {
				getDataBundle();
			}
			
		});
		this.handler.startUpdates();
		return Service.START_NOT_STICKY;
	}
	
	
	public void getDataBundle(){
		Listener<JSONObject> findDataRequestListener = new Listener<JSONObject>(){
			public void onResponse(JSONObject jsonRoot){
				ZombieARDataManager.parseBundle(jsonRoot);	
			}
		};
		
		ErrorListener findErrorRequestListener = new ErrorListener(){
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Error " + error.getMessage());
			}		
		};
		
		RequestQueue mRequestQueue = RequestQueueManager.getRequestQueueInstance(getApplicationContext());			
		mRequestQueue.add(new ZombieClientRequest(Method.POST,ZombieARApplication.URL_CLIENT_SERVICE,ZombieARRequestArgs.getDataBundleArgs(ZombieARApplication.getUsername()),findDataRequestListener,findErrorRequestListener));
	}
	
	public void getZombieLoc(){
		Listener<JSONObject> findPlayerRequestListener = new Listener<JSONObject>(){
			public void onResponse(JSONObject jsonRoot){
				Log.i(TAG, "Zombie Data" + jsonRoot);
				String result = jsonRoot.optString("result");
				if(result.equals("0")){
					JSONArray arr = jsonRoot.optJSONArray("locdata");
					ZombieARDataManager.getInstance().getZombies().clear();
					for(int i=0; i<arr.length() && i<10;i++){
						JSONObject data = arr.optJSONObject(i);
						if(data!=null){
							String username = data.optString("username");
							double longitude = data.optDouble("longitude",0.0);
							double latitidue = data.optDouble("latitude",0.0);
							double infectionRange = data.optDouble("infectionRange", 10.0);
							ZombieModel model = new ZombieModel(username,latitidue,longitude,infectionRange);
							ZombieARDataManager.getInstance().getZombies().add(model);
						}
					}
				}			
			}
		};
		
		RequestQueue mRequestQueue = RequestQueueManager.getRequestQueueInstance(getApplicationContext());			
		mRequestQueue.add(new ZombieClientRequest(Method.POST,ZombieARApplication.URL_CLIENT_SERVICE,ZombieARRequestArgs.getFindZombieParameter(),findPlayerRequestListener,findPlayerErrorListener));	
	}
	
	public void getHumanLoc(){
		Listener<JSONObject> findPlayerRequestListener = new Listener<JSONObject>(){
			public void onResponse(JSONObject jsonRoot){
				Log.i(TAG, "Human Data" + jsonRoot);
				String result = jsonRoot.optString("result");
				if(result.equals("0")){
					JSONArray arr = jsonRoot.optJSONArray("locdata");
					ZombieARDataManager.getInstance().getZombies().clear();
					for(int i=0; i<arr.length() && i<10;i++){
						JSONObject data = arr.optJSONObject(i);
						if(data!=null){
							String username = data.optString("username");
							double longitude = data.optDouble("longitude",0.0);
							double latitidue = data.optDouble("latitude",0.0);
							HumanModel model = new HumanModel(username,latitidue,longitude);
							ZombieARDataManager.getInstance().getHumans().add(model);
						}
					}
				}			
			}
		};
		
		
		RequestQueue mRequestQueue = RequestQueueManager.getRequestQueueInstance(getApplicationContext());			
		mRequestQueue.add(new ZombieClientRequest(Method.POST,ZombieARApplication.URL_CLIENT_SERVICE,ZombieARRequestArgs.getFindHumanParameter(),findPlayerRequestListener,findPlayerErrorListener));	
	}
	
	ErrorListener findPlayerErrorListener = new ErrorListener(){
		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e(TAG, "Error " + error.getMessage());
		}		
	};

	@Override
	public void onLocationChanged(Location location) {
		Log.i(TAG, "Updating location " + location.toString());
		double longitude  = location.getLongitude();
		double latitude = location.getLatitude();
		String username = ZombieARApplication.getUsername();		
		if(username.length() != 0 ){
			if(ZombieARDataManager.getInstance().getCurrentPlayer() == null){
				PlayerModel model = new HumanModel(username,location.getLatitude(),location.getLongitude());
				ZombieARDataManager.getInstance().setCurrentPlayer(model);
			}else{
				ZombieARDataManager.getInstance().getCurrentPlayer().latitude = location.getLatitude();
				ZombieARDataManager.getInstance().getCurrentPlayer().longitude = location.getLongitude();
			}			
			RequestQueue mRequestQueue = RequestQueueManager.getRequestQueueInstance(getApplicationContext());			
			mRequestQueue.add(new ZombieClientRequest(Method.POST,ZombieARApplication.URL_CLIENT_SERVICE,
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

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}