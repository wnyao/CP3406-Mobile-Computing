package com.example.kongwenyao.guesstheceleb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnSettingSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onRadioButtonSelected(int buttonText) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ActivityMainFragment.BUTTON_TEXT, String.valueOf(buttonText));
        startActivity(intent);
    }
}
