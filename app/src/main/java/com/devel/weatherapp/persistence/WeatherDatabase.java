package com.devel.weatherapp.persistence;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.WeatherResponse;

@Database(entities = {WeatherResponse.class, SavedDailyForecast.class}, version = 1 )
public abstract class WeatherDatabase extends RoomDatabase {


    public static final String DATABASE_NAME = "weather_db";

    private static volatile WeatherDatabase instance;

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

    public abstract WeatherDao getWeatherDao();
}
