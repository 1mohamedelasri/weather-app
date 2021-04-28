package com.devel.weatherapp.api;

import androidx.lifecycle.LiveData;

import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.WeatherResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {


    @GET("/data/2.5/forecast")
    LiveData<ApiResponse<WeatherResponse>> getWeather(
            @Query("id") int locationId,
            @Query("APPID") String apiKey,
            @Query("units") String metric,
            @Query("cnt") int count
    );


}
