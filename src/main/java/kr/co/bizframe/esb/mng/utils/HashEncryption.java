package kr.co.bizframe.esb.mng.utils;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

public class HashEncryption {

	private static HashEncryption instance = new HashEncryption();

	public static HashEncryption getInstance() {
		return instance;
	}

	public String encryptSHA1(String plainText) throws Exception {
		MessageDigest md = null;

		md = MessageDigest.getInstance("SHA");
		md.update(plainText.getBytes("UTF-8"));

		byte raw[] = md.digest();

		String hash = Base64.encodeBase64String(raw);
		return hash;
	}
	
}
