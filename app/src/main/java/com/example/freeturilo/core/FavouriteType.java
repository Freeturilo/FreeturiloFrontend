package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;

public enum FavouriteType {
    HOME, SCHOOL, WORK, OTHER;

    private static int getTypeTextId(@NonNull FavouriteType favouriteType) {
        switch (favouriteType) {
            case HOME:
                return R.string.favourite_home_text;
            case SCHOOL:
                return R.string.favourite_school_text;
            case WORK:
                return R.string.favourite_work_text;
            default:
                return R.string.favourite_other_text;
        }
    }

    @NonNull
    public static String getTypeText(@NonNull Context context, @NonNull FavouriteType favouriteType) {
        return context.getString(getTypeTextId(favouriteType));
    }

    private static int getMarkerIconId(@NonNull FavouriteType favouriteType) {
        switch (favouriteType) {
            case HOME:
                return R.drawable.marker_home;
            case WORK:
                return R.drawable.marker_work;
            case SCHOOL:
                return R.drawable.marker_school;
            default:
                return R.drawable.marker_other;
        }
    }

    @NonNull
    public static Bitmap getMarkerIcon(@NonNull Context context, @NonNull FavouriteType favouriteType) {
        return BitmapFactory.decodeResource(context.getResources(), getMarkerIconId(favouriteType));
    }
}
