/*
 * @(#)ZipUtil.java 2015年9月5日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.packzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import com.uuola.commons.StringUtil;


/**
 * <pre>
 * jdk zip 打包解压工具
 * @author tangxiaodong
 * 创建日期: 2015年9月5日
 * </pre>
 */
public abstract class ZipUtil {


    /**
     * 解压zip文件到输出目录
     * @param zipFile
     * @param outDir
     * @param charset 
     * @return
     */
    public static boolean unpack(File zipFile, File outDir, String charset) {
        if (!zipFile.exists() || !zipFile.canRead() || !outDir.exists() || !outDir.isDirectory()) {
            return false;
        }
        boolean result = true;
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile, Charset.forName(charset));
            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = entries.nextElement();
                String zipEntryName = entry.getName();
                String zipEntryPath = StringUtil.replace(zipEntryName, "\\", "/");
                String entryDirPath = zipEntryPath.substring(0, zipEntryPath.lastIndexOf('/'));
                File file = new File(outDir, entryDirPath);
                File outFile = new File(outDir, zipEntryPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                if (!outFile.isDirectory()) {
                    InputStream inp = zip.getInputStream(entry);
                    OutputStream out = new FileOutputStream(outFile);
                    IOUtils.copy(inp, out);
                    IOUtils.closeQuietly(out);
                    IOUtils.closeQuietly(inp);
                }
            }
        } catch (Exception e) {
            result = false;
        } finally {
            IOUtils.closeQuietly(zip);
        }
        return result;
    }
    
    /**
     * 打包多个文件到ZIP输出文件中
     * @param zip
     * @param zipRootPath
     * @param charset
     * @param srcFiles
     * @throws FileNotFoundException 
     */
    public static void pack(File zip, String zipRootPath, String charset, File... srcFiles) throws FileNotFoundException{
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip), Charset.forName(charset));
        pack(out, zipRootPath, srcFiles);  
        IOUtils.closeQuietly(out);
    } 
    
    /**
     * 打包多个文件到ZIP输出流
     * @param out
     * @param zipRootPath
     * @param srcFiles
     */
    public static void pack(ZipOutputStream out, String zipRootPath, File... srcFiles) {
        zipRootPath = StringUtil.replace(zipRootPath, "\\", "/");
        if (!zipRootPath.endsWith("/")) {
            zipRootPath += "/";
        }
        try {
            for (int i = 0; i < srcFiles.length; i++) {
                if (srcFiles[i].isDirectory()) {
                    File[] files = srcFiles[i].listFiles();
                    String srcPath = srcFiles[i].getName();
                    srcPath = StringUtil.replace(srcPath, "\\", "/");
                    if (!srcPath.endsWith("/")) {
                        srcPath += "/";
                    }
                    String zipPath = zipRootPath.concat(srcPath);
                    out.putNextEntry(new ZipEntry(zipPath));
                    // 递归添加到压缩包
                    pack(out, zipPath, files);
                } else {
                    FileInputStream in = new FileInputStream(srcFiles[i]);
                    out.putNextEntry(new ZipEntry(zipRootPath + srcFiles[i].getName()));
                    IOUtils.copy(in, out);
                    out.closeEntry();
                    IOUtils.closeQuietly(in);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
