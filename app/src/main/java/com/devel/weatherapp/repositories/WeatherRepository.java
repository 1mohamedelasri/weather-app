package com.devel.weatherapp.repositories;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.devel.weatherapp.api.WeatherApi;
import com.devel.weatherapp.models.DailyForecast;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.models.WeatherRes;
import com.devel.weatherapp.persistence.WeatherDao;
import com.devel.weatherapp.persistence.WeatherDatabase;
import com.devel.weatherapp.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRepository {
    private final static String TAG = "WeatherRepository";

    private WeatherDatabase database;
    private LiveData<List<SavedDailyForecast>> savedDailyForecast;
    private WeatherApi service;

    public Call<WeatherForecast>  getCurrentLocationForecast(String lat, String lon, String apiKey)
    {

        return service.getCurrentLocationForecast(
                lat,
                lon,
                apiKey
        );
    }

    private static WeatherRepository instance;

    private WeatherDao weatherDao;


    private WeatherRepository(Context context) {
        weatherDao = WeatherDatabase.getInstance(context).getWeatherDao();
        database= WeatherDatabase.getInstance(context);
        savedDailyForecast = database.getWeatherDao().loadForecast();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(WeatherApi.class);
    }


    public static WeatherRepository getInstance(Context context) {
        if (instance == null) {
            instance = new WeatherRepository(context);
        }
        return instance;
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


    public Call<WeatherForecast> getWeatherByCity(String city, String apiKey)
    {
        return service.getCurrentWeatherDataOfCity(
                city,
                apiKey
        );
    }

    public Call<WeatherForecast>  getWeatherWeeklyCityData(String city, String days, String apiKey)
    {
         return service.getWeeklyForecast(
                city,
                days,
                apiKey
        );
    }

    protected boolean shouldFetch(WeatherForecast data) {
        Log.d(TAG, "shouldFetch: recipe: " + data.toString());
        int currentTime = (int)(System.currentTimeMillis() / 1000);
        Log.d(TAG, "shouldFetch: current time: " + currentTime);
        int lastRefresh = data.getTimestamp();
        Log.d(TAG, "shouldFetch: last refresh: " + lastRefresh);
        Log.d(TAG, "shouldFetch: it's been " + ((currentTime - lastRefresh) / 60 / 60 / 24) +
                " days since this recipe was refreshed. 30 days must elapse before refreshing. ");
        if((currentTime - data.getTimestamp()) >= Constants.WEATHER_REFRESH_TIME){
            Log.d(TAG, "shouldFetch: SHOULD REFRESH RECIPE?! " + true);
            return true;
        }
        Log.d(TAG, "shouldFetch: SHOULD REFRESH RECIPE?! " + false);
        return false;
    }

}
