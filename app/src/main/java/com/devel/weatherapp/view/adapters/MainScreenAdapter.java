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
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.models.WeatherList;
import com.devel.weatherapp.utils.UtilityHelper;
import com.devel.weatherapp.view.SunView;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class MainScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TextView cityNameText;
    private TextView cityTempText;
    private TextView cityDescText;
    private View layoutScreen;
    private int currentPos = 0;
    private TextView feelLikeValue ;
    private TextView HumidityValue;
    private TextView cloudiness;
    private TextView WindSpeedValue;
    private SunView sv;
    private AirQuality airQuality;
    private TextView pm2;
    private TextView pm10;
    private TextView so2;
    private TextView no2;
    private TextView o3;
    private TextView co;
    private TextView qualityTextView;
    private final int currentScreenPosition;

    private RecyclerView recyclerView;
    public WeeklyAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        return 5;
    }

    @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view =null;
            RecyclerView.ViewHolder viewHolder = null;

            switch (viewType){
                case 0:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weekly_recycleview,parent,false);
                    viewHolder = new ViewHolderWeekly(view);
                    recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                    layoutManager = new LinearLayoutManager(parent.getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerAdapter = new WeeklyAdapter(mContext,favouriteItem.getConvertedDaily());
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerView.setHasFixedSize(true);
                    break;
                case 1:
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
                case 2:
                        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sunview, parent, false);
                        viewHolder = new ViewHolderSun(view);
                    break;
                case 3:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_airquality,parent,false);
                        qualityTextView= view.findViewById(R.id.qualityTextView);
                        viewHolder = new ViewHolderAirQuality(view);
                    break;
                case 4:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_airquality,parent,false);
                    qualityTextView= view.findViewById(R.id.qualityTextView);
                    viewHolder = new ViewHolderAirQuality(view);
                    break;

            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            switch (position){
                case 0:
                    ViewHolderWeekly viewHolderWeekly = (ViewHolderWeekly) holder;
                    break;
                case 1:
                    ViewHolderWeather viewHolderWeather = (ViewHolderWeather) holder;

                    break;
                case 2:
                    ViewHolderSun viewHolderSun = (ViewHolderSun) holder;
                    break;
                case 3:
                    ViewHolderAirQuality viewHolderAirQuality = (ViewHolderAirQuality) holder;
                    break;
            }

        }


        public class ViewHolderWeekly extends RecyclerView.ViewHolder{

            public ViewHolderWeekly(View itemView) {
                super(itemView);
            }
        }

        public class ViewHolderWeather extends RecyclerView.ViewHolder{

            public ViewHolderWeather(View itemView) {
                super(itemView);

            }
        }

        public class ViewHolderSun extends RecyclerView.ViewHolder{

            public ViewHolderSun(View itemView) {
                super(itemView);
                if(favouriteItem != null);
                {
                    sv      = itemView.findViewById(R.id.sv);

                    Date sunRisedate = new Date((long) (favouriteItem.getCity().getSunrise() * 1000));
                    Date sunSetdate = new Date((long) (favouriteItem.getCity().getSunrise()* 1000));

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

                }
            }
        }

        public class ViewHolderAirQuality extends RecyclerView.ViewHolder{

            public ViewHolderAirQuality(View itemView) {
                super(itemView);
                pm2     = itemView.findViewById(R.id.pm2);;
                pm10    = itemView.findViewById(R.id.pm10);
                so2     = itemView.findViewById(R.id.so2);
                no2     = itemView.findViewById(R.id.no2);
                o3      = itemView.findViewById(R.id.o3);
                co      = itemView.findViewById(R.id.co);

                weatherViewModel.getAirQuality().observe((LifecycleOwner) mContext, new Observer<AirQuality>() {
                    @Override
                    public void onChanged(AirQuality airQuality) {
                        if(airQuality != null)
                        {
                            if(airQuality != null && airQuality.getAirList().size() > 0 ) {
                                pm2.setText(String.valueOf(airQuality.getAirList().get(0).component.pm2)+"%");
                                pm10.setText(String.valueOf(airQuality.getAirList().get(0).component.pm10)+"%");
                                so2.setText(String.valueOf(airQuality.getAirList().get(0).component.so2)+"%");
                                no2.setText(String.valueOf(airQuality.getAirList().get(0).component.no2)+"%");
                                o3.setText(String.valueOf(airQuality.getAirList().get(0).component.o3)+"%");
                                co.setText(String.valueOf(airQuality.getAirList().get(0).component.co)+"%");
                                //qualityTextView.setText(airQuality.getAirList().get(0).main.getAqi());

                                switch (airQuality.getAirList().get(0).main.getAqi()){
                                    case 1:
                                        qualityTextView.setText("Good");
                                        break;
                                    case 2:
                                        qualityTextView.setText("Fair");
                                        break;
                                    case 3:
                                        qualityTextView.setText("Moderate");
                                        break;
                                    case 4:
                                        qualityTextView.setText("Poor");
                                        break;
                                    case 5:
                                        qualityTextView.setText("Very Poor");
                                        break;

                                }
                                qualityColor(qualityTextView,airQuality.getAirList().get(0).main.getAqi());
                            }
                        }
                    }
                });

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
}