package com.devel.weatherapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherRes {

    @SerializedName("coord")
    public Coord coord;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("weather")
    public ArrayList weather = new ArrayList();
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("rain")
    public Rain rain;
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("dt")
    public float dt;
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("cod")
    public float cod;
}
class Rain {
    @SerializedName("3h")
    public float h3;
}

