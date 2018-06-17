package com.example.kongwenyao.weathertoday.hourly_forecast_db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {HourlyForecast.class}, version = 1)
public abstract class HourlyForecastDatabase extends RoomDatabase {

    //Instance of this class
    private static HourlyForecastDatabase INSTANCE;

    //Data access object (DAO) interface
    public abstract HourlyForecastDao hourlyForecastModel();

    public static HourlyForecastDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                    HourlyForecastDatabase.class).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    @SuppressWarnings("unused")
    public static void deleteInstance() {
        INSTANCE = null;
    }
}
