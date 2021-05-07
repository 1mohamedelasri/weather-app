package com.devel.weatherapp.repositories;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.devel.weatherapp.api.ServiceFactory;
import com.devel.weatherapp.api.IWeatherApi;
import com.devel.weatherapp.models.AirQuality;
import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.persistence.WeatherDao;
import com.devel.weatherapp.persistence.WeatherDatabase;
import com.devel.weatherapp.utils.AppExecutors;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.NetworkBoundResource;
import com.devel.weatherapp.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherRepository {

    private static final String TAG = "RecipeRepository";

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

    public LiveData<Resource<List<FavouriteItem>>> fetchForecast(String city) {
        return new NetworkBoundResource<List<FavouriteItem>, WeatherForecast>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                if (item != null && item.getDailyForecasts() != null) {

                    weatherDao.insertWeather(mapWeatherToFavortie(item));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<FavouriteItem> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<FavouriteItem>> loadFromDb() {
                return weatherDao.loadForecast();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherForecast>> createCall() {
                return weatherApi.getCurrentHourlyDataOfCity(city, Constants.API_KEY);
            }

        }.getAsLiveData();
    }

    public FavouriteItem mapWeatherToFavortie(WeatherForecast data) {

        if (data != null) {
            if (data != null && data.getDailyForecasts() != null) {
                List<SavedDailyForecast> savedDailyForecasts = new ArrayList<SavedDailyForecast>();

                for (int i = 0; i < data.getDailyForecasts().size() - 1; i++) {
                    SavedDailyForecast savedDailyForecast = new SavedDailyForecast();
                    savedDailyForecast.setLat(data.getCity().getCoord().getLat());
                    savedDailyForecast.setLon(data.getCity().getCoord().getLon());
                    savedDailyForecast.setDate(data.getDailyForecasts().get(i).getDt());
                    savedDailyForecast.setMaxTemp(data.getDailyForecasts().get(i).getTemp().getMax());
                    savedDailyForecast.setMinTemp(data.getDailyForecasts().get(i).getTemp().getMin());
                    savedDailyForecast.setDayTemp(data.getDailyForecasts().get(i).getTemp().getDay());
                    savedDailyForecast.setEveningTemp(data.getDailyForecasts().get(i).getTemp().getEve());
                    savedDailyForecast.setMorningTemp(data.getDailyForecasts().get(i).getTemp().getMorn());
                    savedDailyForecast.setNightTemp(data.getDailyForecasts().get(i).getTemp().getNight());
                    savedDailyForecast.setFeelslikeDay(data.getDailyForecasts().get(i).getFeelsLike().getDay());
                    savedDailyForecast.setFeelslikeEve(data.getDailyForecasts().get(i).getFeelsLike().getEve());
                    savedDailyForecast.setFeelslikeMorning(data.getDailyForecasts().get(i).getFeelsLike().getMorn());
                    savedDailyForecast.setFeelslikeNight(data.getDailyForecasts().get(i).getFeelsLike().getNight());
                    savedDailyForecast.setHumidity(data.getDailyForecasts().get(i).getHumidity());
                    savedDailyForecast.setWind(data.getDailyForecasts().get(i).getSpeed());
                    savedDailyForecast.setDescription(data.getDailyForecasts().get(i).getWeather().get(0).getDescription());
                    savedDailyForecast.setWeatherid(data.getDailyForecasts().get(i).getWeather().get(0).getId());
                    savedDailyForecast.setImageUrl(data.getDailyForecasts().get(i).getWeather().get(0).getIcon());
                    savedDailyForecast.setPressure(data.getDailyForecasts().get(i).getHumidity());
                    savedDailyForecast.setMain(data.getDailyForecasts().get(i).getWeather().get(0).getMain());
                    savedDailyForecast.setClouds(data.getDailyForecasts().get(i).getClouds());
                    savedDailyForecast.setSunrise(data.getDailyForecasts().get(i).getSunrise());
                    savedDailyForecast.setSunset(data.getDailyForecasts().get(i).getSunset());
                    savedDailyForecasts.add(savedDailyForecast);
                }
               /* SharedPreferences.getInstance(context).putStringValue(NOT_TITLE, savedDailyForecasts.get(0).getDescription());
                SharedPreferences.getInstance(context).putStringValue(NOT_BODY,
                        "the weather feels like " + savedDailyForecasts.get(0).getFeelslikeDay() + "C°"
                );*/

                return new FavouriteItem(data.getCity().getId(),
                        data.getCity().getName(),
                        savedDailyForecasts.get(0).getDescription(),
                        data.getCity().getCountry(),
                        savedDailyForecasts);
            }

        }
        return null;
    }

    public LiveData<Resource<List<FavouriteItem>>> fetchForecastByLocation(String lat, String lon) {
        return new NetworkBoundResource<List<FavouriteItem>, WeatherForecast>(AppExecutors.getInstance()) {

            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                weatherDao.deleteAll();
                if (item != null && item.getDailyForecasts() != null) {

                    weatherDao.insertWeather(mapWeatherToFavortie(item));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<FavouriteItem> data) {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<FavouriteItem>> loadFromDb() {
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

    public void insertFavouriteDb(WeatherForecast favouriteItem) {
        this.weatherDao.insertWeather(mapWeatherToFavortie(favouriteItem));
    }

    public void dropFravourtieItem(FavouriteItem favouriteItem) {
        new InsertAsynTask(WeatherDatabase.getInstance(context)).execute(favouriteItem);
    }

    static class InsertAsynTask extends AsyncTask<FavouriteItem,Void,Void> {
        private WeatherDao weatherDao;
        InsertAsynTask(WeatherDatabase weatherDatabase)
        {
            weatherDao= weatherDatabase.getWeatherDao();
        }
        @Override
        protected Void doInBackground(FavouriteItem... favouriteItem) {
            weatherDao.deleteFavouriteItem(favouriteItem[0].id);
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

    public Call<WeatherForecast>  getCurrentLocationForecast(String lat, String lon, String apiKey)
    {

        return weatherApi.getCurrentLocationForecast(
                lat,
                lon,
                apiKey
        );
    }


}












