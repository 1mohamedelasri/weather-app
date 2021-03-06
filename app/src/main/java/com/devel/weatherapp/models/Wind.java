package com.devel.weatherapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    @Expose
    @ColumnInfo(name = "speed")
    private Double speed;

    @SerializedName("deg")
    @Expose
    @ColumnInfo(name = "deg")
    private Long deg;

    /**
     * No args constructor for use in serialization
     */
    @Ignore
    public Wind() {
    }

    /**
     * @param deg
     * @param speed
     */
    public Wind(Double speed, Long deg) {
        super();
        this.speed = speed;
        this.deg = deg;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Long getDeg() {
        return deg;
    }

    public void setDeg(Long deg) {
        this.deg = deg;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "speed=" + speed +
                ", deg=" + deg +
                '}';
    }
}
