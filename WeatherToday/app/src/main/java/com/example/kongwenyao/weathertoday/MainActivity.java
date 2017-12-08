package com.example.kongwenyao.weathertoday;

import android.content.Intent;
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


public class MainActivity extends AppCompatActivity {

    private RetrieveData retrieveData;
    GifImageView weatherView;
    TextView conditionView;
    TextView locationView;
    TextView dateView;
    TextView tempView;
    ImageView subWeatherView;
    TextView subLabelView;

    private final String weatherConditions = "http://api.wunderground.com/api/e4287e3de768ea5e/conditions/q/autoip.json";
    private final String weatherHourly = "http://api.wunderground.com/api/e4287e3de768ea5e/hourly/q/autoip.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Custom Toolbar
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

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

                if (string.contains(CONDITIONS)) {
                    key = CONDITIONS;
                } else {
                    key = HOURLY;
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
                if (jsonTxts.containsKey(CONDITIONS)) {
                    jsonObject = new JSONObject(jsonTxts.get(CONDITIONS));
                    String[] todayData = getWeatherToday(jsonObject);
                    displayWeatherToday(todayData[0], todayData[1], todayData[2], todayData[3]);
                }

                if (jsonTxts.containsKey(HOURLY)) {
                    jsonObject = new JSONObject(jsonTxts.get(HOURLY));
                    Map<String, String[]> hourlyData = getWeatherHourly(jsonObject);
                    displayWeatherHourly(hourlyData);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //TODO: set up settings
        private Map<String, String[]> getWeatherHourly(JSONObject jsonObjInHourly) throws JSONException, IOException {

            String condition, time;
            int counter = 1;

            Map<String, String[]> weatherHourly = new HashMap<>();
            JSONArray hourlyForecast = jsonObjInHourly.getJSONArray("hourly_forecast");

            for (int i = 0; i < 3; i++) {
                condition = hourlyForecast.getJSONObject(counter).getString("condition"); //Example: "Chance of Thunderstorm"
                time = hourlyForecast.getJSONObject(counter).getJSONObject("FCTTIME").getString("civil"); //Example: "5:00 PM", "10:20 PM"

                String weatherImageID = String.valueOf(getWeatherImageID(condition, ICON_TYPE));
                time = processTimeFormat(time);

                weatherHourly.put(String.valueOf(i), new String[]{weatherImageID, time});
                counter += 2;
            }

            return weatherHourly;
            //Log.i("hourly", String.valueOf(hourlyForecast.length())); //test
        }

        public void displayWeatherHourly(Map<String, String[]> weatherHourly) {
            int imageViewId, textViewId;
            String key;
            String[] hourlyData;

            for (int i = 0; i < weatherHourly.size(); i++) {
                imageViewId = getResources().getIdentifier("sub_weather_" + i, "id", getPackageName());
                textViewId = getResources().getIdentifier("sub_label_" + i, "id", getPackageName());

                subWeatherView = findViewById(imageViewId);
                subLabelView = findViewById(textViewId);

                key = String.valueOf(i);
                if (weatherHourly.containsKey(key)) {
                    hourlyData = weatherHourly.get(key);
                    subWeatherView.setImageResource(Integer.parseInt(hourlyData[0]));
                    subLabelView.setText(hourlyData[1]);
                }

            }
        }

        public String processTimeFormat(String time) {
            time = time.charAt(0) + time.replaceAll("[:\\d+]", "");
            return time.toLowerCase();
        }

        public String[] getWeatherToday(JSONObject jsonObjInConditions) throws JSONException, IOException {
            String location = jsonObjInConditions.getJSONObject("current_observation").getJSONObject("display_location").getString("full");
            String date = jsonObjInConditions.getJSONObject("current_observation").getString("observation_time_rfc822"); //Example: "Sun, 03 Dec 2017 16:52:46 +0800"
            String temperature = jsonObjInConditions.getJSONObject("current_observation").getString("temp_c");
            String weather = jsonObjInConditions.getJSONObject("current_observation").getString("weather");

            date = setDateFormat(date);
            temperature = setTempCelsius(temperature);

            return (new String[]{location, date, temperature, weather});
        }

        public void displayWeatherToday(String location, String date, String temperature, String weather) throws IOException, JSONException {
            //Weather image view
            int weatherImageID = getWeatherImageID(weather,GIF_TYPE);
            weatherView = findViewById(R.id.weather_Imageview);
            weatherView.setImageResource(weatherImageID);

            //Weather condition text view
            conditionView = findViewById(R.id.condition_view);
            conditionView.setText(weather);

            //Location text view
            locationView = findViewById(R.id.location_view);
            locationView.setText(location);

            //Date text view
            dateView = findViewById(R.id.date_view);
            dateView.setText(date);

            //Temperature text view
            tempView = findViewById(R.id.temperature_view);
            tempView.setText(temperature);
        }


        private int getWeatherImageID(String condition, String imageType) throws IOException, JSONException {
            JSONObject weatherKeywords = getWeathersKeywords();
            JSONArray weatherTypes = weatherKeywords.names();
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

