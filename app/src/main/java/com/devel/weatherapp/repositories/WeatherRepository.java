package com.devel.weatherapp.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.devel.weatherapp.api.ServiceGenerator;
import com.devel.weatherapp.api.WeatherApi;
import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.WeatherRes;
import com.devel.weatherapp.models.WeatherResponse;
import com.devel.weatherapp.persistence.WeatherDao;
import com.devel.weatherapp.persistence.WeatherDatabase;
import com.devel.weatherapp.utils.Constants;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRepository {


    private static final String TAG ="SaveCallResult" ;
    /**
     * Instantiate an instance of the repository
     */
    private static WeatherRepository instance;

    private WeatherDao weatherDao;


    private WeatherRepository(Context context) {
        weatherDao = WeatherDatabase.getInstance(context).getWeatherDao();
    }


    public static WeatherRepository getInstance(Context context) {
        if (instance == null) {
            instance = new WeatherRepository(context);
        }
        return instance;
    }




    public Call<WeatherRes> getWeather(String lat, String lon, String apiKey) {
        return ServiceGenerator.getWeatherApi().getCurrentWeatherData(lat,lon,apiKey);
    }

    public Call<WeatherRes> getWeatherByCity(String city, String apiKey)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherApi service = retrofit.create(WeatherApi.class);
        return service.getCurrentWeatherDataOfCity(
                city,
                apiKey
        );
    }



}
