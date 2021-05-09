package com.devel.weatherapp.models;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.devel.weatherapp.utils.ListConverter;
import com.devel.weatherapp.utils.UtilityHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Entity
public class WeatherForecast implements Comparable<WeatherForecast>{

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

    @ColumnInfo(name = "list")
    @TypeConverters(ListConverter.class)
    @SerializedName("list")
    @Expose
    private List<WeatherList> dailyForecasts = null;

    @ColumnInfo(name = "timestamp")
    @SerializedName("timestamp")
    @Expose
    private Long timestamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());;




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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<WeatherList> getConvertedDaily(){

        List<WeatherList> dailyWeather = new ArrayList<>();
        final Map<Integer, List<WeatherList>> listMap =
                this.getDailyForecasts().stream().collect( Collectors.groupingBy(item -> UtilityHelper.timestampToDate(item.getDt()).getDay()));

        Set<Integer> keys = listMap.keySet();

        for (Integer key : keys) {
            List<WeatherList> w = (List<WeatherList>) listMap.get(key);
            dailyWeather.add(w.get(0));
        }
        return dailyWeather;
    }

    @Override
    public int compareTo(WeatherForecast o) {
        return getTimestamp().compareTo(o.getTimestamp());
    }
}
