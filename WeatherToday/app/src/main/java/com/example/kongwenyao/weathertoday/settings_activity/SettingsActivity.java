package com.example.kongwenyao.weathertoday.settings_activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.kongwenyao.weathertoday.R;

/**
 * SettingsActivity.class contains setting functions for MainActivity.class with listeners for
 * SwitchCompat widget and NumberPicker widget. It also contain SharedPreferences that used to saved
 * instance state for current data and restore instance state of previous initialized data.
 *
 * Created by kongwenyao on 12/9/17.
 */

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, NumberPicker.OnValueChangeListener {

    private final String PREFS_NAME = "PREFS_FILE";

    //Views variables
    private SwitchCompat enlargedText;
    private SwitchCompat getFahrenheit;
    private TextView currentInterval;

    //Global variables
    private boolean enlargedBoolean;
    private boolean fahrenheitBoolean;
    private int intervalValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //View references
        enlargedText = findViewById(R.id.enlarged_text);
        getFahrenheit = findViewById(R.id.get_fahrenheit);
        currentInterval = findViewById(R.id.current_Interval);

        enlargedText.setOnCheckedChangeListener(this);
        getFahrenheit.setOnCheckedChangeListener(this);
    }

    @Override
    public void onStart() {
        //Restore instance state from last session
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        boolean enlargedBoolean = sharedPreferences.getBoolean("ENLARGED_SWITCH", false);
        boolean fahrenheitBoolean = sharedPreferences.getBoolean("FAHRENHEIT_SWITCH", false);
        intervalValue = sharedPreferences.getInt("HOURLY_INTERVAL", 2);

        enlargedText.setChecked(enlargedBoolean);
        getFahrenheit.setChecked(fahrenheitBoolean);

        String message = "Current Interval: " + String.valueOf(intervalValue);
        currentInterval.setText(message);

        super.onStart();
    }

    @Override
    public void onPause() {
        //Save instance state of current session
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ENLARGED_SWITCH", enlargedBoolean);
        editor.putBoolean("FAHRENHEIT_SWITCH", fahrenheitBoolean);
        editor.putInt("HOURLY_INTERVAL", intervalValue);
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

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        String message = "Current Interval:  " + i1;
        currentInterval.setText(message);
        intervalValue = i1;

        //Snackbar message for number picker
        String statusMessage = "Hourly interval set to " + String.valueOf(i1);
        Snackbar snackbar = Snackbar.make(findViewById(R.id.enlarged_text), statusMessage, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    /**
     *  This method calls on NumberPickerDialog.class for custom number picker. ValueChangeListener
     *  can be override accordingly within activity class through onValueChange(NumberPicker np, int i, int, i1)
     *
     *  @param view view that handle the action listener of this method
     */
    public void hourlyForecastHandler(View view) {
        NumberPickerDialog numberPicker = new NumberPickerDialog();
        numberPicker.setValueChangeListener(this);
        numberPicker.show(getSupportFragmentManager(), "Number_Picker");
    }

}
