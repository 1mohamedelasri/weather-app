package com.devel.weatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.devel.weatherapp.MainActivity;
import com.devel.weatherapp.R;
import com.devel.weatherapp.models.DailyForecast;
import com.devel.weatherapp.models.SavedDailyForecast;
import com.devel.weatherapp.models.ScreenItem;

import java.util.ArrayList;
import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {

   Context mContext ;
   List<ScreenItem> mListScreen;
    private RecyclerView recyclerView;
    private WeeklyAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public IntroViewPagerAdapter(Context mContext, List<ScreenItem> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen = inflater.inflate(R.layout.layout_screen,null);


        container.addView(layoutScreen);

        recyclerView = (RecyclerView) container.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(container.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new WeeklyAdapter(mContext);

        List<SavedDailyForecast> dailyForecasts = new ArrayList();
        dailyForecasts.add(new SavedDailyForecast());
        dailyForecasts.add(new SavedDailyForecast());
        recyclerAdapter.setForecasts(dailyForecasts);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setHasFixedSize(true);


        return layoutScreen;


    }

    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);

    }
}
