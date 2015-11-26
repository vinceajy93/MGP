package com.sidm.mgpgame;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;

import android.widget.Button;
/**
 * Created by 144116C on 11/23/2015.
 */
public class Gamepage extends Activity{

    /*Called when the activity is first created */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title

        //making it fullscreen window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar


        //set our GamePanelSufaceview as the view

        setContentView(new GamePanelSurfaceView(this));

    }


    //pause
    protected void onPause() {
        super.onPause();
    }

    //stop application
    protected void onStop() {
        super.onPause();
    }

    //destroy when not in use
    protected void onDestroy() {
        super.onPause();
    }
}
