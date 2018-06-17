package com.example.kongwenyao.weathertoday.hourly_forecast_db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class HourlyForecast {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String condition;

    public String time;

    public String temperature;

    public String uvindex;

    public String humidity;

    public String sky;

    public String date;

    @ColumnInfo(name = "day_of_week")
    public String dayOfWeek;

    public String tag;

}
