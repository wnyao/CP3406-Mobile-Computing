package com.example.kongwenyao.weathertoday;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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


public class MainActivity extends AppCompatActivity {

    private RetrieveData retrieveData;
    GifImageView weatherView;
    TextView conditionView;
    TextView locationView;
    TextView dateView;
    TextView tempView;

    private final String weatherConditions = "http://api.wunderground.com/api/e4287e3de768ea5e/conditions/q/autoip.json";
    private final String weatherHourly = "http://api.wunderground.com/api/e4287e3de768ea5e/hourly/q/autoip.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Custom Toolbar
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        //views instance references
        weatherView = findViewById(R.id.weather_Imageview);
        conditionView = findViewById(R.id.condition_view);
        locationView = findViewById(R.id.location_view);
        tempView = findViewById(R.id.temperature_view);
        dateView = findViewById(R.id.date_view);

        retrieveData = new RetrieveData();
        retrieveData.execute(weatherConditions, weatherHourly);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_menu:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class RetrieveData extends AsyncTask<String, String, Map<String, String>> {

        private final String conditions = "conditions";
        private final String hourly = "hourly";

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

                if (string.contains(conditions)) {
                    key = conditions;
                } else {
                    key = hourly;
                }

                assert stringBuffer != null;
                jsonText.put(key, stringBuffer.toString());
            }

            return jsonText;
        }

        @Override
        protected void onPostExecute(Map<String, String> jsonTxts) {
            super.onPostExecute(jsonTxts);

            JSONObject jsonObject;
            try {

                for (int i = 0; i < jsonTxts.size(); i++) {

                    if (jsonTxts.containsKey(conditions)) {
                        jsonObject = new JSONObject(jsonTxts.get(conditions));
                        getWeatherToday(jsonObject);
                    } else {
                        jsonObject = new JSONObject(jsonTxts.get(hourly));
                        getWeatherHourly(jsonObject);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void getWeatherHourly(JSONObject jsonObject) throws JSONException, IOException {
            //TODO: get data here as getWeather Today
            //TODO: create display method fot main weather and sub weather
            //TODO: set up settings
        }

        public void getWeatherToday(JSONObject jsonObject) throws JSONException, IOException {
            String location = jsonObject.getJSONObject("current_observation").getJSONObject("display_location").getString("full");
            String date = jsonObject.getJSONObject("current_observation").getString("observation_time_rfc822"); //Example: "Sun, 03 Dec 2017 16:52:46 +0800"
            String temperature = jsonObject.getJSONObject("current_observation").getString("temp_c");
            String weather = jsonObject.getJSONObject("current_observation").getString("weather");

            //String[] weatherToday = {location, date, temperature, weather};

            conditionView.setText(weather);
            locationView.setText(location);

            date = setDateFormat(date);
            dateView.setText(date);

            temperature = setTempCelsius(temperature);
            tempView.setText(temperature);

            int weatherImageId = getWeatherImageID(weather);
            weatherView.setImageResource(weatherImageId);
        }

        private int getWeatherImageID(String condition) throws IOException, JSONException {
            JSONObject weatherKeywords = getWeathersKeywords();
            JSONArray weatherTypes = weatherKeywords.names();
            condition = condition.toLowerCase();
            int weatherID = 0;

            for (int i = 0; i < weatherKeywords.length(); i++) {
                String weather = weatherTypes.getString(i);
                JSONArray keywords = weatherKeywords.getJSONArray(weather);

                for (int j = 0; j < keywords.length(); j++) {
                    if (condition.contains(keywords.getString(j).toLowerCase())) {
                        weatherID = getResources().getIdentifier(weather, "drawable", getPackageName());
                        break;
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

        public String setDateFormat (String datetime) {
            final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            Calendar now = Calendar.getInstance();
            String year = Integer.toString(now.get(Calendar.YEAR));

            List<String> dateTimeArray = asList(datetime.split(" "));    //Example: [Sun,, 03, Dec, 2017, 16:52:46, +0800]
            dateTimeArray = dateTimeArray.subList(0, dateTimeArray.indexOf(year));    //Example: [Sun,, 03, Dec]
            dateTimeArray.set(0, dateTimeArray.get(0).replace(",", ""));

            for (String day: days) {
                if (day.contains(dateTimeArray.get(0))) {
                    dateTimeArray.set(0, day);
                }
            }

            String date = dateTimeArray.get(1);
            if (date.charAt(0) == '0') {    //Example: 01 - 09
                date = Character.toString(date.charAt(1));
                dateTimeArray.set(1, date);
            }

            return (dateTimeArray.get(0) + ", " + dateTimeArray.get(2) + " " + dateTimeArray.get(1));
        }
    }

}

