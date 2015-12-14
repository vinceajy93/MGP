package com.sidm.mgpgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class Homepage extends Activity implements OnClickListener {

    private Button btn_shop;
    private Button btn_gadget;
    private Button btn_achievement;
    private Button btn_friend;
    private Button btn_avatar;
    private Button btn_back;

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
    }

    public void onClick(View v) {
        Intent intent = new Intent();

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
        }else if (v == btn_back) {
            intent.setClass(this, Mainmenu.class);
            finish();
        }
        startActivity(intent);
        onDestroy();
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

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        Intent intent = new Intent();
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            intent.setClass(this, Mainmenu.class);
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
