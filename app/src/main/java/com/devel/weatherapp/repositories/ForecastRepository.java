package com.devel.weatherapp.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devel.weatherapp.api.ServiceGenerator;
import com.devel.weatherapp.api.WeatherApi;
import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.models.WeatherResponse;
import com.devel.weatherapp.persistence.WeatherDao;
import com.devel.weatherapp.persistence.WeatherDatabase;
import com.devel.weatherapp.utils.AppExecutors;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.NetworkBoundResource;
import com.devel.weatherapp.utils.Resource;
import com.devel.weatherapp.utils.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastRepository {

    private static final String TAG = "RecipeRepository";

    private static ForecastRepository instance;
    private WeatherDao weatherDao;
    private WeatherApi weatherApi;
    private Context context;
    public static ForecastRepository getInstance(Context context){
        if(instance == null){
            instance = new ForecastRepository(context);
        }
        return instance;
    }


    private ForecastRepository(Context context) {
        weatherDao = WeatherDatabase.getInstance(context).getWeatherDao();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherApi = ServiceGenerator.getWeatherApi();
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
                return weatherApi.getCurrentWeatherDataOfCityV2(city, Constants.API_KEY);
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
                return weatherApi.getCurrentLocationForecastV2(lat,lon, Constants.API_KEY);
            }

        }.getAsLiveData();
    }

    public LiveData<ApiResponse<WeatherForecast>> getWeatherByCity(String city, String apiKey)
    {
        return weatherApi.getCurrentWeatherDataOfCityV2(
                city,
                apiKey
        );
    }

    public void insertFavouriteDb(WeatherForecast favouriteItem) {
        this.weatherDao.insertWeather(mapWeatherToFavortie(favouriteItem));
    }

    public void dropFravourtieItem(FavouriteItem favouriteItem) {
        this.weatherDao.deleteFavouriteItem(favouriteItem.id);
    }
}












