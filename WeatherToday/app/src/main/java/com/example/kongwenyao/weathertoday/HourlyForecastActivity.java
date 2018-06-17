package com.example.kongwenyao.weathertoday;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.kongwenyao.weathertoday.hourly_forecast_db.HourlyForecast;
import com.example.kongwenyao.weathertoday.hourly_forecast_db.HourlyForecastViewModel;
import com.example.kongwenyao.weathertoday.main_activity.MainActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class HourlyForecastActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout; //TODO: swipe to switch forecast info
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
    private HourlyForecastViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forecast);

        //Custom Toolbar
        Toolbar toolBar = findViewById(R.id.toolbar);
        toolBar.setTitle("Hourly Forecast");
        setSupportActionBar(toolBar);

        //View assignments
        //constraintLayout = findViewById(R.id.constraint_layout); //TODO: on swipe feature
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

        //View model reference
        mViewModel = ViewModelProviders.of(this)
                .get(HourlyForecastViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        boolean enlargedVal = sharedPreferences.getBoolean("ENLARGED_SWITCH", false);
        mainActivity.EnlargedTextSetting(enlargedVal, this);

        //get intent
        Intent intent = getIntent();
        String location = intent.getStringExtra(MainActivity.LOCATION);
        String tag = intent.getStringExtra(MainActivity.TAG);

        //Get forecast data from Room database
        List<HourlyForecast> forecast = mViewModel.findForecastByTag(tag);

        if (forecast.size() != 0) {
            try {
                //Display data
                displayData(forecast.get(0), location);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
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
                intent = new Intent(this, IconInfoActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayData(HourlyForecast data, String location) throws IOException, JSONException {
        int drawableID = mainActivity.getWeatherImageID(data.condition, MainActivity.GIF_TYPE, this);
        String date = data.dayOfWeek + ", " + data.date;

        //Set content of view
        weatherView.setImageResource(drawableID);
        conditionView.setText(data.condition);
        humidityView.setText(data.humidity);
        timeView.setText(data.time);
        tempView.setText(data.temperature);
        skyView.setText(data.sky);
        locationView.setText(location);
        dateView.setText(date);

        if (!data.uvindex.equals("0")) { //If uvIndex is zero
            uvIndexView.setText(data.uvindex);
        } else {
            GridLayout gridLayout = findViewById(R.id.grid_layout);
            View view = findViewById(R.id.divider2);

            //Remove view
            gridLayout.removeView(view);
            gridLayout.removeView(uvIndexView);
        }
    }

}
