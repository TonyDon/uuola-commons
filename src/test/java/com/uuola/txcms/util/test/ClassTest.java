/*
 * @(#)ClassTest.java 2014年11月19日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txcms.util.test;

import java.util.Set;

import com.uuola.commons.reflect.ClassUtil;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2014年11月19日
 * </pre>
 */
public class ClassTest {

    public static void main(String[] args){
        Set<Class<?>> clazzes = ClassUtil.getClasses("org.apache.commons.codec", false);
        for(Class<?> c : clazzes){
            System.out.println(c.getName());
        }
        ClassLoader classLoader = ClassUtil.getDefaultClassLoader();
        System.out.println(classLoader.getClass().getCanonicalName());
        System.out.println(classLoader.getParent().getClass().getCanonicalName());
    }
}
