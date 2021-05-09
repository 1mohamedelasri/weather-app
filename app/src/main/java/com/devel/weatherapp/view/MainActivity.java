package com.devel.weatherapp.view;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.Tuple;
import com.devel.weatherapp.models.Weather;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.models.WeatherList;
import com.devel.weatherapp.repositories.WeatherRepository;
import com.devel.weatherapp.utils.AlarmReceiver;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.Resource;
import com.devel.weatherapp.utils.UtilityHelper;
import com.devel.weatherapp.view.adapters.IntroViewPagerAdapter;
import com.devel.weatherapp.viewmodels.WeatherViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.yayandroid.locationmanager.base.LocationBaseActivity;
import com.yayandroid.locationmanager.configuration.Configurations;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


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
    private AlarmReceiver alarm;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationPresenter = new LocationPresenter(this);

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager);
        screenPager.setOffscreenPageLimit(1);
        mWeatherListViewModel = WeatherViewModel.getInstance(getApplication());
        introViewPagerAdapter = new IntroViewPagerAdapter(this, getApplication(), mWeatherListViewModel,screenPager);
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

                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.flipping);
                anim.setTarget(temperatureTextView);
                anim.setDuration(500);
                anim.start();
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
                            WeatherForecast myItem = introViewPagerAdapter.getCurrentDisplayedWeather();
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "I share with you the weather of  "+ myItem.getCity().getName()+ " the temperature is " + myItem.getDailyForecasts().get(0).getMain().getTemp()+". It's " + myItem.getDailyForecasts().get(0).getWeathers().get(0).getDescription() );
                            sendIntent.setType("text/plain");

                            if (sendIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(sendIntent);
                            }

                        } else if ("Favorites".equals(title)) {
                            Intent myIntent = new Intent(getApplication(), FavouriteActivity.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(myIntent,Constants.MY_CODE_REQUEST);

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

        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

        exec.schedule(new Runnable() {
            public void run() {
                mWeatherListViewModel.setCurrentLocationCity(new WeatherForecast());
            }
        }, 1, TimeUnit.SECONDS);


    }

    private void SetupObservers() {


        mWeatherListViewModel.getDataSource().observe(this, new Observer<Resource<List<WeatherForecast>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<WeatherForecast>> listResource) {
                introViewPagerAdapter.notifyChange();

                if(listResource != null){
                    Log.d(TAG, "onChanged: status: " + listResource.status);

                    if(listResource.data != null){
                        if(listResource.data.size() > 0) {
                            TextView nointernet = findViewById(R.id.nointernet);
                            nointernet.setVisibility(View.GONE);
                        }

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
        mWeatherListViewModel.getlastAddedItemIndex().observe(this, new Observer<Tuple>() {
            @Override
            public void onChanged(Tuple value) {
                if(value.source == Tuple.Source.FAVORTIES){
                    screenPager.setCurrentItem(value.index,true);
                }else if(value.source == Tuple.Source.SEARCH){
                    screenPager.setCurrentItem(introViewPagerAdapter.getCount()+1,true);
                }
                introViewPagerAdapter.notifyChange();
            }
        });

        mWeatherListViewModel.getCurrentLocationCity().observe(this, new Observer<WeatherForecast>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(WeatherForecast newWeather) {
                if(newWeather != null &&  newWeather.getDailyForecasts() !=null && newWeather.getDailyForecasts().size() > 2) {
                    if(newWeather.getDailyForecasts().get(0).getWeathers().get(0).getId() != newWeather.getDailyForecasts().get(1).getWeathers().get(0).getId()) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, 15);
                        calendar.set(Calendar.MINUTE, 5);
                        calendar.set(Calendar.SECOND, 0);
                        Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
                        Gson gson = new Gson();
                        String myJson = gson.toJson(newWeather);
                        intent1.putExtra("newWeather", myJson);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
                        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                    }
                }
            }
        });
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
            if(!isNetworkAvailable()) {
                TextView nointernet = findViewById(R.id.nointernet);
                nointernet.setVisibility(View.VISIBLE);
                introViewPagerAdapter.notifyChange();
            }else{
                TextView nointernet = findViewById(R.id.nointernet);
                nointernet.setVisibility(View.GONE);

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void refreshFromAnyWhere(){
        this.introViewPagerAdapter.notifyChange();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotificaiton(WeatherForecast newWeather) {

        StringBuilder str = new StringBuilder();
        WeatherList forcast = newWeather.getDailyForecasts().get(0);
        str.append(UtilityHelper.formatTemperature(this,forcast.getMain().getTemp(),true));
        str.append(" at");
        str.append(UtilityHelper.formatHourly(forcast.getDt()));
        str.append(" in ");
        str.append(newWeather.getCity().getName());

        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "test"; // The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
// Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(MainActivity.this)
                .setContentTitle(str)
                .setContentText(UtilityHelper.computeNotificaitonMessage(forcast.getWeathers().get(0).getId()))
                .setSmallIcon(R.drawable.app_icon)
                .setChannelId(CHANNEL_ID)
                .build();


        // notificationId is a unique int for each notification that you must define
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(mChannel);

// Issue the notification.
        mNotificationManager.notify(notifyID , notification);

    }

}