/*
 * @(#)ZipTest.java 2015年9月5日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txcms.util.test;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.io.FileUtils;

import com.uuola.commons.packzip.ZipUtil;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2015年9月5日
 * </pre>
 */
public class ZipTest {

    /**
     * @param args
     * @throws InterruptedException 
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        ZipUtil.unpack(new File("E:\\tmp\\my-2car.zip"), new File("E:\\tmp\\my-2car"), "UTF-8");
        Thread.sleep(2000);
        FileUtils.deleteQuietly(new File("E:\\tmp\\my-2car.zip"));
        Thread.sleep(2000);
        ZipUtil.pack( new File("E:\\tmp\\my-2car.zip") , "game",  "UTF-8", new File("E:\\tmp\\my-2car"));
    }

}
