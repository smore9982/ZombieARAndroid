package com.android.zombiearplanet.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.zombiearplanet.R;
import com.android.zombiearplanet.network.RequestQueueManager;
import com.android.zombiearplanet.network.ZombieARRequestArgs;
import com.android.zombiearplanet.network.ZombieClientRequest;
import com.android.zombiearplanet.service.ZombieARLocationService;
import com.android.zombiearplanet.util.LocationManager;
import com.metaio.sdk.ARViewActivity;
import com.metaio.sdk.SensorsComponentAndroid;
import com.metaio.sdk.jni.IGeometry;
import com.metaio.sdk.jni.IMetaioSDKCallback;
import com.metaio.sdk.jni.IRadar;
import com.metaio.sdk.jni.LLACoordinate;
import com.metaio.sdk.jni.Rotation;
import com.metaio.sdk.jni.TrackingValuesVector;
import com.metaio.tools.io.AssetsManager;

public class ZombieViewerActivity extends ARViewActivity implements SensorsComponentAndroid.Callback{
	private final static String TAG = "ZombieViewerActivity";
	
	
	ArrayList<IGeometry> zombieModels = new ArrayList<IGeometry>();
	IRadar mRadar;	
	private MetaioSDKCallbackHandler mCallbackHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		metaioSDK.setTrackingConfiguration("GPS");
		mCallbackHandler = new MetaioSDKCallbackHandler();		
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		if (mSensors != null){
			mSensors.registerCallback(null);
		}			
	}

	@Override
	protected void onResume(){
		super.onResume();
		if (mSensors != null){
			mSensors.registerCallback(this);
		}
		
	}

	@Override
	public void onGravitySensorChanged(float[] arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onHeadingSensorChanged(float[] orientation) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onLocationSensorChanged(LLACoordinate location) {
		// TODO Auto-generated method stub		
	}

	@Override
	protected int getGUILayout() {
		return R.layout.location_overlay;
	}

	@Override
	protected IMetaioSDKCallback getMetaioSDKCallbackHandler() {
		return mCallbackHandler;
	}

	@Override
	protected void loadContents() {				
		String zombieModelStr = AssetsManager.getAssetPath("Zombie1Assets/Zombie1.obj");
				
		for(int i=0; i<10;i++){
			if(zombieModelStr != null){
				IGeometry zombieGeometry = metaioSDK.createGeometry(zombieModelStr);
				zombieModels.add(zombieGeometry);				
			}
		}
				
		mRadar = metaioSDK.createRadar();
		mRadar.setBackgroundTexture(AssetsManager.getAssetPath("radar.png"));
		mRadar.setObjectsDefaultTexture(AssetsManager.getAssetPath("yellow.png"));
		mRadar.setRelativeToScreen(IGeometry.ANCHOR_TL);	
		
		String url = "http://ec2-54-204-47-139.compute-1.amazonaws.com:8080/ZombieARWebService/client";
		RequestQueue mRequestQueue = RequestQueueManager.getRequestQueueInstance(getApplicationContext());			
		mRequestQueue.add(new ZombieClientRequest(Method.POST,url,ZombieARRequestArgs.getFindZombieParameter(),findZombieRequestListener,findZombieErrorListener));	
	}
	
	Listener<JSONObject> findZombieRequestListener = new Listener<JSONObject>(){
		public void onResponse(JSONObject jsonRoot){
			Log.i(TAG, "Zombie Data" + jsonRoot);
			String result = jsonRoot.optString("result");
			if(result.equals("0")){
				JSONArray arr = jsonRoot.optJSONArray("locdata");
				for(int i=0; i<arr.length() && i<10;i++){
					JSONObject data = arr.optJSONObject(i);
					if(data!=null){
						String username = data.optString("username");
						double longitude = data.optDouble("longitude",0.0);
						double latitidue = data.optDouble("latitude",0.0);
						
						LLACoordinate coord = ZombieViewerActivity.this.mSensors.getLocation();
						coord.setLatitude(latitidue);
						coord.setLongitude(longitude);
						zombieModels.get(i).setTranslationLLA(coord);
						zombieModels.get(i).setScale(1000f);
						zombieModels.get(i).setRotation(new Rotation(0,0,270));
						zombieModels.get(i).setVisible(true);
						
						mRadar.add(zombieModels.get(i));
					}
				}
				
			}
		}
	};
	
	ErrorListener findZombieErrorListener = new ErrorListener(){

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e(TAG, "Error " + error.getMessage());
		}
		
	};

	@Override
	protected void onGeometryTouched(IGeometry geometry) {
		
	}	
	
	final private class MetaioSDKCallbackHandler extends IMetaioSDKCallback{
		@Override
		public void onSDKReady(){
			// show GUI after SDK is ready
			runOnUiThread(new Runnable(){
				@Override
				public void run() {
					mGUIView.setVisibility(View.VISIBLE);
				}
			});
		}
				
		@Override
		public void onTrackingEvent(TrackingValuesVector trackingValues){
			super.onTrackingEvent(trackingValues);

		}
	}

}
