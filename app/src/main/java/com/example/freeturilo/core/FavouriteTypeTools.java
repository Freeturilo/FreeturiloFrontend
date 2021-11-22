package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.freeturilo.R;

public class FavouriteTypeTools {
    public static String getTypeText(Context context, FavouriteType favouriteType) {
        switch (favouriteType){
            case HOME:
                return context.getString(R.string.favourite_home_text);
            case SCHOOL:
                return context.getString(R.string.favourite_school_text);
            case WORK:
                return context.getString(R.string.favourite_work_text);
            default:
                return context.getString(R.string.favourite_other_text);
        }
    }

    public static int getMarkerIconId(FavouriteType favouriteType) {
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

    public static Bitmap getMarkerIcon(Context context, FavouriteType favouriteType) {
        return BitmapFactory.decodeResource(context.getResources(), getMarkerIconId(favouriteType));
    }
}
