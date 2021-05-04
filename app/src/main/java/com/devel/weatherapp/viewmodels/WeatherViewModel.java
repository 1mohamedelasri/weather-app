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
import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.repositories.ForecastRepository;
import com.devel.weatherapp.repositories.WeatherRepository;
import com.devel.weatherapp.utils.Constants;
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
    private WeatherRepository mWeatherRepository;
    private final List<FavouriteItem> _favouriteItems = new ArrayList<>();
    private final MutableLiveData<WeatherForecast> _data = new MutableLiveData<>();
    private final MutableLiveData<WeatherForecast> _searchedCity= new MutableLiveData<>();
    private static WeatherViewModel instance;
    private ForecastRepository forecastRepository = ForecastRepository.getInstance(getApplication());
    private MediatorLiveData<Resource<List<FavouriteItem>>> _dataSource = new MediatorLiveData<>();
    private MediatorLiveData<Resource<List<FavouriteItem>>> _searchedData = new MediatorLiveData<>();

    public MediatorLiveData<Resource<List<FavouriteItem>>> getDataSource(){
        return _dataSource;
    }

    public MediatorLiveData<Resource<List<FavouriteItem>>> getSearchedDataSource(){
        return _searchedData;
    }
    // query extras
    private boolean isQueryExhausted;
    private boolean isPerformingQuery;
    private int pageNumber;
    private String query;
    private boolean cancelRequest;
    private long requestStartTime;


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
        forecastRepository.insertFavouriteDb(_searchedCity.getValue());
    }


    public void fetchbyCity(String city){
        final LiveData<Resource<List<FavouriteItem>>> repositorySource = forecastRepository.fetchForecast(_searchedCity.getValue().getCity().getName());
        fetchWithCaching(repositorySource,_dataSource);
    }

    public void fetchbyLocation(String lat, String lon){
        final LiveData<Resource<List<FavouriteItem>>> repositorySource = forecastRepository.fetchForecastByLocation(lat,lon);
        fetchWithCaching(repositorySource, _dataSource);
    }

    public void fetchWithCaching(LiveData<Resource<List<FavouriteItem>>> repositorySource,MediatorLiveData<Resource<List<FavouriteItem>>> usedRepo   ){
        requestStartTime = System.currentTimeMillis();
        cancelRequest = false;
        isPerformingQuery = true;
        usedRepo.addSource(repositorySource, new Observer<Resource<List<FavouriteItem>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<FavouriteItem>> listResource) {
                if(!cancelRequest){
                    if(listResource != null){
                        if(listResource.status == Resource.Status.SUCCESS){
                            Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                            Log.d(TAG, "onChanged: page number: " + pageNumber);
                            Log.d(TAG, "onChanged: " + listResource.data);

                            isPerformingQuery = false;
                            if(listResource.data != null){
                                if(listResource.data.size() == 0 ){
                                    Log.d(TAG, "onChanged: query is exhausted...");
                                    usedRepo.setValue(
                                            new Resource<List<FavouriteItem>>(
                                                    Resource.Status.ERROR,
                                                    listResource.data,
                                                    QUERY_EXHAUSTED
                                            )
                                    );
                                    isQueryExhausted = true;
                                }
                            }
                            usedRepo.removeSource(repositorySource);
                        }
                        else if(listResource.status == Resource.Status.ERROR){
                            Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                            isPerformingQuery = false;
                            if(listResource.message.equals(QUERY_EXHAUSTED)){
                                isQueryExhausted = true;
                            }
                            usedRepo.removeSource(repositorySource);
                        }
                        usedRepo.setValue(listResource);
                    }
                    else{
                        usedRepo.removeSource(repositorySource);
                    }
                }
                else{
                    usedRepo.removeSource(repositorySource);
                }
            }
        });

    }

   /* public LiveData<Resource<List<SavedDailyForecast>>> fetchResults(String city, String numDays) {
        return forecastRepository.fetchForecast(city, numDays);
    }*/

    public void searchWeatherOfCity(String city){
        final LiveData<Resource<List<FavouriteItem>>> repositorySource = forecastRepository.fetchForecast(city);
        fetchWithCaching(repositorySource,_searchedData);
    }

    public void dropFravourtieItem(FavouriteItem favouriteItem){
        forecastRepository.dropFravourtieItem(favouriteItem);
    }

    private AirQuality obj;
    public void getAirQuality(String lat , String lon) {

        mWeatherRepository = WeatherRepository.getInstance(getApplication());
        final Call<AirQuality> call = forecastRepository.getAirQuality(lat,lon);
        call.enqueue(new Callback<AirQuality>() {
            @Override
            public void onResponse(Call<AirQuality> call, Response<AirQuality> response) {
                obj = response.body();

            }

            @Override
            public void onFailure(Call<AirQuality> call, Throwable t) {
                //_data.postValue(null);
                Log.d("getAirQuality",t.getMessage());
            }
        });
    }

}
