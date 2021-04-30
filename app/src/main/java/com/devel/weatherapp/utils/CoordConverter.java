package com.devel.weatherapp.utils;

import androidx.room.TypeConverter;

import com.devel.weatherapp.models.Coord;

public class CoordConverter {
    @TypeConverter
    public Coord storedStringToLanguages(String value) {
        String[] res = value.split("-");
        return new Coord(Double.parseDouble(res[0]),Double.parseDouble(res[1]));
    }

    @TypeConverter
    public String languagesToStoredString(Coord cl) {
        return cl.getLat() +"-" + cl.getLon();
    }
}
