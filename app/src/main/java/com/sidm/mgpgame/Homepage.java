package com.sidm.mgpgame;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class Homepage extends Activity implements OnClickListener {

    private Button btn_shop;
    private Button btn_gadget;
    private Button btn_achievement;
    private Button btn_friend;
    private Button btn_avatar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
    }

    public void onClick(View v) {
        Intent intent = new Intent();

        if (v == btn_shop) {
            intent.setClass(this, Shop.class);
        } else if (v == btn_gadget) {
            intent.setClass(this, Gadget.class);
        } else if (v == btn_achievement) {
            intent.setClass(this, Achievement.class);
        } else if (v == btn_friend) {
            intent.setClass(this, Friend.class);
        } else if (v == btn_avatar) {
            intent.setClass(this, Avatar.class);
        }

        startActivity(intent);
    }

    //pause
    protected void onPause() {
        super.onPause();
    }

    //stop application
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Homepage Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.sidm.mgpgame/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    //destroy when not in use
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Homepage Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.sidm.mgpgame/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
}
