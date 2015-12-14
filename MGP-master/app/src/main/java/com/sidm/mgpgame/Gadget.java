package com.sidm.mgpgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class Gadget extends Activity implements OnClickListener {

    private Button btn_back;
    private int cash = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar


        setContentView(R.layout.gadget);

        btn_back = (Button) findViewById(R.id.btn_level2);
        btn_back.setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent = new Intent();

        //go back to menu page
        if (v == btn_back) {
            intent.setClass(this, Homepage.class);
            finish();
        }
        startActivity(intent);
        onDestroy();
    }

    public void update(Canvas canvas)
    {
        // Bonus) To print FPS on the screen
        Paint paint = new Paint();
        paint.setARGB(255, 0, 0, 0);
        paint.setStrokeWidth(100);
        paint.setTextSize(30);
        canvas.drawText("Cash $: " + cash, 130, 75, paint);
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
            intent.setClass(this, Homepage.class);
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
