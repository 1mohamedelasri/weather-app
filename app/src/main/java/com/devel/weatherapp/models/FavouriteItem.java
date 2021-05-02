package com.devel.weatherapp.models;

import java.util.List;

public class FavouriteItem {
    public Long id;
    public String city;
    public String temperature;
    public String description;
    public List<SavedDailyForecast> savedDailyForecast;


    public FavouriteItem(Long id, String city, String temperature, String description, List<SavedDailyForecast> savedDailyForecast) {
        this.city = city;
        this.temperature = temperature;
        this.description = description;
        this.savedDailyForecast = savedDailyForecast;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof FavouriteItem) ) return false;

        FavouriteItem other = (FavouriteItem) o;
        if(this.id != other.id)      return false;
        if(! this.id.equals(other.id)) return false;

        return true;

    }

    @Override
    public int hashCode() {
        return (int) (id*city.hashCode());
    }

}
