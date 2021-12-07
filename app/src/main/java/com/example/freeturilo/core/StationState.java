package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;

public class StationState {

    @NonNull
    public static String getStateText(@NonNull Context context, int state) {
        switch (state) {
            case 1:
                return context.getString(R.string.station_reported_text);
            case 2:
                return context.getString(R.string.station_broken_text);
            default:
                return context.getString(R.string.station_working_text);
        }
    }

    public static int getMarkerIconId(int state) {
        switch (state) {
            case 0:
                return R.drawable.marker_station;
            case 1:
                return R.drawable.marker_station_reported;
            default:
                return R.drawable.marker_station_broken;
        }
    }

    @NonNull
    public static Bitmap getMarkerIcon(@NonNull Context context, int state) {
        return BitmapFactory.decodeResource(context.getResources(), getMarkerIconId(state));
    }
}