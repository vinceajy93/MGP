package com.sidm.mgpgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;


public class Splashpage extends Activity {

    protected boolean _active = true;
    protected int _splashTime = 5000; // in mili seconds

    private Bitmap splash;
    //bitmap array to stores 9 images of the loading icon
   private Bitmap[] Loading_Icon = new Bitmap[8];

    //Variable as an index to keep track of the loading icon images
    private short Loading_IconIndex = 0;


    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if (canvas == null){
            return;
        }
        // 4d) Draw the loading icons
        canvas.drawBitmap(Loading_Icon[Loading_IconIndex], 100, 100, null);

        // Bonus) To print FPS on the screen
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas){
        RenderGameplay(canvas);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar


        setContentView(R.layout.splashpage);

        //Load the images of the loading icons
       /* Loading_Icon[0]  = BitmapFactory.decodeResource(getResources(),R.drawable.loading_1);
        Loading_Icon[1]  = BitmapFactory.decodeResource(getResources(),R.drawable.loading_2);
        Loading_Icon[2]  = BitmapFactory.decodeResource(getResources(),R.drawable.loading_3);
        Loading_Icon[3]  = BitmapFactory.decodeResource(getResources(),R.drawable.loading_4);
        Loading_Icon[5]  = BitmapFactory.decodeResource(getResources(),R.drawable.loading_5);
        Loading_Icon[4]  = BitmapFactory.decodeResource(getResources(),R.drawable.loading_6);
        Loading_Icon[6]  = BitmapFactory.decodeResource(getResources(),R.drawable.loading_7);
        Loading_Icon[7]  = BitmapFactory.decodeResource(getResources(),R.drawable.loading_8);*/



        //thread for displaying the Splash Screen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(200);
                        if (_active) {
                            waited += 200;
                        }
                    }
                } catch (InterruptedException e) {
                    //do nothing
                } finally {
                    finish();

                    // Add codes
                    Intent intent = new Intent(Splashpage.this, Mainmenu.class);

                    startActivity(intent);
                }
            }
        };


        splashTread.start();
    }

    public boolean OnTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            _active = false;
        }
        return true;
    }
}
