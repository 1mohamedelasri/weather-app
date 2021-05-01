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
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.devel.weatherapp.R;
import com.devel.weatherapp.view.adapters.IntroViewPagerAdapter;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.ScreenItem;
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
    final List<ScreenItem> mList = new ArrayList<>();
    Location currentLocation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationPresenter = new LocationPresenter(this);


        mList.add(new ScreenItem("10", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img1));
        mList.add(new ScreenItem("20", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img2));
        mList.add(new ScreenItem("30", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img3));

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(getApplicationContext(), mList, ViewModelProviders.of(this).get(WeatherViewModel.class));
        screenPager.setAdapter(introViewPagerAdapter);

        // hide the action bar

        getSupportActionBar().hide();

        SetupObservers();
        getLocation();



        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {


                TextView title = findViewById(R.id.temperatureTextView);

                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.flipping);
                anim.setTarget(title);
                anim.setDuration(500);
                anim.start();

                title.setText(mList.get(position).getTitle());

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


    }


    private void SetupObservers() {

        // Instantiate the weather View Model.
        mWeatherListViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        mWeatherListViewModel.data().observe(this::getLifecycle, new Observer<WeatherForecast>() {
            @Override
            public void onChanged(WeatherForecast data) {
                Log.d("TEST", "subscribeObservers: ");
                List<SavedDailyForecast> savedDailyForecasts = new ArrayList<SavedDailyForecast>();

                if (data != null) {
                    if (data != null && data.getDailyForecasts() != null) {

                        for (int i = 0; i < data.getDailyForecasts().size()-1; i++) {
                            SavedDailyForecast savedDailyForecast = new SavedDailyForecast();
                            savedDailyForecast.setLat(data.getCity().getCoord().getLat());
                            savedDailyForecast.setLon(data.getCity().getCoord().getLon());
                            savedDailyForecast.setDate(data.getDailyForecasts().get(i).getDt());
                            savedDailyForecast.setMaxTemp(data.getDailyForecasts().get(i).getTemp().getMax());
                            savedDailyForecast.setMinTemp(data.getDailyForecasts().get(i).getTemp().getMin());
                            savedDailyForecast.setDayTemp(data.getDailyForecasts().get(i).getTemp().getDay());
                            savedDailyForecast.setEveningTemp(data.getDailyForecasts().get(i).getTemp().getEve());
                            savedDailyForecast.setMorningTemp(data.getDailyForecasts().get(i).getTemp().getMorn());
                            savedDailyForecast.setNightTemp(data.getDailyForecasts().get(i).getTemp().getNight());
                            savedDailyForecast.setFeelslikeDay(data.getDailyForecasts().get(i).getFeelsLike().getDay());
                            savedDailyForecast.setFeelslikeEve(data.getDailyForecasts().get(i).getFeelsLike().getEve());
                            savedDailyForecast.setFeelslikeMorning(data.getDailyForecasts().get(i).getFeelsLike().getMorn());
                            savedDailyForecast.setFeelslikeNight(data.getDailyForecasts().get(i).getFeelsLike().getNight());
                            savedDailyForecast.setHumidity(data.getDailyForecasts().get(i).getHumidity());
                            savedDailyForecast.setWind(data.getDailyForecasts().get(i).getSpeed());
                            savedDailyForecast.setDescription(data.getDailyForecasts().get(i).getWeather().get(0).getDescription());
                            savedDailyForecast.setWeatherid(data.getDailyForecasts().get(i).getWeather().get(0).getId());
                            savedDailyForecast.setImageUrl(data.getDailyForecasts().get(i).getWeather().get(0).getIcon());
                            savedDailyForecasts.add(savedDailyForecast);
                        }
                        //introViewPagerAdapter.addNewPage(new ScreenItem(String.valueOf(data.getDailyForecasts().get(0).getTemp().getDay()), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img3));
                        introViewPagerAdapter.recyclerAdapter.setForecasts(savedDailyForecasts);

                        TextView temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
                        TextView cityTextView = (TextView) findViewById(R.id.cityTextView);
                        cityTextView.setText(Utility.toTitleCase(data.getCity().getName()));
                        Calendar c = Calendar.getInstance();
                        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                        if(timeOfDay >= 0 && timeOfDay < 12){
                        }else if(timeOfDay >= 12 && timeOfDay < 16){
                            temperatureTextView.setText(Utility.formatTemperature(getApplicationContext(), savedDailyForecasts.get(0).getMorningTemp()));
                        }else if(timeOfDay >= 16 && timeOfDay < 21){
                            temperatureTextView.setText(Utility.formatTemperature(getApplicationContext(), savedDailyForecasts.get(0).getEveningTemp()));
                        }else if(timeOfDay >= 21 && timeOfDay < 24){
                            temperatureTextView.setText(Utility.formatTemperature(getApplicationContext(), savedDailyForecasts.get(0).getNightTemp()));
                        }

                    }
                }
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

        String[] res = Utility.geoLocToString(currentLocation);
        mWeatherListViewModel.getForecastByCurrentLocation(res[0],res[1],Constants.API_KEY);

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