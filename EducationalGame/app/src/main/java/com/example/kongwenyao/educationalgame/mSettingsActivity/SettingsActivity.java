package com.example.kongwenyao.educationalgame.mSettingsActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CompoundButton;

import com.example.kongwenyao.educationalgame.mGameActivity.GameActivity;
import com.example.kongwenyao.educationalgame.Objects.MediaPlayerManager;
import com.example.kongwenyao.educationalgame.R;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    public static final String PREFS_NAME = "PREFS_FILE";
    public static final String SENSOR_SETTINGS = "ENABLE_SENSOR";
    public static final String MUSIC_SETTINGS = "ENABLE_MUSIC";

    //View
    private SwitchCompat sEnableSensor;
    private SwitchCompat sEnableMusic;

    //Global variable
    private boolean musicEnabled;
    private boolean sensorEnabled;
    private boolean announcement = false;

    //Background music
    private MediaPlayerManager mediaPlayerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        //Custom Toolbar
        Toolbar toolbar = findViewById(R.id.leaderboard_toolbar);
        toolbar.setTitle(R.string.settings);
        setSupportActionBar(toolbar);

        //View assignment
        sEnableSensor = findViewById(R.id.setting_EnableSensor);
        sEnableMusic = findViewById(R.id.setting_EnabeMusic);

        //EventListener
        sEnableSensor.setOnCheckedChangeListener(this);
        sEnableMusic.setOnCheckedChangeListener(this);

        musicInit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Get settings from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        sensorEnabled = sharedPreferences.getBoolean(SENSOR_SETTINGS, true);
        musicEnabled = sharedPreferences.getBoolean(MUSIC_SETTINGS, true);

        //Set switch combat setting
        sEnableSensor.setChecked(sensorEnabled);
        sEnableMusic.setChecked(musicEnabled);
        announcement = true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Save settings
        SharedPreferences.Editor sharedPreferences = getSharedPreferences(PREFS_NAME, 0).edit();
        sharedPreferences.putBoolean(SENSOR_SETTINGS, sensorEnabled);
        sharedPreferences.putBoolean(MUSIC_SETTINGS, musicEnabled);
        sharedPreferences.apply(); //Apply commit
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (musicEnabled) {
            mediaPlayerManager.stop();
        }

        if (item.getItemId() == R.id.back_to_game) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Snackbar snackbar = null;

        switch (buttonView.getId()) {
            case R.id.setting_EnableSensor:
                if (!isChecked) {
                    sensorEnabled = false;
                    snackbar = Snackbar.make(findViewById(R.id.setting_EnableSensor), R.string.enable_sensor_false, Snackbar.LENGTH_SHORT);
                } else {
                    sensorEnabled = true;
                    snackbar = Snackbar.make(findViewById(R.id.setting_EnableSensor), R.string.enable_sensor_true, Snackbar.LENGTH_SHORT);
                }
                break;
            case R.id.setting_EnabeMusic:
                if (!isChecked) {
                    musicEnabled = false;
                    snackbar = Snackbar.make(findViewById(R.id.setting_EnabeMusic), R.string.enable_music_false, Snackbar.LENGTH_LONG);
                    mediaPlayerManager.pause();
                } else {
                    musicEnabled = true;
                    snackbar = Snackbar.make(findViewById(R.id.setting_EnabeMusic), R.string.enable_music_true, Snackbar.LENGTH_LONG);
                    mediaPlayerManager.resume();
                }
                break;
        }

        if (announcement) {
            assert snackbar != null;
            snackbar.show();
        }
    }

    private void musicInit() {
        mediaPlayerManager = new MediaPlayerManager(this);
        mediaPlayerManager.create(R.raw.music_wii, true);
    }
}
