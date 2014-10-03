/*
 * 
 * v1.2 by tonydon
 */
package com.uuola.commons.coder;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.uuola.commons.constant.CST_ENCODING;
import com.uuola.commons.exception.BusinessException;

/**
 *
 * @author txdnet
 */
public class AES {

    private AES() {
    }
    private final static String AES = "AES";

    private static byte[] encrypt(byte[] src, byte[] key) throws BusinessException {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(AES);
            kgen.init(128, new SecureRandom(key));
            SecretKey secretKey = kgen.generateKey();
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    private static byte[] decrypt(byte[] src, byte[] key) throws BusinessException {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(AES);
            kgen.init(128, new SecureRandom(key));
            SecretKey secretKey = kgen.generateKey();
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public static String decrypt(String data, String key) throws BusinessException {
        if (data == null || key == null) {
            return null;
        }
        try {
            byte[] b = decrypt(ByteHexUtil.hex2byte(data.getBytes(CST_ENCODING.UTF8)), key.getBytes());
            if (b == null) {
                return null;
            }
            return new String(b, CST_ENCODING.UTF8);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    public static String encrypt(String data, String key) throws BusinessException {
        if (data == null || key == null) {
            return null;
        }
        try {
            return ByteHexUtil.byte2hex(encrypt(data.getBytes(CST_ENCODING.UTF8), key.getBytes()));
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
}
