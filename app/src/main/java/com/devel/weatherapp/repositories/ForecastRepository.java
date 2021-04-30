package com.devel.weatherapp.repositories;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.devel.weatherapp.api.ServiceGenerator;
import com.devel.weatherapp.api.WeatherApi;
import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.SavedWeatherResp;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.models.WeatherRes;
import com.devel.weatherapp.persistence.ForecastDao;
import com.devel.weatherapp.persistence.ForecastDatabase;
import com.devel.weatherapp.utils.AppExecutors;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class ForecastRepository {

    private final ForecastDao forecastDao;
    private final WeatherApi weatherService;
    private final AppExecutors appExecutors;

    public ForecastRepository(Context context) {
        this.forecastDao = ForecastDatabase.getInstance(context).forecastDao();
        this.weatherService = ServiceGenerator.getWeatherApi();
        this.appExecutors = new AppExecutors();
    }

    public LiveData<Resource<List<SavedDailyForecast>>> loadForecast(String city, String numDays) {
        return new NetworkBoundResource<List<SavedDailyForecast>, WeatherForecast>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                if (item != null && item.getDailyForecasts() != null) {
                    List<SavedDailyForecast> savedDailyForecasts = new ArrayList<SavedDailyForecast>();
                    for (int i = 0; i < item.getDailyForecasts().size(); i++) {
                        SavedDailyForecast savedDailyForecast = new SavedDailyForecast();
                        savedDailyForecast.setLat(item.getCity().getCoord().getLat());
                        savedDailyForecast.setLon(item.getCity().getCoord().getLon());
                        savedDailyForecast.setDate(item.getDailyForecasts().get(i).getDt());
                        savedDailyForecast.setMaxTemp(item.getDailyForecasts().get(i).getTemp().getMax());
                        savedDailyForecast.setMinTemp(item.getDailyForecasts().get(i).getTemp().getMin());
                        savedDailyForecast.setDayTemp(item.getDailyForecasts().get(i).getTemp().getDay());
                        savedDailyForecast.setEveningTemp(item.getDailyForecasts().get(i).getTemp().getEve());
                        savedDailyForecast.setMorningTemp(item.getDailyForecasts().get(i).getTemp().getMorn());
                        savedDailyForecast.setNightTemp(item.getDailyForecasts().get(i).getTemp().getNight());
                        savedDailyForecast.setFeelslikeDay(item.getDailyForecasts().get(i).getFeelsLike().getDay());
                        savedDailyForecast.setFeelslikeEve(item.getDailyForecasts().get(i).getFeelsLike().getEve());
                        savedDailyForecast.setFeelslikeMorning(item.getDailyForecasts().get(i).getFeelsLike().getMorn());
                        savedDailyForecast.setFeelslikeNight(item.getDailyForecasts().get(i).getFeelsLike().getNight());
                        savedDailyForecast.setHumidity(item.getDailyForecasts().get(i).getHumidity());
                        savedDailyForecast.setWind(item.getDailyForecasts().get(i).getSpeed());
                        savedDailyForecast.setDescription(item.getDailyForecasts().get(i).getWeather().get(0).getDescription());
                        savedDailyForecast.setWeatherid(item.getDailyForecasts().get(i).getWeather().get(0).getId());
                        savedDailyForecast.setImageUrl(item.getDailyForecasts().get(i).getWeather().get(0).getIcon());
                        savedDailyForecasts.add(savedDailyForecast);
                    }
                    forecastDao.insertForecastList(savedDailyForecasts);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SavedDailyForecast> data) {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<SavedDailyForecast>> loadFromDb() {
                return forecastDao.loadForecast();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherForecast>> createCall() {
                return weatherService.getWeatherForecast(city, numDays, Constants.UNIT_SYSTEM, Constants.API_KEY);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    public LiveData<Resource<List<SavedDailyForecast>>> fetchForecast(String city, String numDays) {
        return new NetworkBoundResource<List<SavedDailyForecast>, WeatherForecast>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                forecastDao.deleteNewsTable();
                if (item != null && item.getDailyForecasts() != null) {
                    List<SavedDailyForecast> savedDailyForecasts = new ArrayList<SavedDailyForecast>();
                    for (int i = 0; i < item.getDailyForecasts().size(); i++) {
                        SavedDailyForecast savedDailyForecast = new SavedDailyForecast();
                        savedDailyForecast.setLat(item.getCity().getCoord().getLat());
                        savedDailyForecast.setLon(item.getCity().getCoord().getLon());
                        savedDailyForecast.setDate(item.getDailyForecasts().get(i).getDt());
                        savedDailyForecast.setMaxTemp(item.getDailyForecasts().get(i).getTemp().getMax());
                        savedDailyForecast.setMinTemp(item.getDailyForecasts().get(i).getTemp().getMin());
                        savedDailyForecast.setDayTemp(item.getDailyForecasts().get(i).getTemp().getDay());
                        savedDailyForecast.setEveningTemp(item.getDailyForecasts().get(i).getTemp().getEve());
                        savedDailyForecast.setMorningTemp(item.getDailyForecasts().get(i).getTemp().getMorn());
                        savedDailyForecast.setNightTemp(item.getDailyForecasts().get(i).getTemp().getNight());
                        savedDailyForecast.setFeelslikeDay(item.getDailyForecasts().get(i).getFeelsLike().getDay());
                        savedDailyForecast.setFeelslikeEve(item.getDailyForecasts().get(i).getFeelsLike().getEve());
                        savedDailyForecast.setFeelslikeMorning(item.getDailyForecasts().get(i).getFeelsLike().getMorn());
                        savedDailyForecast.setFeelslikeNight(item.getDailyForecasts().get(i).getFeelsLike().getNight());
                        savedDailyForecast.setHumidity(item.getDailyForecasts().get(i).getHumidity());
                        savedDailyForecast.setWind(item.getDailyForecasts().get(i).getSpeed());
                        savedDailyForecast.setDescription(item.getDailyForecasts().get(i).getWeather().get(0).getDescription());
                        savedDailyForecast.setWeatherid(item.getDailyForecasts().get(i).getWeather().get(0).getId());
                        savedDailyForecast.setImageUrl(item.getDailyForecasts().get(i).getWeather().get(0).getIcon());
                        savedDailyForecasts.add(savedDailyForecast);
                    }
                    forecastDao.insertForecastList(savedDailyForecasts);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SavedDailyForecast> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<SavedDailyForecast>> loadFromDb() {
                return forecastDao.loadForecast();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherForecast>> createCall() {
                return weatherService.getWeatherForecast(city, numDays, Constants.UNIT_SYSTEM, Constants.API_KEY);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }



    public LiveData<Resource<SavedWeatherResp>> loadWeatherResp(String city, String numDays) {
        return new NetworkBoundResource<SavedWeatherResp, WeatherRes>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull WeatherRes item) {

                SavedWeatherResp SavedWeatherResp = new SavedWeatherResp();
                SavedWeatherResp.clouds = item.clouds.toString();
                SavedWeatherResp.name = item.name;
                SavedWeatherResp.cod = item.cod;
                forecastDao.insertWeatherResp(SavedWeatherResp);
            }

            @Override
            protected boolean shouldFetch(@Nullable SavedWeatherResp data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<SavedWeatherResp> loadFromDb() {
                return forecastDao.loadWeatherRes();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherRes>> createCall() {
                return weatherService.getCurrentWeatherDataOfCity2(city, Constants.API_KEY);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    public LiveData<Resource<SavedWeatherResp>> fetchWeatherResp(String city, String numDays) {
        return new NetworkBoundResource<SavedWeatherResp, WeatherRes>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull WeatherRes item) {
                    forecastDao.deleteNewsTable();
                    SavedWeatherResp obj = new SavedWeatherResp();
                    obj.cod = item.cod;
                    obj.clouds = item.clouds.toString();
                    obj.name = item.name;
                    forecastDao.insertWeatherResp(obj);
            }

            @Override
            protected boolean shouldFetch(@Nullable SavedWeatherResp data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<SavedWeatherResp> loadFromDb() {
                return forecastDao.loadWeatherRes();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherRes>> createCall() {
                LiveData<ApiResponse<WeatherRes>> res = weatherService.getCurrentWeatherDataOfCity2(city, Constants.API_KEY);
                return res;
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

}
