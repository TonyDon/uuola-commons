/*
 * @(#)CodeParams.java 2016年1月19日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.commons.image.checkcode;

import java.awt.Color;
import java.awt.Font;
import java.io.OutputStream;


/**
 * <pre>
 * 验证码参数设置
 * @author tangxiaodong
 * 创建日期: 2016年1月19日
 * </pre>
 */
public class ImageCodeParams {
    // 图片宽
    private int width;
    
    //图片高度
    private int height;
    
    // 字符区域尺寸：（字体+之间间隔）尺寸
    private int charBoxSize;
    
    // 是否偏转字体 默认不偏转
    private boolean isRotate = false;
    
    // 干扰点是否使用混色 默认不使用
    private boolean isMixedColor = false;
    
    // 是否画干扰直线
    private boolean drawLine = false;
    
    // 是否画弧线
    private boolean drawArc = false;
    
    // 点数量
    private int pointNum = 0;
    
    // 画的文本内容
    private String text ;
    
    //默认输出图片类型
    private String imgMime = "png" ;
    
    // 字体设置
    private Font font;
    
    // 字体颜色数组
    private Color[] fontColors;
    
    // 背景颜色数组
    private Color[] backgroundColors;
    
    // 输出流对象
    private OutputStream outputStream;
    
    public ImageCodeParams(String text){
        this.text = text;
    }

    
    public int getWidth() {
        return width;
    }

    
    public ImageCodeParams setWidth(int width) {
        this.width = width;
        return this;
    }

    
    public int getHeight() {
        return height;
    }

    
    public ImageCodeParams setHeight(int height) {
        this.height = height;
        return this;
    }

    
    public int getCharBoxSize() {
        return charBoxSize;
    }

    
    public ImageCodeParams setCharBoxSize(int charBoxSize) {
        this.charBoxSize = charBoxSize;
        return this;
    }

    
    public boolean isRotate() {
        return isRotate;
    }

    
    public ImageCodeParams setRotate(boolean isRotate) {
        this.isRotate = isRotate;
        return this;
    }

    
    public boolean isMixedColor() {
        return isMixedColor;
    }

    
    public ImageCodeParams setMixedColor(boolean isMixedColor) {
        this.isMixedColor = isMixedColor;
        return this;
    }

    
    public boolean isDrawLine() {
        return drawLine;
    }

    
    public ImageCodeParams setDrawLine(boolean drawLine) {
        this.drawLine = drawLine;
        return this;
    }

    
    public boolean isDrawArc() {
        return drawArc;
    }

    
    public ImageCodeParams setDrawArc(boolean drawArc) {
        this.drawArc = drawArc;
        return this;
    }

    
    public int getPointNum() {
        return pointNum;
    }

    
    public ImageCodeParams setPointNum(int pointNum) {
        this.pointNum = pointNum;
        return this;
    }

    
    public String getText() {
        return text;
    }

    
    public void setText(String text) {
        this.text = text;
    }

    
    public String getImgMime() {
        return imgMime;
    }

    
    public ImageCodeParams setImgMime(String imgMime) {
        this.imgMime = imgMime;
        return this;
    }

    
    public Font getFont() {
        return font;
    }

    
    public ImageCodeParams setFont(Font font) {
        this.font = font;
        return this;
    }

    
    public Color[] getFontColors() {
        return fontColors;
    }

    
    public ImageCodeParams setFontColors(Color[] fontColors) {
        this.fontColors = fontColors;
        return this;
    }

    
    public Color[] getBackgroundColors() {
        return backgroundColors;
    }

    
    public ImageCodeParams setBackgroundColors(Color[] backgroundColors) {
        this.backgroundColors = backgroundColors;
        return this;
    }

    
    public OutputStream getOutputStream() {
        return outputStream;
    }

    
    public ImageCodeParams setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        return this;
    }
}
