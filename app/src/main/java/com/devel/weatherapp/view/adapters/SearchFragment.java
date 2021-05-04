package com.devel.weatherapp.view.adapters;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.DailyForecast;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.Status;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.utils.Utility;
import com.devel.weatherapp.view.SearchWeatherCity;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

import java.util.Calendar;

public class SearchFragment extends Fragment {

    private WeatherForecast favouriteItem;
    private TextView searchResCity;
    private TextView    searchResCountry ;
    private TextView    searchTempText ;
    private ImageButton addCityToFav;
    private ImageView searchWeatherIcon;
    private WeatherViewModel mWeatherListViewModel;
    private SearchWeatherCity.STATUS status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_notfound, container, false);
        switch (status){
            case FOUND:
                 view = inflater.inflate(R.layout.fragement_search, container, false);

                addCityToFav = view.findViewById(R.id.AddCityToFav);
                searchResCity = view.findViewById(R.id.searchCityTextView);
                searchResCountry = view.findViewById(R.id.searchCountyTextView);
                searchWeatherIcon = view.findViewById(R.id.searchWeatherIcon);
                searchTempText =  view.findViewById(R.id.searchTempTextView);
                mWeatherListViewModel = WeatherViewModel.getInstance(getActivity().getApplication());
                addCityToFav.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        mWeatherListViewModel.fetchbyCity("");
                                                        getActivity().finish();
                                                    }
                                                }
                );

                this.searchResCity.setText((CharSequence) favouriteItem.getCity().getName());
                this.searchResCountry.setText(Utility.getCountryName(favouriteItem.getCity().getCountry()));
                this.searchTempText.setText(getTemperature(favouriteItem.getDailyForecasts().get(0)));

                break;
            case NOT_FOUND:
                 view = inflater.inflate(R.layout.fragement_notfound, container, false);
                break;
        }


        return view;
    }

    public void setFavouriteItem( WeatherForecast favouriteItem){
        this.favouriteItem = favouriteItem;
    }

    public void setStatus(SearchWeatherCity.STATUS status) {

        this.status = status;
    }

    public String getTemperature(DailyForecast s){
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String temperatureText = "";

        if (timeOfDay >= 5 && timeOfDay < 12) {
            temperatureText = (Utility.formatTemperature(getContext(), s.getTemp().getMorn()));
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            temperatureText = (Utility.formatTemperature(getContext(), s.getTemp().getDay()));
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            temperatureText = (Utility.formatTemperature(getContext(), s.getTemp().getEve()));
        } else if ((timeOfDay >= 21 || timeOfDay >= 0)  && timeOfDay < 5) {
            temperatureText = (Utility.formatTemperature(getContext(), s.getTemp().getNight()));
        }
        return temperatureText;
    }


}