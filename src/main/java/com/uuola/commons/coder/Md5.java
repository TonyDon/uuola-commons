package com.uuola.commons.coder;

import java.security.NoSuchAlgorithmException;


public class Md5 {

    private Md5() {
    }

    /**
     * @see Encodes a string
     * 
     * @param str
     *            String to encode
     * @return Encoded String
     * @throws NoSuchAlgorithmException
     */
    public static String encode(String str) {
        return SHA.encode(str, SHA.MD5);
    }
}
