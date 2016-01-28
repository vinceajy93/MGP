package com.sidm.mgpgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Paint;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


import java.sql.SQLSyntaxErrorException;
import java.util.Random;

/**
 * Created by 144116C on 11/23/2015.
 */


public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering
    protected static final String TAG = null;

    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg;

    // 1b) Define Screen width and Screen height as integer
    int ScreenWidth, ScreenHeight;

    // 1c) Variables for defining background start and end point\
    private short bgX, bgY;

    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap[] Spaceship = new Bitmap[4];

    //bitmap array to store 2 images of the health(0 - gray 1 - color)
    private Bitmap[] Hearts = new Bitmap[2];

    // 4b) Variable as an index to keep track of the spaceship images
    // Variables for FPS
    public float FPS;

    // Variable for Game State check
    enum GameState {
        Play,
        Gameover,
        Win
    }

    ;

    GameState state = GameState.Play;

    // Variables for touch events
    private short mX = 0, mY = 0;

    // Variables for animation
    int aX, aY;

    // Variables for Drag
    private int score = 0;

    // Variables for vibrations
    public Vibrator vibrate;

    //variables for gameplay
    private int health = 3;

    //variable for invulnerability tiem
    private boolean isHit = false;
    protected int invunTime = 50; //in milli seconds

    //Variables for vibration switch button
    private Switch Sw_vibrate;
    private boolean vibrate_on;

    //Variable to print cash at hand
    //private TextView handCash;
    private int cash = 1000;

    //char sprites
    //private SpriteAnimation robot_anim;
    private SpriteAnimation android_anim;
    private int translateplayerY; //for jumping
    boolean isJump = false;
    boolean freefall = false;


    //enemy sprites
    private SpriteAnimation enemy_anim;
    private int translateEnemyX;
    private int randEnemyTrans_spd = 10;

    // BGM
    MediaPlayer bgm;

    // Sound
    private SoundPool sounds;
    private int soundcorrect, soundwrong;

    // Pause Button
    private boolean pausepress = false;
    private Objects Pause1;
    private Objects Pause2;

    // Alert
    public boolean showAlert = false;
    AlertDialog.Builder alert = null;
    Activity activityTracker;
    public boolean showed = false;
    private Alert AlertObj;

    protected void onCreate(Bundle savedInstanceState) {
       // Sw_vibrate = (Switch) findViewById(R.id.Sw_vibrate);
        //handCash = (TextView) findViewById(R.id.handCash);

        //Set the switch to ON
        Sw_vibrate.setChecked(true);
        vibrate_on = false;

        //init enemy translate pos
        translateEnemyX = 1290;

        //initi player translate Y pos for jumping
        translateplayerY = 0;

        /*
        //Attach a listener to check for changes in state
        Sw_vibrate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    vibrate_on = true;
                } else {
                    vibrate_on = false;
                }
            }
        });*/
    }

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView(Context context) {

        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        // 1e)load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg_lv1);
        scaledbg = Bitmap.createScaledBitmap(bg, ScreenWidth, ScreenHeight, true);

        // 4c) Load the images of the spaceships
        Spaceship[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_1);
        Spaceship[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_2);
        Spaceship[2] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_3);
        Spaceship[3] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_4);

        //load the images of the hearts
        Hearts[0] = BitmapFactory.decodeResource(getResources(), R.drawable.gray_heart);
        Hearts[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart);

        //Load the animation sprite sheet(char)
        // robot_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(),R.drawable.char_robot),600,73,10,10);
        android_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.char_android), 362, 82, 6, 6);
        enemy_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.enemy), 234, 36, 8, 8);

        bgm = MediaPlayer.create(context, R.raw.background_music);

        // Define Soundpool will be used
        sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        // Load audio file
        soundcorrect = sounds.load(context, R.raw.correct, 1);
        soundwrong = sounds.load(context, R.raw.incorrect, 1);

        //Load pause button
        Pause1 = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.pause), 200, 72);
        Pause2 = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.pause1), 200, 72);

        //Practical 13
        // Load Shared Preferences
        // SharePrefScore = getContext().getSharedPreferences("Scoredata", Context.MODE_PRIVATE);
        //  editor = SharePrefScore.edit();
        //  highscore = SharePrefScore.getInt("KeyHighscore", 0);

        //Practical 9
        //Alert Dialog
        AlertObj = new Alert(this);
        alert = new AlertDialog.Builder(getContext());

        // Allow players to input name
        final EditText input = new EditText(getContext());

        // Define input method where 'enter' key is disabled
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        // Define max of 20 chars to be entered for 'Name' field
        int maxLength = 20;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        input.setFilters(FilterArray);

        //set up dialog event
        alert.setCancelable(false);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent();
                // Push highscore to another activity, this is a comment.
                // intent.putExtra("highscore", highscore);
                intent.setClass(getContext(), Mainmenu.class);
                activityTracker.startActivity(intent);
            }
        });


        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder) {
        // Create the thread
        if (!myThread.isAlive()) {
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }

        //Play background music
        bgm.setVolume(0.8f, 0.8f);
        bgm.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Destroy the thread
        if (myThread.isAlive()) {
            myThread.startRun(false);
        }
        boolean retry = true;
        while (retry) {
            try {
                myThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

        // End background music
        bgm.stop();
        bgm.release();

        // End audio file
        sounds.unload(soundcorrect);
        sounds.unload(soundwrong);
        sounds.release();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public boolean checkCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        if (x2 >= x1 && x2 <= x1 + w1) // Start to detect collision of the top left corner
        {
            if (y2 >= y1 && y2 <= y1 + h1) //  Comparing yellow box to blue box
            {
                return true;
            }
        }

        if (x2 + w2 >= x1 && x2 + w2 <= x1 + w1) // Start to detect collision of the top right corner
        {
            if (y2 >= y1 && y2 <= y1 + h1) {
                return true;
            }
        }

        if (x2 >= x1 && x2 <= x1 + w1) // Start to detect collision of the bottom left
        {
            if (y2 + h2 >= y1 && y2 + h2 <= y1 + h1) {
                return true;
            }
        }

        if (x2 + w2 >= x1 && x2 + w2 <= x1 + w1) // Start to detect collision of the bottom right
        {
            if (y2 + h2 >= y1 && y2 + h2 <= y1 + h1) {
                return true;
            }
        }

        return false;
    }

    public void startVibration() {
        //if (vibrate_on) {
            long Pattern[] = {0, 200, 500};
            vibrate = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrate.vibrate(Pattern, -1); // sets the vibration as: REPEAT. -1 as do not repeat
            Log.v(TAG, "Test");
        //}
    }

    public void stopVibrate() {
        vibrate.cancel();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();

        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP

        matrix.postScale(scaleWidth, scaleHeight);

        // RECREATE THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;

    }

    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if (canvas == null) {
            return;
        }
        canvas.drawBitmap(scaledbg, bgX, bgY, null);
        canvas.drawBitmap(scaledbg, bgX + ScreenWidth, bgY / 2, null);

        // 4d) Draw the spaceships
        //canvas.drawBitmap(Spaceship[SpaceshipIndex], 100, 100, null);

        //Draw the hearts
        switch (health) {
            case 3:
                canvas.drawBitmap(Hearts[1], android_anim.getX() - 20f, android_anim.getY() - (ScreenHeight * 0.015f), null); //left
                canvas.drawBitmap(Hearts[1], android_anim.getX() + 35f, android_anim.getY() - (ScreenHeight * 0.015f), null); //middle
                canvas.drawBitmap(Hearts[1], android_anim.getX() + 90f, android_anim.getY() - (ScreenHeight * 0.015f), null); //right
                break;

            case 2:
                canvas.drawBitmap(Hearts[1], android_anim.getX() - 20f, android_anim.getY() - (ScreenHeight * 0.015f), null); //left
                canvas.drawBitmap(Hearts[1], android_anim.getX() + 35f, android_anim.getY() - (ScreenHeight * 0.015f), null); //middle
                canvas.drawBitmap(Hearts[0], android_anim.getX() + 90f, android_anim.getY() - (ScreenHeight * 0.015f), null); //right
                break;

            case 1:
                canvas.drawBitmap(Hearts[1], android_anim.getX() - 20f, android_anim.getY() - (ScreenHeight * 0.015f), null); //left
                canvas.drawBitmap(Hearts[0], android_anim.getX() + 35f, android_anim.getY() - (ScreenHeight * 0.015f), null); //middle
                canvas.drawBitmap(Hearts[0], android_anim.getX() + 90f, android_anim.getY() - (ScreenHeight * 0.015f), null); //right
                break;

            case 0:
                canvas.drawBitmap(Hearts[0], android_anim.getX() - 20f, android_anim.getY() - (ScreenHeight * 0.015f), null); //left
                canvas.drawBitmap(Hearts[0], android_anim.getX() + 35f, android_anim.getY() - (ScreenHeight * 0.015f), null); //middle
                canvas.drawBitmap(Hearts[0], android_anim.getX() + 90f, android_anim.getY() - (ScreenHeight * 0.015f), null); //right
                break;
        }
        //draw char
        // robot_anim.draw(canvas);
        // robot_anim.setY(600);

        //draw char android
        android_anim.draw(canvas);
        android_anim.setY(500 + translateplayerY);

        //draw enemy
        enemy_anim.draw(canvas);
        enemy_anim.setY(600);
        enemy_anim.setX(translateEnemyX);

        // Bonus) To print FPS on the screen
        Paint paint = new Paint();
        paint.setARGB(255, 0, 0, 0);
        paint.setStrokeWidth(100);
        paint.setTextSize(50);
        paint.setFakeBoldText(true);
        paint.setColor(Color.WHITE);
        canvas.drawText("FPS: " + FPS, ScreenWidth * 0.2f, ScreenHeight * 0.1f, paint);

        //Print score
        canvas.drawText("SCORE: " + score, ScreenWidth * 0.6f, ScreenHeight * 0.1f, paint);

        //Render the pause option
        RenderPause(canvas);
    }

    //Update method to update the game play
    public void update(float dt, float fps) {
        FPS = (int) fps;

        switch (state) {
            case Play: {
                // 3) Update the background to allow panning effect
                bgX -= 500 * dt;

                //wrapping
                if (bgX < -ScreenWidth) {
                    bgX = 0;
                }

                //brief invulnerability after hit
                invunTime++;

                //robot_anim.update(System.currentTimeMillis());
                android_anim.update(System.currentTimeMillis());
                enemy_anim.update(System.currentTimeMillis());

                //update enemy to move left per sec
                enemy_anim.setX(translateEnemyX -= 5);

                if (translateEnemyX < 0) {
                    translateEnemyX = ScreenWidth - randEnemyTrans_spd;

                    Random rn = new Random();

                    randEnemyTrans_spd = rn.nextInt(20) + 10;
                    //Difficulty level
                    if (score >= 50 && score < 100) {
                        randEnemyTrans_spd = rn.nextInt(100) + 30;
                    } else if (score >= 100 && score < 500) {
                        randEnemyTrans_spd = rn.nextInt(200) + 50;
                    } else if (score >= 500 && score < 1000) {
                        randEnemyTrans_spd = rn.nextInt(400) + 100;
                    }

                    score += 10;
                }

                if (invunTime >= 50 && isHit == false) {
                    isHit = true;
                }

                if (health <= 0)
                    health = 0;


                if (isJump == true) {
                    translateplayerY -= 5;
                    if (translateplayerY < -200) {
                        //translateplayerY = 0; //reset when hit highest peak (0 acceleration)
                        isJump = false;
                        freefall = true;
                    }
                }

                if (freefall == true) {
                    translateplayerY += 5;
                    if (translateplayerY > 40) {
                        translateplayerY = 0;
                        freefall = false;
                    }
                }

                System.out.println(translateplayerY);
                System.out.println("freefall " + freefall);
                System.out.println("isjump " + isJump);

                if (checkCollision(android_anim.getX(), android_anim.getY(), android_anim.getSpriteWidth(),
                        android_anim.getSpriteHeight(),
                        enemy_anim.getX(), enemy_anim.getY(), enemy_anim.getSpriteWidth(), enemy_anim.getSpriteHeight())) {

                    if (isHit == true) {
                        invunTime = 0;
                        health--;
                        score -= 10;


                        isHit = false;

                        if (score <= 0)
                            score = 0;
                    }

                    //lose
                    if (health <= 0) {
                        //you lose
                        // Intent intent = new Intent();
                        //intent.setClass(this, Losepage.class);
                        //Intent i = new Intent().setClass(getContext(), Losepage.class);
                        // ((Activity) getContext()).startActivity(i);
                    }
                    //win
                    if (score >= 1000) {
                        //you win
                        //intent.setClass(,Winpage.class);
                        //state = GameState.Win;
                    }
                }
            }
            break;
        }

        if (showAlert == true && !showed) {
            showed = true;
            alert.setMessage("Game Over");
            AlertObj.RunAlert();
            showAlert = false;
            showed = false;
        }

    }

    public void RenderPause(Canvas canvas) {
        // Draw the pause button
        if (pausepress == true) {
            canvas.drawBitmap(Pause2.getBitmap(), Pause2.getX(), Pause2.getY(), null);
        } else if (pausepress == false) {
            canvas.drawBitmap(Pause1.getBitmap(), Pause1.getX(), Pause1.getY(), null);
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas) {
        switch (state) {
            case Play:
                RenderGameplay(canvas);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 5) In event of touch on screen, the spaceship will relocate to the point of touch

        int action = event.getAction(); // check for the action of touch

        short X = (short) event.getX();
        short Y = (short) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // check if player presses the pause button
                if (checkCollision(Pause1.getX(), Pause1.getY(), Pause1.getWidth(), Pause1.getHeight(), X, Y, 0, 0)) {
                    if (!pausepress) {
                        pausepress = true;
                        myThread.pause();
                        bgm.pause();

                    } else {
                        pausepress = false;
                        myThread.unPause();
                        bgm.start();
                    }

                    startVibration();
                }

                // if game is not paused
                if (pausepress == false) {
                    if (isJump == false) {
                        isJump = true;
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                break;
//                if (moveShip == true) {
//                    // New Location where the image lands on
//                    mX = (short) (X - Spaceship[SpaceshipIndex].getWidth() / 2);
//                    mY = (short) (X - Spaceship[SpaceshipIndex].getHeight() / 2);
//                }

            //check if stone and ship collide
                /*if (checkCollision(mX, mY, Spaceship[SpaceshipIndex].getWidth(),
                        Spaceship[SpaceshipIndex].getHeight(),
                        aX, aY, android_anim.getSpriteWidth(), android_anim.getSpriteHeight())) {
                    Random r = new Random();
                    aX = r.nextInt(ScreenWidth);
                    aY = r.nextInt(ScreenHeight);
                    // score += 10;
                    //health -= 1;

                    sounds.play(soundwrong, 1.0f, 1.0f, 0, 0, 1.5f);
                    startVibration();

                    Lives--;
                    Score += 10;

                    if (Lives == 0) {
                        EndLevel();
                        //showAlert = true;

                        //Sharedpreferences mode
                        // if (scoreno > highscore)
                        //{
                        // must put in "KeyHighscore" since it's initialised as KeyHighscore
                        //editor.putInt("KeyHighscore", highscore); // stores information to scoreboard
                        //editor.clear() // clears all information in game, flushes the information. do not uncomment, this is just information
                        //editor.commit(); // stores the information
                        // }
                    }

                    // if (hits == 3)
                    //  {
                    //  canvas.drawBitmap(score, 28, Screenheight - 700, null);
                    //  canvas.drawBitmap(score, 58, Screenheight - 700, null);
                    //   canvas.drawBitmap(score, 88, Screenheight - 700, null);
                    //  }
                    //  else if(hits == 2)
                    //{
                    //   canvas.drawBitmap(score, 28, Screenheight - 700, null);
                    //    canvas.drawBitmap(score, 58, Screenheight - 700, null);

                    //  }
                    //   else if (hits == 1)
                    //  {
                    //   canvas.drawBitmap(score, 28, Screenheight - 700, null)
                    // }
                    //test for vibrate
                    startVibration();
                }
                break;*/
        }

        return true;
    }
}

