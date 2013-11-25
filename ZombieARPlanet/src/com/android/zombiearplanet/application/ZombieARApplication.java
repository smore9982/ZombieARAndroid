package com.android.zombiearplanet.application;

import android.app.Application;
import android.content.SharedPreferences;

public class ZombieARApplication extends Application{
	private static String TAG = "ZombieARApplication";
	private static String PREF_FILE_NAME = "ZombieARPRef";
	
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
