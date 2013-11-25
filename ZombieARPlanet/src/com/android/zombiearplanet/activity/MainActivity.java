package com.android.zombiearplanet.activity;

import java.io.IOException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.android.zombiearplanet.R;
import com.metaio.sdk.MetaioDebug;
import com.metaio.tools.io.AssetsManager;



public class MainActivity extends BaseActivity {
	AssetsExtracter mTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_main);
		mTask = new AssetsExtracter();
		mTask.execute(0);
	}
		
	@Override
	protected void onPostCreate(Bundle savedInstanceState){
	       super.onPostCreate(savedInstanceState);
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
	
	
	private class AssetsExtracter extends AsyncTask<Integer, Integer, Boolean>
	{

		@Override
		protected void onPreExecute() 
		{			
		}
		
		@Override
		protected Boolean doInBackground(Integer... params) 
		{
			try 
			{
				AssetsManager.extractAllAssets(getApplicationContext(), true);
			} 
			catch (IOException e) 
			{
				MetaioDebug.printStackTrace(Log.ERROR, e);
				return false;
			}

			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) 
		{			
			
			if (result)
			{
				//Toast.makeText(SplashScreenActivity.this, "Metaio Assets Loaded", Toast.LENGTH_SHORT).show();
			}
			else
			{
				MetaioDebug.log(Log.ERROR, "Error extracting assets, closing the application...");
				finish();
			}
	    }
	}
}
