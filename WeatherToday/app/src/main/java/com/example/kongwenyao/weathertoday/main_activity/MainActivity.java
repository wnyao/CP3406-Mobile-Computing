package com.example.kongwenyao.weathertoday.main_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kongwenyao.weathertoday.HourlyForecastActivity;
import com.example.kongwenyao.weathertoday.InfoActivity;
import com.example.kongwenyao.weathertoday.R;
import com.example.kongwenyao.weathertoday.settings_activity.SettingsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

/**
 * WeatherToday application is an application that display weather condition of current weather or
 * forecast weathers happening in next few hours.
 *
 * MainActivity.class contains the functionality to retrieve data and display data in
 * activity_main.xml. The functionality of retrieving and displaying data are built through inner
 * class within the MainActivity.class. Reason of using inner class is due that android does not
 * allow application in performing network operation on its main thread, which is onCreate() or even
 * within methods executed through onCreate(). The MainActivity.class also include overridden method
 * declaration of onCreateOptionsMenu() and onOptionsItemSelected(). onCreateOptionsMenu() methods
 * is used to inflate custom Actionbar, while another handles the processing when an item of option
 * menu is clicked.
 *
 * Created by kongwenyao on 12/9/17.
 */

public class MainActivity extends AppCompatActivity implements OnDataSendToActivity, View.OnClickListener {

    private LinearLayout linearLayout;
    private GifImageView weatherView;
    private TextView conditionView;
    private TextView locationView;
    private TextView dateView;
    private TextView tempView;

    public static final String GIF_TYPE = "gif";
    private Map<String, String[]> hourlyForecastData;

