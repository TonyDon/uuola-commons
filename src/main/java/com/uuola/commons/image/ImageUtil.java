/*
 * @(#)ImageUtil.java 2013-11-3
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.file.FileUtil;


/**
 * <pre>
 * 图片压缩剪裁
 * @author tangxiaodong
 * 创建日期: 2013-11-3
 * </pre>
 */
public class ImageUtil {

    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 图片水印
     *
     * @param srcImg 原图片
     * @param markImg 标志图片文件对象
     * @param alpha 透明度,默认在右下角
     */
    public static void pressImage(File srcImg, File markImg, float alpha) {
        BufferedImage src, markImage;
        try {
            src = ImageIO.read(srcImg);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            //image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = src.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            // 水印文件
            markImage = ImageIO.read(markImg);
            int wb = markImage.getWidth(null);
            int hb = markImage.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(markImage, (width - wb), (height - hb), wb, hb, null);
            g.dispose();
            ImageIO.write(src, FileUtil.getFileExt(srcImg.getCanonicalPath()), srcImg);
            src.flush();
            markImage.flush();
        } catch (Exception ex) {
            logger.error(".pressImage() error. ", ex);
        }
    }
    
    /**
     * 文字水印
     *
     * @param srcImg 目标图片
     * @param pressText 水印文字
     * @param font 字体
     * @param color 字体颜色
     * @param x 修正值 相对中间位置偏移的x量，大于0向右偏移，反之向左
     * @param y 修正值 相对底部位置的抬升y量, 小于0向上偏移，反之向下
     * @param alpha 透明度
     */
    public static void pressText(
            File srcImg,
            String pressText,
            Font font,
            Color color,
            int x, int y, float alpha) {
        BufferedImage src ; //, image;
        Graphics2D g;
        int fontSize = font.getSize();
        try {
            src = ImageIO.read(srcImg);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            //image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            g = src.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(font);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawString(pressText, (width - ((pressText.length()) * fontSize)) / 2 + x, (height - fontSize) + y);
            g.dispose();
            ImageIO.write(src, FileUtil.getFileExt(srcImg.getCanonicalPath()), srcImg);
            src.flush();
        } catch (IOException ex) {
            logger.error(".pressText() error. ", ex);
        }
    }
    
    /*
     * 图像场景预处理
     * @param srcImg 图像源文件对象
     * @param width 新场景宽度
     * @param height 新场景高度
     * @param scaleType 创建场景取样类型 见Image字段
     */

