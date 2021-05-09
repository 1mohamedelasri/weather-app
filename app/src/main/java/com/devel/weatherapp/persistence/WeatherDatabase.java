package com.devel.weatherapp.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.devel.weatherapp.models.WeatherForecast;

@Database(entities = {WeatherForecast.class}, version = 2)
public abstract class WeatherDatabase extends RoomDatabase {


    public static final String DATABASE_NAME = "weather_db";

    private static volatile WeatherDatabase instance;

    public static WeatherDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    WeatherDatabase.class,
                    DATABASE_NAME
            ).build();
        }

        return instance;
    }

    public abstract WeatherDao getWeatherDao();
}
