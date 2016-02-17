/*
 * @(#)WebContext.java 2013-8-31
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-8-31
 * </pre>
 */
public class WebContext implements ServletContextListener {

    protected static Logger logger = LoggerFactory.getLogger(WebContext.class);

    private static ServletContext sct;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info(" -- Web Context Destroyed. --");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info(" -- Web Context Initialized. --");
        sct = sce.getServletContext();
    }

    /**
     * 得到资源在WEB上下文中的绝对路径
     * 
     * @param path
     * @return
     */
    public static String getRealPath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        String realPath = sct.getRealPath(path);
        if (realPath == null) {
            logger.warn("ServletContext resource [" + path + "] cannot be resolved to absolute file path.");
        }
        return realPath;
    }

    /**
     * 得到WEB Servlet 上下文
     * 
     * @return
     */
    public static ServletContext getServletContext() {
        return sct;
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // if(ip.length()>15)ip=ip.substring(0,15);
        if (ip.indexOf(',') > 0) {
            String[] ipa = ip.split(",");
            for (String v : ipa) {
                if (v.indexOf("unknown") < 0) {
                    ip = v;
                    break;
                }
            }
        }
        return ip;
    }
    
    /**
     * 得到会话对象
     * @param request
     * @param name
     * @return
     */
    public static Object getSessionAttr(HttpServletRequest request, String name) {
        if (request == null) {
            return null;
        }
        HttpSession sn = request.getSession(false);
        return (sn != null ? sn.getAttribute(name) : null);
    }

    /**
     * 设置会话对象
     * @param request
     * @param name
     * @param obj
     */
    public static void setSessionAttr(HttpServletRequest request, String name, Object obj) {
        if (request != null) {
            HttpSession sn = request.getSession(false);
            if (sn == null) {
                sn = request.getSession(true);
            }
            sn.setAttribute(name, obj);
        }
    }

}