    public static BufferedImage makeThumbSnap(BufferedImage srcImg, int width, int height) {
        int biType = srcImg.getType();
        BufferedImage tag;
        if (biType == BufferedImage.TYPE_CUSTOM) {
            tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        } else {
            ColorModel dstCM = srcImg.getColorModel();
            tag = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(width, height), dstCM.isAlphaPremultiplied(), null);
        }
        Graphics2D g = tag.createGraphics();
        g.drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        g.dispose();
        return tag;
    }

    public static BufferedImage makeThumbSnap(BufferedImage src, double rate) {
        int w = (int) (src.getWidth() * rate);
        int h = (int) (src.getHeight() * rate);
        return makeThumbSnap(src, w, h);
    }
    
 
    /*
     *  JPEG 压缩质量控制
     */
    private static void jpegHandle(BufferedImage image, File distImg) throws Exception{
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
            ImageWriter writer = iter.next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(distImg);
            writer.setOutput(ios);
            //ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
            ImageWriteParam iwparam = writer.getDefaultWriteParam();
            iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwparam.setCompressionQuality(0.91F);
            iwparam.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
            writer.write(null, new IIOImage(image, null, null), iwparam);
            ios.flush();
            writer.dispose();
            ios.close();
    }
    
    /**
     * 图片缩放函数 在原图上进行缩放 是否产生新缩放图 由newImg是否为null决定,非空则缩图为新文件.
     * @param srcImg 原图
     * @param newImg 新图
     * @param width 目标宽度, 如果为0, 则按height/原图高度, 比率进行缩放
     * @param height 目标高度, 如果为0，则按width/原图宽度，比进行缩放
     * @param isblank 是否补白, 如果是按宽或高缩小，此处设置false
     * @return
     * @throws IOException 
     */
    public static boolean resize(File srcImg, File newImg, int width, int height, boolean isblank) {
        if (!srcImg.exists() || !srcImg.canRead() || (width == 0 && height == 0)) {
            return false;
        }
        boolean isCoverSrcImage = newImg == null ;
        File np = isCoverSrcImage ? srcImg : newImg;
        BufferedImage bi;
        BufferedImage itemp;
        int ow, oh, x, y;
        // AffineTransformOp op = null;
        double ratio = 0; // 缩放比例
        boolean flag = true;
        
        try {
            bi = ImageIO.read(srcImg);
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (width != 0 && height != 0) {
                    // 等比缩小
                    if (bi.getHeight() > bi.getWidth()) {
                        ratio = ((double) height) / bi.getHeight();
                    } else {
                        ratio = ((double) width) / bi.getWidth();
                    }
                } else if (width != 0) {
                    // 等宽缩小
                    ratio = ((double) width) / bi.getWidth();
                    height = bi.getHeight();
                } else if (height != 0) {
                    // 等高缩小
                    ratio = ((double) height) / bi.getHeight();
                    width = bi.getWidth();
                }
            }

            // 比缩放长宽
            if (ratio < 1 && ratio > 0) {
                itemp = ImageUtil.makeThumbSnap(bi, ratio);
            } else {
                // 比缩放长宽都要小或者相等
                itemp = bi;
            }

            BufferedImage image = itemp;
            // 以下为补白居中处理
            if (isblank) {
                ow = width;
                oh = height;
                x = (width - itemp.getWidth()) >> 1;
                y = (height - itemp.getHeight()) >> 1;
                image = new BufferedImage(ow, oh, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setBackground(Color.white);
                g.setColor(Color.white);
                g.fillRect(0, 0, ow, oh);
                g.drawImage(itemp, x, y, itemp.getWidth(null), itemp.getHeight(null), null);
                g.dispose();
            }

            String ext = FileUtil.getFileExt(srcImg.getCanonicalPath());
            // 覆盖原始文件
            if(isCoverSrcImage){
                srcImg.delete(); 
            }
            // 单独处理 jpeg 图片
            if ("jpegjpg".contains(ext.toLowerCase())) {
                ImageUtil.jpegHandle(image, np);
            } else {
                // 处理PNG GIF BMP
                ImageIO.write(image, ext, np);
            }
            image.flush();
        } catch (Exception ex) {
            logger.error(".resize() error. ", ex);
            flag = false;
        }
        return flag;
    }

    // 补白居中等比缩图
    public static boolean resize(File srcImg, File newImg, int width, int height) {
        return ImageUtil.resize(srcImg, newImg, width, height, true);
    }
    
    /*
     * 切图方法
     * @param image 原始Image对象
     * @param subBounds 切图选区方形对象
     * @param subFile 切图输出文件对象
     * @param formatName 图片后缀类型
     */
    public static boolean saveSubImg(BufferedImage image, Rectangle subBounds, File subFile, String formatName) {
        BufferedImage subimg = new BufferedImage(subBounds.width, subBounds.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = subimg.createGraphics();
        boolean flag = true;

        int left = subBounds.x;
        int top = subBounds.y;
        int uleft = Math.abs(left);
        int utop = Math.abs(top);
        int imgW = image.getWidth();
        int imgH = image.getHeight();
        int sW = subBounds.width;
        int sH = subBounds.height;
        try {
            if (left >= 0 && top >= 0 && imgW - left >= sW && imgH - top >= sH) {
                g.drawImage(image.getSubimage(left, top, sW, sH), 0, 0, null);
            } else if (left >= 0 && top >= 0 && ((imgW - left) < sW || (imgH - top) < sH)) {
                g.setBackground(Color.white);
                g.fillRect(0, 0, sW, sH);// 填充矩形场景
                g.drawImage(image, -left, -top, Color.white, null);
            } else if (left < 0 || top < 0) {
                if (sW > imgW) {
                    if ((sW - imgW) > uleft) {
                        left = (sW - imgW) >> 1;
                    } else {
                        left = uleft;
                    }
                } else if (left < 0) {
                    left = uleft;
                } else {
                    left = (-left);
                }
                if (sH > imgH) {
                    if ((sH - imgH) > utop) {
                        top = (sH - imgH) >> 1;
                    } else {
                        top = utop;
                    }
                } else if (top < 0) {
                    top = utop;
                } else {
                    top = (-top);
                }
                g.setBackground(Color.white);
                g.fillRect(0, 0, sW, sH);// 填充矩形场景
                g.drawImage(image, left, top, Color.white, null);
            }
            g.dispose();
            if ("jpegjpg".contains(formatName.toLowerCase())) {
                ImageUtil.jpegHandle(subimg, subFile);
            } else {
                ImageIO.write(subimg, formatName, subFile);
            }
            subimg.flush();
        } catch (Exception e) {
            logger.error(".saveSubImg() error. ", e);
            flag = false;
        }
        return flag;
    }
 
    /*
     *  切图实现 
     * @param srcFile 源图片文件对象
     * @param distFile 分发图片文件对象
     * @param width 原图片新场景宽
     * @param height 原图片新场景高
     * @param cutLeft 选框左边离新场景图片左边边距
     * @param cutTop 选框顶部离新场景图片顶部边距
     * @param dropWidth 选框宽度
     * @param dropHeight 选框高度
     */
    public static boolean cutImage(File srcFile, File distFile, int width, int height, int cutLeft, int cutTop,
            int dropWidth, int dropHeight) {
        boolean flag = true;
        try {
            flag = ImageUtil.saveSubImg(
                    ImageUtil.makeThumbSnap(ImageIO.read(srcFile), width, height), 
                    new Rectangle(cutLeft, cutTop, dropWidth, dropHeight), 
                    distFile, 
                    FileUtil.getFileExt(srcFile.getName()));
        } catch (Exception e) {
            logger.error(".cutImage() error. ", e);
            flag = false;
        }
        return flag;
    }
    
    /**
     * 探测图片文件信息，如类型，长宽，大小
     * @param img
     * @return
     */
    public static ImageInfo detect(File img){
        if(null == img || !img.exists() || !img.isFile() || !img.canRead()){
            return null;
        }
        ImageInfo info = new ImageInfo();
        info.setSize(img.length());
        ImageInputStream iis = null;
        try {
            iis = ImageIO.createImageInputStream(img);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                logger.error(".detect() error. No readers found! file:" + img.getAbsolutePath());
            }else{
                ImageReader ird = iter.next();
                ird.setInput(iis, true);
                info.setWidth(ird.getWidth(0));
                info.setHeight(ird.getHeight(0));
                info.setFormatName(ird.getFormatName());
                if(null != info.getFormatName()){
                    info.setFormatName(info.getFormatName().toLowerCase());
                }
            }
        } catch (IOException e) {
            logger.error(".detect() error. ", e);
        } finally{
            IOUtils.closeQuietly(iis);
        }
        return info;
    }
}
