/*
 * @(#)HttpClientConPoolFactory.java 2015年1月18日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.http;

import java.util.concurrent.TimeUnit;

import org.apache.http.Consts;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;


/**
 * <pre>
 * HttpClient & connection pool factory
 * @author tangxiaodong
 * 创建日期: 2015年1月18日
 * </pre>
 */
public abstract class HttpClientFactory {

    public static PoolingHttpClientConnectionManager create(){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(1L, TimeUnit.HOURS);
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(10);
        
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoKeepAlive(false)
                .setSoLinger(60)
                .setSoTimeout(60000)
                .setTcpNoDelay(true)
                .setSoReuseAddress(false).build();
        cm.setDefaultSocketConfig(socketConfig);
        
        ConnectionConfig connConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).build();
        cm.setDefaultConnectionConfig(connConfig);
        return cm;
    }
    
    /**
     * 得到 连接池100，SOCKET超时60S的 HTTP CLIENT，
     * @return
     */
    public static CloseableHttpClient getHttpClient(){
        return HttpClients.custom().setConnectionManager(create()).build();
    }
    
}
