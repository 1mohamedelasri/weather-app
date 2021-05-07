package com.devel.weatherapp.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.SavedDailyForecast;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WeatherDao {

    @Insert(onConflict = REPLACE)
    long[] insertWeather(FavouriteItem... FavouriteItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertForecastList(List<FavouriteItem> FavouriteItem);


    @Query("SELECT * FROM FavouriteItem")
    LiveData<List<FavouriteItem>> loadForecast();

    @Query("DELETE FROM FavouriteItem")
    void deleteAll();

    @Query("DELETE FROM FavouriteItem where id = :id")
    void deleteFavouriteItem(Long id);


}
