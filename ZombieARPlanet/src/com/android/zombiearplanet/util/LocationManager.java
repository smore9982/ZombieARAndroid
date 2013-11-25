package com.android.zombiearplanet.util;

import android.location.Location;

public class LocationManager {
	
	public static LocationManager instance;
	
	private Location currentLocation;
	
	public static LocationManager getInstance(){
		if(instance == null){
			instance = new LocationManager();
		}
		return instance;
	}
	
	private LocationManager(){
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}
	
}
