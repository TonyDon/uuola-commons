package com.uuola.txcms.util.test;

import org.junit.Assert;
import org.junit.Test;

import com.uuola.commons.NumberUtil;


/*
 * @(#)NumberUtilTest.java 2013-8-31
 * 
 * Copy Right@ uuola
 */

/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-8-31
 * </pre>
 */
public class NumberUtilTest {

    @Test
    public void test_format(){
        Double d1 = new Double("12300101.1254567");
        Assert.assertEquals("12,300,101.125", NumberUtil.format(d1.doubleValue(), "#,###.00#"));
        
        Double d2 = new Double("12300101.12");
        Assert.assertEquals("12,300,101.12", NumberUtil.format(d2.doubleValue(), "#,###.00#"));
        
        Double d3 = new Double("12300101.12");
        Assert.assertEquals("12,300,101.120", NumberUtil.format(d3.doubleValue(), "#,###.000"));
        
        System.out.println(NumberUtil.format(123321001.2345d, "0.00"));
        
        for(int i=0; i<10; i++){
            System.out.println(NumberUtil.genRndInt(0, 5));
        }
    }
}
