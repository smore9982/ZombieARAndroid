package com.android.zombiearplanet.model;

public class PlayerModel {
	public String name;
	public double latitude;
	public double longitude;	
	private boolean isZombie;
	
	public PlayerModel(String name,double latitude, double longitude){
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public boolean isZombie() {
		return isZombie;
	}

	public void setZombie(boolean isZombie) {
		this.isZombie = isZombie;
	}
	
	
}
