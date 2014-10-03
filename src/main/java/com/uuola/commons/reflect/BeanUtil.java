/*
 * @(#)BeanUtil.java 2013-12-22
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.reflect;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.sf.cglib.beans.BeanCopier;


/**
 * <pre>
 * 使用cglib 复制对象属性
 * @author tangxiaodong
 * 创建日期: 2013-12-22
 * </pre>
 */
public class BeanUtil {

    // 缓存cglib的beanCopier 避免反复创建
    private static ConcurrentMap<Class<?>, BeanCopier> cacheCglibCopierMap = new ConcurrentHashMap<Class<?>, BeanCopier>();

    /**
     * 复制JAVABEAN属性到新对象
     * 
     * @param to_obj
     * @param from_obj
     * @throws Exception
     */
    public static void copyProperties(Object srcBean, Object targetBean){
        Class<?> clazz = srcBean.getClass();
        BeanCopier copier = cacheCglibCopierMap.get(clazz);
        if (null == copier) {
            cacheCglibCopierMap.putIfAbsent(clazz, BeanCopier.create(clazz, targetBean.getClass(), false));
            copier = cacheCglibCopierMap.get(clazz);
        }
        copier.copy(srcBean, targetBean, null);
    }

    /**
     * 清理缓存
     */
    public static void clearCache(){
        cacheCglibCopierMap.clear();
    }
}
