package com.uuola.txcms.util.test;

import com.uuola.commons.coder.DES;
import com.uuola.commons.coder.DESede;
import com.uuola.commons.coder.Md5;
import com.uuola.commons.coder.MyBase64;


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
        System.out.println(129>>2);
    }

}
