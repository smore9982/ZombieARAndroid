package com.android.zombiearplanet.network;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class ZombieClientRequest extends Request<JSONObject> {
	
	private Listener<JSONObject> listener;
	private JSONObject jsonArgument;
	
	public ZombieClientRequest(int method, String url, JSONObject argument, Listener<JSONObject> listener, ErrorListener errorListener){		
		super(method, url,errorListener);
		this.listener = listener;
		this.jsonArgument = argument;
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try{
			String jsonString  = new String(response.data,HttpHeaderParser.parseCharset(response.headers));
			JSONObject returnData = new JSONObject(jsonString);
			return Response.success(returnData, HttpHeaderParser.parseCacheHeaders(response));
		}catch(Exception e){
			return Response.error(new ParseError(e));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		listener.onResponse(response);
	}
	
	@Override
	protected Map<String, String> getParams(){
		Map<String,String> params = new HashMap<String,String>();
		params.put("payload", jsonArgument.toString());
		return params;
	};
	
}
