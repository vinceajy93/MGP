package com.sidm.mgpgame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by vincent's on 8/12/2015.
 */

public class SpriteAnimation extends Activity{

    private Bitmap bitmap; // the animation sequence
    private Rect sourceRect; // the rectangle to be drawn from the animation bitmap
    private int frame; // number of frames in animation
    private int currentFrame; // the current frame
    private long frameTicker; // the time of the last frame update
    private int framePeriod; // milliseconds between each frame (1000/fps)
    private int spriteWidth; // the width of the sprite to calculate the cut out rectangle
    private int spriteHeight; // the height of the sprite
    private int x; // the X coordinate of the object (top left of the image)
    private int y; // the Y coordinate of the object (top left of the image)
    private float fx; // float coord of object for x
    private float fy;


    public SpriteAnimation(Bitmap bitmap, int x, int y, int fps, int frameCount)
    {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.fx = x;
        this.fy = y;

        currentFrame = 0;

        frame = frameCount;

        spriteWidth = bitmap.getWidth() / frameCount; // Assumed that each frame has the same width
        spriteHeight = bitmap.getHeight();
        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
        framePeriod = 1000 / fps;
        frameTicker = 01;

    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap= bitmap;
    }

    public Rect getSourceRect()
    {
        return sourceRect;
    }

    public void setSourceRect(Rect sourceRect)
    {
        this.sourceRect = sourceRect;
    }

    public int getFrame()
    {
        return frame;
    }

    public void setFrame(int frame)
    {
        this.frame = frame;
    }
    public int getCurrentFrame()
    {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame)
    {
        this.currentFrame = currentFrame;
    }

    public int getFramePeriod()
    {
        return framePeriod;
    }

    public void setFramePeriod(int framePeriod)
    {
        this.framePeriod= framePeriod;
    }

    public int getSpriteWidth()
    {
        return spriteWidth;
    }
    public void setSpriteWidth(int spriteWidth)
    {
        this.spriteWidth= spriteWidth;
    }

    public int getSpriteHeight()
    {
        return spriteHeight;
    }

    public void setSpriteHeight(int spriteHeight)
    {
        this.spriteHeight= spriteHeight;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x= x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y= y;
    }

//    public void setY(float y) {this.fy = y;}
//
//    public void SetX(float x) {this.fx = x;}

    public void update(long gameTime)       // gameTime= system time from the thread  which is running
    {
        if (gameTime > frameTicker + framePeriod) {
            frameTicker = gameTime;
            currentFrame++;// increment the frame

            if(currentFrame >= frame) //frame = total no, of frames and currentframe starts from 0
            {
                currentFrame = 0;  //reached end of frame, reset to 0
            }
        }

        //define the rectangle to cut out sprite
        this.sourceRect.left = currentFrame * spriteWidth;
        this.sourceRect.right = this.sourceRect.left + spriteWidth;

    }

    public void draw(Canvas canvas)
    {
    // Image of each frame is defined by sourceRect // destRectis the area defined for the image of each frame to be drawn
        Rect destRect= new Rect(getX(), getY(), getX() + spriteWidth, getY() + spriteHeight);
        canvas.drawBitmap(bitmap, sourceRect, destRect, null);
    }


    //pause
    protected void onPause() {
        super.onPause();
    }

    //stop application
    protected void onStop() {
        super.onStop();
    }

    //destroy when not in use
    protected void onDestroy() {
        super.onDestroy();
    }
}
