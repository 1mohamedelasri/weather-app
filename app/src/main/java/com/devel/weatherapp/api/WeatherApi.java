package com.devel.weatherapp.api;

import androidx.lifecycle.LiveData;

import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.models.WeatherRes;
import com.devel.weatherapp.models.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {


    @GET("data/2.5/weather?")
    LiveData<ApiResponse<WeatherResponse>> getWeather(
            @Query("id") int locationId,
            @Query("APPID") String apiKey,
            @Query("units") String metric,
            @Query("cnt") int count
    );

    @GET("data/2.5/forecast/daily/?units=metric&")
    Call<WeatherForecast> getCurrentLocationForecast(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id);

    @GET("data/2.5/forecast/daily/?units=metric&")
    Call<WeatherForecast> getCurrentWeatherDataOfCity(@Query("q") String city, @Query("APPID") String app_id);

    @GET("data/2.5/forecast/daily/?units=metric&")
    Call<WeatherForecast> getWeeklyForecast(@Query("q") String city,
                                            @Query("cnt") String numDays,
                                            @Query("APPID") String apiKey);
}
