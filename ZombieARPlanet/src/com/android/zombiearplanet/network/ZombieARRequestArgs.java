package com.android.zombiearplanet.network;

import org.json.JSONObject;

public class ZombieARRequestArgs {
	public static JSONObject getLoginRequestParameter(String username, String password){
		JSONObject obj = new JSONObject();
		try{
			obj.put("action", "LOGIN");
			obj.put("username", username);
			obj.put("password", password);
		}catch(Exception e){
			
		}
		return obj;
	}
	
	public static JSONObject getRegisterParameter(String username, String password){
		JSONObject obj = new JSONObject();
		try{
			obj.put("action", "REGISTER");
			obj.put("username", username);
			obj.put("password", password);
		}catch(Exception e){
			
		}
		return obj;
	}
	
	public static JSONObject getUpdateLocationParameter(String username, double longitude, double latitude){
		JSONObject obj = new JSONObject();
		try{
			obj.put("action", "LOCATIONUPDATE");
			obj.put("username", username);
			obj.put("longitude",longitude);
			obj.put("latitude", latitude);
		}catch(Exception e){
			
		}
		return obj;
	}
	
	public static JSONObject getFindZombieParameter(){
		JSONObject obj = new JSONObject();
		try{
			obj.put("action", "findzombies");
		}catch(Exception e){
			
		}
		return obj;
	}
}
