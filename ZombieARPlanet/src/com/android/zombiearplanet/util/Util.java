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
}
