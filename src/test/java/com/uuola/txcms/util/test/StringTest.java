package com.uuola.txcms.util.test;

import com.uuola.commons.StringUtil;


public class StringTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        String[] array = {"uc_name", "u_name", "ucname", "uC_nAme", "ucName", "a_Nc_nAme"};
        
        for(String s : array){
            System.out.println(StringUtil.getCamelcaseName(s));
        }
        
        System.out.println(" \r\n -- ");
        
        String[] arr1 = {"ucName", "uName", "_uname", "uname", "u1_name", "Uname"};
        
        for(String s : arr1){
            System.out.println(StringUtil.getUnderscoreName(s));
        }
    }

}
