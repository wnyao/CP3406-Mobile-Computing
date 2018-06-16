package com.example.kongwenyao.weathertoday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import com.example.kongwenyao.weathertoday.R;

/**
 * Created by kongwenyao on 12/9/17.
 */

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private final String PREFS_NAME = "PREFS_FILE";

    //Views variables
    private SwitchCompat enlargedText;
    private SwitchCompat getFahrenheit;

    //Global variables
    private boolean enlargedBoolean;
    private boolean fahrenheitBoolean;

    public SettingsActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //View references
        enlargedText = findViewById(R.id.enlarged_text);
        getFahrenheit = findViewById(R.id.get_fahrenheit);

        enlargedText.setOnCheckedChangeListener(this);
        getFahrenheit.setOnCheckedChangeListener(this);
    }

    @Override
    public void onStart() {
        //Restore instance state from last session
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        boolean enlargedBoolean = sharedPreferences.getBoolean("ENLARGED_SWITCH", false);
        boolean fahrenheitBoolean = sharedPreferences.getBoolean("FAHRENHEIT_SWITCH", false);
        enlargedText.setChecked(enlargedBoolean);
        getFahrenheit.setChecked(fahrenheitBoolean);

        super.onStart();
    }

    @Override
    public void onPause() {
        //Save instance state of current session
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ENLARGED_SWITCH", enlargedBoolean);
        editor.putBoolean("FAHRENHEIT_SWITCH", fahrenheitBoolean);
        editor.apply();
        super.onPause();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Snackbar snackbar = null;

        switch (buttonView.getId()) {
            case (R.id.enlarged_text):  //"Enlarged Text" SwitchCompat
                if (!isChecked) {
                    enlargedBoolean = false;
                    snackbar = Snackbar.make(findViewById(R.id.enlarged_text), R.string.enlarged_status_false, Snackbar.LENGTH_SHORT);
                } else {
                    enlargedBoolean = true;
                    snackbar = Snackbar.make(findViewById(R.id.enlarged_text), R.string.enlarged_status_true, Snackbar.LENGTH_SHORT);
                }
                break;

            case (R.id.get_fahrenheit): //"Display Fahrenheit" Switch Compat
                if (!isChecked) {
                    fahrenheitBoolean = false;
                    snackbar = Snackbar.make(findViewById(R.id.enlarged_text), R.string.get_fahrenheit_false, Snackbar.LENGTH_SHORT);
                } else {
                    fahrenheitBoolean = true;
                    snackbar = Snackbar.make(findViewById(R.id.enlarged_text), R.string.get_fahrenheit_true, Snackbar.LENGTH_SHORT);
                }
                break;
        }
        assert snackbar != null;
        snackbar.show();
    }


}
