package com.uuola.commons.uploader.apache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.coder.KeyGenerator;
import com.uuola.commons.constant.CST_CHAR;
import com.uuola.commons.file.FileUtil;

/**
 * 
 * <pre>
 * 多文件上传
 * @author tangxiaodong
 * 创建日期: 2013-10-13
 * </pre>
 */
public class MutiFileUpload extends FileUploadBase{

    private static Logger logger = LoggerFactory.getLogger(MutiFileUpload.class);
    
    private ArrayList<FileItem> files;// 保存上传的文件
    private int filesSize = 0; // 所有文件的总大小
    private ArrayList<String> saveFileList ;


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
        files = new ArrayList<FileItem>(); // 顺序保存文件
        try{
            List<FileItem> items =  upload.parseRequest(request);
            for (FileItem item : items){
                if (item.isFormField()){
                   parameters.put(item.getFieldName(), item.getString(encoding));
                } else{
                    if (isValidFile(item)){
                    files.add(item);
                    filesSize += item.getSize();
                    }
                }
            }
        } catch (Exception ex){
            logger.error("MutiFileUpload.parseRequest() Error:", ex);
        }
    }


    /**
     * 上传文件, 调用该方法之前必须先调用 parseRequest(HttpServletRequest request)<br/>
     * 用fileNamePrefix充当新文件前缀<br/>
     * eg: /www/files/2013/09/08/u12321/9823/fileABC_0.jpg 
     * @param parent /www/files/
     * @param fileNamePrefix eg: 2013/09/08/u12321/9823/fileABC
     * @param isRandomSuffix if true , eg:/www/files/2013/09/08/u12321/9823/fileABC_0_s0Km.jpg 
     * @throws Exception
     */
    public void upload(String parent, String fileNamePrefix, boolean isRandomSuffix) throws Exception {
        if (files == null && files.isEmpty()){
            return;
        }
        if (sizeMax > -1 && filesSize > super.sizeMax) {
            String message = String.format(
                    "the request was rejected because its size (%1$s) exceeds the configured maximum (%2$s)",
                    filesSize, super.sizeMax);
            throw new SizeLimitExceededException(message, filesSize, super.sizeMax);
        }
        saveFileList = new ArrayList<String>();
        for (int idx=0, len = files.size(); idx<len; idx++) {
            FileItem item = files.get(idx);
            // 以下为自动生成随机文件名
            StringBuilder rndName = (new StringBuilder(fileNamePrefix))
                    .append(CST_CHAR.CHAR_UNDER_LINE).append(idx);
            if(isRandomSuffix){
                rndName.append(CST_CHAR.CHAR_UNDER_LINE).append(KeyGenerator.getRndChr(4));
            }
            rndName.append(CST_CHAR.CHAR_DOT).append(FileUtil.getFileExt(item.getName()));
            String tmpRndFile = rndName.toString();
            File file = new File(parent, tmpRndFile);
            item.write(file);
            saveFileList.add(idx, tmpRndFile);
            if (!item.isInMemory()) {
                item.delete();
            }
        }
    }

    public List<FileItem> getFiles(){
        return files;
    }

    public List<String> getSaveFileList(){
        return saveFileList;
    }

    public int getFilesSize(){
        return filesSize;
    }

}