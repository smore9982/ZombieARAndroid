package com.android.zombiearplanet.tasks;

import android.os.Handler;
import android.os.Looper;

public class ZombieClientHandler {
	// Create a Handler that uses the Main Looper to run in
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Runnable runnable;
    private int UPDATE_INTERVAL = 10000;

    public ZombieClientHandler(final Runnable runnable) {
    	this.runnable = new Runnable() {
    		@Override
    		public void run() {
    			runnable.run();
    			mHandler.postDelayed(this, UPDATE_INTERVAL);
    		}
    	};
    }

    public ZombieClientHandler(Runnable runnable, int interval){
    	this(runnable);
    	UPDATE_INTERVAL = interval;            
    }

    
    public synchronized void startUpdates(){
    	runnable.run();
    }

    
    public synchronized void stopUpdates(){
    	mHandler.removeCallbacks(runnable);
    }
}
