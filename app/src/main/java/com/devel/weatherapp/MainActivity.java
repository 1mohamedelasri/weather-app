package com.devel.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.devel.weatherapp.adapters.IntroViewPagerAdapter;
import com.devel.weatherapp.adapters.RecyclerAdapter;
import com.devel.weatherapp.models.ScreenItem;
import com.devel.weatherapp.viewmodels.WeatherViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.devel.weatherapp.utils.Constants.*;

public class MainActivity extends AppCompatActivity {

    private WeatherViewModel mWeatherListViewModel;
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<String> title, subTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI(this);
        setContentView(R.layout.activity_main);

        initialiseVariables();

        mWeatherListViewModel.getCityData("GRenoble",API_KEY);

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("10", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",R.drawable.img1));
        mList.add(new ScreenItem("20","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",R.drawable.img2));
        mList.add(new ScreenItem("30","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua, consectetur  consectetur adipiscing elit",R.drawable.img3));

        // setup viewpager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);






        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

                title = new ArrayList<>();
                subTitle = new ArrayList<>();

                title.add("Alpha");
                title.add("Beta");
                title.add("Gamma");
                title.add("Delta");
                title.add("Epsilon");
                title.add("Zeta");
                title.add("Eta");
                title.add("Theta");
                title.add("Lambda");
                title.add("Kappa");
                title.add("Mu");
                title.add("Nu");

                layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerAdapter = new RecyclerAdapter(title);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerView.setHasFixedSize(true);

            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // if you want some fade in / fade out anim, you may want the values provided here
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });




    }


    private void initialiseVariables() {

        // Instantiate the weather View Model.
        mWeatherListViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);

    }

    public static void hideSystemUI(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }



}