package com.devel.weatherapp.view.adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.AirQuality;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.models.WeatherList;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.UtilityHelper;
import com.devel.weatherapp.view.SunView;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "IntroViewPagerAdapter";

    private final WeatherViewModel weatherViewModel;
    private Application application ;
    private Context mContext ;

    private RecyclerView recyclerView;
    public MainScreenAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Location geoLocation;
    private TextView cityNameText;
    private TextView cityTempText;
    private TextView cityDescText;
    private int currentPos = 0;
    private List<WeatherForecast> favouriteItems = new ArrayList<>();
    private ViewPager screenPager;
    public IntroViewPagerAdapter(Context mContext, Application application, WeatherViewModel weatherViewModel, ViewPager screenPager) {
        this.mContext = mContext;
        this.application = application;
        this.weatherViewModel = WeatherViewModel.getInstance(application);
        //subscribeObservers();
        this.screenPager = screenPager;


    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen,container,false);



        Activity hostActivity = (Activity)mContext;
        cityNameText =  hostActivity.findViewById(R.id.cityTextView);

        container.addView(layoutScreen);



        //fetchData();
        if(favouriteItems.size() > 0 && currentPos < favouriteItems.size()) {

            recyclerView = (RecyclerView) container.findViewById(R.id.mainScreenRecycleView);
            recyclerAdapter = new MainScreenAdapter(mContext,application,currentPos);
            layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerAdapter.setFavourtieItem(favouriteItems.get(currentPos));
            recyclerAdapter.notifyDataSetChanged();

            WeatherList mSavedDailyForecast = favouriteItems.get(currentPos).getDailyForecasts().get(0);


           // String date = String.format("%s, %s", UtilityHelper.format(mSavedDailyForecast.), UtilityHelper.formatDate(mSavedDailyForecast.getDate()));


            cityNameText.setText(favouriteItems.get(currentPos).getCity().getName());
            //favouriteItems.get(currentPos). = temperatureText;
/*
            this.weatherViewModel.getAirQuality().observe((LifecycleOwner) mContext, new Observer<AirQuality>() {
                @Override
                public void onChanged(AirQuality airQuality) {
                    recyclerAdapter.setAirQuality(airQuality);
                }
            });
*/
        }


        return layoutScreen;


    }

    public WeatherForecast getCurrentDisplayedWeather(){
        return favouriteItems.get(currentPos);
    }

    public void notifyChange(){
        this.notifyDataSetChanged();
        if(this.recyclerAdapter != null)
        {
            this.recyclerAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return favouriteItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    public void setCurrentPos(int pos){
        this.currentPos = pos;
    }

    public void setFavouriteItems(List<WeatherForecast> favouriteItemsList){
        this.favouriteItems = favouriteItemsList;
        notifyChange();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);

    }
    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }



    public void setCurrentLocation(Location location) {
        this.geoLocation = location;
    }




}