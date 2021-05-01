package com.devel.weatherapp.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.ScreenItem;
import com.devel.weatherapp.view.MyActivity;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter implements LifecycleOwner {
    private static final String TAG = "IntroViewPagerAdapter";

    private final WeatherViewModel weatherViewModel;
    private Context mContext ;
    private List<ScreenItem> mListScreen;
    private RecyclerView recyclerView;
    public WeeklyAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private Location geoLocation;

    public IntroViewPagerAdapter(Context mContext, List<ScreenItem> mListScreen, WeatherViewModel weatherViewModel) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
        this.weatherViewModel = weatherViewModel;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen,null);


        container.addView(layoutScreen);

        layoutScreen.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent myIntent = new Intent(mContext, MyActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(myIntent);
            }
        });
        recyclerView = (RecyclerView) container.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new WeeklyAdapter(mContext);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setHasFixedSize(true);
        subscribeObservers();

        return layoutScreen;


    }

    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);

    }

    private void subscribeObservers(){

        if(geoLocation != null){
            weatherViewModel.getWeather(1,"","",4);

        }
        /*
        weatherViewModel.data().observe(this::getLifecycle, new Observer<WeatherForecast>() {
            @Override
            public void onChanged(WeatherForecast data) {
                Log.d(TAG, "subscribeObservers: " + weatherViewModel);

                if(data != null) {
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
                        recyclerAdapter.setForecasts(savedDailyForecasts);
                    }
                }
            }
        });
        */
    }

    public void addNewPage(ScreenItem screenItem){
        this.mListScreen.add(screenItem);
        this.notifyDataSetChanged();
    }



    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    public void setCurrentLocation(Location location) {
        this.geoLocation = location;
    }
}