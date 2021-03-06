package com.devel.weatherapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.devel.weatherapp.utils.IdsConverter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "city")
public class City {


    @SerializedName("id")
    @Expose
    @PrimaryKey
    private Long id;

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName("coord")
    @Expose
    @Embedded
    private Coord coord;

    @SerializedName("country")
    @Expose
    @ColumnInfo(name = "country")
    private String country;

    @SerializedName("timezone")
    @Expose
    @ColumnInfo(name = "timezone")
    private Long timezone;

    @SerializedName("sunrise")
    @Expose
    @ColumnInfo(name = "sunrise")
    private Long sunrise;

    @SerializedName("sunset")
    @Expose
    @ColumnInfo(name = "sunset")
    private Long sunset;

    @SerializedName("dailyForcast")
    @Expose
    @ColumnInfo(name = "dailyForcast")
    @TypeConverters(IdsConverter.class)
    private List<Long> dailyForcastIds;


    /**
     * No args constructor for use in serialization
     */
    public City() {
    }

    /**
     * @param country
     * @param coord
     * @param sunrise
     * @param timezone
     * @param sunset
     * @param name
     * @param id
     */
    public City(Long id, String name, Coord coord, String country, Long timezone, Long sunrise, Long sunset) {
        super();
        this.id = id;
        this.name = name;
        this.coord = coord;
        this.country = country;
        this.timezone = timezone;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getTimezone() {
        return timezone;
    }

    public void setTimezone(Long timezone) {
        this.timezone = timezone;
    }

    public Long getSunrise() {
        return sunrise;
    }

    public void setSunrise(Long sunrise) {
        this.sunrise = sunrise;
    }

    public Long getSunset() {
        return sunset;
    }

    public void setSunset(Long sunset) {
        this.sunset = sunset;
    }

    public List<Long> getDailyForcastIds() {
        return dailyForcastIds;
    }

    public void setDailyForcastIds(List<Long> dailyForcastIds) {
        this.dailyForcastIds = dailyForcastIds;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coord=" + coord +
                ", country='" + country + '\'' +
                ", timezone=" + timezone +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof City) ) return false;

        City other = (City) o;
        if(this.id == other.id && name.equals(other.name))      return true;

        return false;

    }
}
