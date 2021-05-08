package com.devel.weatherapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.devel.weatherapp.utils.ListConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "WeatherForecast")
public class WeatherForecast {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    private Long id;

    @ColumnInfo(name = "city")
    @SerializedName("city")
    @Expose
    @TypeConverters({ListConverter.class})
    private City city;

    @SerializedName("cod")
    @Expose
    private String cod;

    @SerializedName("message")
    @Expose
    private Double message;

    @SerializedName("cnt")
    @Expose
    private Integer cnt;

    @TypeConverters(ListConverter.class)
    @SerializedName("list")
    @Expose
    private List<WeatherList> dailyForecasts = null;

    private int timestamp;


    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public List<WeatherList> getDailyForecasts() {
        return dailyForecasts;
    }

    public void setDailyForecasts(List<WeatherList> dailyForecasts) {
        this.dailyForecasts = dailyForecasts;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof WeatherForecast) ) return false;

        WeatherForecast other = (WeatherForecast) o;
        if(this.id != other.id && !city.equals(other.city))      return false;

        return true;

    }
}
