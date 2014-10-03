package com.uuola.commons.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;

import com.uuola.commons.DateUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.constant.CST_CHAR;
import com.uuola.commons.constant.CST_DATE_FORMAT;
import com.uuola.commons.exception.Assert;


public class FieldUtil {

   

    /**
     * 将obj对象转为对应的type类型的子类对象 <br/> 仅应用于 String, Byte, Integer, Long, Float,
     * Double,<br/> BigDecimal, Date 及 String[]与List转换
     *
     * @param srcObj
     * @param type
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static Object parseObject(Object srcObj, Field field) throws Exception {
        Object v = srcObj;
        Class srcCls = v.getClass();
        Class tagCls = getBoxClass(field.getType());

        // 如果目标类型是List
        if (List.class.isAssignableFrom(tagCls)) {
            if (List.class.isAssignableFrom(srcCls)) {
                return v;
            }
            if (srcCls.isArray()) {
                return parseArrayToList(v, (Class<?>) (getWildcardTypes(field)[0]));
            }
            if (srcCls == String.class) {
                return Arrays.asList(v);
            }
            return Collections.emptyList();
        }

        // 如果目标对象是数组类型
        if (tagCls.isArray()) {
            if (srcCls.isArray()) {
                return castArrayToType(v, tagCls);
            }
            if (List.class.isAssignableFrom(srcCls)) {
                return parseListToArray(v, tagCls);
            }
            if (srcCls == String.class) {
                String[] arr = {(String) v};
                return castArrayToType(arr, tagCls);
            }
            return null;
        }

        // 目标类型是普通类型，而原对象为数组时只取第一个元素
        if (srcCls.isArray()) {
            v = (Array.getLength(srcObj) == 0) ? null : Array.get(srcObj, 0);
        }

        // 这里没有对Boolean Char Short BigInteger 进行转换
        if (tagCls == String.class) {
            return castString(v);
        }

        if (tagCls == Byte.class) {
            return castByte(v);
        }

        if (tagCls == Integer.class) {
            return castInteger(v);
        }

        if (tagCls == Float.class) {
            return castFloat(v);
        }

        if (tagCls == Double.class) {
            return castDouble(v);
        }

        if (tagCls == Long.class) {
            return castLong(v);
        }

        if (tagCls == BigInteger.class) {
            return castBigInteger(v);
        }

        if (tagCls == BigDecimal.class) {
            return castBigDecimal(v);
        }

        if (tagCls == java.util.Date.class) {
            return castSqlDate(v);
        }

        if (tagCls == java.sql.Timestamp.class) {
            return castTimestamp(v);
        }
        return v;
    }

    /**
     * 尝试将对象转为String类型
     *
     * @param v
     * @return
     */
    public static String castString(Object v) {
        if (v == null) {
            return null;
        } else if (v instanceof String) {
            String val = (String) v;
            if (val.isEmpty()) {
                return null;
            }
            return val;
        } else {
            return null;
        }
    }

