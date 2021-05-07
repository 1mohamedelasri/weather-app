package com.devel.weatherapp.api;

import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.LiveDataCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create());

    // Returns a singleton instance of Retrofit
    private static Retrofit retrofit = retrofitBuilder.build();

    // Use the retrofit instance to create another instance of the API.
    private static IWeatherApi weatherApi = retrofit.create(IWeatherApi.class);

    // Public method to access the API
    public static IWeatherApi getWeatherApi() {
        return weatherApi;
    }
}
