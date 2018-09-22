/*
 * @(#)CodecTest.java 2014-11-2
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txcms.util.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.Crypt;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import com.uuola.commons.coder.Md5;
import com.uuola.commons.coder.MyBase64;
import com.uuola.commons.coder.SHA;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2014-11-2
 * </pre>
 */
public class CodecTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println(Hex.encodeHexString(DigestUtils.sha1("test")));
        System.out.println(SHA.encode("test", SHA.SHA_1));
        
        System.out.println(Base64.encodeBase64String("test哈哈".getBytes()));
        System.out.println(new String(Base64.decodeBase64("dGVzdOWTiOWTiA==")));
        System.out.println(MyBase64.encode("test哈哈ab123"));
        System.out.println(MyBase64.decode("8GVB8O1tiO1tiGfiMtIB"));
        System.out.println(MyBase64.encode(Md5.encode("1985")));
        System.out.println(MyBase64.decode("M1yBnmM6n1QdytnkMtHkntjlOGQYOtniyBg6O08JyJk="));
        
        System.out.println(Crypt.crypt("abc"));
        
        try {
//            fileSHA1();
//            fileMD5();
            //base64Image();
            //fileSHA256();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    private static void fileSHA1() throws FileNotFoundException, IOException{
        File inputFile = new File("G:\\Downloads\\golang\\go1.4.windows-386.zip");
        String sha1Hex = DigestUtils.sha1Hex(new BufferedInputStream(new FileInputStream(inputFile)));
        System.out.println(sha1Hex);
    }
    
    private static void fileSHA256() throws FileNotFoundException, IOException{
        File inputFile = new File("D:\\迅雷下载\\go1.6.2.windows-386.zip");
        String sha1Hex = DigestUtils.sha256Hex(new BufferedInputStream(new FileInputStream(inputFile)));
        System.out.println(sha1Hex);
    }
    
    private static void fileMD5() throws FileNotFoundException, IOException{
        File inputFile = new File("G:\\Downloads\\golang\\go1.4.windows-386.zip");
        String hex = DigestUtils.md5Hex(new BufferedInputStream(new FileInputStream(inputFile)));
        System.out.println(hex);
    }
    
    private static void base64Image() throws FileNotFoundException, IOException{
        File inputFile = new File("C:\\eca72374ae68a1032dde.jpg");
        byte[] buffer = new byte[(int)inputFile.length()];
        IOUtils.read(new BufferedInputStream(new FileInputStream(inputFile)), buffer);
        String hex = Base64.encodeBase64String(buffer);
        System.out.println(hex);
    }

}
