package com.devel.weatherapp;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.devel.weatherapp.adapters.IntroViewPagerAdapter;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.ScreenItem;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.view.LocationPresenter;
import com.devel.weatherapp.viewmodels.WeatherViewModel;
import com.google.android.material.tabs.TabLayout;
import com.yayandroid.locationmanager.base.LocationBaseActivity;
import com.yayandroid.locationmanager.configuration.Configurations;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends LocationBaseActivity {

    private WeatherViewModel mWeatherListViewModel;
    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private LocationPresenter locationPresenter;
    TabLayout tabIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI(this);
        setContentView(R.layout.activity_main);


        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("10", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img1));
        mList.add(new ScreenItem("20", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img2));
        mList.add(new ScreenItem("30", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit", R.drawable.img3));

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(getApplicationContext(), mList, ViewModelProviders.of(this).get(WeatherViewModel.class));
        screenPager.setAdapter(introViewPagerAdapter);

        // hide the action bar

        getSupportActionBar().hide();

        initialiseVariables();

        getLocation();

        locationPresenter = new LocationPresenter(this);


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


    private void initialiseVariables() {

        // Instantiate the weather View Model.
        mWeatherListViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        mWeatherListViewModel.data().observe(this::getLifecycle, new Observer<WeatherForecast>() {
            @Override
            public void onChanged(WeatherForecast data) {
                Log.d("TEST", "subscribeObservers: ");
                if (data != null) {
                    if (data != null && data.getDailyForecasts() != null) {

                        List<SavedDailyForecast> savedDailyForecasts = new ArrayList<SavedDailyForecast>();
                        for (int i = 0; i < data.getDailyForecasts().size(); i++) {
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
                        introViewPagerAdapter.recyclerAdapter.setForecasts(savedDailyForecasts);
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
                        //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
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
}