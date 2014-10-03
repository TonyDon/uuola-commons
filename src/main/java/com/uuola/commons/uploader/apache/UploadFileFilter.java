package com.uuola.commons.uploader.apache;
import org.apache.commons.fileupload.FileItem;

/**
 * <pre>
 * @author tangxiaodong
 * 创建日期: 2013-10-13
 * </pre>
 */
public interface UploadFileFilter {
    /**
     * 通过文件名后缀判断文件是否被接受
     * @param filename 文件名，不包括路径
     * @return
     */
    public boolean check(FileItem item);
}
