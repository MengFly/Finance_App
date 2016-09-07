package com.example.econonew.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 这个类用于提供一些加密的方法
 *
 * @author mengfei
 *
 */
public class EncodeStrTool {

	private EncodeStrTool() {
	}

	private static volatile EncodeStrTool mInstance;

	/**
	 * 使用MD5算法对传入的key进行加密并返回
	 */
	public String getEncodeMD5Str(String key) {
		String cacheKey;
		try {
			MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public static EncodeStrTool getInstance() {
		if(mInstance == null) {
			synchronized(EncodeStrTool.class) {
				if(mInstance == null) {
					mInstance = new EncodeStrTool();
				}
			}
		}
		return mInstance;
	}


}
