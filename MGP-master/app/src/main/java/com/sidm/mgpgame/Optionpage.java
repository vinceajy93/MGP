package com.sidm.mgpgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


/**
 * Created by Vincent's PC on 27/11/2015.
 */
public class Optionpage extends Activity implements View.OnClickListener {

    private Button btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar


        setContentView(R.layout.options);

        btn_back = (Button) findViewById(R.id.btn_level2);
        btn_back.setOnClickListener(this);


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

