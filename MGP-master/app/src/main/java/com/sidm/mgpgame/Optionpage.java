package com.sidm.mgpgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


/**
 * Created by Vincent's PC on 27/11/2015.
 */
public class Optionpage extends Activity implements View.OnClickListener {

    private Button btn_back;
    private GamePanelSurfaceView panelview;

    //vibration is on by default
    public static boolean isVibrationOn = true;

    //declare context
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar


        setContentView(R.layout.options);

        btn_back = (Button) findViewById(R.id.btn_level2);
        btn_back.setOnClickListener(this);

        //for vibration switch
        Switch sw_vibrate = (Switch) findViewById(R.id.switch_vibrate);


        //attach a listener to check for state change
        sw_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //System.out.println("Switch is currently ON");
                    panelview.startVibration();
                } else {
                    //System.out.println("Switch is currently OFF");
                    panelview.stopVibrate();
                }
            }


        });

    }

    public void onClick(View v) {
        Intent intent = new Intent();

        if (v == btn_back) {
            intent.setClass(this, Mainmenu.class);
            finish();
        }
        startActivity(intent);
        onDestroy();
    }


    //for use with other java classes to set the vibration
    public static boolean GetVibrationChanged() {
        return isVibrationOn;
    }

    public static void SetVibrationChanged(boolean bool) {
        isVibrationOn = bool;
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intent.setClass(this, Mainmenu.class);
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }
}

