package com.android.zombiearplanet.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

public class RequestQueueManager {
	
	private static RequestQueue instance; 	
	
	public static RequestQueue getRequestQueueInstance(Context ctx){
		if(instance == null){
			instance = Volley.newRequestQueue(ctx);
		}
		return instance;
	}
}
