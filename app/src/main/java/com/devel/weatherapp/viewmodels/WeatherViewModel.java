package com.devel.weatherapp.viewmodels;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.devel.weatherapp.MainActivity;
import com.devel.weatherapp.api.ServiceGenerator;
import com.devel.weatherapp.api.WeatherApi;
import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.models.WeatherRes;
import com.devel.weatherapp.models.WeatherResponse;
import com.devel.weatherapp.repositories.WeatherRepository;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.Resource;

import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherViewModel extends AndroidViewModel {

    private String TAG ="AppDebug";

    /**
     * Instantiate the weather repository.
     */
    private WeatherRepository mWeatherRepository;
    private LiveData<List<SavedDailyForecast>> SavedDailyForecast;

    public WeatherViewModel(Application application) {
        super(application);
        mWeatherRepository = WeatherRepository.getInstance(application);
        SavedDailyForecast= mWeatherRepository.getAllSavedDailyForecast();

    }

    /**
     * Constructor containing the weather repository instance.
     */
    public void getCurrentData() {

        Call call = mWeatherRepository.getWeather(Constants.lat, Constants.lon, Constants.API_KEY);
        Log.d(TAG, "getWeather: called.");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    WeatherRes weatherResponse = (WeatherRes) response.body();
                    assert weatherResponse != null;

                    String stringBuilder = "Country: " +
                            weatherResponse.sys.country +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main.getTemp() +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main.getTempMin() +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main.getTempMax() +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main.getHumidity() +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main.getPressure();

                    Log.d(TAG, stringBuilder);

                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());

            }


        });
    }

    public void getCityData(String city, String api_key) {

        Call call = mWeatherRepository.getWeatherByCity(city, api_key);
        Log.d(TAG, "getCityData: called.");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    WeatherRes weatherResponse = (WeatherRes) response.body();
                    assert weatherResponse != null;

                    String stringBuilder = "Country: " +
                            weatherResponse.sys.country +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main.getTemp() +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main.getTempMin() +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main.getTempMax() +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main.getHumidity() +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main.getPressure();

                    Log.d(TAG, stringBuilder);

                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());

            }


        });
    }

    public void getCityDataWeeklyData(String city, String days, String api_key) {

        Call<WeatherForecast> call = mWeatherRepository.getWeatherWeeklyCityData(city,days, api_key);
        Log.d(TAG, "getCityData: called.");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    WeatherForecast weatherResponse = (WeatherForecast) response.body();
                    assert weatherResponse != null;

                    String stringBuilder = "getCity: " +
                            weatherResponse.getCity() +
                            "\n" +
                            "getClouds: " +
                            weatherResponse.getDailyForecasts().get(0).getClouds() +
                            "\n" +
                            "getTemp(Min): " +
                            weatherResponse.getDailyForecasts().get(0).getTemp()+
                            "\n" +
                            "getPressure: " +
                            weatherResponse.getDailyForecasts().get(0).getPressure() +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.getDailyForecasts().get(0).getSpeed();

                    Log.d(TAG, stringBuilder);

                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d(TAG, t.getMessage());
                Toast.makeText(getApplication(), "something went wrong..."+ t.getMessage(), Toast.LENGTH_SHORT).show();

            }


        });
    }


    public void insert(List<SavedDailyForecast> list)
    {
        mWeatherRepository.insert(list);
    }

    public LiveData<List<SavedDailyForecast>> getAllSavedDailyForecast()
    {

        networkRequest();

        return SavedDailyForecast;
    }


    private void networkRequest() {

        Call<WeatherForecast> call= ServiceGenerator.getWeatherApi().getWeatherForecast("grenoble","5",Constants.API_KEY);
        call.enqueue(new Callback<WeatherForecast>() {
            @Override
            public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
                if(response.isSuccessful())
                {
                    //actorRespository.insert(response.body());
                    Log.d("main", "onResponse: "+response.body());
                }
            }

            @Override
            public void onFailure(Call<WeatherForecast> call, Throwable t) {
                Toast.makeText(getApplication(), "something went wrong..."+ t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, t.getMessage());

            }
        });

    }

}
