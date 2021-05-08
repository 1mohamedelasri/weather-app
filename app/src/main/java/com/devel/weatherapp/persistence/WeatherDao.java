package com.devel.weatherapp.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.WeatherForecast;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WeatherDao {

    @Insert(onConflict = REPLACE)
    long[] insertWeather(WeatherForecast... WeatherForecast);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertForecastList(List<WeatherForecast> WeatherForecast);


    @Query("SELECT * FROM WeatherForecast")
    LiveData<List<WeatherForecast>> loadForecast();

    @Query("DELETE FROM WeatherForecast")
    void deleteAll();

    @Query("DELETE FROM WeatherForecast where id = :ids")
    void deleteFavouriteItem(Long ids);


}
