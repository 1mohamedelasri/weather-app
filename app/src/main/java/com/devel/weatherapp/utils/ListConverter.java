package com.devel.weatherapp.utils;

import androidx.room.TypeConverter;

import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.WeatherList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ListConverter {

    @TypeConverter
    public static List<SavedDailyForecast> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<SavedDailyForecast>>() {
        }.getType();
        return new Gson().fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<SavedDailyForecast> someObjects) {
        return new Gson().toJson(someObjects);
    }
}
