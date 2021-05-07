package com.devel.weatherapp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.City;

import java.util.Calendar;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    // Member variable to handle item clicks
    private List<City> searchCitiesResult;
    private Context mContext;

    public CityAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_weekly, parent, false);

        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        // Determine the values of the wanted data
        City resultedCity = searchCitiesResult.get(position);
        holder.searchResCity.setText(resultedCity.getName());
        holder.SearchResCountry.setText(resultedCity.getCountry());
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (searchCitiesResult == null) {
            return 0;
        }
        return searchCitiesResult.size();
    }

    public List<City> getForecasts() {
        return searchCitiesResult;
    }

    public void setForecasts(List<City> forecastEntities) {
        searchCitiesResult = forecastEntities;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        searchCitiesResult.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(City item, int position) {
        searchCitiesResult.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    public class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView searchResCity;
        TextView SearchResCountry;

        CityViewHolder(View itemView) {
            super(itemView);

            searchResCity = itemView.findViewById(R.id.FavCity);
            SearchResCountry = itemView.findViewById(R.id.FavCountry);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Long elementId = searchCitiesResult.get(getAdapterPosition()).getId();
        }
    }
}

