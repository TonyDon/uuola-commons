/*
 * @(#)ObjectUtil.java 2013-8-31
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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
    
    /**
     * 判断对象数组是否为空或空集合
     * @param array
     * @return
     */
    public static boolean isNotEmpty(Object[] array){
        return !isEmpty(array);
    }
    
    /**
     * 将数组对象Object中的集合对象拆解成单个元素组合到新的对象数组中，新对象数组不应有集合对象。
     * eg: args ={ "a", 123, array{1,2,3} , 1, "t"  , list{1,2}}
     * return : list{"a", 123, 1, 2, 3, 1, "t" , 1, 2}
     * @param args
     * @return
     */
    public static List<Object> getArgsList(Object... args) {
        List<Object> results = new ArrayList<Object>(args.length);
        for(Object o: args) {
            if(o instanceof Collection) {
                Collection<?> c = (Collection<?>)o;
                for(Object item: c) {
                    results.add(item);
                }
            } else if(o.getClass().isArray()) {
                Object[] c = (Object[])o;
                for(Object item: c) {
                    results.add(item);
                }
            } else {
                results.add(o);
            }
        }
        return results;
    }
    
    /**
     * 将多个参数对象解析成数组对象
     * @param args
     * @return
     */
    public static Object[] getArgsArray(Object... args){
        return getArgsList(args).toArray();
    }
}
