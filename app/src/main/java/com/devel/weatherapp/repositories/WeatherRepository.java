package com.devel.weatherapp.repositories;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.devel.weatherapp.api.IWeatherApi;
import com.devel.weatherapp.api.ServiceFactory;
import com.devel.weatherapp.models.AirQuality;
import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.persistence.WeatherDao;
import com.devel.weatherapp.persistence.WeatherDatabase;
import com.devel.weatherapp.utils.AppExecutors;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.NetworkBoundResource;
import com.devel.weatherapp.utils.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherRepository {

    private static final String TAG = "weatherRepository";

    private static WeatherRepository instance;
    private WeatherDao weatherDao;
    private IWeatherApi weatherApi;
    private Context context;
    public static WeatherRepository getInstance(Context context){
        if(instance == null){
            instance = new WeatherRepository(context);
        }
        return instance;
    }

    private WeatherRepository(Context context) {
        weatherDao = WeatherDatabase.getInstance(context).getWeatherDao();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherApi = ServiceFactory.getWeatherApi();
    }

    public LiveData<Resource<List<WeatherForecast>>> fetchForecast(String city) {
        return new NetworkBoundResource<List<WeatherForecast>, WeatherForecast>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                if (item != null && item.getDailyForecasts() != null) {
                    item.setId(item.getCity().getId());
                    weatherDao.insertWeather(item);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<WeatherForecast> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<WeatherForecast>> loadFromDb() {
                return weatherDao.loadForecast();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherForecast>> createCall() {
                return weatherApi.getCurrentHourlyDataOfCity(city, Constants.API_KEY);
            }

        }.getAsLiveData();
    }

    public LiveData<Resource<List<WeatherForecast>>> fetchForecastByLocation(String lat, String lon) {
        return new NetworkBoundResource<List<WeatherForecast>, WeatherForecast>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                weatherDao.deleteAll();
                if (item != null && item.getDailyForecasts() != null) {
                    item.setId(item.getCity().getId());
                    weatherDao.insertWeather(item);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<WeatherForecast> data) {
                //if(shouldFetch(data)) return false;
                return data == null || data.isEmpty() || computeShouldFetch(data.get(0));
            }

            @NonNull
            @Override
            protected LiveData<List<WeatherForecast>> loadFromDb() {
                return weatherDao.loadForecast();
            }
            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherForecast>> createCall() {
                return weatherApi.getCurrentLocationHourlyData(lat,lon, Constants.API_KEY);
            }
        }.getAsLiveData();
    }

    public final Call<WeatherForecast>  getWeatherByCity(String city, String apiKey)
    {
        return weatherApi.getCurrentWeatherDataOfCity(
                city,
                apiKey
        );
    }

    public void insertFavouriteDb(WeatherForecast weatherForecast) {
        this.weatherDao.insertWeather(weatherForecast);
    }

    public void dropFravourtieItem(WeatherForecast weatherForecast) {
        new InsertAsynTask(WeatherDatabase.getInstance(context)).execute(weatherForecast);
    }

    static class InsertAsynTask extends AsyncTask<WeatherForecast,Void,Void> {
        private WeatherDao weatherDao;
        InsertAsynTask(WeatherDatabase weatherDatabase)
        {
            weatherDao= weatherDatabase.getWeatherDao();
        }
        @Override
        protected Void doInBackground(WeatherForecast... weatherForecasts) {
            weatherDao.deleteFavouriteItem(weatherForecasts[0].getId());
            return null;
        }
    }

    public Call<AirQuality> getAirQuality(String lat, String lon)
    {
        return weatherApi.getAirQuality(
                lat,
                lon,
                Constants.API_KEY
        );
    }

    protected boolean computeShouldFetch(WeatherForecast data) {
        if(data ==null) return true;
        Log.d(TAG, "shouldFetch: recipe: " + data.toString());
        int currentTime = (int)(System.currentTimeMillis() / 1000);
        Log.d(TAG, "shouldFetch: current time: " + currentTime);
        Long lastRefresh = data.getTimestamp();
        Log.d(TAG, "shouldFetch: last refresh: " + lastRefresh);
        Log.d(TAG, "shouldFetch: it's been " + ((currentTime - lastRefresh) / 60 / 60 / 24) +
                " days since this Weather was refreshed. 30 days must elapse before refreshing. ");
        if((currentTime - data.getTimestamp()) >= Constants.WEATHER_REFRESH_TIME){
            Log.d(TAG, "shouldFetch: SHOULD REFRESH Weather with from api ?! " + true);
            return true;
        }
        Log.d(TAG, "shouldFetch: SHOULD REFRESH Weather?! " + false);
        return false;
    }


}












