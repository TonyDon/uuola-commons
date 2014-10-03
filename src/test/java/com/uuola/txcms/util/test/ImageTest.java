/*
 * @(#)ImageTest.java 2013-10-19
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txcms.util.test;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.uuola.commons.NumberUtil;
import com.uuola.commons.image.ImageUtil;
import com.uuola.commons.image.ImageVerifier;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-10-19
 * </pre>
 */
public class ImageTest {

    /**
     * @param args
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws Exception {
        test5();
    }
    
    private static void test5(){
        File srcFile =new File("f:/temp/20120921002239.jpg");
        File mark = new File("f:/temp/google.jpg");
        
        ImageUtil.pressImage(srcFile, mark, 0.5f);
    }
    
    
    private static void test4(){
        ImageUtil.resize(new File("f:/temp/f.jpg"), 
                new File("f:/temp/f_s1.jpg"),
                200, 0, false);
    }
    
    private static void test3(){
        Font f = new Font("Tahoma", Font.BOLD, 16);
        ImageUtil.pressText(new File("f:/temp/20120921002239.jpg"), 
                "baobei.uuola.com", 
                f, 
                Color.BLUE, 
                120, 10, 
                0.6f);
        System.out.println(f.getSize());
    }
    
    private static void test2(){
        File srcFile =new File("f:/temp/T2LWTBXoJaXXXXXXXX_!!880734.jpg");
        File distFile = new File("f:/temp/T2LWTBXoJaXXXXXXXX_!!880734_c.jpg");
        
        ImageUtil.cutImage(srcFile, distFile, 790, 828, 200, 200, 300, 200);
    }
    
    private static void test1() throws FileNotFoundException{
        // TODO Auto-generated method stub
         Color[] fontcolor = {
                new Color(85,85,85),
                new Color(106,24,236),
                new Color(116,168,62)
                };
         Color[] bgcolor = {
                new Color(239,239,239),
                new Color(255,247,244),
                new Color(227,240,250)
                };
         Font[] font ={ 
                new Font("Tahoma", Font.BOLD, 32),
                new Font("Arial", Font.PLAIN|Font.ITALIC, 32)
            };
        OutputStream imageOS = new BufferedOutputStream(new FileOutputStream(new File("C:\\captcha.png")));
        ImageVerifier.outputImage(130, 45, 32, 100, true, true, 
                fontcolor, bgcolor, true, false, "0987AB",
                font[NumberUtil.genRndInt(0, 2)], "png", imageOS);
    }

}
