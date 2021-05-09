package com.devel.weatherapp.view.adapters;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.AirQuality;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.models.WeatherList;
import com.devel.weatherapp.utils.UtilityHelper;
import com.devel.weatherapp.view.SunView;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import okhttp3.internal.Util;


public class MainScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int INTRO = 0;
    private final int DAILY = 1;
    private final int DETAILS = 2;
    private final int SUNVIEW = 3;
    private final int HOURLY = 4;
    private final int AIRQUALITY = 5;


    private View layoutScreen;
    private int currentPos = 0;
    private TextView feelLikeValue ;
    private TextView HumidityValue;
    private TextView cloudiness;
    private TextView WindSpeedValue;
    private SunView sv;
    private AirQuality airQuality;
    private final int currentScreenPosition;

    private RecyclerView dailyRecyclerView;
    public DailyAdapter dailyRecyclerAdapter;
    private RecyclerView.LayoutManager dailyLayoutManager;

    private RecyclerView hourlyRecyclerView;
    public HourlyAdapter hourlyRecyclerAdapter;
    private RecyclerView.LayoutManager hourlyLayoutManager;

    private final WeatherViewModel weatherViewModel;
    private final Application application;
    private final Context mContext ;

    // Data
    private WeatherForecast favouriteItem;


    public MainScreenAdapter(Context mContext, Application application, int currentScreenPosition) {
        this.mContext = mContext;
        this.weatherViewModel = WeatherViewModel.getInstance(application);
        this.application = application;
        this.currentScreenPosition = currentScreenPosition;
    }

    @Override
        public int getItemViewType(int position)
        {
            return position;
        }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view =null;
            RecyclerView.ViewHolder viewHolder = null;

            switch (viewType){
                case INTRO:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intro, parent, false);
                    viewHolder = new ViewHolderIntro(view);
                    break;

                case DAILY:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_recycleview,parent,false);
                    viewHolder = new ViewHolderWeekly(view);
                    dailyRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                    dailyLayoutManager = new LinearLayoutManager(parent.getContext(), LinearLayoutManager.HORIZONTAL, false);
                    dailyRecyclerView.setLayoutManager(dailyLayoutManager);
                    dailyRecyclerAdapter = new DailyAdapter(mContext,favouriteItem.getConvertedDaily());
                    dailyRecyclerView.setAdapter(dailyRecyclerAdapter);
                    dailyRecyclerView.setHasFixedSize(true);
                    break;
                case DETAILS:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weathercard,parent,false);
                    viewHolder = new ViewHolderWeather(view);
                    feelLikeValue   = view.findViewById(R.id.feelLikeValue);
                    HumidityValue   = view.findViewById(R.id.HumidityValue);
                    cloudiness = view.findViewById(R.id.CloudinessValue);
                    WindSpeedValue  = view.findViewById(R.id.WindSpeedValue);

                    WeatherList mSavedDailyForecast = favouriteItem.getDailyForecasts().get(0);

                    HumidityValue.setText(mSavedDailyForecast.getMain().getHumidity() + "%");
                    cloudiness.setText(mSavedDailyForecast.getClouds().getAll() + "%");
                    WindSpeedValue.setText(UtilityHelper.getFormattedWind(mContext, mSavedDailyForecast.getWind().getSpeed()));
                    feelLikeValue.setText(mSavedDailyForecast.getMain().getFeelsLike()+"°C");
                    break;
                case SUNVIEW:
                        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sunview, parent, false);
                        viewHolder = new ViewHolderSun(view);
                    break;
                case AIRQUALITY:
                        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_airquality,parent,false);
                        viewHolder = new ViewHolderAirQuality(view);
                        this.weatherViewModel.getAirQuality(String.valueOf(favouriteItem.getCity().getCoord().getLat()),String.valueOf(favouriteItem.getCity().getCoord().getLon()));

                    break;
                case HOURLY:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_recycleview,parent,false);
                    viewHolder = new ViewHolderHourly(view);
                    hourlyRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                    hourlyLayoutManager = new LinearLayoutManager(parent.getContext(), LinearLayoutManager.HORIZONTAL, false);
                    hourlyRecyclerView.setLayoutManager(hourlyLayoutManager);
                    hourlyRecyclerAdapter = new HourlyAdapter(mContext,favouriteItem.getDailyForecasts());
                    hourlyRecyclerView.setAdapter(hourlyRecyclerAdapter);
                    hourlyRecyclerView.setHasFixedSize(true);
                    break;


            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            switch (position){
                case INTRO:
                    ViewHolderIntro viewHolderIntro= (ViewHolderIntro) holder;

                    viewHolderIntro.cityTempText.setText(UtilityHelper.formatTemperature(mContext,favouriteItem.getDailyForecasts().get(0).getMain().getTemp(),false));
                    viewHolderIntro.cityDescText.setText(favouriteItem.getDailyForecasts().get(0).getWeathers().get(0).getDescription());
                    break;
                case AIRQUALITY:
                    ViewHolderAirQuality viewHolderAirQuality = (ViewHolderAirQuality) holder;
                    weatherViewModel.getAirQuality().observe((LifecycleOwner) mContext, new Observer<AirQuality>() {
                        @Override
                        public void onChanged(AirQuality airQuality) {
                            if(airQuality != null)
                            {
                                    viewHolderAirQuality.pm2.setText(String.valueOf(airQuality.getAirList().get(0).component.pm2));
                                    viewHolderAirQuality.pm10.setText(String.valueOf(airQuality.getAirList().get(0).component.pm10));
                                    viewHolderAirQuality.so2.setText(String.valueOf(airQuality.getAirList().get(0).component.so2));
                                    viewHolderAirQuality.no2.setText(String.valueOf(airQuality.getAirList().get(0).component.no2));
                                    viewHolderAirQuality.o3.setText(String.valueOf(airQuality.getAirList().get(0).component.o3));
                                    viewHolderAirQuality.co.setText(String.valueOf(airQuality.getAirList().get(0).component.co));
                                    //qualityTextView.setText(airQuality.getAirList().get(0).main.getAqi());
                                    viewHolderAirQuality.publishdate.setText(UtilityHelper.formatDate(airQuality.getAirList().get(0).dt));
                                    qualityColor(viewHolderAirQuality.qualityTextView,airQuality.getAirList().get(0).main.getAqi());
                                    viewHolderAirQuality.qualityTextView.setText(whichQuality(airQuality.getAirList().get(0).main.getAqi()));

                            }
                        }
                    });
/*
                    if(airQuality != null)
                    {
                        viewHolderAirQuality.pm2.setText(String.valueOf(airQuality.getAirList().get(0).component.pm2) + "%");
                        viewHolderAirQuality.pm10.setText(String.valueOf(airQuality.getAirList().get(0).component.pm10) + "%");
                        viewHolderAirQuality.so2.setText(String.valueOf(airQuality.getAirList().get(0).component.so2) + "%");
                        viewHolderAirQuality.no2.setText(String.valueOf(airQuality.getAirList().get(0).component.no2) + "%");
                        viewHolderAirQuality.o3.setText(String.valueOf(airQuality.getAirList().get(0).component.o3) + "%");
                        viewHolderAirQuality.co.setText(String.valueOf(airQuality.getAirList().get(0).component.co) + "%");
                        //qualityTextView.setText(airQuality.getAirList().get(0).main.getAqi());

                        qualityColor(viewHolderAirQuality.qualityTextView,airQuality.getAirList().get(0).main.getAqi());
                        viewHolderAirQuality.qualityTextView.setText(whichQuality(airQuality.getAirList().get(0).main.getAqi()));

                    }
*/
                    break;
            }

        }

        public class ViewHolderIntro extends RecyclerView.ViewHolder{
            private TextView cityTempText;
            private TextView cityDescText;

            public ViewHolderIntro(View itemView) {
                super(itemView);
                cityTempText =  itemView.findViewById(R.id.temperatureTextView);
                cityDescText =  itemView.findViewById(R.id.TempDescTextView);
            }
        }

        public class ViewHolderWeekly extends RecyclerView.ViewHolder{

            public ViewHolderWeekly(View itemView) {
                super(itemView);
                itemView.setBackgroundResource(UtilityHelper.getCardViewColorResourceForWeatherCondition(favouriteItem.getDailyForecasts().get(0).getWeathers().get(0).getId()));
            }
        }

        public class ViewHolderHourly extends RecyclerView.ViewHolder{

        public ViewHolderHourly(View itemView) {
            super(itemView);
            itemView.setBackgroundResource(UtilityHelper.getCardViewColorResourceForWeatherCondition(favouriteItem.getDailyForecasts().get(0).getWeathers().get(0).getId()));
        }
     }

        public class ViewHolderWeather extends RecyclerView.ViewHolder{

            public ViewHolderWeather(View itemView) {
                super(itemView);
                itemView.setBackgroundResource(UtilityHelper.getCardViewColorResourceForWeatherCondition(favouriteItem.getDailyForecasts().get(0).getWeathers().get(0).getId()));

            }
        }

        public class ViewHolderSun extends RecyclerView.ViewHolder{

            public ViewHolderSun(View itemView) {
                super(itemView);
                if(favouriteItem != null);
                {
                    sv      = itemView.findViewById(R.id.sv);

                    Date sunRisedate = new Date((long) (favouriteItem.getCity().getSunrise() * 1000));
                    Date sunSetdate = new Date((long) (favouriteItem.getCity().getSunset()* 1000));

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
                    itemView.setBackgroundResource(UtilityHelper.getCardViewColorResourceForWeatherCondition(favouriteItem.getDailyForecasts().get(0).getWeathers().get(0).getId()));


                }
            }
        }

        public class ViewHolderAirQuality extends RecyclerView.ViewHolder{
            private TextView pm2;
            private TextView pm10;
            private TextView so2;
            private TextView no2;
            private TextView o3;
            private TextView co;
            private TextView qualityTextView;
            private TextView publishdate;

            public ViewHolderAirQuality(View itemView) {
                super(itemView);
                pm2     = itemView.findViewById(R.id.pm2);;
                pm10    = itemView.findViewById(R.id.pm10);
                so2     = itemView.findViewById(R.id.so2);
                no2     = itemView.findViewById(R.id.no2);
                o3      = itemView.findViewById(R.id.o3);
                co      = itemView.findViewById(R.id.co);
                qualityTextView = itemView.findViewById(R.id.qualityTextView);
                publishdate = itemView.findViewById(R.id.publishdate);
                itemView.setBackgroundResource(UtilityHelper.getCardViewColorResourceForWeatherCondition(favouriteItem.getDailyForecasts().get(0).getWeathers().get(0).getId()));
            }
        }

        public void setFavourtieItem(WeatherForecast favouriteItem) {
            this.favouriteItem = favouriteItem;
            notifyDataSetChanged();
    }

        public String whichQuality(int quality){
        switch (quality){
            case 1:
                return "Good";
            case 2:
                return "Fair";
            case 3:
                return "Moderate";
            case 4:
                return "Poor";
            case 5:
                return "Very Poor";
        }
        return "";
    }

        public void qualityColor(TextView textView, int quality){
        switch (quality){
            case 1:
                textView.setTextColor(Color.parseColor("#7CFF0A"));
                break;
            case 2:
                textView.setTextColor(Color.parseColor("#FBC02D"));
                break;
            case 3:
                textView.setTextColor(Color.parseColor("#E64A19"));
                break;
            case 4:
                textView.setTextColor(Color.parseColor("#D32F2F"));
                break;
            case 5:
                textView.setTextColor(Color.parseColor("#7C1414"));
                break;
        }
    }

    public AirQuality getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(AirQuality airQuality) {
        if(airQuality != null) {
            this.airQuality = airQuality;
            notifyDataSetChanged();
        }
    }
}
