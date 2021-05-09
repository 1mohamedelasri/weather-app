package com.devel.weatherapp.view.adapters;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.Tuple;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.utils.UtilityHelper;
import com.devel.weatherapp.view.SearchWeatherCity;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

public class SearchFragment extends Fragment {

    private WeatherForecast favouriteItem;
    private TextView searchResCity;
    private TextView    searchResCountry ;
    private TextView    searchTempText ;
    private ImageButton addCityToFav;
    private ImageView searchWeatherIcon;
    private WeatherViewModel mWeatherListViewModel;
    private SearchWeatherCity.STATUS status;
    private Context context;

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
                                                        mWeatherListViewModel.fetchbyCity();
                                                        getParentFragmentManager().popBackStack();
                                                        mWeatherListViewModel.setlastAddedItemIndex(new Tuple(Tuple.Source.SEARCH,mWeatherListViewModel.getDataSource().getValue().data.size()-1));
                                                        getActivity().finish();
                                                    }
                                                }
                );

                this.searchResCity.setText((CharSequence) favouriteItem.getCity().getName());
                this.searchResCountry.setText(UtilityHelper.getCountryName(favouriteItem.getCity().getCountry()));
                this.searchTempText.setText(UtilityHelper.formatTemperature(context,favouriteItem.getDailyForecasts().get(0).getMain().getTemp(),true));

                break;
            case NOT_FOUND:
                 view = inflater.inflate(R.layout.fragement_notfound, container, false);
                break;
            case NONE:
                view = inflater.inflate(R.layout.fragment_none, container, false);
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

    public void setContext(Context cx){
        this.context = cx;
    }

}