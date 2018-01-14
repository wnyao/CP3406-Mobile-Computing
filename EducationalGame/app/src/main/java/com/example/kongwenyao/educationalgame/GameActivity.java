package com.example.kongwenyao.educationalgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Objects;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, SensorEventListener {

    //Variable for view and object
    private GamePanel gamePanel;
    private MainPlayer mainPlayer;
    private ImageView playerImageView;
    private PlayerState facingDirection;

    //Variable for player info
    public static PointF playerPos;
    public static PointF playerSize;

    //Variable for sensor
    private SensorManager sensorManager;

    //Variable for SharedPreferences
    private boolean sEnableSensor;
    private boolean sMusicEnabled;

    //Variable for music
    private MediaPlayerManager mediaPlayerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        //Custom Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //Variable Assignment
        playerImageView = findViewById(R.id.player_view);
        gamePanel = findViewById(R.id.game_view);

        //Player Default
        mainPlayer = new MainPlayer(this, PlayerState.REST_RIGHT);
        playerImageView.setImageDrawable(mainPlayer.getDrawable());
        facingDirection = PlayerState.REST_RIGHT;
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.PREFS_NAME, 0);
        sEnableSensor = sharedPreferences.getBoolean(SettingsActivity.SENSOR_SETTINGS, true);
        sMusicEnabled = sharedPreferences.getBoolean(SettingsActivity.MUSIC_SETTINGS, true);
        musicInit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sEnableSensor) {
            //Sensor
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            Sensor sensor = sensorManager != null ? sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) : null;

            if (sensorManager != null && sensor != null) {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
            }
        } else {
            gamePanel.setOnTouchListener(this);
        }
        //TODO: gameThread.setRunning(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (sEnableSensor) {
            sensorManager.unregisterListener(this);
        }
        //TODO: terminate gameThread
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //Set Initial Values
        playerPos = new PointF(playerImageView.getX(), playerImageView.getY());
        playerSize = new PointF(playerImageView.getWidth(), playerImageView.getHeight());

        if (!hasFocus) {
            mediaPlayerManager.stop();
        }
    }

    private float filterPosition(float x) {  //Position filter for sensor
        if (x > (gamePanel.getWidth() - playerImageView.getWidth()) + 50) {
            x = (gamePanel.getWidth() - playerImageView.getWidth()) + 50;
        } else if (x < -50) {
            x = -50;
        }
        return x;
    }

    @Override
    public void onSensorChanged(SensorEvent event) { //test needed
        float targetPosition;
        float x = event.values[0];

        if (x < -1 && x > 1) {
            PlayerState playerState = getRestDirection(facingDirection);
            animatePlayer(playerState);
        }

        if (x < -1) { //tilt device right
            animatePlayer(PlayerState.WALK_RIGHT);
            targetPosition = filterPosition(playerImageView.getX() + 7);
            playerImageView.setX(targetPosition);
            facingDirection = PlayerState.WALK_RIGHT;

        } else if (x > 1){ //tilt device left
            animatePlayer(PlayerState.WALK_LEFT);
            targetPosition = filterPosition(playerImageView.getX() - 7);
            playerImageView.setX(targetPosition);
            facingDirection = PlayerState.WALK_LEFT;
        }

        playerPos = new PointF(playerImageView.getX(), playerImageView.getY());   //Update changed x coordinate for collision detection
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        if (sMusicEnabled) { //Stop background music
            mediaPlayerManager.stop();
        }

        if (id == R.id.game_settings) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.game_leaderboard) {
            intent = new Intent(this, LeaderboardActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PlayerState playerState;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //Player WALK Movement
                playerState = getWalkDirection(event.getX());
                animatePlayer(playerState);
                updatePlayerViewCoordinate(event.getX());
                facingDirection = playerState;
                break;
            case MotionEvent.ACTION_UP:
                //Player REST Movement
                playerState = getRestDirection(facingDirection);
                animatePlayer(playerState);
                updatePlayerViewCoordinate(event.getX());
                break;
        }

        playerPos = new PointF(playerImageView.getX(), playerImageView.getY());   //Player ImageView top left coordinate
        return true;
    }

    private void updatePlayerViewCoordinate(float pointX) { //Position filter for touch event

        //Check if point.x out of restricted width
        if (pointX > (gamePanel.getWidth() + 50) - playerImageView.getWidth() / 2) {
            pointX = (gamePanel.getWidth() + 50) - playerImageView.getWidth() / 2;
        } else if (pointX < playerImageView.getWidth() / 2) {
            pointX = (playerImageView.getWidth() / 2) - 50;  //Image side padding equals 50
        }

        playerImageView.setX(pointX - playerImageView.getWidth() / 2);
        playerImageView.setY(gamePanel.getHeight() - playerImageView.getHeight());
    }

    private void animatePlayer(PlayerState playerState) {
        if (!Objects.equals(mainPlayer.getPlayerState(), playerState)) {    //Check previous player state
            mainPlayer.stopAnimation(); //Stop previous running animation
            mainPlayer.updatePlayer(playerState);
            playerImageView.setImageDrawable(mainPlayer.getDrawable());
            mainPlayer.startAnimation();
        }
    }

    private PlayerState getWalkDirection(float x) {
        PlayerState playerState;

        //Determine which direction based on x value
        if (x < playerImageView.getX() + playerImageView.getX() / 2) {
            playerState = PlayerState.WALK_RIGHT;
        } else {
            playerState = PlayerState.WALK_LEFT;
        }

        return playerState;
    }

    private PlayerState getRestDirection(PlayerState facingDirection) {
        PlayerState playerState;
        if (facingDirection == PlayerState.REST_RIGHT || facingDirection == PlayerState.WALK_RIGHT) {   //Determine player facing direction
            playerState = PlayerState.REST_RIGHT;
        } else {
            playerState = PlayerState.REST_LEFT;
        }

        return playerState;
    }

    public MediaPlayerManager getMediaPlayerManager() {
        return mediaPlayerManager;
    }

    private void musicInit() {
        if (sMusicEnabled) {
            mediaPlayerManager = new MediaPlayerManager(this);
            mediaPlayerManager = new MediaPlayerManager(this);
            mediaPlayerManager.create(R.raw.music_funny_jazz, true);
            mediaPlayerManager.play();
        }
    }

}
