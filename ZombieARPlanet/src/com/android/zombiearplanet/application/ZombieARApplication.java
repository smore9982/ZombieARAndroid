package com.android.zombiearplanet.application;

import org.json.JSONObject;

import android.app.Application;
import android.content.SharedPreferences;

public class ZombieARApplication extends Application{
	private static String TAG = "ZombieARApplication";
	private static String PREF_FILE_NAME = "ZombieARPRef";
	
	public static String URL_CLIENT_SERVICE = "http://ec2-54-204-47-139.compute-1.amazonaws.com:8080/ZombieARWebService/client"; 
	
	private static SharedPreferences pref;
	
	@Override
	public void onCreate(){
		super.onCreate();
		pref = this.getSharedPreferences(PREF_FILE_NAME, 0);
	}
	
	public static boolean setUsername(String username){		
		return pref.edit().putString("username", username).commit();
	}
	
	public static String getUsername(){		
		return pref.getString("username", "");
	}
	
}
