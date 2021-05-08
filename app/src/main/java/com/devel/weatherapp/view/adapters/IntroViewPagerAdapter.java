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

public class IntroViewPagerAdapter extends PagerAdapter implements LifecycleOwner {
    private static final String TAG = "IntroViewPagerAdapter";

    private final WeatherViewModel weatherViewModel;
    private Application application ;
    private Context mContext ;

    private RecyclerView recyclerView;
    public MainScreenAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private Location geoLocation;
    private TextView cityNameText;
    private TextView cityTempText;
    private TextView cityDescText;
    private int currentPos = 0;
    private List<WeatherForecast> favouriteItems = new ArrayList<>();

    public IntroViewPagerAdapter(Context mContext, Application application, WeatherViewModel weatherViewModel) {
        this.mContext = mContext;
        this.application = application;
        this.weatherViewModel = WeatherViewModel.getInstance(application);
        //subscribeObservers();



    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d("URGENT", "position ==" + position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen,container,false);



        Activity hostActivity = (Activity)mContext;
        cityNameText =  hostActivity.findViewById(R.id.cityTextView);
        cityTempText =  layoutScreen.findViewById(R.id.temperatureTextView);
        cityDescText =  layoutScreen.findViewById(R.id.TempDescTextView);


        container.addView(layoutScreen);

        /*layoutScreen.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent myIntent = new Intent(mContext, FavouriteActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(myIntent);
            }
        });*/


        recyclerView = (RecyclerView) container.findViewById(R.id.mainScreenRecycleView);
        recyclerAdapter = new MainScreenAdapter(mContext,application,currentPos);
        layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setHasFixedSize(true);

        //fetchData();
        if(favouriteItems.size() > 0 && currentPos < favouriteItems.size() &&  favouriteItems.get(currentPos).getDailyForecasts().size() > 0) {

            WeatherList mSavedDailyForecast = favouriteItems.get(currentPos).getDailyForecasts().get(0);
            this.weatherViewModel.getAirQuality(String.valueOf(favouriteItems.get(currentPos).getCity().getCoord().getLat()),String.valueOf(favouriteItems.get(currentPos).getCity().getCoord().getLat()));


           // String date = String.format("%s, %s", UtilityHelper.format(mSavedDailyForecast.), UtilityHelper.formatDate(mSavedDailyForecast.getDate()));


            cityNameText.setText(favouriteItems.get(currentPos).getCity().getName());
            cityTempText.setText(UtilityHelper.formatTemperature(mContext,favouriteItems.get(currentPos).getDailyForecasts().get(0).getMain().getTemp()));
            cityDescText.setText(favouriteItems.get(currentPos).getDailyForecasts().get(0).getWeathers().get(0).getDescription());
            //favouriteItems.get(currentPos). = temperatureText;


            recyclerAdapter.setFavourtieItem(favouriteItems.get(currentPos));
            recyclerAdapter.notifyDataSetChanged();

        }


        return layoutScreen;


    }

    public WeatherForecast getCurrentDisplayedWeather(){
        return favouriteItems.get(currentPos);
    }

    private void fetchData() {


        if(favouriteItems.size() > 0 && currentPos < favouriteItems.size() &&  favouriteItems.get(currentPos).getDailyForecasts().size() > 0) {

            WeatherList mSavedDailyForecast = favouriteItems.get(currentPos).getDailyForecasts().get(0);
            this.weatherViewModel.getAirQuality(String.valueOf(favouriteItems.get(currentPos).getCity().getCoord().getLat()),String.valueOf(favouriteItems.get(currentPos).getCity().getCoord().getLat()));

           // recyclerAdapter.setFavourtieItem(favouriteItems.get(currentPos));
            recyclerAdapter.notifyDataSetChanged();

        }

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


    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }

    public void setCurrentLocation(Location location) {
        this.geoLocation = location;
    }








}