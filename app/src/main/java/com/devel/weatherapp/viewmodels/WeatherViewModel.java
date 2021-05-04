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

import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.repositories.ForecastRepository;
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
    private WeatherRepository mWeatherRepository;
    private final List<FavouriteItem> _favouriteItems = new ArrayList<>();
    private final MutableLiveData<WeatherForecast> _data = new MutableLiveData<>();
    private final MutableLiveData<WeatherForecast> _searchedCity= new MutableLiveData<>();
    private static WeatherViewModel instance;
    private ForecastRepository forecastRepository = ForecastRepository.getInstance(getApplication());
    private MediatorLiveData<Resource<List<FavouriteItem>>> _dataSource = new MediatorLiveData<>();
    public MediatorLiveData<Resource<List<FavouriteItem>>> getDataSource(){
        return _dataSource;
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
    }

    public void testRepository(){

/*
        mWeatherRepository.getWeatherByCity("grenoble",Constants.API_KEY).enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {

                Log.d(TAG, "onResponse: server response " + response.toString());

                // response code 200 means a successful request
                // if successful store the response body in the lod
                if (response.code() == 200) {
                    // mWeatherResponseList = response.body();
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    //  Log.d(TAG, "onResponse: " + mWeatherResponse.toString());


                } else {
                    Log.d(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                Log.d(TAG, "onResponse: ERROR: " + t.getMessage());

            }
        });

        /
 */
    }


    public void fetchbyCity(String city){
        final LiveData<Resource<List<FavouriteItem>>> repositorySource = forecastRepository.fetchForecast(city);
        fetchWithCaching(repositorySource);
    }

    public void fetchbyLocation(String lat, String lon){
        final LiveData<Resource<List<FavouriteItem>>> repositorySource = forecastRepository.fetchForecastByLocation(lat,lon);
        fetchWithCaching(repositorySource);
    }

    public void fetchWithCaching(LiveData<Resource<List<FavouriteItem>>> repositorySource  ){
        requestStartTime = System.currentTimeMillis();
        cancelRequest = false;
        isPerformingQuery = true;
        _dataSource.addSource(repositorySource, new Observer<Resource<List<FavouriteItem>>>() {
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
                                    _dataSource.setValue(
                                            new Resource<List<FavouriteItem>>(
                                                    Resource.Status.ERROR,
                                                    listResource.data,
                                                    QUERY_EXHAUSTED
                                            )
                                    );
                                    isQueryExhausted = true;
                                }
                            }
                            _dataSource.removeSource(repositorySource);
                        }
                        else if(listResource.status == Resource.Status.ERROR){
                            Log.d(TAG, "onChanged: REQUEST TIME: " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds.");
                            isPerformingQuery = false;
                            if(listResource.message.equals(QUERY_EXHAUSTED)){
                                isQueryExhausted = true;
                            }
                            _dataSource.removeSource(repositorySource);
                        }
                        _dataSource.setValue(listResource);
                    }
                    else{
                        _dataSource.removeSource(repositorySource);
                    }
                }
                else{
                    _dataSource.removeSource(repositorySource);
                }
            }
        });

    }

   /* public LiveData<Resource<List<SavedDailyForecast>>> fetchResults(String city, String numDays) {
        return forecastRepository.fetchForecast(city, numDays);
    }*/

}
