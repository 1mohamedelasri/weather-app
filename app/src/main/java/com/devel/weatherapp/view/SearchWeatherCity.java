package com.devel.weatherapp.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.Resource;
import com.devel.weatherapp.utils.Utility;
import com.devel.weatherapp.view.adapters.SearchFragment;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchWeatherCity extends AppCompatActivity {
    private WeatherViewModel mWeatherListViewModel;
    final List<WeatherForecast> mList = new ArrayList<>();
    private ProgressBar searchProgress;
    private SearchView searchView;
    public enum STATUS {NOT_FOUND,FOUND,NONE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        searchProgress      = findViewById(R.id.searchProgress);
        searchView = (SearchView)findViewById(R.id.search_view);
        mWeatherListViewModel =  WeatherViewModel.getInstance(getApplication());
        searchView.requestFocus();



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                searchProgress.setVisibility(View.VISIBLE);
                mWeatherListViewModel.getForecastByCity(s,Constants.API_KEY);
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

                    loadFragment(data,STATUS.FOUND);
                }else{
                    loadFragment(data,STATUS.NOT_FOUND);
                }
                searchProgress.setVisibility(View.INVISIBLE);
                searchView.clearFocus();

            }
        });


        searchProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchProgress.setVisibility(View.INVISIBLE);
    }


    private void loadFragment(WeatherForecast favouriteItem, STATUS status) {

        SearchFragment fg = new SearchFragment();
        fg.setFavouriteItem(favouriteItem);
        fg.setStatus(status);
        fg.setContext(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.searchFragment, fg, "fragmentTag")
                .disallowAddToBackStack()
                .commit();

    }

    @Override
    public void onBackPressed() {
        finish();

    }

}
