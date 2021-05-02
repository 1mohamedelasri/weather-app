package com.devel.weatherapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchWeatherCity extends AppCompatActivity {
    private WeatherViewModel mWeatherListViewModel;
    final List<WeatherForecast> mList = new ArrayList<>();
    private ProgressBar searchProgress;
    private SearchView searchView;
    private TextView    searchResCity;
    private TextView    SearchResCountry ;
    private ImageButton addCityToFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        searchProgress      = findViewById(R.id.searchProgress);
        searchView = (SearchView)findViewById(R.id.search_view);
        searchResCity       = findViewById(R.id.searchResCity);
        SearchResCountry    = findViewById(R.id.SearchResCountry);
        addCityToFav        = (ImageButton)findViewById(R.id.AddCityToFav);
        mWeatherListViewModel =  WeatherViewModel.getInstance(getApplication());
        addCityToFav.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                               // mWeatherListViewModel.addSearchCityToFavorties();
                                            }
                                        }
        );


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                searchProgress.setVisibility(View.VISIBLE);
                mWeatherListViewModel.getForecastByCity(s, Constants.API_KEY);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mWeatherListViewModel.searchedResult().observe(this::getLifecycle, new Observer<WeatherForecast>() {
            @Override
            public void onChanged(WeatherForecast data) {
                Log.d("TEST", "subscribeObservers: ");
                if (data != null) {
                    searchResCity.setText(data.getCity().getName());
                    SearchResCountry.setText(data.getCity().getCountry());
                    searchResCity.setVisibility(View.VISIBLE);
                    SearchResCountry.setVisibility(View.VISIBLE);
                    addCityToFav.setVisibility(View.VISIBLE);
                }else{
                    searchResCity.setText("No city match your search criteria");
                    SearchResCountry.setText("");
                    addCityToFav.setVisibility(View.INVISIBLE);
                }
                searchProgress.setVisibility(View.INVISIBLE);
                searchView.clearFocus();

            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast t = Toast.makeText(getApplication(), "close", Toast.LENGTH_SHORT);
                searchProgress.setVisibility(View.INVISIBLE);
                searchResCity.setVisibility(View.INVISIBLE);
                SearchResCountry.setVisibility(View.INVISIBLE);
                addCityToFav.setVisibility(View.INVISIBLE);

                return false;
            }
        });
    }
}
