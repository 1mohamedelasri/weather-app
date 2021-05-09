package com.devel.weatherapp.models;

public class Tuple {
    public enum Source {FAVORTIES,SEARCH};

    public Source source;
    public Integer index;
    public Tuple(Source s, Integer index){
        this.index = index;
        this.source = s;
    }
}