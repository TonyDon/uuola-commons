/*
 * @(#)UrlHttpUtil.java 2015年8月23日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.http;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.ByteUtil;
import com.uuola.commons.CollectionUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.coder.KeyGenerator;
import com.uuola.commons.constant.CST_CHAR;
import com.uuola.commons.constant.CST_ENCODING;


/**
 * <pre>
 * java UrlConnection 简单封装，支持GET, POST, 上传文件POST
 * @author tangxiaodong
 * 创建日期: 2015年8月23日
 * </pre>
 */
public abstract class HttpUtil {
    
    private static Logger log = LoggerFactory.getLogger(HttpUtil.class);
    

    public static final int TIME_OUT = 8000;
    
    

    /**
     * 简单的Get请求， application/x-www-form-urlencoded
     *
     * @param siteUrl
     * @param charset
     * @param connectTimeout
     * @param readTimeout
     * @param headers
     * @return String
     *
     */
    public static String doGet(String siteUrl, String charset, Integer connectTimeout, Integer readTimeout, Map<String, Object> headers) {
        String content = null;
        BufferedInputStream bis = null;
        HttpURLConnection urlconn = null;
        try {
            urlconn = (HttpURLConnection) (new URL(siteUrl)).openConnection();
            urlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=".concat(charset));
            setCommonRequestProperty(urlconn, "GET", charset, connectTimeout, readTimeout);
            setRequestHeader(urlconn, headers);
            urlconn.connect();
            if (urlconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bis = new BufferedInputStream(urlconn.getInputStream());
                content = IOUtils.toString(bis, charset);
            }
        } catch (Exception e) {
            log.error("doGet()", e);
        } finally {
            IOUtils.closeQuietly(bis);
            if (urlconn != null) {
                urlconn.disconnect();
            }
        }
        return content;
    }
    
    /**
     * GET 请求返回字节数组
     * @param siteUrl
     * @param charset
     * @param connectTimeout
     * @param readTimeout
     * @param headers
     * @return
     */
    public static ByteBuffer doGetForBytes(String siteUrl, String charset, Integer connectTimeout, Integer readTimeout, Map<String, Object> headers){
        ByteBuffer data = null;
        InputStream ds = null;
        InputStream is = null;
        HttpURLConnection urlconn = null;
        try {
            urlconn = (HttpURLConnection) (new URL(siteUrl)).openConnection();
            urlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=".concat(charset));
            setCommonRequestProperty(urlconn, "GET", charset, connectTimeout, readTimeout);
            setRequestHeader(urlconn, headers);
            urlconn.connect();
            if (urlconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                ds = urlconn.getErrorStream() != null ? urlconn.getErrorStream() : urlconn.getInputStream();
                String encoding = urlconn.getContentEncoding();
                is = null != encoding && encoding.toLowerCase().contains("gzip") ? new BufferedInputStream(
                        new GZIPInputStream(ds)) : new BufferedInputStream(ds);
                data = ByteUtil.readToByteBuffer(is, ByteUtil.BUFF_SIZE * 4);
            }
        } catch (Exception e) {
            log.error("doGetForBytes()", e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(ds);
            if (urlconn != null) {
                urlconn.disconnect();
            }
        }
        return data;
    }
    

    
    /**
     * 简单的POST请求，使用application/x-www-form-urlencoded
     * @param postUrl
     * @param charset
     * @param headers
     * @param params
     * @param connectTimeout
     * @param readTimeout
     * @return
     */
    public static String doPost(String postUrl, String charset, Integer connectTimeout, Integer readTimeout, Map<String, Object> headers, Map<String, Object> params) {
        String content = null;
        BufferedInputStream bis = null;
        DataOutputStream dos = null;
        HttpURLConnection urlconn = null;
        try {
            urlconn = (HttpURLConnection) (new URL(postUrl)).openConnection();
            urlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=".concat(charset));
            setCommonRequestProperty(urlconn, "POST", charset, connectTimeout, readTimeout);
            setRequestHeader(urlconn, headers);
            urlconn.connect();
            dos = new DataOutputStream(urlconn.getOutputStream());
            dos.writeBytes(StringUtil.parseQueryText(params));
            dos.flush();
            dos.close();
            if (urlconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bis = new BufferedInputStream(urlconn.getInputStream());
                content = IOUtils.toString(bis, charset);
            }
        } catch (Exception e) {
            log.error("doPost()", e);
        } finally {
            IOUtils.closeQuietly(dos);
            IOUtils.closeQuietly(bis);
            if (urlconn != null) {
                urlconn.disconnect();
            }
        }
        return content;
    }
    
    
    
    /**
     * 简单的POST请求,以数据流方式
     * @param postUrl
     * @param charset
     * @param headers
     * @param connectTimeout
     * @param readTimeout
     * @return
     */
    public static String doPostAsStream(String postUrl, String charset, Map<String, Object> headers, byte[] body,
            Integer connectTimeout, Integer readTimeout) {
        String content = null;
        BufferedInputStream bis = null;
        DataOutputStream dos = null;
        HttpURLConnection urlconn = null;
        try {
            urlconn = (HttpURLConnection) (new URL(postUrl)).openConnection();
            urlconn.setRequestProperty("Content-length", String.valueOf(body.length)); 
            urlconn.setRequestProperty("Content-Type", "application/octet-stream"); 
            urlconn.setRequestProperty("Connection", "Keep-Alive");
            setCommonRequestProperty(urlconn, "POST", charset, connectTimeout, readTimeout);
            setRequestHeader(urlconn, headers);
            urlconn.connect();
            dos = new DataOutputStream(urlconn.getOutputStream());
            dos.write(body);
            dos.flush();
            dos.close();
            if (urlconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bis = new BufferedInputStream(urlconn.getInputStream());
                content = IOUtils.toString(bis, charset);
            }
        } catch (Exception e) {
            log.error("doPostAsStream()", e);
        } finally {
            IOUtils.closeQuietly(dos);
            IOUtils.closeQuietly(bis);
            if (urlconn != null) {
                urlconn.disconnect();
            }
        }
        return content;
    }
    
    /**
     * 设置urlconn header信息
     * @param urlconn
     * @param headers
     */
    public static void setRequestHeader(HttpURLConnection urlconn, Map<String, Object> headers) {
        if (CollectionUtil.isNotEmpty(headers)) {
            for (Map.Entry<String, Object> parVal : headers.entrySet()) {
                urlconn.setRequestProperty(parVal.getKey(), String.valueOf(parVal.getValue()));
            }
        }
    }


    /**
     * 简单的POST请求 以表单提交含上传文件
     * @param postUrl
     * @param charset
     * @param headers
     * @param datas
     * @param connectTimeout
     * @param readTimeout
     * @return
     */
    public static String doPostAsFormdata(String postUrl, String charset, Map<String, Object> headers, Map<String, Object> datas, Integer connectTimeout, Integer readTimeout) {
        String content = null;
        BufferedInputStream bis = null;
        DataOutputStream dos = null;
        HttpURLConnection urlconn = null;
        String BOUNDARY_LINE = "----------uuola".concat(KeyGenerator.getRndChr(8)); // 定义数据分隔线
        try {
            urlconn = (HttpURLConnection) (new URL(postUrl)).openConnection();
            urlconn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY_LINE); 
            urlconn.setRequestProperty("Connection", "Keep-Alive");
            setCommonRequestProperty(urlconn, "POST", charset, connectTimeout, readTimeout);
            setRequestHeader(urlconn, headers);
            urlconn.connect();
            dos = new DataOutputStream(urlconn.getOutputStream());
            fillRequestBody(dos, BOUNDARY_LINE, datas);
            dos.flush();
            dos.close();
            if (urlconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bis = new BufferedInputStream(urlconn.getInputStream());
                content = IOUtils.toString(bis, charset);
            }
        } catch (Exception e) {
            log.error("doPostAsFormdata()", e);
        } finally {
            IOUtils.closeQuietly(dos);
            IOUtils.closeQuietly(bis);
            if (urlconn != null) {
                urlconn.disconnect();
            }
        }
        return content;
    }
    
