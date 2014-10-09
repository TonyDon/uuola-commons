/*
 * @(#)BusinessExceptionMessageProvider.java 2013-7-21
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.CST_CHAR;


/**
 * <pre>
 * 获取异常信息定义
 * @author tangxiaodong
 * 创建日期: 2013-7-21
 * </pre>
 */
public class BusinessExceptionMessageProvider {

    private static final String BASE = "exceptions/";
    private Map<String, List<ResourceBundle>> cachedBundleMap = new HashMap<String, List<ResourceBundle>>();
    private static BusinessExceptionMessageProvider instance = new BusinessExceptionMessageProvider();
    
    private ResourceBundle getBundleFromCache(List<ResourceBundle> cachedBundles, Locale locale){
        for(ResourceBundle bundle: cachedBundles){
            if(bundle.getLocale().equals(locale)){
                return bundle;
            }
        }
        return null;
    }
    
    public ResourceBundle getResourceBundle(String exceptionName, Locale locale){
        List<ResourceBundle> cachedBundles = cachedBundleMap.get(exceptionName);
        if(cachedBundles == null){
            cachedBundles = new ArrayList<ResourceBundle>();
            cachedBundleMap.put(exceptionName, cachedBundles);
        }else{
            ResourceBundle bundle = getBundleFromCache(cachedBundles, locale);
            if(bundle != null){
                return bundle;
            }
        }
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BASE+exceptionName, locale);
            cachedBundles.add(bundle);
            return bundle;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取异常对应的错误信息。
     * @param e 异常类
     * @param locale 语言
     * @return
     */
    public static String getMessage(BusinessException e, Locale locale) {
        String key = e.getMessage();
        if (key == null || key.length() == 0 || locale == null) {
            return CST_CHAR.STR_AT;
        }
        ResourceBundle bundle = instance.getResourceBundle(StringUtil.substringAfterLast(e.getClass().getName(), "."),
                locale);
        if (bundle == null) {
            return CST_CHAR.STR_AT + key;
        }
        String value = null;
        try {
            value = bundle.getString(key);
            if (value == null) {
                return CST_CHAR.STR_AT + key;
            }
        } catch (Exception ex) {
            return CST_CHAR.STR_AT + key;
        }
        if (value.contains("%s") || value.contains("%d")) {
            value = String.format(value, e.getParams());
        }
        if (e.getErrorCode() != 0) {
            value = value + " ErrorCode:" + e.getErrorCode();
        }
        return value;
    }
    
    /**
     * 获取异常对应的错误信息，取中文错误信息
     * @param e
     * @return
     */
    public static String getMessage(BusinessException be){
        return getMessage(be, Locale.CHINA);
    }
}
