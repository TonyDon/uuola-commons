/*
 * @(#)FileFilter.java 2013-10-14
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.uploader.apache;

import org.apache.commons.fileupload.FileItem;

import com.uuola.commons.constant.CST_FILE_TYPE;
import com.uuola.commons.file.FileUtil;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-10-14
 * </pre>
 */
public class FileFilter implements UploadFileFilter {

    @Override
    public boolean check(FileItem item) {
        boolean flag = true;
        try {
            flag = (CST_FILE_TYPE.UPLOAD_FILE_TYPE.contains(FileUtil.getFileExt(item.getName()).toLowerCase()));
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

}
