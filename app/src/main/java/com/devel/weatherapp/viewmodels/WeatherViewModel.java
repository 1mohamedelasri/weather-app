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

    public void insertInFavourtieItems(FavouriteItem wf){
        for(FavouriteItem ele : this.getFavourtieItems())
            if(ele.equals(wf)) return;

            this.getFavourtieItems().add(wf);
    }

    public void getForecastByCurrentLocation(String lat , String lon , String apiKey) {

        mWeatherRepository = WeatherRepository.getInstance(getApplication());

        final Call<WeatherForecast> call = mWeatherRepository.getCurrentLocationForecast(lat,lon, apiKey);
        call.enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
               _data.postValue(response.body());

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
                _searchedCity.postValue(response.body());
                //_data.postValue(response.body());

            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                _searchedCity.postValue(null);
            }
        });
    }

    public void addSearchCityToFavorties() {
        _data.postValue(_searchedCity.getValue());
    }
}
