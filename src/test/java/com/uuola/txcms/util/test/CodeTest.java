package com.uuola.txcms.util.test;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import com.uuola.commons.DateUtil;
import com.uuola.commons.coder.DES;
import com.uuola.commons.coder.KeyGenerator;
import com.uuola.commons.coder.Md5;
import com.uuola.commons.coder.SHA;
import com.uuola.commons.constant.CST_DATE_FORMAT;


public class CodeTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(Md5.encode("5"));
        String k = "1234567g";
        System.out.println(Md5.encode("111"));
        System.out.println(Md5.encode("222"));
        String e1 = DES.encrypt(Md5.encode("111"),k);
        String e2 = DES.encrypt(Md5.encode("222"),k);
        System.out.println(e1.length());
        System.out.println(e2);
        System.out.println(DES.decrypt(e1, k));
        System.out.println(Md5.encode("hongdeviltxd#163.com"));
        System.out.println(Md5.encode("hokit#%#,.7@nm"));
        System.out.println(14<<3);
        System.out.println(8>>2);
        System.out.println(Math.ceil((double)9/(double)4));
        System.out.println(KeyGenerator.getUUID());
        System.out.println(KeyGenerator.getRndChr(8, KeyGenerator.LETTER_NUMBER_MAP).toLowerCase()+UUID.randomUUID().toString());
        System.out.println(KeyGenerator.getRndChr(24)+"-"+Long.toHexString(System.currentTimeMillis()));
        System.out.println(KeyGenerator.getRndChr(2).toLowerCase()+UUID.randomUUID().toString());
        System.out.println(System.currentTimeMillis());
        System.out.println(DateUtil.formatTimeMillis(System.currentTimeMillis() * 5, CST_DATE_FORMAT.YYYYMMDDHHMMSSSSS));
        System.out.println(SHA.encode("你好"));
        System.out.println(DigestUtils.sha1Hex("你好"));
    }

}
