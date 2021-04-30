package com.devel.weatherapp.persistence;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.SavedWeatherResp;
import com.devel.weatherapp.models.UviDb;
import com.devel.weatherapp.models.WeatherResponse;

@Database(entities = {SavedDailyForecast.class, UviDb.class, SavedWeatherResp.class},
        version = 4,
        exportSchema = false)

public abstract class WeatherDB extends RoomDatabase {

    public static final String DATABASE_NAME = "weather_db";

    private static WeatherDatabase instance;

    public static WeatherDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    WeatherDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    abstract public ForecastDao forecastDao();
}

