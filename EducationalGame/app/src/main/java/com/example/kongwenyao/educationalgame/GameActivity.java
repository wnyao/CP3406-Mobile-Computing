package com.example.kongwenyao.educationalgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Objects;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {

    private ImageView playerImageView;
    private MainPlayer mainPlayer;
    private GameView gameView;
    private PlayerState facingDirection;

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
        gameView = findViewById(R.id.game_view);
        gameView.setOnTouchListener(this);

        //Player Default
        mainPlayer = new MainPlayer(this, PlayerState.REST_RIGHT);
        playerImageView.setImageDrawable(mainPlayer.getDrawable());
        facingDirection = PlayerState.REST_RIGHT;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.game_settings) {
            //TODO: Implicit intent launch game menu
            return true;
        } else if (id == R.id.game_leaderboard) {
            //TODO: Implicit intent launch game menu
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
        //System.out.println(String.format("%f %f", event.getX(), event.getY())); //test
        return true;
    }

    public void updatePlayerViewCoordinate(float pointX) {

        //Check if point.x out of restricted width
        if (pointX > (gameView.getWidth() + 50) - playerImageView.getWidth()/2) {
            pointX = (gameView.getWidth() + 50) - playerImageView.getWidth()/2;
        } else if (pointX < playerImageView.getWidth()/2) {
            pointX = (playerImageView.getWidth()/2) - 50;  //Image side padding equals 50
        }

        playerImageView.setX(pointX - playerImageView.getWidth()/2);
        playerImageView.setY(gameView.getHeight() - playerImageView.getHeight());
    }

    public void animatePlayer(PlayerState playerState) {
        if (!Objects.equals(mainPlayer.getPlayerState(), playerState)) {    //Check previous player state
            mainPlayer.stopAnimation(); //Stop previous running animation
            mainPlayer.updatePlayer(playerState);
            playerImageView.setImageDrawable(mainPlayer.getDrawable());
            mainPlayer.startAnimation();
        }
    }

    public PlayerState getWalkDirection(float x) {
        PlayerState playerState;

        //Determine which direction based on x value
        if (x < playerImageView.getX() + playerImageView.getX()/2) {
            playerState = PlayerState.WALK_RIGHT;
        } else {
            playerState = PlayerState.WALK_LEFT;
        }

        return  playerState;
    }

    public PlayerState getRestDirection(PlayerState facingDirection) {
        PlayerState playerState;
        if (facingDirection == PlayerState.REST_RIGHT || facingDirection == PlayerState.WALK_RIGHT) {   //Determine player facing direction
            playerState = PlayerState.REST_RIGHT;
        } else {
            playerState = PlayerState.REST_LEFT;
        }

        return playerState;
    }

}
