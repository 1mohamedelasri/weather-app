package com.devel.weatherapp.utils;

import androidx.room.TypeConverter;

import com.devel.weatherapp.models.Coord;
import com.devel.weatherapp.models.Sys;

public class SysConverter {
    @TypeConverter
    public Sys storedStringToLanguages(String value) {
        String[] res = value.split("-");
        Sys s = new Sys();
        s.country = res[0];
        s.sunrise = Long.parseLong(res[1]);
        s.sunset = Long.parseLong(res[2]);
        return s;
    }

    @TypeConverter
    public String languagesToStoredString(Sys cl) {
        return cl.country +"-" + cl.sunrise + "-" +cl.sunset;
    }
}
