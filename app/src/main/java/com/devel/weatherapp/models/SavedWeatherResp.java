package com.devel.weatherapp.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class SavedWeatherResp implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @SerializedName("lat")
    public double lat;
    @SerializedName("lon")
    public double lon;

    @SerializedName("clouds")
    public String clouds;
    @SerializedName("name")
    public String name;
    @SerializedName("cod")
    public float cod;

}
