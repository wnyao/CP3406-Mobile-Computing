package com.example.kongwenyao.weathertoday.hourly_forecast_db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface HourlyForecastDao {

    @Query("SELECT * FROM HourlyForecast")
    List<HourlyForecast> getAllForecasts();

    @Query("SELECT * FROM HourlyForecast WHERE HourlyForecast.tag LIKE :tag ")
    List<HourlyForecast> findForecastByTag(String tag);

    @Insert(onConflict = OnConflictStrategy.REPLACE) //Replace existing record if conflict occurs
    void insertForecast(HourlyForecast forecast);

    @Query("DELETE FROM HourlyForecast")
    void clearAll();
}
