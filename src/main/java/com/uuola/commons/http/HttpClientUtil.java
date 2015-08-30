/*
 * @(#)HttpClientConPoolFactory.java 2015年1月18日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.http;

import java.util.concurrent.TimeUnit;

import org.apache.http.Consts;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;


/**
 * <pre>
 * HttpClient & connection pool utils
 * @author tangxiaodong
 * 创建日期: 2015年1月18日
 * </pre>
 */
public abstract class HttpClientUtil {

    /**
     * 创建一个连接池管理器<br/>
     * 最大连接数500, 每路由100个连接, 连接池存活检测时间2分钟，等待数据响应超时8S
     * @return
     */
    public static PoolingHttpClientConnectionManager create(){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(2L, TimeUnit.MINUTES);
        cm.setMaxTotal(500);//最大连接数
        cm.setDefaultMaxPerRoute(100); //每路由100个连接
        
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoKeepAlive(false)
                .setSoLinger(8) //  socket.close() 延迟8s关闭
                .setSoTimeout(8000) //等待数据响应超时毫秒
                .setTcpNoDelay(true)
                .setSoReuseAddress(false).build();
        cm.setDefaultSocketConfig(socketConfig);
        
        ConnectionConfig connConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).build();
        cm.setDefaultConnectionConfig(connConfig);
        return cm;
    }
    
    /**
     * 使用外部连接池管理器创建一个httpclient
     * @param cm
     * @return
     */
    public static CloseableHttpClient getHttpClient(PoolingHttpClientConnectionManager cm){
        return HttpClients.custom().setConnectionManager(cm).build();
    }
    
    /**
     * 使用HttpClientUtil一个默认配置的连接池管理器创建的httpclient<br/>
     * 最大连接数500, 每路由100个连接, 连接池存活检测时间2分钟，等待数据响应超时8S
     * @return
     */
    public static CloseableHttpClient getHttpClient(){
        return getHttpClient(create());
    }
    
    /**
     * 得到一个默认配置的httpclient<br/>
     * 每个路由5个连接，最大连接10，没有超时限制
     */
    public static CloseableHttpClient getDefaultHttpClient(){
        return HttpClients.createDefault();
    }
    
    /**
     * 得到请求配置，设置超时信息, 均为8S内超时
     * @return
     */
    public static RequestConfig getRequestConfig() {
        return RequestConfig.custom().setConnectTimeout(8000).setSocketTimeout(8000).setConnectionRequestTimeout(8000)
                .build();
    }
    
}
