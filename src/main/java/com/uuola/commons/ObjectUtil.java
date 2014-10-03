/*
 * @(#)ObjectUtil.java 2013-8-31
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-8-31
 * </pre>
 */
public abstract class ObjectUtil {

    /**
     * 判断对象数组是否为空或空集合
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array){
        return (array == null || array.length == 0);
    }
}
