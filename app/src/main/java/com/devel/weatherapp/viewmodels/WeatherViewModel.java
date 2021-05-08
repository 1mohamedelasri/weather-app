package com.devel.weatherapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.devel.weatherapp.models.AirQuality;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.repositories.WeatherRepository;
import com.devel.weatherapp.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends AndroidViewModel  {

    private String TAG ="WeatherViewModel";
    public static final String QUERY_EXHAUSTED = "No more results.";

    /**
     * Instantiate the weather repository.
     */
    private static WeatherViewModel instance;


    /**
     * Observables data declarations.
     */
    private WeatherRepository mWeatherRepository;
    private final MutableLiveData<WeatherForecast> _searchedCity= new MutableLiveData<>();
    private MediatorLiveData<Resource<List<WeatherForecast>>> _dataSource = new MediatorLiveData<>();
    private MutableLiveData<AirQuality> _airQuality = new MutableLiveData<>();
    // query extras
    private boolean isQueryExhausted;
    private boolean isPerformingQuery;
    private boolean cancelRequest;
    private long requestStartTime;


    // Observables
    public LiveData<Resource<List<WeatherForecast>>> getDataSource(){
        return _dataSource;
    }

    public LiveData<AirQuality> getAirQuality(){
        return _airQuality;
    }

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mWeatherRepository = WeatherRepository.getInstance(application);
    }
    List<FavouriteItem> test;

    public static WeatherViewModel getInstance(Application application) {
        if (instance == null) {
            instance = new WeatherViewModel(application);
        }
        return instance;
    }

    public LiveData<WeatherForecast> searchedResult() {
        return _searchedCity;
    }

    public void insertInFavourtieItems(WeatherForecast wf){
       /* for(WeatherForecast ele : this.getFavourtieItems())
            if(ele.equals(wf)) return;

            this.getFavourtieItems().add(wf);*/
    }


    public void searchWeatherByCity(String city , String apiKey) {

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
                Log.d("IWeatherApi", t.getMessage());
            }
        });
    }

    public void fetchbyCity(String city){
        final LiveData<Resource<List<WeatherForecast>>> repositorySource = mWeatherRepository.fetchForecast(_searchedCity.getValue().getCity().getName());
        fetchWithCaching(_dataSource, repositorySource);
    }

    public void fetchbyLocation(String lat, String lon){
        final LiveData<Resource<List<WeatherForecast>>> repositorySource = mWeatherRepository.fetchForecastByLocation(lat,lon);
        fetchWithCaching(_dataSource, repositorySource);
    }

    public void fetchWithCaching(MediatorLiveData<Resource<List<WeatherForecast>>> destinationRepo, LiveData<Resource<List<WeatherForecast>>> sourceRepo   ){
        requestStartTime = System.currentTimeMillis();
        cancelRequest = false;
        isPerformingQuery = true;
        destinationRepo.addSource(sourceRepo, new Observer<Resource<List<WeatherForecast>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<WeatherForecast>> listResource) {
                if(!cancelRequest){
                    if(listResource != null){
                        if(listResource.status == Resource.Status.SUCCESS){
                            Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                            Log.d(TAG, "onChanged: " + listResource.data);

                            isPerformingQuery = false;
                            if(listResource.data != null){
                                if(listResource.data.size() == 0 ){
                                    Log.d(TAG, "onChanged: query is exhausted...");
                                    destinationRepo.setValue(
                                            new Resource<List<WeatherForecast>>(
                                                    Resource.Status.ERROR,
                                                    listResource.data,
                                                    QUERY_EXHAUSTED
                                            )
                                    );
                                    isQueryExhausted = true;
                                }
                            }
                            destinationRepo.removeSource(sourceRepo);
                        }
                        else if(listResource.status == Resource.Status.ERROR){
                            Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                            isPerformingQuery = false;
                            if(listResource.message.equals(QUERY_EXHAUSTED)){
                                isQueryExhausted = true;
                            }
                            destinationRepo.removeSource(sourceRepo);
                        }
                        destinationRepo.setValue(listResource);
                    }
                    else{
                        destinationRepo.removeSource(sourceRepo);
                    }
                }
                else{
                    destinationRepo.removeSource(sourceRepo);
                }
            }
        });

    }

    public void dropFravourtieItem(WeatherForecast favouriteItem){
        mWeatherRepository.dropFravourtieItem(favouriteItem);
    }

    private AirQuality obj;
    public Call<AirQuality> getAirQuality(String lat , String lon) {

        mWeatherRepository = WeatherRepository.getInstance(getApplication());
        final Call<AirQuality> call = mWeatherRepository.getAirQuality(lat,lon);
        call.enqueue(new Callback<AirQuality>() {
            @Override
            public void onResponse(Call<AirQuality> call, Response<AirQuality> response) {
                if(response.body() != null) {
                    obj = response.body();
                    _airQuality.postValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<AirQuality> call, Throwable t) {
                _airQuality.postValue(null);
                Log.d("getAirQuality",t.getMessage());
            }
        });
        return call;
    }

}
