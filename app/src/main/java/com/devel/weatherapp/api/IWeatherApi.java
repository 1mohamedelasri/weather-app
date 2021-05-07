package com.devel.weatherapp.api;

import androidx.lifecycle.LiveData;

import com.devel.weatherapp.models.AirQuality;
import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.WeatherForecast;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IWeatherApi {



    @GET("data/2.5/forecast/daily/?units=metric&cnt=7&")
    Call<WeatherForecast> getCurrentLocationForecast(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id);

    @GET("data/2.5/forecast/daily/?units=metric&cnt=7&")
    Call<WeatherForecast> getCurrentWeatherDataOfCity(@Query("q") String city, @Query("APPID") String app_id);

    @GET("data/2.5/forecast/daily/?units=metric&")
    Call<WeatherForecast> getWeeklyForecast(@Query("q") String city,
                                            @Query("cnt") String numDays,
                                            @Query("APPID") String apiKey);

    @GET("data/2.5/air_pollution?")
    Call<AirQuality> getAirQuality(@Query("lat") String city,
                                   @Query("lon") String numDays,
                                   @Query("APPID") String apiKey);


    @GET("data/2.5/forecast/daily/?units=metric&cnt=7&")
    LiveData<ApiResponse<WeatherForecast>> getCurrentWeeklyDataOfCity(@Query("q") String city, @Query("APPID") String app_id);

    @GET("data/2.5/forecast/daily/?units=metric&cnt=7&")
    LiveData<ApiResponse<WeatherForecast>> getCurrentLocationWeeklyData(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id);

    @GET("data/2.5/forecast/daily/?units=metric&cnt=7&")
    LiveData<ApiResponse<WeatherForecast>> getCurrentHourlyDataOfCity(@Query("q") String city, @Query("APPID") String app_id);

    @GET("data/2.5/forecast/daily/?units=metric&cnt=7&")
    LiveData<ApiResponse<WeatherForecast>> getCurrentLocationHourlyData(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id);

}
