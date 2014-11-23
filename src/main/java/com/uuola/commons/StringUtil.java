/*
 * @(#)StringUtil.java 2013-6-12
 * 
 * Copy Right@ uuola
 */

package com.uuola.commons;

import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.uuola.commons.constant.CST_CHAR;
import com.uuola.commons.constant.CST_REGEX;

/**
 * <pre>
 * @author tangxiaodong
 * common-lang/3用到的方法在此封装一次
 * 项目中统一使用此工具类
 * 创建日期: 2013-6-12
 * </pre>
 */
public class StringUtil {

    /**
     * 检查字符串是否为空
     * 
     * @param value
     * @return boolean
     */
    public static boolean isEmpty(CharSequence value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNotEmpty(CharSequence value) {
        return !StringUtil.isEmpty(value);
    }

    /**
     * 检查字符串是否为数字含小数
     * 
     * @param value
     * @return boolean
     */
    public static boolean isDecimal(CharSequence value) {
        if (value == null) {
            return false;
        }
        char[] chars = value.toString().toCharArray();
        int length = chars.length;
        if (length < 1) {
            return false;
        }

        int i = 0;
        if (length > 1 && chars[0] == CST_CHAR.CHAR_LINE) {
            i = 1;
        }

        if (chars[length - 1] == CST_CHAR.CHAR_DOT || !Character.isDigit(chars[i])) {
            return false;
        }

        int n = 0;
        for (; i < length; i++) {
            if (chars[i] == CST_CHAR.CHAR_DOT) {
                n++;
            }
            if (((!Character.isDigit(chars[i])) && chars[i] != CST_CHAR.CHAR_DOT) || n > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否为整数型字符串
     * 
     * @param value
     * @return
     */
    public static boolean isInteger(CharSequence value) {
        if (value == null) {
            return false;
        }
        char[] chars = value.toString().toCharArray();
        int length = chars.length;
        if (length < 1) {
            return false;
        }

        int i = 0;
        if (length > 1 && chars[0] == CST_CHAR.CHAR_LINE) {
            i = 1;
        }

        for (; i < length; i++) {
            if (!Character.isDigit(chars[i])) {
                return false;
            }
        }
        return true;
    }
    

    /**
     * 字符验证函数，使用编译后的匹配模式快速判断是否符合模式要求，不符合则返回 retStr.
     * @param val
     * @param pattern
     * @param retStr
     * @return
     */
    public static String getValid(String val, Pattern pattern, String retStr) {
        if (isEmpty(val)) {
            return retStr;
        }
        return pattern.matcher(val).matches() ? val : retStr;
    }
    
    /**
     * 检查,为空则返回一个定义值
     *
     * @param val
     * @param retstr
     * @return
     */
    public static String checkNull(String val, String retStr) {
        if (isEmpty(val)) {
            return retStr;
        } else {
            return val.trim();
        }
    }

    public static String[] split(String ids, char c) {
        return StringUtils.split(ids, c);
    }
    
    public static String[] split(String ids, String c) {
        return StringUtils.split(ids, c);
    }

    public static String join(String[] ida, char c) {
        return StringUtils.join(ida, c);
    }
    
    public static String join(String[] ida, String c){
        return StringUtils.join(ida, c);
    }
    
    public static String join(Collection<?> col, String c){
        return StringUtils.join(col, c);
    }
    
    public static String join(Collection<?> col, char c){
        return StringUtils.join(col, c);
    }
    
    /**
     * <pre>
     * StringUtils.substringAfterLast(null, *)      = null
     * StringUtils.substringAfterLast("", *)        = ""
     * StringUtils.substringAfterLast(*, "")        = ""
     * StringUtils.substringAfterLast(*, null)      = ""
     * StringUtils.substringAfterLast("abc", "a")   = "bc"
     * StringUtils.substringAfterLast("abcba", "b") = "a"
     * StringUtils.substringAfterLast("abc", "c")   = ""
     * StringUtils.substringAfterLast("a", "a")     = ""
     * StringUtils.substringAfterLast("a", "z")     = ""
     * </pre>
     */
    public static String substringAfterLast(String str, String separator){
        return StringUtils.substringAfterLast(str, separator);
    }
    
    /**
     * <p>Replaces all occurrences of a String within another String.</p>
     *
     * <p>A <code>null</code> reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("any", null, *)    = "any"
     * StringUtils.replace("any", *, null)    = "any"
     * StringUtils.replace("any", "", *)      = "any"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "b"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
     * @see #replace(String text, String searchString, String replacement, int max)
     * @param text  text to search and replace in, may be null
     * @param searchString  the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @return the text with any replacements processed,
     *  <code>null</code> if null String input
     */
    public static String replace(String text, String searchString, String replacement){
        return StringUtils.replace(text, searchString, replacement, -1);
    }
    
    /**
     * 转换为驼峰式名称
     * Convert a column name with underscores to the corresponding property name using "camel case".  A name
     * like "customer_number" would match a "customerNumber" property name.
     * @param name the column name to be converted
     * @return the name using "camel case"
     */
    public static String getCamelcaseName(String name) {
        if (name == null || name.isEmpty()) {
            return CST_CHAR.STR_EMPTY;
        }
        if (name.indexOf(CST_CHAR.CHAR_UNDER_LINE) < 0) {
            return name.toLowerCase();
        }
        StringBuilder result = new StringBuilder();
        boolean nextIsUpper = false;
        int len = name.length();
//        if (len > 1 && name.substring(1, 2).equals(CST_CHAR.STR_UNDER_LINE)) {
//            result.append(name.substring(0, 1).toUpperCase());
//        } else {
//            result.append(name.substring(0, 1).toLowerCase());
//        }
        for (int i = 0; i < len; i++) {
            String s = name.substring(i, i + 1);
            if (s.equals(CST_CHAR.STR_UNDER_LINE)) {
                nextIsUpper = true;
            } else {
                if (nextIsUpper) {
                    result.append(s.toUpperCase());
                    nextIsUpper = false;
                } else {
                    result.append(s.toLowerCase());
                }
            }
        }
        return result.toString();
    }
    
    /**
     * “customerNumber” -&gt; “customer_number”
     * Convert a name in camelCase to an underscored name in lower case.
     * Any upper case letters are converted to lower case with a preceding underscore.
     * @param name
     * @return
     */
    public static String getUnderscoreName(String name) {
        if (name == null || name.isEmpty()) {
            return CST_CHAR.STR_EMPTY;
        }
        StringBuilder result = new StringBuilder();
        result.append(name.substring(0, 1).toLowerCase());
        int len = name.length();
        for (int i = 1; i < len; i++) {
            String s = name.substring(i, i + 1);
            String slc = s.toLowerCase();
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            }
            else {
                result.append(s);
            }
        }
        return result.toString();
    }
    
    /**
     * 过滤掉文件中的注释部分
     * @param str
     * @return
     */
    public static String clearNote(String str) {
        return CST_REGEX.RE_CLR_NOTE.matcher(str).replaceAll(CST_CHAR.STR_EMPTY);
    }
    
    /**
     * str  不以prefix 字符串为开始 返回true ,prefix大小写敏感<br/>
     * 使用此方法减少‘!’非的使用
     * @param str
     * @param prefix
     * @return
     */
    public static boolean startsNotWith(String str, String prefix){
        return !StringUtils.startsWith(str, prefix);
    }
    
    /**
     * str  不以suffix 字符串结束, 返回true , suffix大小写敏感<br/>
     * 使用此方法减少‘!’非的使用
     * @param str
     * @param suffix
     * @return
     */
    public static boolean endNotWith(String str, String suffix){
        return !StringUtils.endsWith(str, suffix);
    }
    
    
    /**
     * 构建参数占位符如 3个参数为 ?,?,?
     * eg: Long[] inIds = {111L,222L,333L}; length = 3
     * return ?,?,?
     * @param argNum
     * @return
     */
    public static String getPlaceholder(int argNum) {
        if (1 == argNum) {
            return CST_CHAR.STR_QUESTION;
        }
        StringBuilder sb = new StringBuilder();
        int lastNum = argNum - 1;
        for (int k = 0; k < lastNum; k++) {
            sb.append(CST_CHAR.CHAR_QUESTION);
            sb.append(CST_CHAR.CHAR_COMMA);
        }
        return sb.append(CST_CHAR.CHAR_QUESTION).toString();
    }
}
