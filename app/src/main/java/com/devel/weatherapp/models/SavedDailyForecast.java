package com.devel.weatherapp.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class SavedDailyForecast implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;
    @SerializedName("mdate")
    public long mdate;
    @SerializedName("temperatureMin")
    public double minTemp;
    @SerializedName("temperatureMax")
    public double maxTemp;
    @SerializedName("temperatureDay")
    public double dayTemp;
    @SerializedName("temperatureNight")
    public double nightTemp;
    @SerializedName("temperatureEvening")
    public double eveningTemp;
    @SerializedName("temperatureMorning")
    public double morningTemp;
    @SerializedName("feelslikeDay")
    public double mfeelslikeDay;
    @SerializedName("feelslikeNight")
    public double mfeelslikeNight;
    @SerializedName("feelslikeEvening")
    public double mfeelslikeEve;
    @SerializedName("feelslikeMorning")
    public double mfeelslikeMorning;
    @SerializedName("humidity")
    public int mhumidity;
    @SerializedName("wind")
    public double mwind;
    @SerializedName("pressure")
    public double pressure;
    @SerializedName("description")
    public String mdescription;
    @SerializedName("weatherid")
    public Long weatherid;
    @SerializedName("main")
    public String main;
    @SerializedName("clouds")
    public Integer clouds;

    @SerializedName("imageUrl")
    public String imageUrl;

    @SerializedName("sunrise")
    @Expose
    private Double sunrise;

    @SerializedName("sunset")
    @Expose
    private Double sunset;


    public SavedDailyForecast() {
        this.mdescription = "feelslikeshiit";
        this.minTemp = 22;
        this.dayTemp = 565554;
        this.mhumidity = 44;
        this.weatherid= Long.valueOf(200);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getDate() {
        return mdate;
    }

    public void setDate(long mdate) {
        this.mdate = mdate;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getDayTemp() {
        return dayTemp;
    }

    public void setDayTemp(double dayTemp) {
        this.dayTemp = dayTemp;
    }

    public double getNightTemp() {
        return nightTemp;
    }

    public void setNightTemp(double nightTemp) {
        this.nightTemp = nightTemp;
    }

    public double getEveningTemp() {
        return eveningTemp;
    }

    public void setEveningTemp(double eveningTemp) {
        this.eveningTemp = eveningTemp;
    }

    public double getMorningTemp() {
        return morningTemp;
    }

    public void setMorningTemp(double morningTemp) {
        this.morningTemp = morningTemp;
    }


    public double getFeelslikeDay() {
        return mfeelslikeDay;
    }

    public void setFeelslikeDay(double feelslikeDay) {
        this.mfeelslikeDay = feelslikeDay;
    }

    public double getFeelslikeNight() {
        return mfeelslikeNight;
    }

    public void setFeelslikeNight(double feelslikeNight) {
        this.mfeelslikeNight = feelslikeNight;
    }

    public double getFeelslikeEve() {
        return mfeelslikeEve;
    }

    public void setFeelslikeEve(double feelslikeEve) {
        this.mfeelslikeEve = feelslikeEve;
    }

    public double getFeelslikeMorning() {
        return mfeelslikeMorning;
    }

    public void setFeelslikeMorning(double feelslikeMorning) {
        this.mfeelslikeMorning = feelslikeMorning;
    }

    public int getHumidity() {
        return mhumidity;
    }

    public void setHumidity(int humidity) {
        this.mhumidity = humidity;
    }

    public double getWind() {
        return mwind;
    }

    public void setWind(double wind) {
        this.mwind = wind;
    }

    public String getDescription() {
        return mdescription;
    }

    public void setDescription(String description) {
        this.mdescription = description;
    }

    public Long getWeatherid() {
        return weatherid;
    }

    public void setWeatherid(Long weatherid) {
        this.weatherid = weatherid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public Integer getClouds() {
        return clouds;
    }

    public void setClouds(Integer clouds) {
        this.clouds = clouds;
    }

    public Double getSunrise() {
        return sunrise;
    }

    public void setSunrise(Double sunrise) {
        this.sunrise = sunrise;
    }

    public Double getSunset() {
        return sunset;
    }

    public void setSunset(Double sunset) {
        this.sunset = sunset;
    }
}
