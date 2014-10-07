/*
 * @(#)KeyGenerator.java 2013-10-14
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.coder;

import java.util.UUID;

import com.uuola.commons.DateUtil;
import com.uuola.commons.NumberUtil;
import com.uuola.commons.StringUtil;


/**
 * <pre>
 * key值生成器
 * @author tangxiaodong
 * 创建日期: 2013-10-14
 * </pre>
 */
public final class KeyGenerator {

    public static char[] LETTER_NUMBER_MAP = { 
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z' };

    public static char[] LETTER_MAP = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q','r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z','_','A', 'B',
            'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    
    public static char[] NUMBER_MAP = {'1','2','3','4','5','6','7','8','9','0'};

    /*
     * 随机生成字符串,使用Def.MAP1的CHAR表(MAP1是数字和大小写字母, MAP2只有大小写字母及下划线)
     * 
     * @return String
     */
    public static String getRndChr(int len) {
        return getRndChr(len, LETTER_NUMBER_MAP);
    }

    public static String getRndChr(int len, char[] map) {
        int size = map.length;
        if (1 == len) {
            return String.valueOf(map[NumberUtil.genRndInt(0, size)]);
        }
        StringBuilder sb = new StringBuilder(len);
        if (4 == len) {
            sb.append(map[NumberUtil.genRndInt(0, size)]);
            sb.append(map[NumberUtil.genRndInt(0, size)]);
            sb.append(map[NumberUtil.genRndInt(0, size)]);
            sb.append(map[NumberUtil.genRndInt(0, size)]);
            
        } else if (0 == len % 8) {
            int times = len / 8;
            for (int i = 0; i < times; i++) {
                sb.append(map[NumberUtil.genRndInt(0, size)]);
                sb.append(map[NumberUtil.genRndInt(0, size)]);
                sb.append(map[NumberUtil.genRndInt(0, size)]);
                sb.append(map[NumberUtil.genRndInt(0, size)]);
                sb.append(map[NumberUtil.genRndInt(0, size)]);
                sb.append(map[NumberUtil.genRndInt(0, size)]);
                sb.append(map[NumberUtil.genRndInt(0, size)]);
                sb.append(map[NumberUtil.genRndInt(0, size)]);
            }
            
        } else {
            for (int k = 0; k < len; k++) {
                sb.append(map[NumberUtil.genRndInt(0, size)]);
            }
            
        }
        return sb.toString();
    }

    /*
     * 生成UUID，利用java.util.UUID+4个随机字符然后经过SHA HASH得到 40 位字符串序列，适合非数字ID的生成环境
     * 如SESSIONID，单号等
     * 
     * @return String
     */
    public static String getUUID() {
        return getUUID(4, SHA.SHA_1);
    }

    public static String getUUID(int rndCharNum, String shaflag) {
        return SHA.encode(UUID.randomUUID().toString().concat(getRndChr(rndCharNum)), shaflag);
    }

    /*
     * 短字串生成,输入一个字符串计算他的短字符串序列,返回 4组 6位HASH字串
     * 
     * @return String
     */
    public static String getShortChar(String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        String str_hex = Md5.encode(str);
        long hexint;
        StringBuilder sb = new StringBuilder();
        String tmp;
        for (int i = 0; i < 4; i++) {// 分4组
            tmp = str_hex.substring(i * 8, (i + 1) * 8);
            // 这里使用 long 型来转换， Inteper .parseInt() 只能处理 31 位 ,
            hexint = 0x3fffffff & Long.parseLong(tmp, 16);
            for (int j = 0; j < 6; j++) {
                // 把得到的值与字符表字符个数进行位与运算，取得字符数组MAP CHAR 索引
                // 把取得的字符相加
                sb.append(LETTER_NUMBER_MAP[(int) ((LETTER_NUMBER_MAP.length - 1) & hexint)]);
                // 每次循环按位右移5位
                hexint = hexint >> 5;
            }
            sb.append(" ");
        }
        return sb.toString();
    }
    
    public static void main(String... arg) throws Exception{
        System.out.println(getRndChr(1));
        System.out.println(getRndChr(4));
        System.out.println(getRndChr(8));
        System.out.println(getShortChar("asdfasfsdfasf"));
        System.out.println(getRndChr(8).concat(getUUID()).concat("!").concat(Long.toString(DateUtil.getCurrMsTime(),16)));
    }
}
