package com.sidm.mgpgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Paint;
import java.util.Random;

/**
 * Created by 144116C on 11/23/2015.
 */


public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering
    protected  static final String TAG = null;

    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg;

    // 1b) Define Screen width and Screen height as integer
    int ScreenWidth, ScreenHeight;

    // 1c) Variables for defining background start and end point\
    private short bgX, bgY;

    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap[] Spaceship = new Bitmap[4];

    // 4b) Variable as an index to keep track of the spaceship images
    private short SpaceshipIndex = 0;

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;
    Paint paint = new Paint();

    // Variable for Game State check
    private short GameState;

    // Variables for touch events
    private short mX = 0, mY = 0;

    // Variables for something
    int aX, aY;

    // Variables for Drag
    private int score = 0;
    private boolean moveShip = false;

    // Variables for vibrations
    public Vibrator vibrate;
    private int hits = 3;

    long pattern[] = {0, 200, 500};
    // Time to wait before vibrator is ON,
    // Time to keep vibrator ON,
    // Time till vibration is OFF or till vibrator is ON

    public void startVibration()
    {
        long Pattern[] = {0, 200, 500};
        vibrate = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrate.vibrate(pattern, 0); // sets the vibration as: REPEAT. -1 as do not repeat
        Log.v(TAG, "Test");
    }
    public void stopVibrate()
    {
        vibrate.cancel();
    }

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context){

        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        // 1e)load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(),R.drawable.bg_gamescene);
        scaledbg = Bitmap.createScaledBitmap(bg, ScreenWidth, ScreenHeight, true);

        // 4c) Load the images of the spaceships
        Spaceship[0] = bg = BitmapFactory.decodeResource(getResources(),R.drawable.ship2_1);
        Spaceship[1] = bg = BitmapFactory.decodeResource(getResources(),R.drawable.ship2_2);
        Spaceship[2] = bg = BitmapFactory.decodeResource(getResources(),R.drawable.ship2_3);
        Spaceship[3] = bg = BitmapFactory.decodeResource(getResources(),R.drawable.ship2_4);

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder){
        // Create the thread
        if (!myThread.isAlive()){
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder){
        // Destroy the thread
        if (myThread.isAlive()){
            myThread.startRun(false);


        }
        boolean retry = true;
        while (retry) {
            try {
                myThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    public boolean checkCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2)
    {
        if(x2 >= x1 && x2 <= x1 + w1) // Start to detect collision of the top left corner
        {
            if(y2 > y1 && y2 < y1 + h1) //  Comparing yellow box to blue box
            {
                return true;
            }
        }

        if(x2 + w2 >= x1 && x2 + w2 <= x1 + w1) // Start to detect collision of the top right corner
        {
            if(y2 > y1 && y2 < y1 + h1)
            {
                return true;
            }
        }

        if(x2 >= x1 && x2 < x1 + w1) // Start to detect collision of the bottom left
        {
            if(y2 + h2 > y1 && y2 + h2 < y1 + h1)
            {
                return true;
            }
        }

        if(x2 + w2 >= x1 && x2 + w2 < x1 + w1) // Start to detect collision of the bottom right
        {
            if(y2 + h2 > y1 && y2 + h2 < y1 + h1)
            {
                return true;
            }
        }

        return false;
    }

    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if (canvas == null){
            return;
        }
        canvas.drawBitmap(scaledbg, bgX, bgY, null);
        canvas.drawBitmap(scaledbg, bgX + ScreenWidth, bgY, null);
        // 4d) Draw the spaceships
        canvas.drawBitmap(Spaceship[SpaceshipIndex], 100, 100, null);

        // Bonus) To print FPS on the screen
        Paint paint = new Paint();
        paint.setARGB(255,0,0,0);
        paint.setStrokeWidth(100);
        paint.setTextSize(30);
        canvas.drawText("FPS: " + FPS, 130, 75, paint);
    }


    //Update method to update the game play
    public void update(float dt, float fps){
        FPS = fps;

        switch (GameState) {
            case 0: {
                // 3) Update the background to allow panning effect
                bgX -= 500 * dt;

                //wrapping
                if( bgX < - ScreenWidth){
                    bgX = 0;
                }


                // 4e) Update the spaceship images / shipIndex so that the animation will occur.
                SpaceshipIndex++; //edit according to what you need
                SpaceshipIndex %= 4;
            }
            break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas){
        switch (GameState)
        {
            case 0:
                RenderGameplay(canvas);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        // 5) In event of touch on screen, the spaceship will relocate to the point of touch

        int action = event.getAction(); // check for the action of touch

        short X = (short) event.getX();
        short Y = (short) event.getY();

        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
                if(checkCollision(mX, mY, Spaceship[SpaceshipIndex].getWidth(),
                        Spaceship[SpaceshipIndex].getHeight(), X, Y, 0, 0))
                {
                    moveShip = true;
                }
                else
                {
                    moveShip = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(moveShip == true)
                {
                    // New Location where the image lands on
                    mX = (short) (X - Spaceship[SpaceshipIndex].getWidth() / 2);
                    mY = (short) (X - Spaceship[SpaceshipIndex].getHeight() / 2);
                }

                //check if stone and ship collide
               /* if(checkCollision(mX,mY,Spaceship[SpaceshipIndex].getWidth(),
                        Spaceship[SpaceshipIndex].getHeight(),
                        aX,aY,stone_anim.getSpriteWidth(),stone_anim.getSpriteHeight()))
                {
                    Random r = new Random();
                    aX = r.nextInt(ScreenWidth);
                    aY = r.nextInt(ScreenHeight);
                    score += 10;
                    hits -= 1;
                    // if (hits == 3)
                    //  {
                    //  canvas.drawBitmap(score, 28, Screenheight - 700, null);
                    //  canvas.drawBitmap(score, 58, Screenheight - 700, null);
                    //   canvas.drawBitmap(score, 88, Screenheight - 700, null);
                    //  }
                    //  else if(hits == 2)
                    //{
                    //   canvas.drawBitmap(score, 28, Screenheight - 700, null);
                    //    canvas.drawBitmap(score, 58, Screenheight - 700, null);

                    //  }
                    //   else if (hits == 1)
                    //  {
                    //   canvas.drawBitmap(score, 28, Screenheight - 700, null)
                    // }
                    //test for vibrate
                    startVibration();
                }*/
                break;
        }

        return true;
        // return super.onTouchEvent(event);
    }
}

