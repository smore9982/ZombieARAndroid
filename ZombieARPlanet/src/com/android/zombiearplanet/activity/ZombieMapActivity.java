package com.android.zombiearplanet.activity;

import java.util.List;

import com.android.zombiearplanet.R;
import com.android.zombiearplanet.model.HumanModel;
import com.android.zombiearplanet.model.PlayerModel;
import com.android.zombiearplanet.model.ZombieModel;
import com.android.zombiearplanet.util.Util;
import com.android.zombiearplanet.util.ZombieARDataManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class ZombieMapActivity extends BaseActivity {
	public static final String TAG = "ZombieMapActivity";

	MapFragment mapFragment;
	GoogleMap map;
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zombiemap);
        
        mapFragment = (MapFragment) this.getFragmentManager().findFragmentById(R.id.zombieMap);
        map = mapFragment.getMap();
        initMap();
    }
    
    @Override
    public void onPostCreate(Bundle savedInstanceState){
    	super.onPostCreate(savedInstanceState);
    }
    
    public void initMap(){
    	PlayerModel player = ZombieARDataManager.getInstance().getCurrentPlayer();  
    	LatLng playerPos = new LatLng(player.latitude,player.longitude);    											  
    	if(player.isZombie()){
    		List<HumanModel> humans = ZombieARDataManager.getInstance().getHumans();
            for(int i = 0; i<humans.size(); i++){
            	HumanModel humanModel = humans.get(i);
            	LatLng humanPos = new LatLng(humanModel.latitude,humanModel.longitude);   
            	MarkerOptions marker = new MarkerOptions().position(humanPos).title(humanModel.name);
            	map.addMarker(marker);            	
            	double distance = Util.getDistanceFromLatLon(player.latitude, player.longitude, humanModel.latitude, humanModel.longitude);
            	Log.i(TAG, "Distance " + distance);
            	
            }          
        	map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener(){

				@Override
				public void onMyLocationChange(Location location) {
					LatLng markerPos = new LatLng(location.getLatitude(),location.getLongitude());    	
					CircleOptions circle = new CircleOptions().center(markerPos).radius(20.00).fillColor(Color.parseColor("#880000")).strokeWidth(5);
			        map.addCircle(circle);
				}
        		
        	});
    	}else{
    		List<ZombieModel> zombies = ZombieARDataManager.getInstance().getZombies();
            for(int i = 0; i<zombies.size(); i++){
            	ZombieModel zombieModel = zombies.get(i);
            	LatLng zombiePos = new LatLng(zombieModel.latitude,zombieModel.longitude);   
            	MarkerOptions marker = new MarkerOptions().position(zombiePos).title(zombieModel.name);
            	CircleOptions circle = new CircleOptions().center(zombiePos).radius(zombieModel.getInfectionRange()).fillColor(Color.parseColor("#880000")).strokeWidth(5);
            	map.addMarker(marker);
            	map.addCircle(circle);
            	double distance = Util.getDistanceFromLatLon(player.latitude, player.longitude, zombiePos.latitude, zombiePos.longitude);
            	Log.i(TAG, "Distance " + distance);
            }

    	}
            	
    	CameraPosition cameraPosition = new CameraPosition.Builder()
    													  .target(playerPos)
    													  .zoom(17).build();    	
    	map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    	map.setMyLocationEnabled(true);    	
    	map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
			
			@Override
			public boolean onMyLocationButtonClick() {
				Intent i = new Intent(ZombieMapActivity.this,ZombieViewerActivity.class);
				ZombieMapActivity.this.startActivity(i);
				return true;
			}
		});
    }
}
