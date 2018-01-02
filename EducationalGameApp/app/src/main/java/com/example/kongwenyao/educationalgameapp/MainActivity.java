package com.example.kongwenyao.educationalgameapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.andexert.library.RippleView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RippleView vPlayButton;
    private RippleView vLeaderboardButton;
    private RippleView vSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Set fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //No title
        setContentView(R.layout.activity_main);

        //Variable Assignment
        vLeaderboardButton = findViewById(R.id.leaderboard_button);
        vSettingsButton = findViewById(R.id.settings_button);
        vPlayButton = findViewById(R.id.play_button);

        //Set Event Listener
        vLeaderboardButton.setOnClickListener(this);
        vSettingsButton.setOnClickListener(this);
        vPlayButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.play_button:
                //TODO: Launch game panel
                break;
            case R.id.leaderboard_button:
                //TODO: Launch leader board activity
                break;
            case R.id.settings_button:
                //TODO: Launch Setting activity
                break;
        }
    }
}