    /**
     * 尝试将对象转为Byte类型
     *
     * @param v
     * @return
     */
    public static Byte castByte(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof String) {
            String val = (String) v;
            if (val.isEmpty()) {
                return null;
            }
            return Byte.parseByte(val);
        } else if (v instanceof Number) {
            return ((Number) v).byteValue();
        } else {
            return null;
        }
    }

    /**
     * 尝试将对象转为Integer类型
     *
     * @param v
     * @return
     */
    public static Integer castInteger(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof String) {
            String val = (String) v;
            if (val.isEmpty()) {
                return null;
            }
            return Integer.parseInt(val);
        } else if (v instanceof Number) {
            return ((Number) v).intValue();
        } else {
            return null;
        }
    }

    /**
     * 尝试将对象转为Float类型
     *
     * @param v
     * @return
     */
    public static Float castFloat(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof String) {
            String val = (String) v;
            if (val.isEmpty()) {
                return null;
            }
            return Float.parseFloat(val);
        } else if (v instanceof Number) {
            return ((Number) v).floatValue();
        } else {
            return null;
        }
    }

    /**
     * 尝试将对象转为Double类型
     *
     * @param v
     * @return
     */
    public static Double castDouble(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof String) {
            String val = (String) v;
            if (val.isEmpty()) {
                return null;
            }
            return Double.parseDouble(val);
        } else if (v instanceof Number) {
            return ((Number) v).doubleValue();
        } else {
            return null;
        }
    }

    /**
     * 尝试将对象转为Long对象
     *
     * @param v
     * @return
     */
    public static Long castLong(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof String) {
            String val = (String) v;
            if (val.isEmpty()) {
                return null;
            }
            return Long.parseLong(val);
        } else if (v instanceof Number) {
            return ((Number) v).longValue();
        } else {
            return null;
        }
    }

    /**
     * 尝试将对象转为BigDecimal类型
     *
     * @param v
     * @return
     */
    public static BigDecimal castBigDecimal(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof String) {
            String val = (String) v;
            if (val.isEmpty()) {
                return null;
            }
            return new BigDecimal(val);
        } else if (v instanceof Double || v instanceof Float || v instanceof Long || v instanceof Integer || v instanceof Byte) {
            return BigDecimal.valueOf(((Number) v).doubleValue());
        } else if (v instanceof BigDecimal) {
            return (BigDecimal) v;
        } else {
            return null;
        }
    }

    /**
     * 尝试将对象转为BigInteger类型
     *
     * @param v
     * @return
     */
    public static BigInteger castBigInteger(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof String) {
            String val = (String) v;
            if (val.isEmpty()) {
                return null;
            }
            return new BigInteger(val);
        } else if (v instanceof Double || v instanceof Float || v instanceof Long || v instanceof Integer || v instanceof Byte) {
            return BigInteger.valueOf(((Number) v).longValue());
        } else if (v instanceof BigInteger) {
            return (BigInteger) v;
        } else {
            return null;
        }
    }

    /**
     * 尝试将对象转为java.util.Date类型
     *
     * @param v
     * @return
     */
    public static Date castSqlDate(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof String) {
            String dateStr = (String) v;
            int len = dateStr.length();
            int pos = dateStr.indexOf(CST_CHAR.CHAR_LINE);
            if (pos < 3) {
                return null;
            }
            if (len == 10) {
                // yyyy-MM-dd
                return DateUtil.parseDate(dateStr, CST_DATE_FORMAT.YYYY_MM_DD);
            } else if (len == 19) {
                // yyyy-MM-dd HH:mm:ss
                return DateUtil.parseDate(dateStr, CST_DATE_FORMAT.YYYY_MM_DD_HH_MM_SS);
            }
        } else if (v instanceof Date) {
            return (Date) v;
        } else if (v instanceof Number) {
            return new Date(((Number) v).longValue());
        }
        return null;
    }

    public static java.sql.Timestamp castTimestamp(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof Calendar) {
            return new java.sql.Timestamp(((Calendar) v).getTimeInMillis());
        }
        if (v instanceof java.sql.Timestamp) {
            return (java.sql.Timestamp) v;
        }
        if (v instanceof java.util.Date) {
            return new java.sql.Timestamp(((java.util.Date) v).getTime());
        }
        if (v instanceof String) {
            String dateStr = (String) v;
            int len = dateStr.length();
            int pos = dateStr.indexOf(CST_CHAR.CHAR_LINE);
            if (pos < 3) {
                return null;
            }
            if (len == 10) {
                // yyyy-MM-dd
                return new java.sql.Timestamp(DateUtil.parseTimeMillis(dateStr, CST_DATE_FORMAT.YYYY_MM_DD));
            } else if (len == 19) {
                // yyyy-MM-dd HH:mm:ss
                return new java.sql.Timestamp(DateUtil.parseTimeMillis(dateStr, CST_DATE_FORMAT.YYYY_MM_DD_HH_MM_SS));
            }
        }
        if (v instanceof Number) {
            return new java.sql.Timestamp(((Number) v).longValue());
        }
        return null;
    }

    /**
     * 得到通配符类型，从集合字段中得到参数类型,如List<Long>, Map<Long,String>
     *
     * @param f
     * @return
     */
    public static Type[] getWildcardTypes(Field f) {
        return ((ParameterizedType) f.getGenericType()).getActualTypeArguments();
    }

    /**
     * 过滤掉非属性字段
     *
     * @param fields
     * @return
     */
    public static Field[] filterPropertiesFields(Field[] fields) {
        List<Field> list = new ArrayList<Field>(Arrays.asList(fields));
        for (Iterator<Field> it = list.iterator(); it.hasNext();) {
            Field f = it.next();
            if (!isAccessibleField(f)) {
                it.remove();
            }
        }
        return list.toArray((Field[]) Array.newInstance(Field.class, list.size()));
    }

    /**
     * 进行原始类型转化为对应包装类型
     *
     * @param src
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Class getBoxClass(Class src) {
        if (src.isPrimitive()) {
            if (src == Integer.TYPE) {
                return Integer.class;
            }
            if (src == Short.TYPE) {
                return Short.class;
            }
            if (src == Long.TYPE) {
                return Long.class;
            }
            if (src == Character.TYPE) {
                return Character.class;
            }
            if (src == Double.TYPE) {
                return Double.class;
            }
            if (src == Float.TYPE) {
                return Float.class;
            }
            if (src == Byte.TYPE) {
                return Byte.class;
            }
            if (src == Boolean.TYPE) {
                return Boolean.class;
            }
        }
        return src;
    }

    /**
     * 检查类型是否相同
     *
     * @param src
     * @param target
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static boolean isSameCast(Class src, Class target) {
        // 两个类相等
        if (src == target) {
            return true;
        }
        // 如果src是target的子类，可以向上转型
        if (target.isAssignableFrom(src)) {
            return true;
        }
        // 进行原始类型转化
        src = getBoxClass(src);
        target = getBoxClass(target);
        // 两个类相等
        if (src == target) {
            return true;
        }
        return false;
    }

    /**
     * 将List对象转为对应类型的数组对象,不支持递归转换
     *
     * @param src
     * @param type
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object parseListToArray(Object src, Class type) {
        if (List.class.isAssignableFrom(src.getClass())) { // 必须是List的子类
            List list = (List) src;
            return list.toArray((Object[]) Array.newInstance(type.getComponentType(), list.size()));
        }
        return null;
    }

    /**
     * 将字符串数组转为对应的类型LIST，不支持递归转换
     *
     * @param src
     * @param type
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Object parseArrayToList(Object src, Class wildcardType) {
        Class srcCls = getBoxClass(src.getClass());
        if (!srcCls.isArray()) {
            return Collections.emptyList();
        }
        Class srcCompCls = getBoxClass(srcCls.getComponentType());
        Class tagCls = getBoxClass(wildcardType);
        if (isSameCast(srcCompCls, tagCls)) {
            return Arrays.asList((Object[]) src);
        }
        if (srcCompCls != String.class) {
            return Collections.emptyList();
        }
        int size = Array.getLength(src);
        List result = new ArrayList(size + 1);
        for (int i = 0; i < size; i++) {
            Object obj = Array.get(src, i);
            String str = (String) obj;
            if (StringUtil.isEmpty(str)) {
                continue;
            }
            if (tagCls == Double.class) {
                obj = Double.valueOf(str);
            } else if (tagCls == Float.class) {
                obj = Float.valueOf(str);
            } else if (tagCls == Boolean.class) {
                obj = Boolean.valueOf(str);
            } else if (tagCls == Integer.class) {
                obj = Integer.valueOf(str);
            } else if (tagCls == Short.class) {
                obj = Short.valueOf(str);
            } else if (tagCls == Long.class) {
                obj = Long.valueOf(str);
            } else if (tagCls == Byte.class) {
                obj = Byte.valueOf(str);
            } else if (tagCls == Character.class) {
                obj = (str).charAt(0);
            }
            result.add(obj);
        }
        return result;
    }

    /**
     * 将数组对象转为对应类型的数组对象
     *
     * @param src
     * @param type
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object castArrayToType(Object src, Class type) throws Exception {
        Class srcCls = src.getClass();
        // 必须是数组
        if (!srcCls.isArray() || !type.isArray()) {
            return null;
        }
        Class srcCompCls = getBoxClass(srcCls.getComponentType());
        Class tagCompCls = getBoxClass(type.getComponentType());
        if (isSameCast(srcCompCls, tagCompCls)) {
            return src;
        }
        if (srcCompCls != String.class) {
            return null;
        }
        int size = Array.getLength(src);
        List result = new ArrayList(size + 1);
        for (int i = 0; i < size; i++) {
            Object obj = Array.get(src, i);
            String str = (String) obj;
            if (StringUtil.isEmpty(str)) {
                continue;
            }
            if (tagCompCls == Double.class) {
                obj = Double.valueOf(str);
            } else if (tagCompCls == Float.class) {
                obj = Float.valueOf(str);
            } else if (tagCompCls == Boolean.class) {
                obj = Boolean.valueOf(str);
            } else if (tagCompCls == Integer.class) {
                obj = Integer.valueOf(str);
            } else if (tagCompCls == Short.class) {
                obj = Short.valueOf(str);
            } else if (tagCompCls == Long.class) {
                obj = Long.valueOf(str);
            } else if (tagCompCls == Byte.class) {
                obj = Byte.valueOf(str);
            } else if (tagCompCls == Character.class) {
                obj = str.charAt(0);
            }
            result.add(obj);
        }
        return result.toArray((Object[]) Array.newInstance(type.getComponentType(), result.size()));
    }

    /**
     * 检查类是否为抽象类或接口类
     *
     * @param clazz
     * @return
     */
    public static final boolean isAbstractClass(Class<?> clazz) {
        int flag = clazz.getModifiers();
        return (
                Modifier.isInterface(flag) || 
                Modifier.isAbstract(flag)
                );
    }

    /**
     * 得到类所有private, protected字段
     *
     * @param clazz
     * @return
     */
    public static final Set<Field> getAllAccessibleFields(Class<?> clazz) {
        return getAllAccessibleFields(clazz, null);
    }

    /**
     * 得到类所有private, protected字段，遇到停止类则截止寻找字段
     *
     * @param clazz
     * @param stopClazz
     * @return
     */
    public static final Set<Field> getAllAccessibleFields(Class<?> clazz, Class<?> stopClazz) {
        Set<Field> fieldset = new HashSet<Field>();
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
     * 将对象属性转换为Map<属性名称, Field> <br/>
     * 只对clazz获取属性，不涉及超类
     * @param clazz
     * @return
     */
    public static Map<String, Field> getAllAccessibleFieldNameMap(Class<?> clazz){
        return getAllAccessibleFieldNameMap(clazz, null);
    }
    
    /**
     * 将对象属性转换为Map<属性名称, Field>
     * @param clazz 查找类
     * @param stopClazz
     * @return
     */
    public static Map<String, Field> getAllAccessibleFieldNameMap(Class<?> clazz, Class<?> stopClazz){
        Map<String, Field> fieldNameMap = new HashMap<String, Field>();
        while(clazz !=null && clazz != stopClazz){
            Field[] fields = clazz.getDeclaredFields();
                for(Field f : fields){
                    if(isAccessibleField(f)){
                        f.setAccessible(true);
                        fieldNameMap.put(f.getName(), f);
                    }
                }
                clazz = clazz.getSuperclass();//可能超类还有属性
        }
        return fieldNameMap;
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
     * 给obj对象单个字段field尝试设置值为value
     *
     * @param field
     * @param obj
     * @param value
     * @throws Exception
     */
    public static void setValue(Field field, Object obj, Object value)
            throws Exception {
        if (value == null) {
            return;
        }
        if (!isAccessibleField(field)) {
            return;
        }
        field.setAccessible(true);
        field.set(obj, parseObject(value, field));
    }
    
    
    /**
     * 给特定POJO对象的字段设置值，字段名称不区分大小写
     *
     * @param obj
     * @param fieldName
     * @param value
     */
    public static void setValue(Object obj, String fieldName, Object value)
            throws Exception {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (!isAccessibleField(f)) {
                continue;
            }
            if (f.getName().equalsIgnoreCase(fieldName)) {
                f.setAccessible(true);
                f.set(obj, parseObject(value, f));
                break;
            }
        }
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
    
    /**
     * 得到字段值
     * @param field
     * @param target
     * @return
     */
    public static Object getValue(Field field, Object target) {
        try {
            return field.get(target);
        }catch (IllegalAccessException ex) {
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }
   
}
