package com.devel.weatherapp.models;

public class Tuple {
    public Source source;

    ;
    public Integer index;
    public Tuple(Source s, Integer index) {
        this.index = index;
        this.source = s;
    }

    public enum Source {FAVORTIES, SEARCH}
}