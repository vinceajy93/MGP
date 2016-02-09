package com.sidm.mgpgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class Homepage extends Activity implements OnClickListener, SensorEventListener {

    private Button btn_shop;
    private Button btn_gadget;
    private Button btn_achievement;
    private Button btn_friend;
    private Button btn_avatar;
    private Button btn_back;

    SharedPreferences SharePrefScore;
    SharedPreferences SharePrefName;
    AlertDialog.Builder alert_score = null;

    //private final SensorManager sensor;

    //for fun with accelerometer sprite
    private SpriteAnimation forFun_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar


        setContentView(R.layout.homepage);

        btn_shop = (Button) findViewById(R.id.btn_shop);
        btn_shop.setOnClickListener(this);

        btn_gadget = (Button) findViewById(R.id.btn_gadget);
        btn_gadget.setOnClickListener(this);

        btn_achievement = (Button) findViewById(R.id.btn_achievement);
        btn_achievement.setOnClickListener(this);

        btn_friend = (Button) findViewById(R.id.btn_friend);
        btn_friend.setOnClickListener(this);

        btn_avatar = (Button) findViewById(R.id.btn_avatar);
        btn_avatar.setOnClickListener(this);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        forFun_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.forfun), 362, 200, 6, 6);
//        sensor = (SensorManager)
//                getContext().getSystemService(Context.SENSOR_SERVICE);
//        sensor.registerListener(this,
//                sensor.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),
//                SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void onClick(View v) {
        Intent intent = new Intent();
        alertDialog_highscore();

        if (v != btn_achievement) {
            if (v == btn_shop) {
                intent.setClass(this, Shop.class);
                finish();
            } else if (v == btn_gadget) {
                intent.setClass(this, Gadget.class);
                finish();
            } else if (v == btn_achievement) {
                intent.setClass(this, Achievement.class);
                finish();
            } else if (v == btn_friend) {
                intent.setClass(this, Friend.class);
                finish();
            } else if (v == btn_avatar) {
                intent.setClass(this, Avatar.class);
                finish();
            } else if (v == btn_back) {
                intent.setClass(this, Mainmenu.class);
                finish();
            }
            startActivity(intent);
        } else {
            loadHighscore();
            alert_score.show();
        }
    }

    public void loadHighscore() {
        int highscore = 0;

        // Load Shared Preferences
        SharePrefScore =
                getSharedPreferences("Scoredata", Context.MODE_PRIVATE);

        highscore = SharePrefScore.getInt("KeyHighscore", 0);

        //load text, "Highscore: " is just a title
        Toast.makeText(this, "Highscore: " + highscore, Toast.LENGTH_LONG).show();
    }

    public void alertDialog_highscore() {
        int highscore = 0;
        String PlayerName;

        // Load Shared Preferences
        SharePrefScore = getSharedPreferences("Scoredata", Context.MODE_PRIVATE);
        highscore = SharePrefScore.getInt("KeyHighscore", 0);

        SharePrefName = getSharedPreferences("playerName", Context.MODE_PRIVATE);
        PlayerName = SharePrefName.getString("KeyPlayerName", "DEFAULT");

        alert_score = new AlertDialog.Builder(this);
        alert_score.setCancelable(false);
        alert_score.setIcon(R.drawable.button_achievements);
        alert_score.setTitle("Your Highscore: ").setMessage(PlayerName + ": " +
                highscore).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            // do something when the button is clicked

            public void onClick(DialogInterface arg0, int arg1) {

            }

        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public void onSensorChanged(SensorEvent SenseEvent) {
        // Many sensors return 3 values, one for each axis.
        // Do something with this sensor value.
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
