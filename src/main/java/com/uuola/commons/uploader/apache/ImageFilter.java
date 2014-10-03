
package com.uuola.commons.uploader.apache;
import org.apache.commons.fileupload.FileItem;

import com.uuola.commons.constant.CST_FILE_TYPE;
import com.uuola.commons.file.FileUtil;

/**
 * 
 * <pre>
 * 校验图片上传文件类型
 * @author tangxiaodong
 * 创建日期: 2013-10-14
 * </pre>
 */
public class ImageFilter implements UploadFileFilter {

    @Override
    public boolean check(FileItem item) {
        boolean flag = true;
        try {
            flag = (CST_FILE_TYPE.UPLOAD_IMG_TYPE.contains(FileUtil.getFileExt(item.getName()).toLowerCase()));
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}