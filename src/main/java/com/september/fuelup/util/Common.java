package com.september.fuelup.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Common {

	public static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	public static final String KEY_PARTNER_ID = "X-PartnerId";
	public static final String KEY_AUTH_SIGNATURE = "X-AuthSignature";
	
	public static String dataToHmacSHA1(byte[] key, byte[] data) throws InvalidKeyException, NoSuchAlgorithmException {
		SecretKeySpec signingKey = new SecretKeySpec(key, Common.HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(Common.HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		byte[] signature = mac.doFinal(data);
		return Base64.getEncoder().encodeToString(signature);
	}
}
