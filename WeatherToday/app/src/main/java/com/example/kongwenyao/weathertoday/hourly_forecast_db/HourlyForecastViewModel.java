package com.example.kongwenyao.weathertoday.hourly_forecast_db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import java.util.List;

public class HourlyForecastViewModel extends AndroidViewModel {

    private HourlyForecastDatabase mDb;
    public List<HourlyForecast> forecast;
    public List<HourlyForecast> allForecasts;

    //Constructor
    public HourlyForecastViewModel(@NonNull Application application) {
        super(application);
        mDb = createDb();
    }

    //Create database
    private HourlyForecastDatabase createDb() {
        HourlyForecastDatabase mDb = HourlyForecastDatabase.getInMemoryDatabase(this.getApplication());
        return mDb;
    }

    //Get database instance
    public HourlyForecastDatabase getmDb() {
        return mDb;
    }

    //Get data of all hourly forecast
    public List<HourlyForecast> getAllForecasts() {
        allForecasts = mDb.hourlyForecastModel().getAllForecasts();
        return allForecasts;
    }

    //Find forecast based on tag
    public List<HourlyForecast> findForecastByTag(String tag) {
        forecast = mDb.hourlyForecastModel().findForecastByTag(tag);
        return forecast;
    }

    //Clear all records in database
    public void clearAll() {
        mDb.hourlyForecastModel().clearAll();
    }
}
