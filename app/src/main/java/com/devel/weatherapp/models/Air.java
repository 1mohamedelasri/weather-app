package com.devel.weatherapp.models;

import androidx.core.view.KeyEventDispatcher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Air {

    @SerializedName("main")
    @Expose
    public AirMain coord;

    @SerializedName("components")
    @Expose
    public Component component;


    @SerializedName("dt")
    @Expose
    public long dt;


    class AirMain {
        @SerializedName("aqi")
        @Expose
        public Integer aqi;
    }

    class Component {
        @SerializedName("co")
        @Expose
        public float co;
        @SerializedName("no")
        @Expose
        public float no;
        @SerializedName("no2")
        @Expose
        public float no2;
        @SerializedName("o3")
        @Expose
        public float o3;
        @SerializedName("so2")
        @Expose
        public float so2;
        @SerializedName("pm2_5")
        @Expose
        public float pm2;
        @SerializedName("pm10")
        @Expose
        public float pm10;
        @SerializedName("nh3")
        @Expose
        public float nh3;
    }
}
