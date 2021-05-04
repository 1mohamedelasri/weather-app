package com.devel.weatherapp.view;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devel.weatherapp.R;
import com.devel.weatherapp.models.FavouriteItem;
import com.devel.weatherapp.utils.Resource;
import com.devel.weatherapp.view.adapters.FavouritesAdapter;
import com.devel.weatherapp.view.adapters.WeeklyAdapter;
import com.devel.weatherapp.viewmodels.WeatherViewModel;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;

import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    private final  String TAG = "FavouriteActivity";
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

        mViewModel.getDataSource().observe(this, new Observer<Resource<List<FavouriteItem>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<FavouriteItem>> listResource) {
                if(listResource != null){
                    Log.d(TAG, "onChanged: status: " + listResource.status);

                    if(listResource.data != null){
                        switch (listResource.status){
                            case LOADING:{
                            }

                            case ERROR:{
                                Log.e(TAG, "onChanged: cannot refresh the cache." );
                                Log.e(TAG, "onChanged: ERROR message: " + listResource.message );
                                Log.e(TAG, "onChanged: status: ERROR, #recipes: " + listResource.data.size());
                                recyclerAdapter.setFavouriteItems(listResource.data);

                                break;
                            }

                            case SUCCESS:{
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
}
