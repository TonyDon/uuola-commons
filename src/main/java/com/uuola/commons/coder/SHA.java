/*
 by txdnet.cn
 */
package com.uuola.commons.coder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.uuola.commons.constant.CST_ENCODING;
import com.uuola.commons.exception.BusinessException;

/**
 * @author txdnet
 */
public class SHA {

    private SHA() {
    }
    public final static String SHA_1 = "SHA-1";
    public final static String SHA_256 = "SHA-256";
    public final static String SHA_384 = "SHA-384";
    public final static String SHA_512 = "SHA-512";
    public final static String MD5 = "MD5";

    /**
     * Encodes a string
     *
     * @param str String to encode <SHA-1, SHA-384, and SHA-512>
     * @return Encoded String
     * @throws NoSuchAlgorithmException
     */
    public static String encode(String s) {
        return SHA.encode(s, SHA.SHA_1);
    }

    public static String encode(String str, String Algorithm) throws BusinessException {
        if (str == null || str.length() == 0) {
            return null;
        }
        StringBuilder hexString = new StringBuilder(40);
        try {
            MessageDigest md = MessageDigest.getInstance(Algorithm);
            md.update(str.getBytes(CST_ENCODING.UTF8));
            byte[] hash = md.digest();
            for (int i = 0; i < hash.length; i++) {
                int halfbyte = (hash[i] >>> 4) & 0x0F;
                int two_halfs = 0;
                do {
                    if ((0 <= halfbyte) && (halfbyte <= 9)) {
                        hexString.append((char) ('0' + halfbyte));
                    } else {
                        hexString.append((char) ('a' + (halfbyte - 10)));
                    }
                    halfbyte = hash[i] & 0x0F;
                } while (two_halfs++ < 1);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        return hexString.toString();
    }

    public static BigInteger genBigInteger(String str, String Algorithm) throws BusinessException {
        if (str == null || str.length() == 0) {
            return null;
        }
        BigInteger bigInt;
        try {
            MessageDigest md = MessageDigest.getInstance(Algorithm);
            md.update(str.getBytes(CST_ENCODING.UTF8));
            bigInt = new BigInteger(md.digest());
        } catch (Exception e) {
            throw new BusinessException(e);
        }
        return bigInt;
    }
}