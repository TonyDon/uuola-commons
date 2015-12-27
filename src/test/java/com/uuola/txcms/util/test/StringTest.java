package com.uuola.txcms.util.test;

import java.util.Date;

import org.junit.Test;

import com.uuola.commons.DateUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.CST_DATE_FORMAT;


public class StringTest {

    /**
     * @param args
     */
    @Test
    public void test_CaseParse() {
        
        String[] array = {"uc_name", "u_name", "ucname", "uC_nAme", "ucName", "a_Nc_nAme"};
        
        for(String s : array){
            System.out.println(StringUtil.getCamelcaseName(s));
        }
        
        System.out.println(" \r\n -- ");
        
        String[] arr1 = {"ucName", "uName", "_uname", "uname", "u1_name", "Uname"};
        
        for(String s : arr1){
            System.out.println(StringUtil.getUnderscoreName(s));
        }
        
        System.out.println(DateUtil.formatDate(new Date(), CST_DATE_FORMAT.YYYYsMMsDD));
    }


    
    
}
