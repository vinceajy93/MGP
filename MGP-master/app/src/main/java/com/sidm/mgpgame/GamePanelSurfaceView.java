package com.sidm.mgpgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Paint;
import android.widget.EditText;


import java.util.Random;

/**
 * Created by 144116C on 11/23/2015.
 */


public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback,SensorEventListener {
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering
    protected static final String TAG = null;

    //for accelerator
    private final SensorManager sensor;
    //array to store sensor vales
    private float[ ] values = {0,0,0};
    private long lastTime = System.currentTimeMillis();

    // 1a) Variables used for background rendering
    private Bitmap bg, scaledbg;

    boolean collided = false;

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

    GameState state = GameState.Play;

    // Variables for score
    private int score = 0;

    // Variables for vibrations
    public Vibrator vibrate;

    //variables for gameplay
    private int health = 3;

    //variable for invulnerability time
    private boolean isHit = false;
    protected int invunTime = 50; //in milli seconds

    //Variable to print cash at hand
    //private TextView handCash;
    private int cash = 1000;

    //char sprites
    //private SpriteAnimation robot_anim;
    private SpriteAnimation android_anim;


    private int translateplayerY = 0; //for jumping
    boolean isJump = false;
    boolean freefall = false;

    //enemy sprites
    private SpriteAnimation enemy_anim;
    private int translateEnemyX = 1290;
    private int randEnemyTrans_spd = 10;

    // BGM
    MediaPlayer bgm;

    // Sound
    private SoundPool sounds;
    private int soundjump, soundhit;

    //for sound checks
    private boolean isJumpSound;


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

    //Practical 13
    // High Score
    int highscore; //init highscore
    SharedPreferences SharePrefScore; // Share the score
    SharedPreferences.Editor editor;

    // Player Name
    SharedPreferences SharePrefName;
    SharedPreferences.Editor editorN;
    String playerName;

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView(Context context) {



        // Context is the current state of the application/object
        super(context);

        //for accelerometer
        sensor = (SensorManager)
                getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor.registerListener(this,
                sensor.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),
                SensorManager.SENSOR_DELAY_NORMAL);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        // 1e)load the image when this class is being instantiated
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
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
        android_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.char_android), 362, 200, 6, 6);
        enemy_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.enemy), 234, 36, 8, 8);

        bgm = MediaPlayer.create(context, R.raw.background);
        bgm.setLooping(true); // loops the background music :D

        // Define Soundpool will be used
        sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        // Load audio file
        soundjump = sounds.load(context, R.raw.jump, 1);
        soundhit = sounds.load(context, R.raw.hit, 1);

        //init the sound checks
        isJumpSound = true;

        //Load pause button
        Pause1 = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.pause), 200, 72);
        Pause2 = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.pause1), 200, 72);


    //Practical 13
        // Load Shared Preferences
        SharePrefScore = getContext().getSharedPreferences("Scoredata", Context.MODE_PRIVATE);
        editor = SharePrefScore.edit();
        highscore = SharePrefScore.getInt("KeyHighscore", 0);

        SharePrefName = getContext().getSharedPreferences("playerName", Context.MODE_PRIVATE);
        editorN = SharePrefName.edit();
        playerName = SharePrefName.getString("KeyPlayerName", "DEFAULT");

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
                playerName = input.getText().toString();
                editorN.putString("KeyPlayerName", playerName);
                editorN.commit();

                Intent intent = new Intent();
                // Push highscore to another activity, this is a comment.
                intent.putExtra("highscore", highscore);
                intent.setClass(getContext(), Mainmenu.class);
                activityTracker.startActivity(intent);
            }
        });




        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    public void SensorMove(){
        // Temp Variables
        float tempX, tempY;

        // bX and bY are variables used for moving the object
        // values [1] – sensor values for x axis
        // values [0] – sensor values for y axis

        tempX = android_anim.getX() + (values[1] * ((System.currentTimeMillis() - lastTime)/1000 ));
        tempY = android_anim.getY() + (values[0] * ((System.currentTimeMillis() - lastTime)/1000 ));


        // Check if the ball is going out of screen along the x-axis
        if (tempX <= android_anim.getSpriteWidth()/2 || tempX >= ScreenWidth - android_anim.getSpriteWidth()/2)
        {
        // Check if ball is still within screen along the y-axis
            if ( tempY > android_anim.getSpriteHeight()/2 && tempY < ScreenHeight - android_anim.getSpriteHeight()/2) {
                android_anim.setY((int)tempY); //<-- casted float to inti makes it jerky, needs to change
            }
        }

        // If not, both axis of the ball's position is still within screen
        else
        {
        // Move the ball with frame independant movement
            android_anim.setX((int)tempX);
            android_anim.setY((int)tempY);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // Do something here if sensor accuracy changes.
    }
    @Override
    public void onSensorChanged(SensorEvent SenseEvent) {
    // Many sensors return 3 values, one for each axis
    // Do something with this sensor value.
        values = SenseEvent.values;
        //SensorMove(); <--- not using this to move the player no more
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
        sounds.unload(soundjump);
        sounds.unload(soundhit);
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
        long Pattern[] = {0, 200, 500};
        vibrate = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrate.vibrate(Pattern, -1); // sets the vibration as: REPEAT. -1 as do not repeat
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

        //Draw the hearts
        switch (health) {
            //full health
            case 3:
                canvas.drawBitmap(Hearts[1], android_anim.getX() - 24.5f, android_anim.getY() - 50.f, null); //left
                canvas.drawBitmap(Hearts[1], android_anim.getX() + 15.5f, android_anim.getY() - 50.f, null); //middle
                canvas.drawBitmap(Hearts[1], android_anim.getX() + 55.5f, android_anim.getY() - 50.f, null); //right
                break;
            //2 health left
            case 2:
                canvas.drawBitmap(Hearts[1], android_anim.getX() - 24.5f, android_anim.getY() - 50.f, null); //left
                canvas.drawBitmap(Hearts[1], android_anim.getX() + 15.5f, android_anim.getY() - 50.f, null); //middle
                canvas.drawBitmap(Hearts[0], android_anim.getX() + 55.5f, android_anim.getY() - 50.f, null); //right
                break;
            //1 health left
            case 1:
                canvas.drawBitmap(Hearts[1], android_anim.getX() - 24.5f, android_anim.getY() - 50.f, null); //left
                canvas.drawBitmap(Hearts[0], android_anim.getX() + 15.5f, android_anim.getY() - 50.f, null); //middle
                canvas.drawBitmap(Hearts[0], android_anim.getX() + 55.5f, android_anim.getY() - 50.f, null); //right
                break;
            //dead, changed to lose page
            case 0:
                canvas.drawBitmap(Hearts[0], android_anim.getX() - 24.5f, android_anim.getY() - 50.f, null); //left
                canvas.drawBitmap(Hearts[0], android_anim.getX() + 15.5f, android_anim.getY() - 50.f, null); //middle
                canvas.drawBitmap(Hearts[0], android_anim.getX() + 55.5f, android_anim.getY() - 50.f, null); //right
                break;
        }

        //init the position of the player
        float PlayerInitYPos = ScreenHeight * 0.85f;
        //init the position of the enemy
        float EnemyYPos = ScreenHeight * 0.85f;

        //draw char android
        android_anim.draw(canvas);
        //translate the android_anim during update
        android_anim.setY((int) PlayerInitYPos + translateplayerY);

        //draw enemy
        enemy_anim.draw(canvas);
        //set the position of the enemy during update
        enemy_anim.setY((int)EnemyYPos);
        enemy_anim.setX(translateEnemyX);

        // Bonus) To print FPS on the screen
        Paint paint = new Paint();
        paint.setARGB(255, 0, 0, 0);
        paint.setStrokeWidth(100);
        paint.setTextSize(50);
        paint.setFakeBoldText(true);
        paint.setColor(Color.WHITE);
       // canvas.drawText("TranslateY" + translateplayerY, ScreenWidth * 0.2f, ScreenHeight * 0.1f, paint);
        canvas.drawText("ishit" + isHit, ScreenWidth * 0.2f, ScreenHeight * 0.1f, paint);

        //Print score
        canvas.drawText("SCORE: " + score, ScreenWidth * 0.6f, ScreenHeight * 0.1f, paint);

//        if (collided == true) {
//            Paint collide = new Paint();
//            collide.setARGB(255, 0, 0, 0);
//            collide.setStrokeWidth(100);
//            collide.setTextSize(30);
//            canvas.drawText("Collided ", 130, 45, paint);
//        }



        //Render the pause option
        RenderPause(canvas);
    }

    //Update method to update the game play
    public void update(float dt, float fps) {
        FPS = fps;

        switch (state) {
            case Play: {
                // 3) Update the background to allow panning effect
                bgX -= 500 * dt;

                //wrapping
                if (bgX < -ScreenWidth) {
                    bgX = 0;
                }

                //brief invulnerability after hit
                if(isHit == true)
                {
                    //increment the invulnerability timer
                    invunTime++;
                }

                //if invulnerability is more than .5sec and player is not in collision
                if (invunTime >= 50 && isHit == false) {
                    //isHit = true;
                }

                //set health at 0 if its less than 0
                if (health <= 0) {
                    health = 0;
                }

                if (isJump == true) {
                    //player is jumping (screen touched)
                    translateplayerY -= 5;

                    if(isJumpSound)
                    {
                        sounds.play(soundjump, 1.0f, 1.0f, 0, 0, 1.5f);
                        isJumpSound = false;
                    }


                    if (translateplayerY < -120) {
                        //translateplayerY = 0; //reset when hit highest peak (0 acceleration)
                        isJump = false;
                        isJumpSound = true;
                        freefall = true;
                    }
                }

                if (freefall == true) {

                    if (translateplayerY < 20) {
                        translateplayerY += 5;
                    }
                    else {
                        translateplayerY = 0;
                        freefall = false;
                    }
                }

                //update enemy to move left per sec
                //enemy_anim.setX(translateEnemyX -= 5);
                translateEnemyX -= 5;

                //reset the enemy back to the right side of the screen

                if (translateEnemyX < 0) {
                    //increase score if enemy passed the player
                    if(enemy_anim.getX() < android_anim.getX())
                    {
                        score+=10;
                    }
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



                }

                if (checkCollision(android_anim.getX(), android_anim.getY(), android_anim.getSpriteWidth(),
                        android_anim.getSpriteHeight(),
                        enemy_anim.getX(), enemy_anim.getY(), enemy_anim.getSpriteWidth(), enemy_anim.getSpriteHeight())) {

                    //if player collided with enemy
                    isHit = true;
                    collided = true;

                    if (isHit == true) {
                        invunTime = 0;
                        health--;
                        score -= 10;

                        //reset isHit boolean
                        isHit = false;

                        //score cannot be lower than 0
                        if (score <= 0)
                            score = 0;
                    }

                   //sounds.play(soundhit, 1.0f, 1.0f, 0, 0, 1.5f);

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
                //robot_anim.update(System.currentTimeMillis());
                android_anim.update(System.currentTimeMillis());
                enemy_anim.update(System.currentTimeMillis());

                System.out.println(translateplayerY);

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

    //public void EndLevel() {
     //   showAlert = true;
   // }

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
                        //isJump = false;
                        myThread.pause();
                        bgm.pause();
                    } else {
                        pausepress = false;
                        myThread.unPause();
                        bgm.start();
                    }

                    startVibration();
                } else {
                    // if game is not paused
                    //if (pausepress == false) {
                        if (isJump == false) {
                            isJump = true;
                    } else {
                        collided = false;
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