    //Keys
    public static final String FORECAST = "FORECAST";
    public static final String LOCATION = "LOCATION";
    public static final String DATE = "DATE";
    public static final String PREFS_NAME = "PREFS_FILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Custom Toolbar
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        //Views References
        linearLayout = findViewById(R.id.linear_layout);
        weatherView = findViewById(R.id.weather_Imageview);
        conditionView = findViewById(R.id.condition_view);
        locationView = findViewById(R.id.location_view);
        tempView = findViewById(R.id.temperature_view);
        dateView = findViewById(R.id.date_view);
    }

    @Override
    public void onStart() {
        //Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        boolean isFahrenheit = sharedPreferences.getBoolean("FAHRENHEIT_SWITCH", false);
        boolean enlargedVal = sharedPreferences.getBoolean("ENLARGED_SWITCH", false);
        EnlargedTextSetting(enlargedVal, this);

        //Data Retrieve Process
        final String weatherToday = "http://api.wunderground.com/api/e4287e3de768ea5e/conditions/q/autoip.json";
        final String weatherHourly = "http://api.wunderground.com/api/e4287e3de768ea5e/hourly/q/autoip.json";
        DataRetrievalTask dataRetrieval = new DataRetrievalTask(this, isFahrenheit);
        dataRetrieval.execute(weatherToday, weatherHourly);

        super.onStart();
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

    //TODO: bug fix required
    public void EnlargedTextSetting(boolean enlarge, Activity activity) {
        float sp = activity.getResources().getDisplayMetrics().scaledDensity;
        final int enlargeVal = 15;

        //Get view references
        TextView conditionView = activity.findViewById(R.id.condition_view);
        TextView locationView = activity.findViewById(R.id.location_view);
        TextView dateView = activity.findViewById(R.id.date_view);

        //Get current text size in actual pixel
        float conditionTextSize = conditionView.getTextSize();;
        float locationTextSize = locationView.getTextSize();
        float dateTextSize = dateView.getTextSize();

        if (enlarge) {
            if ((conditionTextSize/sp) != 34) { //if text size not equal to 'enlarged value'
                conditionView.setTextSize((conditionTextSize + enlargeVal) / sp);
                locationView.setTextSize((locationTextSize + enlargeVal) / sp);
                dateView.setTextSize((dateTextSize + enlargeVal) / sp);
            }
        } else {
            if ((conditionTextSize/sp) != 29) {   //if text size not equal to 'not enlarged value'
                conditionView.setTextSize((conditionTextSize - enlargeVal) / sp);
                locationView.setTextSize((locationTextSize - enlargeVal) / sp);
                dateView.setTextSize((dateTextSize - enlargeVal) / sp);
            }
        }

    }

    @Override
    public void onDataTodaySend(String[] data) throws IOException, JSONException {
        //Get drawable id
        int weatherImageID = getWeatherImageID(data[3], GIF_TYPE, this);

        //Set views
        weatherView.setImageResource(weatherImageID);
        locationView.setText(data[0]);
        dateView.setText(data[1]);
        tempView.setText(data[2]);
        conditionView.setText(data[3]);
    }

    //Get drawable Id based on weather condition and image types
    public int getWeatherImageID(String condition, String imageType, Activity activity) throws IOException, JSONException {
        JSONObject weatherKeywords = getWeatherKeywords(activity.getResources());
        JSONArray weatherTypes = weatherKeywords.names(); //Get key values within JSON Object
        condition = condition.toLowerCase();
        int weatherID = 0;

        for (int i = 0; i < weatherKeywords.length(); i++) {
            String weather = weatherTypes.getString(i);
            JSONArray keywords = weatherKeywords.getJSONArray(weather);

            for (int j = 0; j < keywords.length(); j++) {
                if (condition.contains(keywords.getString(j).toLowerCase())) {
                    if (imageType.equals(GIF_TYPE)) {
                        weatherID = activity.getResources().getIdentifier("weather_" + weather,
                                "drawable", activity.getPackageName());
                        break;
                    } else {
                        weatherID = activity.getResources().getIdentifier("icon_" + weather,
                                "drawable", activity.getPackageName());
                    }
                }
            }
        }
        return weatherID;
    }

    //Get weather keywords in JSONObject, from raw resources
    private JSONObject getWeatherKeywords(Resources resources) throws IOException, JSONException {
        InputStream inputStream = resources.openRawResource(R.raw.weather_keywords);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return (new JSONObject(stringBuilder.toString()));
    }

    @Override
    public void onDataHourlySend(Map<String, String[]> data) throws IOException, JSONException {
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;
        String[] hourlyData;

        hourlyForecastData = data;
        for (int i = 0; i < data.size(); i++) {

            //Get hourly data
            hourlyData = data.get(String.valueOf(i)); //Eg. {condition, time, temp, uvIndex, humidity, sky}
            int drawableID = getWeatherImageID(hourlyData[0], "icon", this); //get drawable Id

            //Create views
            linearLayout = createLinearLayout(String.valueOf(i));
            textView = createTextView(hourlyData[1]);
            imageView = createImageView(drawableID);

            //View holder add views
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            this.linearLayout.addView(linearLayout);
        }
    }

    private LinearLayout createLinearLayout(String tag) {
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //View parameters
        params.setMargins(40, 0, 40, 0);
        linearLayout.setTag(tag);
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //Set onclick listener
        linearLayout.setOnClickListener(this);
        return linearLayout;
    }

    private TextView createTextView(String text) {

        TextView textView = new TextView(this);

        //View parameter
        textView.setTextAppearance(R.style.textView_textStyle);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(text);
        return textView;
    }

    private ImageView createImageView(int drawableId) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        //View parameter
        params.setMargins(50, 0, 50, 0);
        imageView.setLayoutParams(params);
        imageView.setImageDrawable(this.getDrawable(drawableId));
        return imageView;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() != null) {
            int index = Integer.parseInt((String) v.getTag());

            String[] forecast = hourlyForecastData.get(String.valueOf(index)); //Eg. {condition, time, temp, uvIndex, humidity, sky}

            //Intent
            Intent intent = new Intent(this, HourlyForecastActivity.class);
            intent.putExtra(FORECAST, forecast);
            intent.putExtra(LOCATION, locationView.getText());
            intent.putExtra(DATE, dateView.getText());
            startActivity(intent);
        }
    }

}