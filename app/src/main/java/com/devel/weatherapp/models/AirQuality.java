package com.devel.weatherapp.models;

import androidx.room.ColumnInfo;

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

}
