package com.sidm.mgpgame;

import android.graphics.Bitmap;

/**
 * Created by xStarySkye on 12/14/2015.
 */
public class Objects {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int O_Width;
    private int O_Height;

    public Objects (Bitmap bitmap, int x, int y)
    {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public int setWidth(int O_Width) {
        this.O_Width = O_Width;
        return O_Width;
    }

    public int setHeight(int O_Height) {
        this.O_Height = O_Height;
        return O_Height;
    }

}

