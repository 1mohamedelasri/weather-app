package com.devel.weatherapp.view.adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.Utility;
import com.devel.weatherapp.view.SunView;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter implements LifecycleOwner {
    private static final String TAG = "IntroViewPagerAdapter";

    private final WeatherViewModel weatherViewModel;
    private Application application ;
    private Context mContext ;

    private RecyclerView recyclerView;
    public WeeklyAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private Location geoLocation;
    private TextView cityNameText;
    private TextView cityTempText;
    private TextView cityDescText;
    private View layoutScreen;
    private int currentPos = 0;
    private TextView feelLikeValue ;
    private TextView HumidityValue;
    private TextView cloudiness;
    private TextView WindSpeedValue;

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


        container.addView(layoutScreen);


        /*layoutScreen.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent myIntent = new Intent(mContext, FavouriteActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(myIntent);
            }
        });*/
        List<FavouriteItem> favItems = weatherViewModel.getFavourtieItems();
        Activity hostActivity = (Activity)mContext;
        cityNameText = hostActivity.findViewById(R.id.cityTextView);
        cityTempText = layoutScreen.findViewById(R.id.temperatureTextView);
        cityDescText = layoutScreen.findViewById(R.id.TempDescTextView);
        feelLikeValue   = layoutScreen.findViewById(R.id.feelLikeValue);
        HumidityValue   = layoutScreen.findViewById(R.id.HumidityValue);
        cloudiness = layoutScreen.findViewById(R.id.CloudinessValue);
        WindSpeedValue  = layoutScreen.findViewById(R.id.WindSpeedValue);



        cityNameText.setText(weatherViewModel.getFavourtieItems().get(currentPos).city);
        cityTempText.setText(weatherViewModel.getFavourtieItems().get(currentPos).temperature);
        cityDescText.setText(weatherViewModel.getFavourtieItems().get(currentPos).description);
        feelLikeValue.setText(weatherViewModel.getFavourtieItems().get(currentPos).feelsLike);

        HumidityValue.setText(weatherViewModel.getFavourtieItems().get(currentPos).savedDailyForecast.get(0).mhumidity+"%");
        cloudiness.setText(weatherViewModel.getFavourtieItems().get(currentPos).savedDailyForecast.get(0).clouds+"%");
        WindSpeedValue.setText(Utility.getFormattedWind(mContext,weatherViewModel.getFavourtieItems().get(currentPos).savedDailyForecast.get(position).getWind()));
        feelLikeValue.setText(weatherViewModel.getFavourtieItems().get(position).feelsLike);
        recyclerView = (RecyclerView) container.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new WeeklyAdapter(mContext,weatherViewModel.getFavourtieItems().get(currentPos).savedDailyForecast);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setHasFixedSize(true);


        SunView sv = hostActivity.findViewById(R.id.sv);
        Date sunRisedate = new Date((long) (weatherViewModel.getFavourtieItems().get(currentPos).savedDailyForecast.get(0).getSunrise() * 1000));
        Date sunSetdate = new Date((long) (weatherViewModel.getFavourtieItems().get(currentPos).savedDailyForecast.get(0).getSunset() * 1000));

        // Set sunrise time
        sv.setSunrise(sunRisedate.getHours(), sunRisedate.getMinutes());
        // Set the sunset time
        sv.setSunset(sunSetdate.getHours(), sunSetdate.getMinutes());
        // Get system time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // Set the current time
        sv.setCurrentTime(hour, minute);

        return layoutScreen;


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
        return this.weatherViewModel.getFavourtieItems().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    public void setCurrentPos(int pos){
        this.currentPos = pos;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);

    }
    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    private void subscribeObservers(){

        if(geoLocation != null){
            String[] res = Utility.geoLocToString(geoLocation);
            weatherViewModel.getForecastByCurrentLocation(res[0],res[1],Constants.API_KEY);
        }
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