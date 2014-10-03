/*
by txdnet.cn tonydon
 */

package com.uuola.commons.uploader.apache;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import com.uuola.commons.constant.CST_ENCODING;


/**
 * <pre>
 * 上传文件基类
 * @author tangxiaodong
 * 创建日期: 2013-10-13
 * </pre>
 */
public abstract class FileUploadBase {
    protected Map<String, String> parameters = new HashMap<String, String>();// 保存普通form表单域

    /**
     * 字符编码，当读取上传表单的各部分时会用到该encoding
     */
    protected String encoding = CST_ENCODING.UTF8;

    /**
     * 文件过滤器
     */
    protected UploadFileFilter filter ; 

   /**
    * 设置内存存储文件缓存阈值，超出阈值将使用磁盘临时存储目录
    */
    protected int sizeThreshold = DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD;

    /**
     * 上传文件最大的大小，单位为Byte(字节），为-1时表示无限制
     */
    protected int sizeMax = -1;

    /*
     * 设置磁盘文件存储临时目录
     */
    protected File repository;

    public String getParameter(String key){
        return parameters.get(key);
    }

    public String getEncoding(){
        return encoding;
    }

    public void setEncoding(String encoding){
        this.encoding = encoding;
    }

    public int getSizeMax(){
        return sizeMax;
    }

    public void setSizeMax(int sizeMax){
        this.sizeMax = sizeMax;
    }

    public int getSizeThreshold(){
        return sizeThreshold;
    }

    public void setSizeThreshold(int sizeThreshold){
        this.sizeThreshold = sizeThreshold;
    }

    public File getRepository(){
        return repository;
    }

    public void setRepository(File repository){
        this.repository = repository;
    }

    /**
     * 获取参数列表
     * @return
     */
    public Map<String, String> getParameters(){
        return parameters;
    }

    /** 
     * 获取过滤器
     * @return
     */
    public UploadFileFilter getFilter(){
        return filter;
    }

    /** 
     * 设置文件过滤器，不符合过滤器规则的将不被上传
     * @param filter
     */
    public void setFilter(UploadFileFilter filter){
        this.filter = filter;
    }

    /** 
     * 验证文件是否有效
     * @param item
     * @return
     */
    protected boolean isValidFile(FileItem item){
        if(item==null || item.getName().length()==0 || item.getSize()==0){
            return false;
        }
        if(filter!=null && !filter.check(item)){
            return false;
        }
        return true;
    }

}
