package com.devel.weatherapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.devel.weatherapp.utils.ListConverter;
import com.google.gson.annotations.Expose;

import java.util.List;

@Entity(tableName = "FavouriteItem")
public class FavouriteItem {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    public Long id;

    @ColumnInfo(name = "city")
    public String city;

    @ColumnInfo(name = "country")
    public String country;

    @ColumnInfo(name = "temperature")
    public String temperature;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "savedDailyForecast")
    @TypeConverters(ListConverter.class)
    @Expose
    public List<SavedDailyForecast> savedDailyForecast;

    @ColumnInfo(name = "feelsLike")
    public String feelsLike;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "timestamp")
    public int timestamp;


    public FavouriteItem(Long id, String city, String temperature,String feelsLike,String date, String description, String country, List<SavedDailyForecast> savedDailyForecast) {
        this.city = city;
        this.temperature = temperature;
        this.description = description;
        this.savedDailyForecast = savedDailyForecast;
        this.id = id;
        this.country = country;
        this.feelsLike = feelsLike;
        this.date = date ; }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof FavouriteItem) ) return false;

        FavouriteItem other = (FavouriteItem) o;
        if(this.id != other.id && !city.equals(other.city))      return false;

        return true;

    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
