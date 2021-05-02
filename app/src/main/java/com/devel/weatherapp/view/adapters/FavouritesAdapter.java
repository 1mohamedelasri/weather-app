package com.devel.weatherapp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.utils.Utility;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

import java.util.Calendar;
import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    // Member variable to handle item clicks
    WeatherViewModel mViewModel;
    private Context mContext;
    private List<FavouriteItem> favouriteItems;
    private int currentPos = 0;

    public FavouritesAdapter(Context context, WeatherViewModel mViewModel) {
        this.mContext = context;
        this.mViewModel= mViewModel;
        favouriteItems = mViewModel.getFavourtieItems();
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.favorite_item, parent, false);

        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        this.currentPos = position;
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        // Determine the values of the wanted data
        FavouriteItem favouriteItem = mViewModel.getFavourtieItems().get(position);


        //Set values
        holder.favCity.setText(favouriteItem.city);
        holder.favTemperature.setText(favouriteItem.temperature);
        holder.favImg.setImageResource(Utility.getArtResourceForWeatherCondition(favouriteItem.savedDailyForecast.get(0).weatherid));
        holder.favCountry.setText(favouriteItem.country);

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

    public List<FavouriteItem> getFavouriteItems() {
        return favouriteItems;
    }

    public void setFavouriteItems(List<FavouriteItem> forecastEntities) {
        favouriteItems = forecastEntities;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        favouriteItems.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(FavouriteItem item, int position) {
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
            favTemperature = itemView.findViewById(R.id.FavTemperature);
            favImg = itemView.findViewById(R.id.FavImg);
            favDelete = itemView.findViewById(R.id.FavDelete);
            favDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mViewModel.getFavourtieItems().remove(currentPos);
            notifyDataSetChanged();
        }
    }
}

