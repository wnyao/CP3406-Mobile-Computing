package com.example.kongwenyao.educationalgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.andexert.library.RippleView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Set fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //No title
        setContentView(R.layout.activity_main);

        //Variable Assignment
        RippleView vLeaderboardButton = findViewById(R.id.leaderboard_button);
        RippleView vSettingsButton = findViewById(R.id.settings_button);
        RippleView vPlayButton = findViewById(R.id.play_button);

        //Set Event Listener
        vLeaderboardButton.setOnClickListener(this);
        vSettingsButton.setOnClickListener(this);
        vPlayButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.play_button:
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
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
