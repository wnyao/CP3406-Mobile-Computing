package com.example.kongwenyao.weathertoday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import com.example.kongwenyao.weathertoday.main_activity.MainActivity;
import com.example.kongwenyao.weathertoday.settings_activity.SettingsActivity;
import org.json.JSONException;
import java.io.IOException;
import pl.droidsonroids.gif.GifImageView;

public class HourlyForecastActivity extends AppCompatActivity {

    private GifImageView weatherView;
    private TextView locationView;
    private TextView dateView;
    private TextView tempView;
    private TextView conditionView;
    private TextView skyView;
    private TextView humidityView;
    private TextView uvIndexView;
    private TextView timeView;

    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);

        //Custom Toolbar
        Toolbar toolBar = findViewById(R.id.toolbar);
        toolBar.setTitle("Hourly Forecast");
        setSupportActionBar(toolBar);

        //View assingments
        weatherView = findViewById(R.id.weather_Imageview);
        conditionView = findViewById(R.id.condition_view);
        locationView = findViewById(R.id.location_view);
        tempView = findViewById(R.id.temperature_view);
        timeView = findViewById(R.id.time_view);
        dateView = findViewById(R.id.date_view);
        skyView = findViewById(R.id.sky_view);
        uvIndexView = findViewById(R.id.uvIndex_view);
        humidityView = findViewById(R.id.humidity_view);

        //Instance
        mainActivity = new MainActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        boolean enlargedVal = sharedPreferences.getBoolean("ENLARGED_SWITCH", false);
        mainActivity.EnlargedTextSetting(enlargedVal, this); //TODO: test

        //get intent
        Intent intent = getIntent();
        String[] forecastData = intent.getStringArrayExtra(MainActivity.FORECAST);
        String location = intent.getStringExtra(MainActivity.LOCATION);
        String date = intent.getStringExtra(MainActivity.DATE);

        //Display data
        try {
            displayData(forecastData, location, date);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        //Action Bar menu
        switch (item.getItemId()) {
            case R.id.option_menu:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.info_menu:
                intent = new Intent(this, InfoActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayData(String[] data, String location, String date) throws IOException, JSONException { //Eg. {condition, time, temp, uvIndex, humidity, sky}
        int drawableID = mainActivity.getWeatherImageID(data[0], MainActivity.GIF_TYPE, this);

        weatherView.setImageResource(drawableID);
        locationView.setText(location);
        conditionView.setText(data[0]);
        dateView.setText(date);
        timeView.setText(data[1]);
        tempView.setText(data[2]);
        skyView.setText(data[5]);
        humidityView.setText(data[4]);

        //If uvIndex is zero
        if (!data[3].equals("0")) {
            uvIndexView.setText(data[3]);
        } else {
            GridLayout gridLayout = findViewById(R.id.grid_layout);
            View view = findViewById(R.id.divider2);

            //Remove view
            gridLayout.removeView(view);
            gridLayout.removeView(uvIndexView);
        }
    }


}
