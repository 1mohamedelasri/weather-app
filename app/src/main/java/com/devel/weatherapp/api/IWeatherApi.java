package com.devel.weatherapp.api;

import androidx.lifecycle.LiveData;

import com.devel.weatherapp.models.AirQuality;
import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.WeatherForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IWeatherApi {


    //User to fetch data without cache
    @GET("data/2.5/forecast?units=metric&cnt=1")
    Call<WeatherForecast> getCurrentWeatherDataOfCity(@Query("q") String city, @Query("APPID") String app_id);

    @GET("data/2.5/air_pollution?")
    Call<AirQuality> getAirQuality(@Query("lat") String city,
                                   @Query("lon") String numDays,
                                   @Query("APPID") String apiKey);


    // Used to fetch data with caching
    @GET("data/2.5/forecast?units=metric")
    LiveData<ApiResponse<WeatherForecast>> getCurrentHourlyDataOfCity(@Query("q") String city, @Query("APPID") String app_id);

    @GET("data/2.5/forecast?units=metric")
    LiveData<ApiResponse<WeatherForecast>> getCurrentLocationHourlyData(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id);

}
