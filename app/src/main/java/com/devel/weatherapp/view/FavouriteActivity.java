package com.devel.weatherapp.view;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devel.weatherapp.R;
import com.devel.weatherapp.view.adapters.FavouritesAdapter;
import com.devel.weatherapp.view.adapters.WeeklyAdapter;
import com.devel.weatherapp.viewmodels.WeatherViewModel;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;

public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public FavouritesAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private WeatherViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        getSupportActionBar().hide();
        mViewModel  = WeatherViewModel.getInstance(getApplication());

        recyclerView = (RecyclerView) findViewById(R.id.FavRecyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new FavouritesAdapter(getApplicationContext(),mViewModel);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
