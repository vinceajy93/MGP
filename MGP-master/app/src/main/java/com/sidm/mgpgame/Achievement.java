package com.sidm.mgpgame;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Achievement extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar, fullscreen

        setContentView(R.layout.scorepage);//.xml


        TextView scoreText;
        scoreText = (TextView)findViewById(R.id.scoreText);

        Bundle bundle = getIntent().getExtras();
        String score = bundle.getString("highscore");

        scoreText.setText(score);

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


