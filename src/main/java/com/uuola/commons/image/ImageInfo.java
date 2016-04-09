package com.uuola.commons.image;


public class ImageInfo {

    /**
     * jpeg , png, gif, bmp
     */
    private String formatName;
    
    private int width;
    
    private int height;
    
    private long size;

    
    public String getFormatName() {
        return formatName;
    }

    
    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }


    public int getWidth() {
        return width;
    }

    
    public void setWidth(int width) {
        this.width = width;
    }

    
    public int getHeight() {
        return height;
    }

    
    public void setHeight(int height) {
        this.height = height;
    }

    
    public long getSize() {
        return size;
    }

    
    public void setSize(long size) {
        this.size = size;
    }


    @Override
    public String toString() {
        return "ImageInfo [formatName=" + formatName + ", width=" + width + ", height=" + height + ", size=" + size
                + "]";
    }
    
    
}
