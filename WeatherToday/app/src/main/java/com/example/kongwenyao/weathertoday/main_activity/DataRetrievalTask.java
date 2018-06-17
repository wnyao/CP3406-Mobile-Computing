package com.example.kongwenyao.weathertoday.main_activity;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.kongwenyao.weathertoday.hourly_forecast_db.HourlyForecastDatabase;
import com.example.kongwenyao.weathertoday.hourly_forecast_db.HourlyForecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class DataRetrievalTask extends AsyncTask<String, String, Map<String, String>> {

    private final String CONDITIONS = "conditions";
    private final String HOURLY = "hourly";

    private boolean isFahrenheit;
    private HourlyForecastDatabase mDb;
    private OnDataSendToActivity onDataSendToActivity;

    public DataRetrievalTask(Activity activity, HourlyForecastDatabase mDb, boolean isFahrenheit) {
        this.isFahrenheit = isFahrenheit;
        onDataSendToActivity = (OnDataSendToActivity) activity;
        this.mDb = mDb;
    }


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

                //Build data into string
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Assign key values
            if (string.contains(CONDITIONS)) {
                key = CONDITIONS;
            } else {
                key = HOURLY;
            }

            //Construct dictionary. Sample: {"conditions": "..." or "hourly": "..."}
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
                String[] todayWeather = getWeatherToday(jsonObject, isFahrenheit);
                onDataSendToActivity.onDataTodaySend(todayWeather);
            }

            if (jsonTexts.containsKey(HOURLY)) {
                jsonObject = new JSONObject(jsonTexts.get(HOURLY));
                getWeatherHourly(jsonObject, isFahrenheit);
                onDataSendToActivity.onDataHourlySet(true);
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void getWeatherHourly(JSONObject jsonObj, boolean isFahrenheit) throws JSONException {
        String condition, time, temp, uvIndex, humidity, sky, date, dayOfWeek, tag;
        JSONObject forecastData;
        JSONObject fctTime;

        JSONArray hourlyForecasts = jsonObj.getJSONArray("hourly_forecast"); //Array of hourly forecast data

        for (int i = 0; i < hourlyForecasts.length(); i++) {
            forecastData = hourlyForecasts.getJSONObject(i);
            fctTime = hourlyForecasts.getJSONObject(i).getJSONObject("FCTTIME");

            //Get Data from JSON
            condition = forecastData.getString("condition"); //Eg. "Chance of Thunderstorm"
            dayOfWeek = fctTime.getString("weekday_name"); //Eg. "Sunday"
            time = fctTime.getString("civil"); //Eg. "5:00 PM"
            date = fctTime.getString("mday"); //Eg. "17"
            humidity = forecastData.getString("humidity");
            uvIndex = forecastData.getString("uvi");
            sky = forecastData.getString("sky");

            if (isFahrenheit) {
                temp = forecastData.getJSONObject("temp").getString("english");
                temp = appendFahrenheitSymbol(temp);
            } else {
                temp = forecastData.getJSONObject("temp").getString("metric");
                temp = appendCelsiusSymbol(temp);
            }

            //Post processing data
            time = processTimeFormat(time); //Process time format
            humidity = humidity + "%";
            sky = sky + "%";
            tag = String.valueOf(i);

            //Add to database
            addHourlyForecast(mDb, condition, time, temp, uvIndex, humidity, sky, date, dayOfWeek, tag);
        }
    }

    //Add data to Room database
    private static void addHourlyForecast(final HourlyForecastDatabase mDb, String condition, String time,
                                             String temp, String uvIndex, String humidity, String sky,
                                             String date, String dayOfWeek, String tag) {
        HourlyForecast forecast = new HourlyForecast();
        forecast.condition = condition;
        forecast.time = time;
        forecast.temperature = temp;
        forecast.uvindex = uvIndex;
        forecast.humidity = humidity;
        forecast.sky = sky;
        forecast.date = date;
        forecast.dayOfWeek = dayOfWeek;
        forecast.tag = tag;
        mDb.hourlyForecastModel().insertForecast(forecast);
    }

    //Process time format for weather hourly data
    private String processTimeFormat(String time) {
        if (time.charAt(1) == ':') {
            time = time.charAt(0) + time.replaceAll("[:\\d+]", "");
        } else {
            time = time.substring(0, 2) + time.replaceAll("[:\\d+]", "");
        }
        return time.toLowerCase();
    }

    //Get today weather data in an array from JSON Object
    private String[] getWeatherToday(JSONObject jsonObj, Boolean isFahrenheit) throws JSONException {
        JSONObject currentObservation = jsonObj.getJSONObject("current_observation");

        //Get individual data
        String location = currentObservation.getJSONObject("display_location").getString("full");
        String date = currentObservation.getString("observation_time_rfc822"); //Eg. "Sun, 03 Dec 2017 16:52:46 +0800"
        String weather = currentObservation.getString("weather"); //Eg. "Mostly Cloudy"
        String temp;

        if (isFahrenheit) {
            temp = currentObservation.getString("temp_f");
            temp = appendFahrenheitSymbol(temp);
        } else {
            temp = currentObservation.getString("temp_c");
            temp = appendCelsiusSymbol(temp);
        }

        date = setDateFormat(date);
        return (new String[]{location, date, temp, weather});
    }

    //Append temperature with celsius symbol
    private String appendCelsiusSymbol(String temp) {
        return temp + " \u2103";
    }

    //Append temperature with fahrenheit symbol
    private String appendFahrenheitSymbol(String temp) {
        return temp + " \u2109";
    }

    //Process data of acquired date to Eg. "Sun, 03 Dec"
    private String setDateFormat (String datetime) {
        List<String> dateTimeArray = asList(datetime.split(" ")); //Eg: ["Sun,", "03", "Dec", "2017", "16:52:46", "+0800"]
        dateTimeArray = dateTimeArray.subList(0, 3); //Eg: ["Sun,", "03", "Dec"]
        dateTimeArray.set(0, dateTimeArray.get(0).replace(",", "")); //Eg: [Sun, 03, Dec]
        dateTimeArray.set(0, getDayOfWeek(dateTimeArray.get(0))); //Get word of the abbreviation of day

        //Process time value that initiated with number 0. Eg: 01...
        String date = dateTimeArray.get(1);
        if (date.charAt(0) == '0') {
            date = Character.toString(date.charAt(1));
            dateTimeArray.set(1, date);
        }

        return (dateTimeArray.get(0) + ", " + dateTimeArray.get(2) + " " + dateTimeArray.get(1));
    }

    //Get day of week in full word
    private String getDayOfWeek(String abbrev) {
        final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String dayOfWeek = null;

        for (String day: days) {
            if (day.contains(abbrev)) {
                dayOfWeek = day;
                break;
            }
        }
        return dayOfWeek;
    }

}
