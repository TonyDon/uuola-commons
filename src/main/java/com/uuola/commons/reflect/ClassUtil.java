/*
 * @(#)ClassUtil.java 2013-7-27
 * 
 * Copy Right@ uuola
 */

package com.uuola.commons.reflect;

import javax.persistence.Table;

import com.uuola.commons.StringUtil;

/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-7-27
 * </pre>
 */
public class ClassUtil {

    /**
     * 通过实体获取表名
     * 
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (null == table || StringUtil.isEmpty(table.name())) {
            throw new RuntimeException(String.format("Entity Class:%s , not set table name!",
                    clazz.getCanonicalName()));
        }
        return table.name();
    }
    


}
