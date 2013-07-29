package com.m6.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;


public class SecurityUtils {
	
	public static String MD5Encry(String text) {
		if (TextUtils.isEmpty(text)) {
			return null;
		}
		
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(text.getBytes());
			return Base64.encodeToString(md5.digest(), Base64.DEFAULT);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
