/*
 * @(#)DateUtil.java 2013-6-11
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * <pre>
 * @author tangxiaodong
 * 日期工具类
 * 创建日期: 2013-6-11
 * </pre>
 */
public class DateUtil {


    /**
     * 转换日期文本格式为 日期类
     * @param dateText 
     * @param dateFormat 日期格式
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String dateText, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(true);
        try {
            return sdf.parse(dateText);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Could not parse date: " + dateText);
        }
    }
    
    /**
     * 格式化日期为字符串
     * @param date
     * @param dateFormat
     * @return
     * @throws ParseException
     */
    public static String formatDate(Date date, String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }
    
    /**
     * 将毫秒时间戳转换为日期格式文本
     * @param msTime
     * @param dateFormat
     * @return
     */
    public static String formatTimeMillis(Long msTime, String dateFormat){
        return DateUtil.formatDate(new Date(msTime), dateFormat);
    }
    
    /**
     * 根据日期文本获取毫秒时间戳
     * @param dateText
     * @param dateFormat
     * @return
     * @throws IllegalArgumentException
     */
    public static long parseTimeMillis(String dateText, String dateFormat){
        if(StringUtil.isEmpty(dateText)){
            return 0L;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try{
            return sdf.parse(dateText).getTime();
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Could not get TimeMillis by : " + dateText);
        }
    }
    
    /**
     * 得到当前时间戳，毫秒部分为0
     * @return
     */
    public static long getCurrTime(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    /**
     * 得到当前时间戳，含毫秒数
     * @return
     */
    public static long getCurrMsTime(){
        return System.currentTimeMillis();
    }
    

}
