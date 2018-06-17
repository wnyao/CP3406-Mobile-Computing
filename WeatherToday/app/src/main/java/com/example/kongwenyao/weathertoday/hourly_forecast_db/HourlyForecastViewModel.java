package com.example.kongwenyao.weathertoday.hourly_forecast_db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class HourlyForecastViewModel extends AndroidViewModel {

    private HourlyForecastDatabase mDb;
    public List<HourlyForecast> forecast;
    public List<HourlyForecast> allForecasts;

    public HourlyForecastViewModel(@NonNull Application application) {
        super(application);
        mDb = createDb();
    }

    private HourlyForecastDatabase createDb() {
        HourlyForecastDatabase mDb = HourlyForecastDatabase.getInMemoryDatabase(this.getApplication());
        return mDb;
    }

    public HourlyForecastDatabase getmDb() {
        return mDb;
    }

    public List<HourlyForecast> getAllForecasts() {
        allForecasts = mDb.hourlyForecastModel().getAllForecasts();
        return allForecasts;
    }

    public List<HourlyForecast> findForecastByTag(String tag) {
        forecast = mDb.hourlyForecastModel().findForecastByTag(tag);
        return forecast;
    }

    public void insertForecast(HourlyForecast forecast) {
        mDb.hourlyForecastModel().insertForecast(forecast);
    }

    public void clearAll() {
        mDb.hourlyForecastModel().clearAll();
    }
}
