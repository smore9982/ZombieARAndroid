package com.android.zombiearplanet.model;

public class ZombieModel extends PlayerModel{
	private double infectionRange = 10.0;
	public ZombieModel(String name, double latitude, double longitude, double range) {		
		super(name, latitude, longitude);	
		infectionRange = range;
	}
	public double getInfectionRange() {
		return infectionRange;
	}
	public void setInfectionRange(double infectionRange) {
		this.infectionRange = infectionRange;
	}
	
	

}
