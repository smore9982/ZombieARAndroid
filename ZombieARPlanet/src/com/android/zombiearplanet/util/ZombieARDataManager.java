package com.android.zombiearplanet.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.android.zombiearplanet.application.ZombieARApplication;
import com.android.zombiearplanet.model.HumanModel;
import com.android.zombiearplanet.model.PlayerModel;
import com.android.zombiearplanet.model.ZombieModel;

public class ZombieARDataManager {
	public final static String TAG = "ZombieARDataManager";
	public static ZombieARDataManager instance;
	
	private PlayerModel currentPlayer;
	private List<ZombieModel> zombieList;
	private List<HumanModel> humanList;
	
	public static ZombieARDataManager getInstance(){
		if(instance == null){
			instance = new ZombieARDataManager();
		}
		return instance;
	}
	
	public static void parseBundle(JSONObject bundle){
		Log.i(TAG, "BundleData" + bundle);
		String result = bundle.optString("result");
		if(result.equals("0")){
			JSONArray arr = bundle.optJSONArray("ZombieData");
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
			arr = bundle.optJSONArray("HumanData");
			ZombieARDataManager.getInstance().getHumans().clear();
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
			JSONObject playerJSON = bundle.optJSONObject("PlayerData");
			if(playerJSON!=null){
				String username = playerJSON.optString("username");
				double longitude = playerJSON.optDouble("longitude",0.0);
				double latitidue = playerJSON.optDouble("latitude",0.0);
				PlayerModel playerModel = new PlayerModel(username,latitidue,longitude);
				ZombieARApplication.setUsername(playerModel.name);
				playerModel.setZombie(playerJSON.optBoolean("isZombie", false));				
				ZombieARDataManager.getInstance().setCurrentPlayer(playerModel);
			}
		}
	}	
	
	public static boolean isCurrentPlayerZombie(){
		return ZombieARDataManager.getInstance().currentPlayer.isZombie();
	}
	
	private ZombieARDataManager(){
		currentPlayer =null;
		zombieList = new ArrayList<ZombieModel>();
		humanList = new ArrayList<HumanModel>();
	}

	public PlayerModel getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(PlayerModel currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public List<ZombieModel> getZombies(){
		return this.zombieList;
	}
	
	public void setZombies(List<ZombieModel> zombieList){
		this.zombieList = zombieList;
	}
	
	public List<HumanModel> getHumans(){
		return this.humanList;
	}
	
	public void setHumans(List<HumanModel> humanList){
		this.humanList = humanList;
	}	
}
