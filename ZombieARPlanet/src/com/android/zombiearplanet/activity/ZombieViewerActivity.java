package com.android.zombiearplanet.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.zombiearplanet.R;
import com.android.zombiearplanet.model.PlayerModel;
import com.android.zombiearplanet.model.ZombieModel;
import com.android.zombiearplanet.tasks.ZombieClientHandler;
import com.android.zombiearplanet.util.ZombieARDataManager;
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
	ZombieClientHandler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		metaioSDK.setTrackingConfiguration("GPS");
		mCallbackHandler = new MetaioSDKCallbackHandler();
		handler = new ZombieClientHandler(new Runnable(){

			@Override
			public void run() {
				PlayerModel playerModel = ZombieARDataManager.getInstance().getCurrentPlayer();
				LLACoordinate playerPos = new LLACoordinate();
				playerPos.setLatitude(playerModel.latitude);
				playerPos.setLongitude(playerModel.longitude);
				mSensors.setManualLocation(playerPos);
			}
			
		},10000);
	}
	
	@Override
	protected void onPause() 
	{
		super.onPause();
		if (mSensors != null){
			mSensors.registerCallback(null);
		}
		handler.stopUpdates();
	}

	@Override
	protected void onResume(){
		super.onResume();
		if (mSensors != null){
			mSensors.registerCallback(this);
		}
		handler.startUpdates();
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
		
		mRadar = metaioSDK.createRadar();
		mRadar.setBackgroundTexture(AssetsManager.getAssetPath("radar.png"));
		mRadar.setObjectsDefaultTexture(AssetsManager.getAssetPath("yellow.png"));
		mRadar.setRelativeToScreen(IGeometry.ANCHOR_TL);
		
		PlayerModel playerModel = ZombieARDataManager.getInstance().getCurrentPlayer();
		LLACoordinate playerPos = new LLACoordinate();
		playerPos.setLatitude(playerModel.latitude);
		playerPos.setLongitude(playerModel.longitude);
		mSensors.setManualLocation(playerPos);

		List<ZombieModel> models = ZombieARDataManager.getInstance().getZombies();
		for(int i=0; i< models.size();i++){
			ZombieModel model = models.get(i);			
			
			IGeometry zombieGeometry = metaioSDK.createGeometry(zombieModelStr);		
						
			LLACoordinate coord = ZombieViewerActivity.this.mSensors.getLocation();
			coord.setLatitude(model.latitude);
			coord.setLongitude(model.longitude);
			zombieGeometry.setTranslationLLA(coord);
			zombieGeometry.setScale(1000f);
			zombieGeometry.setRotation(new Rotation(0,0,270));
			zombieGeometry.setVisible(true);		
			mRadar.add(zombieGeometry);
		}
	}
	
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
