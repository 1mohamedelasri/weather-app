package com.devel.weatherapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AirQuality {

    @SerializedName("coord")
    @Expose
    private Coord coord;

    @SerializedName("list")
    @Expose
    private List<Air> airList = null;

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public List<Air> getAirList() {
        return airList;
    }

    public void setAirList(List<Air> airList) {
        this.airList = airList;
    }
}
