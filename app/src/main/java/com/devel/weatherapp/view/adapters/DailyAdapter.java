package com.devel.weatherapp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.WeatherList;
import com.devel.weatherapp.utils.UtilityHelper;

import java.util.Calendar;
import java.util.List;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.WeeklyViewHolder> {

    // Member variable to handle item clicks
    private List<WeatherList> forecasts;
    private Context mContext;

    public DailyAdapter(Context context, List<WeatherList> savedDailyForecast) {
        mContext = context;
        forecasts = savedDailyForecast;
    }

    @NonNull
    @Override
    public WeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_daily, parent, false);

        return new WeeklyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyViewHolder holder, int position) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        // Determine the values of the wanted data
        WeatherList forecast = forecasts.get(position);
        Double max_temp = forecast.getMain().getTempMax();
        Double min_temp = forecast.getMain().getTempMin();
        Long date = forecast.getDt();
        String description = forecast.getWeathers().get(0).getDescription();
        Long weather_id = forecast.getWeathers().get(0).getId();

        //Set values
        holder.desc.setText(description);
        holder.temp.setText(UtilityHelper.formatTemperature(mContext, max_temp, false) + "/" + UtilityHelper.formatTemperature(mContext, min_temp, true));
        holder.imageView.setImageResource(UtilityHelper.getArtResourceForWeatherCondition(weather_id));
        holder.day.setText(UtilityHelper.format(date));
    }


    @Override
    public int getItemCount() {
        if (forecasts == null) {
            return 0;
        }
        return forecasts.size() - 1;
    }

    public List<WeatherList> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<WeatherList> forecastEntities) {
        forecasts = forecastEntities;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        forecasts.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(WeatherList item, int position) {
        forecasts.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    public class WeeklyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView day;
        TextView temp;
        TextView desc;
        ImageView imageView;

        WeeklyViewHolder(View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.FavCity);
            temp = itemView.findViewById(R.id.temp);
            desc = itemView.findViewById(R.id.desc);
            imageView = itemView.findViewById(R.id.weather_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }
}

