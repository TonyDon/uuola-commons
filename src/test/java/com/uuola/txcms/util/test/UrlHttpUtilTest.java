/*
 * @(#)UrlHttpUtilTest.java 2015年8月30日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txcms.util.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;

import com.uuola.commons.StringUtil;
import com.uuola.commons.http.HttpClientUtil;
import com.uuola.commons.http.HttpUtil;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2015年8月30日
 * </pre>
 */
public class UrlHttpUtilTest {

    //@Test
    public void test_post_formdata() throws UnsupportedEncodingException{
        Map<String, Object> header = new LinkedHashMap<String,Object>();
        header.put("webinfo", "webinfo呵呵");
        Map<String, Object> params = new LinkedHashMap<String,Object>();
        params.put("aaa", "唐晓东");
        params.put("bbb", "唐晓东aabb");
        params.put("file1", new File("E:\\tmp\\测试0.jpg"));
        params.put("file2", new File("E:\\tmp\\测试0.jpg"));
        //System.out.println(StringUtil.parseQueryText(params));
        String ret = HttpUtil.doPostAsFormdata("http://www.uuola.com/txcms-web/post.jsp", "utf-8", header, params, null, null);
        System.out.println(ret);
    }
    
    //@Test
    public void test_get_1() throws UnsupportedEncodingException{
        Map<String, Object> headers = new LinkedHashMap<String,Object>();
        headers.put("webinfo", URLEncoder.encode("webinfo呵呵", "UTF-8"));
        Map<String, Object> params = new LinkedHashMap<String,Object>();
        params.put("aaa", "唐晓东");
        params.put("bbb", "唐晓东aabb");
        String ret = HttpUtil.doGet("http://www.uuola.com/txcms-web/get.jsp?"+StringUtil.parseQueryText(params), "utf-8", null, null, headers);
        System.out.println(ret);
    }
    
    //@Test
    public void test_post_1() throws UnsupportedEncodingException{
        Map<String, Object> headers = new LinkedHashMap<String,Object>();
        headers.put("webinfo", URLEncoder.encode("webinfo呵呵", "UTF-8"));
        Map<String, Object> params = new LinkedHashMap<String,Object>();
        params.put("aaa", "唐晓东");
        params.put("bbb", "唐晓东aabb");
        String ret = HttpUtil.doPost("http://www.uuola.com/txcms-web/get.jsp", "utf-8", null, null, headers, params);
        System.out.println(ret);
    }
    
    @Test
    public void test_httpclient_1(){
        long t1 = System.currentTimeMillis();
        CloseableHttpClient hc = HttpClientUtil.getDefaultHttpClient();
        HttpClientContext context = HttpClientContext.create();
        HttpGet get = new HttpGet("http://00.uuola.com/txcms-web/get.jsp?bbb=a");
        get.setConfig(HttpClientUtil.getRequestConfig());
        
        try {
            hc.execute(get, context);
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            try {
                hc.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        //HttpUtil.doGet("http://www.uuola.com/txcms-web/get.jsp?bbb=a", "utf-8", null, null, null);
        System.out.println(System.currentTimeMillis()-t1);
    }
}
