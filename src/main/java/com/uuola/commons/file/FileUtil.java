package com.uuola.commons.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.constant.CST_ENCODING;

/**
 * 
 * @author txdnet
 */
public final class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);


    /*
     * @see 创建不存在的目录
     */
    public static boolean createNoExistsDirs(String path) {
        File dirs = new File(path);
        if (dirs.exists()) {
            return true;
        }
        try {
            FileUtils.forceMkdir(dirs);
            return true;
        } catch (IOException ex) {
            logger.error(ex.toString());
            return false;
        }
    }

    /*
     * @see 生成多级目录
     */
    public static boolean createDirs(String path) {
        try {
            FileUtils.forceMkdir(new File(path));
            return true;
        } catch (IOException ex) {
            logger.error(ex.toString());
            return false;
        }
    }

    /*
     * @see 创建一个目录
     */

    public static boolean createDir(String path) {
        try {
            FileUtils.forceMkdir(new File(path));
            return true;
        } catch (IOException ex) {
            logger.error(ex.toString());
            return false;
        }
    }

    /*
     * @see 创建一个新文件
     */
    public static boolean createNewFile(String path) {
        File file = new File(path);

        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException ex) {
                logger.error(ex.toString());
                return false;
            }
        }
        return false;
    }

    /*
     * @see 删除一个文件或空目录
     */
    public static boolean delete(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        try {
            FileUtils.forceDelete(file);
            return true;
        } catch (IOException ex) {
            logger.error(ex.toString());
            return false;
        }
    }

    /*
     * 遍历删除一个目录返回删除的目录和文件总数
     * 
     * public static int deleteDirs(String path){ File file = new File(path);
     * int deltotal = 0; if(file.isFile()){ file.delete(); deltotal++; }else if(
     * file.isDirectory()){ String[] filelist = file.list(); int len =
     * filelist.length; if(len==0){ file.delete(); deltotal++; }else{ int k=0;
     * File f = null; for(k=0; k< len ; k++){ f = new File(path + File.separator
     * + filelist[k]); if (f.isFile()){ f.delete(); deltotal++; }else
     * if(f.isDirectory()){ deltotal+=deleteDirs(path + File.separator +
     * filelist[k]); } } } file.delete(); deltotal++; } return deltotal; }
     */
    public static boolean deleteDirs(String path) {
        return FileUtils.deleteQuietly(new File(path));
    }

    /*
     * @see 重命名原始文件 不产生新文件
     */
    public static boolean renameFile(String fn, String on) {
        File f1, f2;
        f1 = new File(fn);
        f2 = new File(on);
        if (!f1.exists() || f2.exists() || f1.isDirectory() || f2.isDirectory()) {
            return false;
        }
        return f1.renameTo(f2);
    }

    // public static String readStringByChannel(String ff, int buf_size){
    // ByteBuffer buf = ByteBuffer.allocate(buf_size);
    // StringBuilder sb = new StringBuilder(buf_size);
    // try{
    // FileChannel fc = new FileInputStream(ff).getChannel();
    // while(true){
    // buf.clear();
    // if(fc.read(buf)==-1){
    // break;
    // }
    // buf.flip();
    // while(buf.hasRemaining()){
    // sb.append((char)buf.get());
    // }
    // }
    // fc.close();
    // }catch(Exception ex){
    // Logger.error(clazz, ex.toString());
    // }
    // return sb.toString();
    // }

    /*
     * @see 文件复制从 fromfile通道复制到tofile通道完成文件复制.
     */
    public static boolean copyFileByChannel(String ff, String tf) {
        boolean flag = true;
        FileChannel sc = null;
        FileChannel dc = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(ff);
            sc = fis.getChannel();
            fos = new FileOutputStream(tf);
            dc = fos.getChannel();
            dc.transferFrom(sc, 0, sc.size());
        } catch (IOException ex) {
            logger.error(ex.toString());
            flag = false;
        } finally {
            try {
                sc.close();
                dc.close();
            } catch (IOException ioex) {
                logger.error(ioex.toString());
            }
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(fos);
        }
        return flag;
    }

    /*
     * @see 复制@ff 文件到@tf 目录 目录不存在则先创建 返回boolean
     */
    public static boolean copyFile(String ff, String tf) {
        boolean flag = true;
        try {
            FileUtils.copyFile(new File(ff), new File(tf), true);
        } catch (IOException ex) {
            logger.error(ex.toString());
            flag = false;
        }
        return flag;
    }

    /*
     * @see 移动@srcfile文件到@dir目录 目录不存在则先创建 返回boolean
     */
    public static boolean moveFileToDirectory(String sf, String dir) {
        boolean flag = true;
        try {
            FileUtils.moveFileToDirectory(new File(sf), new File(dir), true);
        } catch (IOException ex) {
            logger.error(ex.toString());
            flag = false;
        }
        return flag;
    }

    /*
     * @see 从文件中读取字符utf8格式文本字符
     */
    public static String readStringByFile(String path, String charset) {
        String fileContent = null;
        try {
            fileContent = FileUtils.readFileToString(new File(path), charset);
        } catch (IOException ex) {
            logger.error(ex.toString());
        }
        return fileContent;
    }

    public static String readStringByFile(InputStream in, String charset) {
        String fileContent;
        try {
            BufferedInputStream bis = new BufferedInputStream(in, 1024);
            int lenght = bis.available();
            byte bytes[] = new byte[lenght];
            bis.read(bytes);
            bis.close();
            fileContent = new String(bytes, charset);
        } catch (Exception ex) {
            logger.error(ex.toString());
            fileContent = null;
        }
        return fileContent;
    }

    /*
     * @see 生成utf8格式文件 存在文件直接覆盖
     */
    public static boolean writeStringToFile(String path, String s) {
        return writeStringToFile(path, s, CST_ENCODING.UTF8);
    }

    public static boolean writeStringToFile(String path, String s, String encoding) {
        boolean flag = true;
        try {
            FileUtils.writeStringToFile(new File(path), s, encoding);
        } catch (IOException ex) {
            logger.error(ex.toString());
            flag = false;
        }
        return flag;
    }

    public static boolean writeStringToFile(OutputStream out, String s, String charset) {
        boolean flag = true;
        try {
            BufferedOutputStream bos = new BufferedOutputStream(out);
            byte bytes[] = s.getBytes(charset);
            bos.write(bytes);
            bos.close();
        } catch (Exception ex) {
            logger.error(ex.toString());
            flag = false;
        }
        return flag;
    }

    /*
     * @see 生成utf8格式文件 存在文件末尾追加字符
     */
    public static boolean appendStringToFile(String path, String s, String charset) {
        boolean flag = true;
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path, true));
            byte bytes[] = s.getBytes(charset);
            bos.write(bytes);
            IOUtils.closeQuietly(bos);
        } catch (Exception ex) {
            logger.error(ex.toString());
            flag = false;
        }
        return flag;
    }

    /*
     * @see 读取属性文件
     */
    public static Properties readPropertiesFile(String path) {
        Properties p;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            p = new Properties();
            p.load(bis);
            IOUtils.closeQuietly(bis);
        } catch (Exception ex) {
            logger.error(ex.toString());
            return null;
        }
        return p;

    }

    public static Properties readPropertiesFile(InputStream in) {
        Properties p;
        try {
            BufferedInputStream bis = new BufferedInputStream(in);
            p = new Properties();
            p.load(bis);
            IOUtils.closeQuietly(bis);
        } catch (Exception ex) {
            logger.error(ex.toString());
            return null;
        }
        return p;

    }

    /*
     * @see 保存属性文件
     */
    public static boolean savePropertiesFile(String path, Properties p, String comment) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
            p.store(bos, comment);
            IOUtils.closeQuietly(bos);
        } catch (Exception ex) {
            logger.error(ex.toString());
            return false;
        }
        return true;
    }

    public static boolean savePropertiesFile(OutputStream out, Properties p, String comment) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(out);
            p.store(bos, comment);
            bos.close();
        } catch (Exception ex) {
            logger.error(ex.toString());
            return false;
        }
        return true;
    }

    /*
     * 得到路径中的文件名含扩展
     */
    public static String getFileName(String s) {
        return FilenameUtils.getName(s);
    }

    /*
     * 得到路径中的文件名不含扩展
     */
    public static String getBaseName(String s) {
        return FilenameUtils.getBaseName(s);
    }

    /*
     * 得到文件名的扩展名
     */
    public static String getFileExt(String s) {
        return FilenameUtils.getExtension(s);
    }
}