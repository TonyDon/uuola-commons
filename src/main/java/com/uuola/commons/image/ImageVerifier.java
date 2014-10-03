package com.uuola.commons.image;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.NumberUtil;


public class ImageVerifier {

    protected static Logger logger = LoggerFactory.getLogger(ImageVerifier.class);

//    private static Color[] COLR = { 
//        Color.LIGHT_GRAY, 
//        Color.ORANGE, 
//        Color.WHITE, 
//        Color.GREEN,
//        Color.YELLOW };


 // 输入随机字符串，输出图像流到客户端
    /**
     * 
     * @param w 图片宽
     * @param h 图片高
     * @param pixSize （字体+之间间隔）尺寸
     * @param pixNum 干扰点数量，建议取0
     * @param bMixcolr 干扰点是否使用混色
     * @param bRot 是否偏转字体
     * @param fontColr 字体颜色数组
     * @param bgColr 背景色数组
     * @param bDwLine 是否画直线
     * @param bDwArc 是否画曲线
     * @param vStr 文本值
     * @param font 文本字体类型
     * @param imgMime 输出图片类型
     * @param ostr OutputStream对象
     */
    public static void outputImage(
            int w,
            int h,
            int pixSize,
            int pixNum,
            boolean bMixcolr,
            boolean bRot,
            Color[] fontColr,
            Color[] bgColr,
            boolean bDwLine,
            boolean bDwArc,
            String vStr,
            Font font,
            String imgMime,
            OutputStream ostr) {
        int num = vStr.length();
        BufferedImage bimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bimg.createGraphics();
        // 优化字体
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        Color c1 =  bgColr[genInt(0, bgColr.length)];
        g2d.setColor(c1);
        g2d.setBackground(c1);
        g2d.fillRect(0, 0, w, h);
        g2d.setFont(font);
        BasicStroke stroke = new BasicStroke(genInt(2,5));

        int hh = (h >> 1) + num;
        int ct_f = (pixSize >> 1);
        int mn = (num << 1);
        int ph = (ct_f + num);
        if (bRot) { // 是否倾斜字体
            int cr_x = 0; //记录移动的X坐标
            int cr_v; //增加的x位移
            double rot;
            int ct_y = h >> 1;
            int cc_f = (ct_f >> 1) - 2;
            // 先将坐标移到left-center.
            g2d.translate(num, ct_y);
            for (int k = 0; k < num; k++) {
                g2d.setColor(fontColr[genInt(0, fontColr.length)]);
                rot = Math.toRadians(genInt(-42, 32)); // 倾斜角度范围
                g2d.rotate(rot);
                g2d.drawString(vStr.substring(k, k + 1), cc_f, ct_f - 3);
                // g2d.drawLine(0, 0, pixSize, 0);
                // 复原角度
                g2d.rotate(-rot);
                // 再横向平移
                cr_v = genInt(ct_f, ct_f + num);
                g2d.translate(cr_v, 0);
                cr_x += cr_v;
            }
            //恢复原点
            g2d.translate(-cr_x, -ct_y);
        } else {
            for (int k = 0; k < num; k++) {
                g2d.setColor(fontColr[genInt(0, fontColr.length)]);
                g2d.drawString(vStr.substring(k, k + 1), (k * ph) + mn, genInt(pixSize, h - 2));
            }
        }

        if (pixNum > 16) { //画点
            int xb = num+2;
            int xe = w-xb;
            int yb = (h-pixSize)>>1;
            int ye = h-yb;
            int actualCircleTimes = (int)(pixNum>>2);
            if (bMixcolr) {
                for (int k = 1; k <= actualCircleTimes; k++) {
                    g2d.setColor(fontColr[genInt(0, fontColr.length)]);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                    g2d.setColor(fontColr[genInt(0, fontColr.length)]);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                    g2d.setColor(fontColr[genInt(0, fontColr.length)]);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                    g2d.setColor(fontColr[genInt(0, fontColr.length)]);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                }
            } else {
                g2d.setColor(fontColr[genInt(0, fontColr.length)]);
                for (int k = 1; k <= actualCircleTimes; k++) {
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                }
            }
        }

        if (bDwArc) { // 画三条干扰曲线
            g2d.setColor(fontColr[genInt(0, fontColr.length)]);
            g2d.setStroke(stroke);
            g2d.drawArc(genInt(8, pixSize), genInt(8, hh - pixSize), 60, 10, -180, 90);
            g2d.drawArc(genInt(8, pixSize), genInt(8, hh), 60, 10, 0, 90);
            g2d.drawArc(genInt(8, pixSize), genInt(8, hh), 60, 10, -180, 90);
        }

        if (bDwLine) { //画一条干扰直线
            int k = (h-pixSize)>>1;
            g2d.setColor(fontColr[genInt(0, fontColr.length)]);
            g2d.setStroke(stroke);
            g2d.drawLine(genInt(num, k), genInt(num, h - num), genInt(w-pixSize, w), genInt(k, h));
        }

        try {
            g2d.dispose();
            bimg.flush();
            ImageIO.write(bimg, imgMime, ostr);
        } catch (Exception ex) {
            logger.warn(ex.toString());
        } finally {
            try {
                ostr.flush();
                ostr.close();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
    }


    /**
     * 画干扰点
     * @param g2d
     * @param xb x起始坐标
     * @param xe x结束坐标
     * @param yb y起始坐标
     * @param ye y结束坐标
     */
    private static void drawNoisePoint(Graphics2D g2d, int xb, int xe, int yb, int ye) {
        int x = genInt(xb, xe);
        int y = genInt(yb, ye);
        int z = genInt(-2, 2);
        g2d.drawLine(x, y, x + z, y - z);
    }


    private static int genInt(int i, int j) {
        return NumberUtil.genRndInt(i, j);
    }
}