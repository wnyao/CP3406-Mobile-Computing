package com.example.kongwenyao.guessinggameapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar minNumSeekbar;
    private SeekBar maxNumSeekbar;
    private TextView labelMinSeekbar;
    private TextView labelMaxSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Seekbar Label Assignment
        labelMinSeekbar = findViewById(R.id.labelMinSeekbar);
        labelMaxSeekbar = findViewById(R.id.labelMaxSeekbar);

        //Seekbar Assignment
        minNumSeekbar = findViewById(R.id.minNumSeekbar);
        maxNumSeekbar = findViewById(R.id.maxNumSeekbar);

        //Set Event Listener
        minNumSeekbar.setOnSeekBarChangeListener(this);
        maxNumSeekbar.setOnSeekBarChangeListener(this);

        try {
           Intent intent = getIntent();
           int[] values = intent.getIntArrayExtra("CURRENT_VALUE");
           minNumSeekbar.setProgress(values[0]);
           maxNumSeekbar.setProgress(values[1]);
        } catch (Exception e) {
            System.out.print("No getIntent() in second activity");
        }
    }

    public void configureSeekBar(SeekBar seekBar, int progress) {
        String progressInString = Integer.toString(progress);

        if (seekBar.getId() == R.id.minNumSeekbar) {
            minNumSeekbar.setProgress(progress);
            labelMinSeekbar.setText("Min Value (" + progressInString + "): ");
        } else {
            maxNumSeekbar.setProgress(progress);
            labelMaxSeekbar.setText("Max Value (" + progressInString + "): ");
        }
    }

    public void returnButtonHandler(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("SETTING_VALUE", new int[] {minNumSeekbar.getProgress(), maxNumSeekbar.getProgress()});
        startActivity(intent);
    }

    public boolean validateMaxValue(int maxVal) {
        if (maxVal < minNumSeekbar.getProgress()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validateMinValue(int minVal) {
        if (minVal > maxNumSeekbar.getProgress()) {
            return false;
        } else {
            return true;
        }
    }

    public void noticeInvalidSetting() {
        Context context = SettingActivity.this;
        String textToShow = "Invalid Setting";
        Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        configureSeekBar(seekBar, progress);

        //Check for valid setting inputs
        boolean result;
        if (seekBar.getId() == R.id.maxNumSeekbar) {
            result = validateMaxValue(progress);
        } else {
            result = validateMinValue(progress);
        }

        if (result == false) {
            noticeInvalidSetting();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

}