    private static void fillRequestBody(DataOutputStream dos, String boundaryLine, Map<String, Object> datas) {
        if (CollectionUtil.isEmpty(datas)) {
            return;
        }
        try {
            StringBuilder parLine = new StringBuilder();
            for (Map.Entry<String, Object> parVal : datas.entrySet()) {
                String name = parVal.getKey();
                Object val = parVal.getValue();
                if (val instanceof String) {
                    parLine.append("--").append(boundaryLine).append(CST_CHAR.STR_CRLF);
                    parLine.append("Content-Disposition: form-data; name=\"").append(name).append("\"")
                    .append(CST_CHAR.STR_CRLF).append(CST_CHAR.STR_CRLF);
                    parLine.append(val);
                    parLine.append(CST_CHAR.STR_CRLF);
                    
                }else if(val instanceof File){
                    File file = (File)val;
                    fillFileData(name, file, boundaryLine, dos);
                }
            }
            dos.write(parLine.toString().getBytes(CST_ENCODING.UTF8));
            dos.write(("--".concat(boundaryLine).concat("--").concat(CST_CHAR.STR_CRLF)).getBytes(CST_ENCODING.UTF8));
        } catch (Exception e) {
            log.error("fillRequestBody()", e);
        }
    }


    private static void fillFileData(String name, File file, String boundaryLine, DataOutputStream dos) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundaryLine).append(CST_CHAR.STR_CRLF)
          .append("Content-Disposition: form-data; name=\"").append(name).append("\"; ")
          .append("filename=\"").append(file.getName()).append("\"").append(CST_CHAR.STR_CRLF)
          .append("Content-Type: application/octet-stream")
          .append(CST_CHAR.STR_CRLF).append(CST_CHAR.STR_CRLF);
        dos.write(sb.toString().getBytes(CST_ENCODING.UTF8));
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] buff = new byte[2048];
        while ((bytes = in.read(buff)) != -1){
            dos.write(buff, 0, bytes);
        }
        dos.writeBytes(CST_CHAR.STR_CRLF);
        IOUtils.closeQuietly(in);
    }


    /**
     * 
     * @param urlconn
     * @param reqMethod 请求方法
     * @param charset 编码
     * @param connectTimeout 建立连接到目标地址的超时毫秒数
     * @param readTimeout 连接到目标地址后，开始读取数据超时的毫秒数
     * @throws Exception
     */
    public static void setCommonRequestProperty(HttpURLConnection urlconn, String reqMethod,
            String charset, Integer connectTimeout, Integer readTimeout) throws Exception {
        urlconn.setConnectTimeout(connectTimeout == null ? TIME_OUT : connectTimeout);
        urlconn.setReadTimeout(readTimeout == null ? TIME_OUT : readTimeout);
        urlconn.setInstanceFollowRedirects(true);
        urlconn.setDoOutput(true);
        urlconn.setDoInput(true);
        urlconn.setUseCaches(false);
        urlconn.setRequestMethod(reqMethod);
        urlconn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:16.0) Gecko/20100101 Firefox/16.0");
        urlconn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        urlconn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        urlconn.setRequestProperty("Charset", charset);
    }
}
