package com.uuola.txcms.util.test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.uuola.commons.StringUtil;
import com.uuola.commons.http.UrlHttpUtil;


public class StringTest {

    /**
     * @param args
     */
    //@Test
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
    }

    //@Test
    public void test_parseQueryString() throws UnsupportedEncodingException{
        Map<String, Object> header = new LinkedHashMap<String,Object>();
        header.put("webinfo", "webinfo");
        Map<String, Object> params = new LinkedHashMap<String,Object>();
        params.put("aaa", "唐晓东");
        params.put("bbb", "唐晓东aabb");
        params.put("file1", new File("E:\\tmp\\测试0.jpg"));
        params.put("file2", new File("E:\\tmp\\测试0.jpg"));
        //System.out.println(StringUtil.parseQueryText(params));
        String ret = UrlHttpUtil.doPostAsFormdata("http://www.uuola.com/txcms-web/post.jsp", "utf-8", header, params, null, null);
        System.out.println(ret);
    }
}
