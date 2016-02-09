package com.sidm.mgpgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;


public class Friend extends Activity implements OnClickListener {

    private Button btn_back;
    private Button btn_FB;

    private LoginButton loginBtn;

    private CallbackManager callbackManager;
    private LoginManager loginManager;

    private TextView userName;
    private ShareDialog shareDialog;


// loginBtn = (LoginButton) findViewById(R.id.);
   // loginManager = LoginManager.getInstance();
   // loginManager.logInWithPublishPermissions(this, PERMISSIONS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        // initialize the sdk before executing any other operations,
        //especially, if you're using facebook UI elements
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        List<String> PERMISSIONS = Arrays.asList("publish_actions");

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar


        setContentView(R.layout.friend);

        userName = (TextView) findViewById(R.id.user_name);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);

        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(this, PERMISSIONS);
        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                userName.setText("Login successful");
                ProfilePictureView profilePictureView;
                profilePictureView = (ProfilePictureView) findViewById(R.id.picture);
                profilePictureView.setProfileId(loginResult.getAccessToken().getUserId());
                sharePhotoToFacebook();

            }


            @Override
            public void onCancel() {
                userName.setText("Login attempt canceled.");
                ProfilePictureView profilePictureView;
                profilePictureView = (ProfilePictureView) findViewById(R.id.picture);
               //profilePictureView.setProfileId();
            }

            @Override
            public void onError(FacebookException e) {
                userName.setText("Login attempt failed.");

            }
        });

        btn_back = (Button) findViewById(R.id.btn_level2);
        btn_back.setOnClickListener(this);

        btn_FB = (Button) findViewById(R.id.btn_FB);
        btn_FB.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                sharePhotoToFacebook();
            }
        });
    }

    public void onClick(View v) {
        Intent intent = new Intent();

        if (v == btn_back) {
            intent.setClass(this, Homepage.class);
            finish();
        }
        startActivity(intent);
        onDestroy();
    }

    private void sharePhotoToFacebook(){
        int highscore = 0;

       // SharePrefscore = getSharedPreferences("Scoredata", Context.MODE_PRIVATE);

       // highscore = SharePrefscore.getInt("Passhighscore", 0);

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("You have played MGPGame. Your current Score is " + highscore + ".")
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
