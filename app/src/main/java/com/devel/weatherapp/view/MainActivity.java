package com.devel.weatherapp.view;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.devel.weatherapp.R;
import com.devel.weatherapp.view.adapters.IntroViewPagerAdapter;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.Utility;
import com.devel.weatherapp.viewmodels.WeatherViewModel;
import com.google.android.material.tabs.TabLayout;
import com.yayandroid.locationmanager.base.LocationBaseActivity;
import com.yayandroid.locationmanager.configuration.Configurations;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends LocationBaseActivity {

    private WeatherViewModel mWeatherListViewModel;
    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private LocationPresenter locationPresenter;
    TabLayout tabIndicator;
    Location currentLocation = null;
    private ImageView searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationPresenter = new LocationPresenter(this);

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        mWeatherListViewModel = WeatherViewModel.getInstance(getApplication());
        introViewPagerAdapter = new IntroViewPagerAdapter(this, getApplication(), mWeatherListViewModel);
        screenPager.setAdapter(introViewPagerAdapter);
        searchButton = findViewById(R.id.magnifyImgView);
        // hide the action bar

        getSupportActionBar().hide();

        SetupObservers();
        getLocation();


        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {


                TextView temperatureTextView = findViewById(R.id.temperatureTextView);
                TextView tempDescTextView = findViewById(R.id.TempDescTextView);


                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.flipping);
                anim.setTarget(temperatureTextView);
                anim.setDuration(500);
                anim.start();
                temperatureTextView.setText(mWeatherListViewModel.getFavourtieItems().get(position).temperature);
                introViewPagerAdapter.notifyDataSetChanged();

                //cityTextView.setText(Utility.toTitleCase(mList.get(position).city));
                //tempDescTextView.setText(Utility.toTitleCase(mList.get(position).description));

                //introViewPagerAdapter.recyclerAdapter.setForecasts(mList.get(position).savedDailyForecast);
                //introViewPagerAdapter.recyclerAdapter.notifyDataSetChanged();


                // cityTextView.setText(mList.get(posintroViewPagerAdapter.recyclerAdapterition).city);
                //tempDescTextView.setText(mList.get(position).description);

                //mWeatherListViewModel.getCityDataWeeklyData("GRenoble", "5", API_KEY);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // if you want some fade in / fade out anim, you may want the values provided here

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplication(), SearchWeatherCity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(myIntent);
            }
        });

    }

    private void SetupObservers() {

        // Instantiate the weather View Model.
        mWeatherListViewModel.data().observe(this::getLifecycle, new Observer<WeatherForecast>() {
            @Override
            public void onChanged(WeatherForecast data) {
                Log.d("TEST", "subscribeObservers: ");

                        introViewPagerAdapter.notifyDataSetChanged();

                        //introViewPagerAdapter.recyclerAdapter.notifyDataSetChanged();
            }
        });

    }

    public static void hideSystemUI(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


    @Override
    public LocationConfiguration getLocationConfiguration() {
        return Configurations.defaultConfiguration("Gimme the permission!", "Would you mind to turn GPS on?");
    }

    @Override
    public void onLocationChanged(Location location) {

        locationPresenter.onLocationChanged();
        currentLocation = location;
        introViewPagerAdapter.setCurrentLocation(location);
        //mWeatherListViewModel.getForecastByCity("Grenoble",Constants.API_KEY);

        //String[] res = Utility.geoLocToString(currentLocation);
        //mWeatherListViewModel.getForecastByCurrentLocation(res[0],res[1],Constants.API_KEY);
       //if(mWeatherListViewModel.getFavourtieItems().size() <1)
        //{
            String[] res = Utility.geoLocToString(currentLocation);
            mWeatherListViewModel.getForecastByCurrentLocation(res[0],res[1],Constants.API_KEY);
            this.introViewPagerAdapter.notifyDataSetChanged();

       // }
    }

    @Override
    public void onLocationFailed(@FailType int failType) {
        locationPresenter.onLocationFailed(failType);

    }

    @Override
    public void onProcessTypeChanged(@ProcessType int processType) {
        locationPresenter.onProcessTypeChanged(processType);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            //displayProgress();
        }
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }

    private class CheckInternet extends AsyncTask<String, String,String> {
        protected String doInBackground(String... urls) {

            Log.d("INTERNET", "isconnected :" +isInternetAvailable());

            return null;
        }

    }


}