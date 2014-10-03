package com.uuola.commons.uploader.apache;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <pre>
 * 单个文件上传处理
 * @author tangxiaodong
 * 创建日期: 2013-10-13
 * </pre>
 */
public class SingleFileUpload extends FileUploadBase{
    
    private static Logger logger = LoggerFactory.getLogger(SingleFileUpload.class);

    /**
     * 只存储单个文件
     */
    private FileItem fileItem;

    /**
     * 转换HttpServletRequest
     * @param request
     */
    public void parseRequest(HttpServletRequest request){

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(sizeThreshold);
        if (repository != null){
            factory.setRepository(repository);
        }
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding(encoding);

        try{
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem item : items){
                if (item.isFormField()){
                    parameters.put(item.getFieldName(), item.getString(encoding));
                } else{
                    if (isValidFile(item)){
                        if (fileItem == null){
                             fileItem = item;
                        }
                    }
                }
            }
        } catch (Exception ex){
            logger.error("SingleFileUpload.parseRequest() Error:", ex);
        }
    }

    /**
     * 上传文件, 调用该方法之前必须先调用<br/>
     * parseRequest(HttpServletRequest request)
     * @param fileName 完整文件路径， 上传的文件必须重命名. eg: /www/files/2013/09/08/u12321/9823/fileABCDFEG.jpg 
     * @return 是否上传成功
     */
    public boolean upload(String fileName){
        if (fileItem == null){
            return false;
        }
        File file = new File(fileName);
        return uploadFile(file);
    }

    private boolean uploadFile(File file){
        boolean isSuccess = true;
        long fileSize = fileItem.getSize();
        if ( sizeMax > -1 && fileSize >  sizeMax) {
            logger.warn(String.format(
                    "the request was rejected because its size (%1$s) exceeds the configured maximum (%2$s)", fileSize,
                    super.sizeMax));
            isSuccess = false;
        }else{
            try {
                fileItem.write(file);
            } catch (Exception e) {
                isSuccess = false;
                logger.warn(ExceptionUtils.getRootCauseMessage(e));
            }
            if(!fileItem.isInMemory()){
                fileItem.delete();
            }
        }
        return isSuccess;
    }

    /**
     * 获取文件信息
     * 必须先调用 parseRequest(HttpServletRequest request)
     * @return
     */
    public FileItem getFileItem(){
        return fileItem;
    }
}