package edu.uofu.cs4862.collage;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

/**
 * Created by Andres on 11/5/13.
 */
public class ImageData {
    private static final int THUMBNAIL_WIDTH = 100;
    private static final int THUMBNAIL_HEIGHT = 100;
    private final Bitmap image;
    private Bitmap thumbnail;
    private int xPos;
    private int yPos;
    private String date;

    public ImageData(Bitmap image, String date){
        this.image = image;
        this.date = date;
    }

    public Bitmap getImage() {
        return image;
    }

    public Bitmap getThumbnail() {
        if (thumbnail != null){
            return thumbnail;
        }
        thumbnail = ThumbnailUtils.extractThumbnail(image, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
