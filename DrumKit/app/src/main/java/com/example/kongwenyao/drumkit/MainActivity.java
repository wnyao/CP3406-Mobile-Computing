package com.example.kongwenyao.drumkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
