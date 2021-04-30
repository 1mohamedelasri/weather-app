package com.devel.weatherapp.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.SavedWeatherResp;
import com.devel.weatherapp.models.UviDb;
import com.devel.weatherapp.models.WeatherRes;
import com.devel.weatherapp.models.WeatherResponse;

@Database(entities = {SavedWeatherResp.class, SavedDailyForecast.class, UviDb.class},
        version = 1,
        exportSchema = false)
public abstract class ForecastDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "weather_db";

    private static ForecastDatabase instance;

    public static ForecastDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ForecastDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    abstract public ForecastDao forecastDao();
}

