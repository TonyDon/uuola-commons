/*
 * by tonydon.
 * site:www.txdnet.cn.
 */
package com.uuola.commons.coder;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.uuola.commons.constant.CST_ENCODING;
import com.uuola.commons.exception.BusinessException;

/**
 * @see 加解密工具类
 * @author Winter Lau
 * @site http://www.oschina.net/code/snippet_12_553
 */
public class DES {

    private DES() {
    }
    /*
     * @see 加密算法见 http://docs.oracle.com/javase/7/docs/api/index.html
     * http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#SecretKeyFactory
     */
    private final static String DES = "DES";

    /**
     * @see 加密
     * @param src 数据源
     * @param key 密钥，长度必须是8位ascii
     * @return	返回加密后的数据
     * @throws Exception
     */
    private static byte[] encrypt(byte[] src, byte[] key) throws BusinessException {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 解密
     *
     * @param src	数据源
     * @param key	密钥，长度必须是8位ascii
     * @return	返回解密后的原始数据 异常返回字节原始数据
     * @throws Exception
     */
    private static byte[] decrypt(byte[] src, byte[] key) throws BusinessException {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            // 现在，获取数据并解密
            // 正式执行解密操作
            return cipher.doFinal(src);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 数据解密
     *
     * @param data
     * @param key 密钥
     * @return
     * @throws Exception
     */
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

    /**
     * 数据加密
     *
     * @param data
     * @param key 密钥
     * @return
     * @throws Exception
     */
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