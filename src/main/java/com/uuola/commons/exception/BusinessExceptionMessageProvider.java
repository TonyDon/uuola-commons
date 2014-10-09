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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger log = LoggerFactory.getLogger(BusinessExceptionMessageProvider.class);

    private static final String BASE = "exceptions/";

    private Map<String, List<ResourceBundle>> cachedBundleMap = new HashMap<String, List<ResourceBundle>>();

    private static BusinessExceptionMessageProvider provider = new BusinessExceptionMessageProvider();

    private ResourceBundle getBundleFromCache(List<ResourceBundle> cachedBundles, Locale locale) {
        for (ResourceBundle bundle : cachedBundles) {
            if (bundle.getLocale().equals(locale)) {
                return bundle;
            }
        }
        return null;
    }

    public ResourceBundle getResourceBundle(String exceptionClassName, Locale locale) {
        List<ResourceBundle> cachedBundles = cachedBundleMap.get(exceptionClassName);
        if (cachedBundles == null) {
            cachedBundles = new ArrayList<ResourceBundle>();
            cachedBundleMap.put(exceptionClassName, cachedBundles);
        } else {
            ResourceBundle bundle = getBundleFromCache(cachedBundles, locale);
            if (bundle != null) {
                return bundle;
            }
        }
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BASE + exceptionClassName, locale);
            cachedBundles.add(bundle);
            return bundle;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 获取异常对应的错误信息。
     * 
     * @param e
     *            异常类
     * @param locale
     *            语言
     * @return
     */
    public static String getMessage(BusinessException e, Locale locale) {
        String key = e.getMessage();
        if (StringUtil.isEmpty(key) || locale == null) {
            return CST_CHAR.STR_AT;
        }
        ResourceBundle bundle = provider.getResourceBundle(StringUtil.substringAfterLast(e.getClass().getName(), "."),
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
            log.error("", ex);
            return CST_CHAR.STR_AT + key;
        }
        // 使用与参数数组匹配的占位符替换参数值
        if (value.contains("{0}")) {
            value = provider.format(value, e.getParams());
        }
        // 如果有，则输出错误码
        if (value.contains("{code}") && e.getErrorCode() != 0) {
            value = StringUtil.replace(value, "{code}", String.valueOf(e.getErrorCode()));
        }
        return value;
    }

    /**
     * 获取异常对应的错误信息，取中文错误信息
     * 
     * @param e
     * @return
     */
    public static String getMessage(BusinessException be) {
        return getMessage(be, Locale.CHINA);
    }

    /**
     * 使用参数格式化异常信息
     * 
     * @param value
     * @param params
     * @return
     */
    private String format(String value, Object[] params) {
        try {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    value = StringUtil.replace(value, "{" + i + "}", String.valueOf(params[i]));
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return value;
    }
}
