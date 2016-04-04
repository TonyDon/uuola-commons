/*
 * @(#)ByteUtil.java 2015年11月26日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2015年11月26日
 * </pre>
 */
public abstract class ByteUtil {
    
    public static final int BUFF_SIZE = 1024*32;

    /**
     * 将inputStream 转换到字节缓存对象中
     * @param is
     * @param maxSize 最大读取尺寸限制
     * @return
     * @throws IOException
     */
    public static ByteBuffer readToByteBuffer(InputStream is, int maxSize) throws IOException {
        final boolean capped = maxSize > 0;
        byte[] buffer = new byte[BUFF_SIZE];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(BUFF_SIZE);
        int read;
        int remaining = maxSize;
        for(;;) {
            read = is.read(buffer);
            if (read == -1) break;
            if (capped) {
                if (read > remaining) {
                    outStream.write(buffer, 0, remaining);
                    break;
                }
                remaining -= read;
            }
            outStream.write(buffer, 0, read);
        }
        ByteBuffer byteData = ByteBuffer.wrap(outStream.toByteArray());
        return byteData;
    }
}
