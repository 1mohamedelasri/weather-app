package com.devel.weatherapp.viewmodels;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.models.WeatherRes;
import com.devel.weatherapp.models.WeatherResponse;
import com.devel.weatherapp.repositories.WeatherRepository;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.Resource;
import com.devel.weatherapp.utils.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends AndroidViewModel {

    private String TAG ="AppDebug";

    /**
     * Instantiate the weather repository.
     */
    private WeatherRepository mWeatherRepository;
    private final List<FavouriteItem> _favouriteItems = new ArrayList<>();
    private final MutableLiveData<WeatherForecast> _data = new MutableLiveData<>();
    private final MutableLiveData<WeatherForecast> _searchedCity= new MutableLiveData<>();
    private static WeatherViewModel instance;


    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mWeatherRepository = WeatherRepository.getInstance(application);

    }

    public static WeatherViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new WeatherViewModel(application);
        }
        return instance;
    }

    public LiveData<WeatherForecast> data() {
        return _data;
    }
    public LiveData<WeatherForecast> searchedResult() {
        return _searchedCity;
    }
    public List<FavouriteItem> getFavourtieItems() {
        return _favouriteItems;
    }

    public void getForecastByCurrentLocation(String lat , String lon , String apiKey) {

        mWeatherRepository = WeatherRepository.getInstance(getApplication());

        final Call<WeatherForecast> call = mWeatherRepository.getCurrentLocationForecast(lat,lon, apiKey);
        call.enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
               _data.postValue(response.body());
                mapWeatherToFavortie(response.body());

            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                _data.postValue(null);
            }
        });
    }

    public void getForecastByCity(String city , String apiKey) {

        mWeatherRepository = WeatherRepository.getInstance(getApplication());

        final Call<WeatherForecast> call = mWeatherRepository.getWeatherByCity(city, apiKey);
        call.enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                _data.postValue(response.body());
                _searchedCity.postValue(response.body());
                mapWeatherToFavortie(response.body());
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                _searchedCity.postValue(null);
            }
        });
    }

    public void mapWeatherToFavortie(WeatherForecast data) {

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
                    savedDailyForecasts.add(savedDailyForecast);
                }

                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
                String temperatureText = "";
                if (timeOfDay >= 0 && timeOfDay < 12) {
                } else if (timeOfDay >= 12 && timeOfDay < 16) {
                    temperatureText = (Utility.formatTemperature(getApplication(), savedDailyForecasts.get(0).getMorningTemp()));
                } else if (timeOfDay >= 16 && timeOfDay < 21) {
                    temperatureText = (Utility.formatTemperature(getApplication(), savedDailyForecasts.get(0).getEveningTemp()));
                } else if (timeOfDay >= 21 && timeOfDay < 24) {
                    temperatureText = (Utility.formatTemperature(getApplication(), savedDailyForecasts.get(0).getNightTemp()));
                }

                        _favouriteItems.add(new FavouriteItem(data.getCity().getId(),
                        data.getCity().getName(),
                        temperatureText,
                        savedDailyForecasts.get(0).getDescription(),
                        savedDailyForecasts));
            }

        }
    }

}
