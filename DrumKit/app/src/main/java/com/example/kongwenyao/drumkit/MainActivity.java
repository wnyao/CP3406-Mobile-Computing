package com.example.kongwenyao.drumkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private DisplayView displayView;
    private SoundManager soundManager;

    private int hihat, kick, openhat, ride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Variable Assignment
        soundManager = new SoundManager(this);
        displayView = findViewById(R.id.display_view);
        displayView.setOnTouchListener(this);

        //Get Sound ID
        hihat = soundManager.getSoundID(R.raw.hihat);
        kick = soundManager.getSoundID(R.raw.kick);
        openhat = soundManager.getSoundID(R.raw.openhat);
        ride = soundManager.getSoundID(R.raw.ride);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.second_displayView:
                Intent intent = new Intent(this, SecondActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (displayView.getPosition(event.getX(), event.getY())) {
            case TOP_LEFT:
                displayView.setDisplayMessage("Hihat");
                soundManager.play(hihat);
                break;
            case TOP_RIGHT:
                displayView.setDisplayMessage("Kick");
                soundManager.play(kick);
                break;
            case BOTTOM_LEFT:
                displayView.setDisplayMessage("Openhat");
                soundManager.play(openhat);
                break;
            case BOTTOM_RIGHT:
                displayView.setDisplayMessage("Ride");
                soundManager.play(ride);
                break;
        }

        displayView.invalidate();   //Invalidate UI to refresh itself
        return false;
    }


}
