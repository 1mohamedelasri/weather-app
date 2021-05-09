package com.devel.weatherapp.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.Tuple;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.utils.Constants;
import com.devel.weatherapp.utils.UtilityHelper;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    // Member variable to handle item clicks
    WeatherViewModel mViewModel;
    private Context mContext;
    private List<WeatherForecast> favouriteItems;

    public FavouritesAdapter(Context context, WeatherViewModel mViewModel) {
        this.mContext = context;
        this.mViewModel= mViewModel;
        favouriteItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_favorite, parent, false);

        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        // Determine the values of the wanted data

        //Set values
        holder.favCity.setText(favouriteItems.get(position).getCity().getName());
        holder.favTemperature.setText(UtilityHelper.formatTemperature(mContext,favouriteItems.get(position).getDailyForecasts().get(0).getMain().getTemp(),true));
        holder.favImg.setImageResource(UtilityHelper.getArtResourceForWeatherCondition(favouriteItems.get(position).getDailyForecasts().get(0).getWeathers().get(0).getId()));
        holder.favCountry.setText(UtilityHelper.getCountryName(favouriteItems.get(position).getCity().getCountry()));

    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (favouriteItems == null) {
            return 0;
        }
        return favouriteItems.size();
    }

    public List<WeatherForecast> getFavouriteItems() {
        return favouriteItems;
    }

    public void setFavouriteItems(List<WeatherForecast> forecastEntities) {
        Collections.sort(forecastEntities);
        favouriteItems = forecastEntities;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        favouriteItems.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(WeatherForecast item, int position) {
        favouriteItems.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    public class FavouritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView favCity;
        TextView favCountry;
        TextView favTemperature;
        ImageView favImg;
        ImageView favDelete;

        FavouritesViewHolder(View itemView) {
            super(itemView);

            favCity = itemView.findViewById(R.id.FavCity);
            favCountry = itemView.findViewById(R.id.FavCountry);
            favImg = itemView.findViewById(R.id.FavImg);
            favDelete = itemView.findViewById(R.id.favDelete);
            favTemperature = itemView.findViewById(R.id.FavTemperature);
            favDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.dropFravourtieItem(favouriteItems.get(getLayoutPosition()));
                    favouriteItems.remove(getLayoutPosition());
                    notifyDataSetChanged();

                }
            });

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            mViewModel.setlastAddedItemIndex(new Tuple(Tuple.Source.FAVORTIES,getLayoutPosition()));
            ((Activity)mContext).finish();
        }
    }
}

