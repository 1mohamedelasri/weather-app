package com.devel.weatherapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.devel.weatherapp.api.ServiceGenerator;
import com.devel.weatherapp.api.WeatherApi;
import com.devel.weatherapp.models.ApiResponse;
import com.devel.weatherapp.models.WeatherRes;
import com.devel.weatherapp.models.WeatherResponse;
import com.devel.weatherapp.repositories.WeatherRepository;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.Resource;

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
    private final MediatorLiveData<Resource<WeatherResponse>> _data = new MediatorLiveData<>();

    public WeatherViewModel(Application application) {
        super(application);
        mWeatherRepository = WeatherRepository.getInstance(application);
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


}
