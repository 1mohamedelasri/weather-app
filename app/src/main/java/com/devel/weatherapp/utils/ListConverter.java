package com.devel.weatherapp.utils;

import androidx.room.TypeConverter;

import com.devel.weatherapp.models.City;
import com.devel.weatherapp.models.WeatherList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ListConverter {

    @TypeConverter
    public static List<WeatherList> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<WeatherList>>() {
        }.getType();
        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<WeatherList> someObjects) {
        return new Gson().toJson(someObjects);
    }

    @TypeConverter
    public static City stringToCity(String data) {
        if (data == null) {
            return new City();
        }

        Type listType = new TypeToken<City>() {
        }.getType();
        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String cityToString(City someObjects) {
        return new Gson().toJson(someObjects);
    }


}
