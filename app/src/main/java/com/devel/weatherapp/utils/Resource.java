package com.devel.weatherapp.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devel.weatherapp.models.FavouriteItem;

import java.util.List;

public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;

    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(@NonNull String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    public void delete(FavouriteItem favouriteItem) {
        ((List)this.data).remove(favouriteItem);
    }

    public void insert(FavouriteItem favouriteItem) {
        ((List)this.data).add(favouriteItem);
    }

    public List getSourceData() {
        return ((List)this.data);
    }

    public enum Status { SUCCESS, ERROR, LOADING}
}