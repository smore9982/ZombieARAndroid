package com.android.zombiearplanet.activity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.zombiearplanet.R;
import com.android.zombiearplanet.application.ZombieARApplication;
import com.android.zombiearplanet.network.RequestQueueManager;
import com.android.zombiearplanet.network.ZombieARRequestArgs;
import com.android.zombiearplanet.network.ZombieClientRequest;
import com.android.zombiearplanet.service.ZombieARLocationService;
import com.android.zombiearplanet.util.Util;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private static final String TAG = "LOGINACTIVITY";
	
	String url = "http://ec2-54-204-47-139.compute-1.amazonaws.com:8080/ZombieARWebService/client";
	EditText usernameEditTextView;
	EditText passwordEditTextView;
	Button signInButton;
	Button registerButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		usernameEditTextView = (EditText) findViewById(R.id.login_username);
		passwordEditTextView = (EditText) findViewById(R.id.login_password);
		signInButton = (Button) findViewById(R.id.login_signin_button);
		registerButton = (Button) findViewById(R.id.login_register_button);
		signInButton.setOnClickListener(signinListener);
		registerButton.setOnClickListener(registerListener);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}
	    
	@Override
	protected void onRestart(){
		super.onRestart();
	}

	@Override
	protected void onResume(){
		super.onResume();
	}
		
	@Override
	protected void onPause(){
		super.onPause();
	}
		
	@Override
	protected void onStop(){
		super.onStop();
	}
		
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	OnClickListener signinListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			try{
				String name = (String) usernameEditTextView.getText().toString();
				String pass = Util.SHA512(passwordEditTextView.getText().toString());
				RequestQueue mRequestQueue = RequestQueueManager.getRequestQueueInstance(getApplicationContext());			
				mRequestQueue.add(new ZombieClientRequest(Method.POST,url,ZombieARRequestArgs.getLoginRequestParameter(name, pass),loginRequestListener,loginErrorListener));				

			}catch(Exception e){
				
			}
		}
		
	};
	
	OnClickListener registerListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			try{
				String name = (String) usernameEditTextView.getText().toString();
				String pass = Util.SHA512(passwordEditTextView.getText().toString());
				RequestQueue mRequestQueue = RequestQueueManager.getRequestQueueInstance(getApplicationContext());			
				mRequestQueue.add(new ZombieClientRequest(Method.POST,url,ZombieARRequestArgs.getRegisterParameter(name, pass),loginRequestListener,loginErrorListener));
				mRequestQueue.start();
			}catch(Exception e){
				
			}
		}
		
	};
	
	Listener<JSONObject> loginRequestListener = new Listener<JSONObject>(){
		public void onResponse(JSONObject jsonRoot){
			Log.i(TAG, "Return Data" + jsonRoot);
			
			String result = jsonRoot.optString("result");
			if(result.equals("0")){
				String username = jsonRoot.optString("username");
				ZombieARApplication.setUsername(username);
				Intent serviceIntent = new Intent(LoginActivity.this,ZombieARLocationService.class);
				LoginActivity.this.startService(serviceIntent);
				
				Intent nextIntent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(nextIntent);
			}						
		}
	};
	
	ErrorListener loginErrorListener = new ErrorListener(){

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e(TAG, "Error " + error.getMessage());			
		}
		
	};
	
	Listener<JSONObject> registerRequestListener = new Listener<JSONObject>(){
		public void onResponse(JSONObject jsonRoot){
			Log.i(TAG, "Return Data" + jsonRoot);
			String result = jsonRoot.optString("result");
			if(result.equals("0")){
				Intent serviceIntent = new Intent(LoginActivity.this,ZombieARLocationService.class);
				LoginActivity.this.startService(serviceIntent);			
				Intent nextIntent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(nextIntent);
			}
		}
	};
	
	ErrorListener registerErrorListener = new ErrorListener(){

		@Override
		public void onErrorResponse(VolleyError error) {
			Log.e(TAG, "Error " + error.getMessage());
		}
		
	};

}
