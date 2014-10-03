/*
 *  txdnet.cn tonydon
 * 
 */
package com.uuola.commons.coder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.uuola.commons.constant.CST_ENCODING;
import com.uuola.commons.exception.BusinessException;

/**
 * Hmac encrypt
 * @author txdnet
 */
public final class HMAC {

    public static String encryptMD5(String data, String secret){
        return encrypt(data, secret, "HmacMD5");
    }
    
    public static String encryptSHA1(String data, String secret){
        return encrypt(data, secret, "HmacSHA1");
    }
    
    public static String encrypt(String data, String secret, String algorithm)  throws BusinessException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(CST_ENCODING.UTF8), algorithm);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(CST_ENCODING.UTF8));
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        return ByteHexUtil.byte2hex(bytes);
    }
}
