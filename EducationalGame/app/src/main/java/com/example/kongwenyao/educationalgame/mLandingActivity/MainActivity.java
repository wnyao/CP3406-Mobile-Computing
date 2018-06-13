package com.example.kongwenyao.educationalgame.mLandingActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.andexert.library.RippleView;
import com.example.kongwenyao.educationalgame.mGameActivity.GameActivity;
import com.example.kongwenyao.educationalgame.mLeaderboardActivity.LeaderboardActivity;
import com.example.kongwenyao.educationalgame.Objects.MediaPlayerManager;
import com.example.kongwenyao.educationalgame.R;
import com.example.kongwenyao.educationalgame.mSettingsActivity.SettingsActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayerManager mediaPlayerManager;
    private boolean musicEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Set fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //No title
        setContentView(R.layout.activity_main);

        //View reference
        RippleView vLeaderboardButton = findViewById(R.id.leaderboard_button);
        RippleView vSettingsButton = findViewById(R.id.settings_button);
        RippleView vPlayButton = findViewById(R.id.play_button);

        //Set Event Listener
        vLeaderboardButton.setOnClickListener(this);
        vSettingsButton.setOnClickListener(this);
        vPlayButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.PREFS_NAME, 0);
        musicEnabled = sharedPreferences.getBoolean(SettingsActivity.MUSIC_SETTINGS, true);
        musicInit();
    }

    @Override
    public void onClick(View v) {
        if (musicEnabled) {
            mediaPlayerManager.stop(); //Stop background music
        }

        Intent intent;
        switch (v.getId()) {
            case R.id.play_button:
                intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                break;
            case R.id.leaderboard_button:
                intent = new Intent(this, LeaderboardActivity.class);
                startActivity(intent);
                break;
            case R.id.settings_button:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void musicInit() {
        if (musicEnabled) {
            mediaPlayerManager = new MediaPlayerManager(this);
            mediaPlayerManager.create(R.raw.music_wii, true);
            mediaPlayerManager.play();
        }
    }
}
