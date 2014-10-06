package com.uuola.txcms.util.test;

import com.uuola.commons.StringUtil;


public class StringTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String name ="uc_name123";
        System.out.println(StringUtil.parseUnderscoreName(name));
        
        String[] array = {"uc_name", "u_name", "ucname", "uC_nAme", "ucName", "a_Nc_nAme"};
        
        for(String s : array){
            System.out.println(StringUtil.parseUnderscoreName(s));
        }
    }

}
