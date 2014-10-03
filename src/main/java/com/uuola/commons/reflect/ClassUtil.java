/*
 * @(#)ClassUtil.java 2013-7-27
 * 
 * Copy Right@ uuola
 */

package com.uuola.commons.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.uuola.commons.StringUtil;
import com.uuola.commons.exception.Assert;

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
    
    /**
     * 判断字段是否标注为非持久化
     * @param f
     * @return
     */
    public static boolean isTransient(Field f) {
        return f.getAnnotation(Transient.class) != null;
    }
    
    /**
     * 检查字段是否是可以被操作的字段,只有私有非终态的字段才能被操作
     *
     * @param f
     * @return
     */
    public static boolean isAccessibleField(Field f) {
        int mod = f.getModifiers();
        return ( 
                !Modifier.isStatic(mod) && 
                !Modifier.isFinal(mod) &&
                !Modifier.isPublic(mod) &&
                !Modifier.isAbstract(mod) &&
                !Modifier.isTransient(mod) &&
                !Modifier.isVolatile(mod)
                );
    }
    
    /**
     * 得到类所有private, protected字段，遇到停止类则截止寻找字段
     *
     * @param clazz
     * @param stopClazz
     * @return
     */
    public static final List<Field> getAllAccessibleFields(Class<?> clazz, Class<?> stopClazz) {
        List<Field> fieldset = new ArrayList<Field>();
        while (clazz != null && clazz != stopClazz) {
            Field[] fields = clazz.getDeclaredFields();
            for(Field f : fields){
                if(isAccessibleField(f)){
                    f.setAccessible(true);
                    fieldset.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fieldset;
    }
    
    /**
     * 通过字段名称找类字段
     * @param clazz
     * @param name
     * @param type
     * @return
     */
    public static Field findField(Collection<Field> fields, String name, Class<?> type){
        Assert.notEmpty(fields, "Class's fields must not be null/emtpy.");
        Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");
        for(Field f : fields){
            if ((name == null || name.equals(f.getName())) && (type == null || type.equals(f.getType()))) {
                return f;
            }
        }
        return null;
    }
    
    public static Object getFieldValue(Field field, Object target) {
        try {
            return field.get(target);
        }catch (IllegalAccessException ex) {
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

}
