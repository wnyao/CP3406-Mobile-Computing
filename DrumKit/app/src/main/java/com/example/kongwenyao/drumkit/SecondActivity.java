package com.example.kongwenyao.drumkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class SecondActivity extends AppCompatActivity implements View.OnTouchListener {

    private SecondDisplayView secondDisplayView;
    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        soundManager = new SoundManager(this);
        secondDisplayView = findViewById(R.id.second_displayView);
        secondDisplayView.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Double cirCenterX = 898.41797;
        //Double cirCenterY = 229.28027;
        //Double radius = Double.valueOf(5);

        //if ((event.getX() - (cirCenterX + radius)) * (event.getX() - (cirCenterX + radius)) + (event.getY() - (cirCenterY + radius)) * (event.getY() - (cirCenterY + radius)) <= radius * radius) {
        //    int soundID = soundManager.getSoundID(R.raw.ride);
        //    soundManager.play(soundID);
        //}

        //int soundID = soundManager.getSoundID(R.raw.snare);
        //soundManager.play(soundID);


        System.out.println("x: " + event.getX() + " y: " + event.getY());
        return false;
    }
}
