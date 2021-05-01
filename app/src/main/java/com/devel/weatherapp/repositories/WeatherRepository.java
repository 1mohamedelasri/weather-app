package com.devel.weatherapp.repositories;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.devel.weatherapp.api.ServiceGenerator;
import com.devel.weatherapp.api.WeatherApi;
import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.DailyForecast;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.models.WeatherRes;
import com.devel.weatherapp.persistence.WeatherDao;
import com.devel.weatherapp.persistence.WeatherDatabase;
import com.devel.weatherapp.utils.AppExecutors;
import com.devel.weatherapp.utils.Constants;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRepository {


    private WeatherDatabase database;
    private LiveData<List<SavedDailyForecast>> savedDailyForecast;
    private WeatherApi service;

    public WeatherRepository(Application application)
    {
        database= WeatherDatabase.getInstance(application);
        savedDailyForecast = database.getWeatherDao().loadForecast();
    }

    public void insert(List<SavedDailyForecast> actorList){
        new InsertAsynTask(database).execute(actorList);
    }

    static class InsertAsynTask extends AsyncTask<List<SavedDailyForecast>,Void,Void> {
        private WeatherDao weatherDao;
        InsertAsynTask(WeatherDatabase weatherDatabase)
        {
            weatherDao= weatherDatabase.getWeatherDao();
        }
        @Override
        protected Void doInBackground(List<SavedDailyForecast>... lists) {
            weatherDao.insertForecastList(lists[0]);
            return null;
        }
    }


    public Call<WeatherRes> getWeather(String lat, String lon, String apiKey) {
        return ServiceGenerator.getWeatherApi().getCurrentWeatherData(lat,lon,apiKey);
    }

    public Call<WeatherRes> getWeatherByCity(String city, String apiKey)
    {
        return service.getCurrentWeatherDataOfCity(
                city,
                apiKey
        );
    }

    public Call<WeatherForecast>  getWeatherWeeklyCityData(String city, String days, String apiKey)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(WeatherApi.class);

        return service.getWeatherForecast(
                city,
                days,
                apiKey
        );
    }




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

}
