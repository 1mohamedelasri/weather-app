package com.devel.weatherapp.viewmodels;


import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.SavedWeatherResp;
import com.devel.weatherapp.models.UviDb;
import com.devel.weatherapp.models.WeatherRes;
import com.devel.weatherapp.repositories.ForecastRepository;
import com.devel.weatherapp.utils.Resource;

import java.util.List;

import javax.inject.Inject;

public class ForecastViewModel extends ViewModel {

    private ForecastRepository forecastRepository;

    public ForecastViewModel(ForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
    }

    public LiveData<Resource<List<SavedDailyForecast>>> fetchResults(String city, String numDays) {
        return forecastRepository.fetchForecast(city, numDays);
    }

    public LiveData<Resource<SavedWeatherResp>> fetchWeatherRes(String city, String numDays) {
        return forecastRepository.fetchWeatherResp(city, numDays);
    }


}
