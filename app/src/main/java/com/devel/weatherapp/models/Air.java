package com.devel.weatherapp.models;

import androidx.core.view.KeyEventDispatcher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Air {

    @SerializedName("main")
    @Expose
    public AirMain main;

    @SerializedName("components")
    @Expose
    public Component component;


    @SerializedName("dt")
    @Expose
    public long dt;

    public AirMain getCoord() {
        return main;
    }

    public void setCoord(AirMain main) {
        this.main = main;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public class AirMain {
        @SerializedName("aqi")
        @Expose
        public Integer aqi;

        public Integer getAqi() {
            return aqi;
        }

        public void setAqi(Integer aqi) {
            this.aqi = aqi;
        }
    }

    public class Component {
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

        public float getCo() {
            return co;
        }

        public void setCo(float co) {
            this.co = co;
        }

        public float getNo() {
            return no;
        }

        public void setNo(float no) {
            this.no = no;
        }

        public float getNo2() {
            return no2;
        }

        public void setNo2(float no2) {
            this.no2 = no2;
        }

        public float getO3() {
            return o3;
        }

        public void setO3(float o3) {
            this.o3 = o3;
        }

        public float getSo2() {
            return so2;
        }

        public void setSo2(float so2) {
            this.so2 = so2;
        }

        public float getPm2() {
            return pm2;
        }

        public void setPm2(float pm2) {
            this.pm2 = pm2;
        }

        public float getPm10() {
            return pm10;
        }

        public void setPm10(float pm10) {
            this.pm10 = pm10;
        }

        public float getNh3() {
            return nh3;
        }

        public void setNh3(float nh3) {
            this.nh3 = nh3;
        }
    }
}
