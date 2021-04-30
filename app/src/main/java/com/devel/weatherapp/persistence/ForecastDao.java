package com.devel.weatherapp.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.SavedWeatherResp;
import com.devel.weatherapp.models.UviDb;
import com.devel.weatherapp.models.WeatherRes;

import java.util.List;

@Dao
public interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertForecastList(List<SavedDailyForecast> savedDailyForecasts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUvi(UviDb uviDb);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeatherResp(SavedWeatherResp wRes);

    @Query("SELECT * FROM saveddailyforecast ORDER BY mdate ASC")
    LiveData<List<SavedDailyForecast>> loadForecast();


    @Query("SELECT * FROM SavedWeatherResp")
    LiveData<SavedWeatherResp> loadWeatherRes();

    @Query("SELECT * FROM uvidb ")
    LiveData<UviDb> loadUvi();

    @Query("DELETE FROM saveddailyforecast")
    void deleteNewsTable();

    @Query("DELETE FROM uvidb")
    void deleteUvi();

    @Query("DELETE FROM SavedWeatherResp")
    void deleteWeatherRes();

}
