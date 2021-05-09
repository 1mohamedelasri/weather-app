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
import java.util.Date;
import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder> {

    // Member variable to handle item clicks
    private List<WeatherList> forecasts;
    private Context mContext;

    public HourlyAdapter(Context context, List<WeatherList> savedDailyForecast) {
        mContext = context;
        forecasts = savedDailyForecast;
    }

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_hourly, parent, false);

        return new HourlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        Calendar today = Calendar.getInstance();
        Calendar check = Calendar.getInstance();


        WeatherList forecast = forecasts.get(position);

        Date date = UtilityHelper.timestampToDate(forecast.getDt());

        Double temp = forecast.getMain().getTemp();
        String description = forecast.getWeathers().get(0).getDescription();
        Long weather_id = forecast.getWeathers().get(0).getId();

        //Set values
        holder.hour.setText(UtilityHelper.formatHourly(forecast.getDt()));
        holder.desc.setText(description);
        holder.temp.setText(UtilityHelper.formatTemperature(mContext, temp, true));
        holder.imageView.setImageResource(UtilityHelper.getArtResourceForWeatherCondition(weather_id));

        check.setTime(UtilityHelper.timestampToDate(forecast.getDt()));
        if (today.get(Calendar.DAY_OF_YEAR) == check.get(Calendar.DAY_OF_YEAR) &&
                today.get(Calendar.YEAR) == check.get(Calendar.YEAR))
            holder.day.setText("Today");
        else
            holder.day.setText(UtilityHelper.format(forecast.getDt()));
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
    public class HourlyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView hour;
        TextView day;
        TextView temp;
        TextView desc;
        ImageView imageView;

        HourlyViewHolder(View itemView) {
            super(itemView);

            hour = itemView.findViewById(R.id.hourly_hour);
            day = itemView.findViewById(R.id.hourly_day);
            temp = itemView.findViewById(R.id.hourly_temp);
            desc = itemView.findViewById(R.id.hourly_desc);
            imageView = itemView.findViewById(R.id.hourly_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }
}

