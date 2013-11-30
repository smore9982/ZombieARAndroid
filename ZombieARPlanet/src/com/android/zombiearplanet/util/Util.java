package com.android.zombiearplanet.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
	public static String SHA512(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
	    md.update(text.getBytes());
	    byte[] hash = md.digest();
	    StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
                String tmp = Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1);
                buffer.append(tmp);
        }
        return buffer.toString();
	}
	
	public static double getDistanceFromLatLon(double lat1, double lon1, double lat2, double lon2) {
		  int R = 6371;
		  double dLat = deg2rad(lat2-lat1);
		  double dLon = deg2rad(lon2-lon1); 
		  double a =	Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2); 
		  double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		  double d = R * c;
		  d = d*1000;
		  return d;
		}

	public static double deg2rad(double deg) {
		return deg * (Math.PI/180);
	}
}
