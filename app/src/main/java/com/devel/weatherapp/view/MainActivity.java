package com.devel.weatherapp.view;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.AirQuality;
import com.devel.weatherapp.repositories.WeatherRepository;
import com.devel.weatherapp.utils.Resource;
import com.devel.weatherapp.view.adapters.IntroViewPagerAdapter;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.utils.UtilityHelper;
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

    private final String TAG = "MainActivity";
    private WeatherViewModel mWeatherListViewModel;
    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private LocationPresenter locationPresenter;
    TabLayout tabIndicator;
    Location currentLocation = null;
    private ImageView searchButton;
    private ImageView baselineBtn;
    private WeatherRepository weatherRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationPresenter = new LocationPresenter(this);

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        screenPager.setOffscreenPageLimit(7);
        mWeatherListViewModel = WeatherViewModel.getInstance(getApplication());
        introViewPagerAdapter = new IntroViewPagerAdapter(this, getApplication(), mWeatherListViewModel);
        screenPager.setAdapter(introViewPagerAdapter);
        searchButton = findViewById(R.id.magnifyImgView);
        baselineBtn = findViewById(R.id.baselineBtn);
        // hide the action bar

        getSupportActionBar().hide();

        SetupObservers();
        getLocation();


        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d("PageChangeListener","posistion = "+position );
                introViewPagerAdapter.setCurrentPos(position);
                introViewPagerAdapter.notifyChange();

                TextView temperatureTextView = findViewById(R.id.temperatureTextView);
                //temperatureTextView.setText(mWeatherListViewModel.getFavourtieItems().get(position).temperature);


                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.flipping);
                anim.setTarget(temperatureTextView);
                anim.setDuration(500);
                anim.start();


                //introViewPagerAdapter.recyclerAdapter.setForecasts(mList.get(position).savedDailyForecast);
                //introViewPagerAdapter.recyclerAdapter.notifyDataSetChanged();


                // cityTextView.setText(mList.get(posintroViewPagerAdapter.recyclerAdapterition).city);
                //tempDescTextView.setText(mList.get(position).description);

                //mWeatherListViewModel.getCityDataWeeklyData("GRenoble", "5", API_KEY);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // if you want some fade in / fade out anim, you may want the values provided here
                //introViewPagerAdapter.notifyChange();

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
        baselineBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("MAIN ACTIVTY !","onTheTest");
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MainActivity.this, baselineBtn);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String title = (String) item.getTitle();
                        if ("Share".equals(title)) {
                            FavouriteItem myItem = introViewPagerAdapter.getCurrentDisplayedWeather();
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "I share with you the weather of  "+ myItem.city+ " the temperature is " + myItem.temperature+". It's " + myItem.savedDailyForecast.get(0).mdescription );
                            sendIntent.setType("text/plain");

                            if (sendIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(sendIntent);
                            }

                        } else if ("Favorites".equals(title)) {
                            Intent myIntent = new Intent(getApplication(), FavouriteActivity.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplication().startActivity(myIntent);

                        }

                        Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method
        weatherRepository = weatherRepository.getInstance(getApplication());

        Calendar calendar = Calendar.getInstance();
        //calendar.set(Calendar.HOUR_OF_DAY,23);
        //calendar.set(Calendar.MINUTE,36);
        //calendar.set(Calendar.SECOND,10);
      /*  Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 4*60*60, pendingIntent);
*/
        //onTheTest();
    }

    public void onTheTest(){
        Log.d("MAINACTIIVTY","testRepository");

        //mWeatherRepository = WeatherRepository.getInstance(getApplication());

        weatherRepository.fetchForecast("grenoble").observe(this, result -> {
            Log.d("MAINACTIIVTY","2");
            if(result != null) {
                if(result.data != null && result.data.size() > 0 ) {
                    Log.d("MAINACTIIVTY",  "3" + result.data.get(0).city);

                }
            }

        });
    }

    private void SetupObservers() {


        mWeatherListViewModel.getDataSource().observe(this, new Observer<Resource<List<FavouriteItem>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<FavouriteItem>> listResource) {
                introViewPagerAdapter.notifyChange();
                if(listResource != null){
                    Log.d(TAG, "onChanged: status: " + listResource.status);

                    if(listResource.data != null){

                        switch (listResource.status){
                            case LOADING:{
                            }

                            case ERROR:{
                                Log.e(TAG, "onChanged: cannot refresh the cache." );
                                Log.e(TAG, "onChanged: ERROR message: " + listResource.message );
                                Log.e(TAG, "onChanged: status: ERROR, #recipes: " + listResource.data.size());
                                introViewPagerAdapter.setFavouriteItems(listResource.data);
                                introViewPagerAdapter.notifyChange();

                                break;
                            }

                            case SUCCESS:{
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #Recipes: " + listResource.data.size());
                                introViewPagerAdapter.setFavouriteItems(listResource.data);
                                introViewPagerAdapter.notifyChange();
                                break;
                            }
                        }
                    }
                }
            }
        });

        mWeatherListViewModel.getAirQuality().observe(this, new Observer<AirQuality>() {
            @Override
            public void onChanged(AirQuality airQuality) {
                if(airQuality != null)
                {
                    introViewPagerAdapter.setAirQuality(airQuality);
                    //introViewPagerAdapter.notifyChange();
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
        fetchWeatherLocationChanged();
        //mWeatherListViewModel.getForecastByCity("Grenoble",Constants.API_KEY);

        //String[] res = Utility.geoLocToString(currentLocation);
        //mWeatherListViewModel.getForecastByCurrentLocation(res[0],res[1],Constants.API_KEY);
       //if(mWeatherListViewModel.getFavourtieItems().size() <1)
      //  {
      //  }
        if(location != null) {
            String[] res = UtilityHelper.geoLocToString(currentLocation);
            mWeatherListViewModel.getAirQuality(res[0],res[1]);

        }

    }

    public void fetchWeatherLocationChanged(){
        if(currentLocation != null) {
            String[] res = UtilityHelper.geoLocToString(currentLocation);
            mWeatherListViewModel.fetchbyLocation(res[0],res[1]);
            introViewPagerAdapter.notifyChange();

        }
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
        fetchWeatherLocationChanged();
        refreshFromAnyWhere();
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

    public void mapWeatherToFavortie(WeatherForecast data) {

        if (data != null) {
            if (data != null && data.getDailyForecasts() != null) {
                List<SavedDailyForecast> savedDailyForecasts = new ArrayList<SavedDailyForecast>();

                for (int i = 0; i < data.getDailyForecasts().size() - 1; i++) {
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
                    savedDailyForecast.setPressure(data.getDailyForecasts().get(i).getHumidity());
                    savedDailyForecast.setMain(data.getDailyForecasts().get(i).getWeather().get(0).getMain());
                    savedDailyForecast.setClouds(data.getDailyForecasts().get(i).getClouds());
                    savedDailyForecast.setSunrise(data.getDailyForecasts().get(i).getSunrise());
                    savedDailyForecast.setSunset(data.getDailyForecasts().get(i).getSunset());
                    savedDailyForecasts.add(savedDailyForecast);
                }



                mWeatherListViewModel.insertInFavourtieItems(new FavouriteItem(data.getCity().getId(),
                        data.getCity().getName(),
                        savedDailyForecasts.get(0).getDescription(), data.getCity().getName(),
                        savedDailyForecasts));
            }

        }
    }

    public void refreshFromAnyWhere(){
        this.introViewPagerAdapter.notifyChange();
    }



}