package com.example.kongwenyao.photoeditor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnColorChosenListener {

    static final String PRES_FILE = "PRES_FILE";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onColorChosen(int red, int green, int blue) {
        sharedPreferences = getSharedPreferences(PRES_FILE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("RED", red);
        editor.putInt("GREEN", green);
        editor.putInt("BLUE", blue);
        editor.apply();
    }

    @Override
    public void onSeekBarChange(int width) {
        sharedPreferences = getSharedPreferences(PRES_FILE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("STROKE_WIDTH", width);
        editor.apply();
    }
}
