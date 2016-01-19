package com.uuola.commons.image.checkcode;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.NumberUtil;


public class ImageVerifier {

    protected static Logger logger = LoggerFactory.getLogger(ImageVerifier.class);
    
    /**
     * 传入参数对象，输出验证码数据流到params.OutputStream
     * @param params
     */
    public static void outputImage(ImageCodeParams params) {
        String text= params.getText();
        int num = text.length();
        int fontColrLen = params.getFontColors().length;
        Color[] fontColors = params.getFontColors();
        int charBoxSize = params.getCharBoxSize();
        int width = params.getWidth();
        int height = params.getHeight();
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bimg.createGraphics();
        // 优化字体
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        
        // 取一个背景色
        Color bgColr =  params.getBackgroundColors()[genInt(0, params.getBackgroundColors().length)];
        g2d.setColor(bgColr);
        g2d.setBackground(bgColr);
        g2d.fillRect(0, 0, width, height);
        g2d.setFont(params.getFont());
        BasicStroke stroke = new BasicStroke(genInt(2,5));

        
        int ct_f = (charBoxSize >> 1);
        if (params.isRotate()) { // 是否倾斜字体
            int cr_x = 0; //记录移动的X坐标
            int cr_v; //增加的x位移
            double rot;
            int ct_y = height >> 1;
            int cc_f = (ct_f >> 1) - 2;
            // 先将坐标移到left-center.
            g2d.translate(num, ct_y);
            for (int k = 0; k < num; k++) {
                g2d.setColor(fontColors[genInt(0, fontColrLen)]);
                rot = Math.toRadians(genInt(-42, 32)); // 倾斜角度范围
                g2d.rotate(rot);
                g2d.drawString(text.substring(k, k + 1), cc_f, ct_f - 3);
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
            int ph = (ct_f + num);
            int mn = (num << 1);
            for (int k = 0; k < num; k++) {
                g2d.setColor(fontColors[genInt(0, fontColrLen)]);
                g2d.drawString(text.substring(k, k + 1), (k * ph) + mn, genInt(charBoxSize, height - 2));
            }
        }

        if (params.getPointNum() > 16) { //画点
            int xb = num+2;
            int xe = width-xb;
            int yb = (height-charBoxSize)>>1;
            int ye = height-yb;
            int actualCircleTimes = (params.getPointNum()>>2);
            if (params.isMixedColor()) {
                for (int k = 1; k <= actualCircleTimes; k++) {
                    g2d.setColor(fontColors[genInt(0, fontColrLen)]);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                }
            } else {
                g2d.setColor(fontColors[genInt(0, fontColrLen)]);
                for (int k = 1; k <= actualCircleTimes; k++) {
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                    drawNoisePoint(g2d, xb, xe, yb, ye);
                }
            }
        }

        if (params.isDrawArc()) { // 画三条干扰曲线
            int hh = (height >> 1) + num;
            g2d.setColor(fontColors[genInt(0, fontColrLen)]);
            g2d.setStroke(stroke);
            g2d.drawArc(genInt(8, charBoxSize), genInt(8, hh - charBoxSize), 60, 10, -180, 90);
            g2d.drawArc(genInt(8, charBoxSize), genInt(8, hh), 60, 10, 0, 90);
            g2d.drawArc(genInt(8, charBoxSize), genInt(8, hh), 60, 10, -180, 90);
        }

        if (params.isDrawLine()) { //画一条干扰直线
            int k = (height-charBoxSize)>>1;
            g2d.setColor(fontColors[genInt(0, fontColrLen)]);
            g2d.setStroke(stroke);
            g2d.drawLine(genInt(num, k), genInt(num, height - num), genInt(width-charBoxSize, width), genInt(k, height));
        }

        try {
            g2d.dispose();
            bimg.flush();
            ImageIO.write(bimg, params.getImgMime(), params.getOutputStream());
        } catch (Exception ex) {
            logger.warn(ex.toString());
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