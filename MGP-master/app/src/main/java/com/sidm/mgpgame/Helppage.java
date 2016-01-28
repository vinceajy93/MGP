package com.sidm.mgpgame;

/*
public class Helppage extends FragmentActivity {

    private LoginButton loginBtn;

    private CallbackManager callbackManager;
    private LoginManager loginManager;

    ShareDialog shareDialog;

    FacebookSdk.sdkInitialize(this.

    getApplicationContext()

    );

    callbackManager = CallbackManager.Factory.create();

    List<String> PERMISSIONS = Arrays.asList("publish_actions");

    loginBtn=(LoginButton)

    findViewById(R.id.fb_login_button);

    loginManager=LoginManager.getInstance();
    loginManager.logInWithPublishPermissions(this,PERMISSIONS);
    loginBtn.registerCallback(callbackManager,new FacebookCallback<LoginResult>()

    {

        @Override
        public void onSuccess (LoginResult loginResult){

        sharePhotoToFacebook();

    }


        @Override
        public void onCancel () {
        userName.setText("Login attempt canceled.");
    }

        @Override
        public void onError (FacebookException e){
        userName.setText("Login attempt failed.");

    }
    }

    );

    private void sharePhotoToFacebook() {
        int highscore = 0;

        SharePrefscore = getSharedPreferences("Scoredata", Context.MODE_PRIVATE);

        highscore = SharePrefscore.getInt("Passhighscore", 0);

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("You have played SIDM. Your current Score is " + highscore + ".")
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
}*/