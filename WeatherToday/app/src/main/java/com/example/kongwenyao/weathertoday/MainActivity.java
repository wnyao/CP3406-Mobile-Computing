package com.example.kongwenyao.weathertoday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

import static java.util.Arrays.asList;

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

public class MainActivity extends AppCompatActivity {

    private GifImageView weatherView;
    private TextView conditionView;
    private TextView locationView;
    private TextView dateView;
    private TextView tempView;

    private boolean storedFahrenheit;
    private int storedInterVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Custom Toolbar
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        //Views References
        weatherView = findViewById(R.id.weather_Imageview);
        conditionView = findViewById(R.id.condition_view);
        locationView = findViewById(R.id.location_view);
        tempView = findViewById(R.id.temperature_view);
        dateView = findViewById(R.id.date_view);
    }

    @Override
    public void onStart() {
        final String PREFS_NAME = "PREFS_FILE";

        //Shared Preferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        storedFahrenheit = sharedPreferences.getBoolean("FAHRENHEIT_SWITCH", false);
        storedInterVal = sharedPreferences.getInt("HOURLY_INTERVAL", 2);
        boolean enlargedVal = sharedPreferences.getBoolean("ENLARGED_SWITCH", false);
        EnlargedTextSetting(enlargedVal);

        //Data Retrieve Process
        final String weatherConditions = "http://api.wunderground.com/api/e4287e3de768ea5e/conditions/q/autoip.json";
        final String weatherHourly = "http://api.wunderground.com/api/e4287e3de768ea5e/hourly/q/autoip.json";
        RetrieveData retrieveData = new RetrieveData();
        retrieveData.execute(weatherConditions, weatherHourly);

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

    public void EnlargedTextSetting(boolean enlarge) {
        float sp = getResources().getDisplayMetrics().scaledDensity;
        final int enlargeVal = 15;

        //Get current text size in actual pixel
        float conditionTextSize = conditionView.getTextSize();
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

    public class RetrieveData extends AsyncTask<String, String, Map<String, String>> {

        private final String CONDITIONS = "conditions";
        private final String HOURLY = "hourly";
        private final String ICON_TYPE = "icon";
        private final String GIF_TYPE = "gif";

        @Override
        protected Map<String, String> doInBackground(String... strings) {
            BufferedReader bufferedReader = null;
            StringBuffer stringBuffer = null;

            Map<String, String> jsonText = new HashMap<>();
            String key;

            for (String string: strings) {
                try {
                    URL url = new URL(string);
                    bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

                    stringBuffer = new StringBuffer();
                    String line;

                    //Build read data into a string
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bufferedReader != null)
                            bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //Assign key value
                if (string.contains(CONDITIONS)) {
                    key = CONDITIONS;
                } else {
                    key = HOURLY;
                }

                //Construct dictionary object. Example: {"conditions": "...", "hourly": "..."}
                assert stringBuffer != null;
                jsonText.put(key, stringBuffer.toString());
            }
            return jsonText;
        }

        @Override
        protected void onPostExecute(Map<String, String> jsonTexts) {
            super.onPostExecute(jsonTexts);

            JSONObject jsonObject;
            try {
                if (jsonTexts.containsKey(CONDITIONS)) {
                    jsonObject = new JSONObject(jsonTexts.get(CONDITIONS));
                    String[] todayData = getWeatherToday(jsonObject, storedFahrenheit);
                    displayWeatherToday(todayData[0], todayData[1], todayData[2], todayData[3]);
                }

                if (jsonTexts.containsKey(HOURLY)) {
                    jsonObject = new JSONObject(jsonTexts.get(HOURLY));
                    Map<String, String[]> hourlyData = getWeatherHourly(jsonObject, storedInterVal);
                    displayWeatherHourly(hourlyData);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private Map<String, String[]> getWeatherHourly(JSONObject jsonObjInHourly, int intervalVal) throws JSONException, IOException {
            String condition, time;
            int counter = intervalVal;

            Map<String, String[]> weatherHourly = new HashMap<>();
            JSONArray hourlyForecast = jsonObjInHourly.getJSONArray("hourly_forecast"); //Array of hourly forecast data

            for (int i = 0; i < 3; i++) {
                condition = hourlyForecast.getJSONObject(counter).getString("condition"); //Example: "Chance of Thunderstorm"
                time = hourlyForecast.getJSONObject(counter).getJSONObject("FCTTIME").getString("civil"); //Example: "5:00 PM", "10:20 PM"

                String weatherImageID = String.valueOf(getWeatherImageID(condition, ICON_TYPE));
                time = processTimeFormat(time);

                weatherHourly.put(String.valueOf(i), new String[]{weatherImageID, time});
                counter += intervalVal;
            }
            return weatherHourly;
        }

        private void displayWeatherHourly(Map<String, String[]> weatherHourly) {
            int imageViewId, textViewId;
            ImageView subWeatherView;
            TextView subLabelView;
            String[] hourlyData;
            String key;

            for (int i = 0; i < weatherHourly.size(); i++) {
                //Get views id
                imageViewId = getResources().getIdentifier("sub_weather_" + i, "id", getPackageName());
                textViewId = getResources().getIdentifier("sub_label_" + i, "id", getPackageName());

                //View references
                subWeatherView = findViewById(imageViewId);
                subLabelView = findViewById(textViewId);

                key = String.valueOf(i);
                if (weatherHourly.containsKey(key)) {   //Example {"1": {"R.drawable.xxx", "1 pm"}, {"2": {"R.drawable.xxx", "2 pm"}}}
                    hourlyData = weatherHourly.get(key);
                    subWeatherView.setImageResource(Integer.parseInt(hourlyData[0]));
                    subLabelView.setText(hourlyData[1]);
                }

            }
        }

        public String processTimeFormat(String time) {
            if (time.charAt(1) == ':') {
                time = time.charAt(0) + time.replaceAll("[:\\d+]", "");
            } else {
                time = time.substring(0,2) + time.replaceAll("[:\\d+]", "");
            }
            return time.toLowerCase();
        }

        private String[] getWeatherToday(JSONObject jsonObjInConditions, Boolean fahrenheit) throws JSONException, IOException {
            String location = jsonObjInConditions.getJSONObject("current_observation").getJSONObject("display_location").getString("full");
            String date = jsonObjInConditions.getJSONObject("current_observation").getString("observation_time_rfc822"); //Example: "Sun, 03 Dec 2017 16:52:46 +0800"
            String weather = jsonObjInConditions.getJSONObject("current_observation").getString("weather");
            String temperature;

            if (fahrenheit) {
                temperature = jsonObjInConditions.getJSONObject("current_observation").getString("temp_f");
                temperature = setTempFahrenheit(temperature);
            } else {
                temperature = jsonObjInConditions.getJSONObject("current_observation").getString("temp_c");
                temperature = setTempCelsius(temperature);
            }

            date = setDateFormat(date);
            return (new String[]{location, date, temperature, weather});
        }

        private void displayWeatherToday(String location, String date, String temperature, String weather) throws IOException, JSONException {
            //Weather image view
            int weatherImageID = getWeatherImageID(weather,GIF_TYPE);
            weatherView.setImageResource(weatherImageID);

            //Weather condition text view
            conditionView.setText(weather);

            //Location text view
            locationView.setText(location);

            //Date text view
            dateView.setText(date);

            //Temperature text view
            tempView.setText(temperature);
        }

        private int getWeatherImageID(String condition, String imageType) throws IOException, JSONException {
            JSONObject weatherKeywords = getWeathersKeywords();
            JSONArray weatherTypes = weatherKeywords.names();   //Key values different weathers
            condition = condition.toLowerCase();
            int weatherID = 0;

            for (int i = 0; i < weatherKeywords.length(); i++) {
                String weather = weatherTypes.getString(i);
                JSONArray keywords = weatherKeywords.getJSONArray(weather);

                for (int j = 0; j < keywords.length(); j++) {
                    if (condition.contains(keywords.getString(j).toLowerCase())) {
                        if (imageType.equals(GIF_TYPE)) {
                            weatherID = getResources().getIdentifier("weather_" + weather, "drawable", getPackageName());
                            break;
                        } else {
                            weatherID = getResources().getIdentifier("icon_" + weather,"drawable", getPackageName());
                        }
                    }
                }
            }
            return weatherID;
        }

        private JSONObject getWeathersKeywords() throws IOException, JSONException {
            InputStream inputStream = getResources().openRawResource(R.raw.weathers_keywords);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            return (new JSONObject(stringBuilder.toString()));
        }

        public String setTempCelsius(String temperature) {
            return (temperature + " \u2103");
        }

        public String setTempFahrenheit(String temperature) {
            return (temperature + " \u2109");
        }

        public String setDateFormat (String datetime) {
            final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            Calendar now = Calendar.getInstance();
            String year = Integer.toString(now.get(Calendar.YEAR));

            List<String> dateTimeArray = asList(datetime.split(" "));    //Return for example: [Sun,, 03, Dec, 2017, 16:52:46, +0800]
            dateTimeArray = dateTimeArray.subList(0, dateTimeArray.indexOf(year));    //Return for example: [Sun,, 03, Dec]
            dateTimeArray.set(0, dateTimeArray.get(0).replace(",", ""));

            //Get full string value of an abbreviation of day. Example: "Mon" = "Monday"
            for (String day: days) {
                if (day.contains(dateTimeArray.get(0))) {
                    dateTimeArray.set(0, day);
                }
            }

            //Process time value that start from 0. Example: 01 - 09
            String date = dateTimeArray.get(1);
            if (date.charAt(0) == '0') {
                date = Character.toString(date.charAt(1));
                dateTimeArray.set(1, date);
            }

            return (dateTimeArray.get(0) + ", " + dateTimeArray.get(2) + " " + dateTimeArray.get(1));
        }
    }

}