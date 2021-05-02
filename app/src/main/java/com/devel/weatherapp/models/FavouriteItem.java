package com.devel.weatherapp.models;

import java.util.List;

public class FavouriteItem {
    public Long id;
    public String city;
    public String country;
    public String temperature;
    public String description;
    public List<SavedDailyForecast> savedDailyForecast;


    public FavouriteItem(Long id, String city, String temperature, String description, String country, List<SavedDailyForecast> savedDailyForecast) {
        this.city = city;
        this.temperature = temperature;
        this.description = description;
        this.savedDailyForecast = savedDailyForecast;
        this.id = id;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof FavouriteItem) ) return false;

        FavouriteItem other = (FavouriteItem) o;
        if(this.id != other.id && !city.equals(other.city))      return false;

        return true;

    }

}
