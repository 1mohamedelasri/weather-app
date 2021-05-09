package com.devel.weatherapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.WeatherForecast;
import com.devel.weatherapp.utils.Resource;
import com.devel.weatherapp.view.adapters.FavouritesAdapter;
import com.devel.weatherapp.viewmodels.WeatherViewModel;

import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    private final String TAG = "FavouriteActivity";
    public FavouritesAdapter recyclerAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private WeatherViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        getSupportActionBar().hide();
        mViewModel = WeatherViewModel.getInstance(getApplication());

        recyclerView = (RecyclerView) findViewById(R.id.FavRecyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new FavouritesAdapter(FavouriteActivity.this, mViewModel);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setHasFixedSize(true);

        mViewModel.getDataSource().observe(this, new Observer<Resource<List<WeatherForecast>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<WeatherForecast>> listResource) {
                if (listResource != null) {
                    Log.d(TAG, "onChanged: status: " + listResource.status);

                    if (listResource.data != null) {
                        switch (listResource.status) {
                            case LOADING: {
                            }

                            case ERROR: {
                                Log.e(TAG, "onChanged: cannot refresh the cache.");
                                Log.e(TAG, "onChanged: ERROR message: " + listResource.message);
                                Log.e(TAG, "onChanged: status: ERROR, #recipes: " + listResource.data.size());
                                recyclerAdapter.setFavouriteItems(listResource.data);

                                break;
                            }

                            case SUCCESS: {
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #Recipes: " + listResource.data.size());
                                recyclerAdapter.setFavouriteItems(listResource.data);

                                break;
                            }
                        }
                    }
                }
            }
        });
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

    public void favReturn(View view) {
        finish();
    }
}
