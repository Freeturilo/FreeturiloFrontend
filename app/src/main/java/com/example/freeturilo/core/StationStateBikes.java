package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;

public class StationStateBikes {

    private static int getStateBikesTextId(int state, int bikes) {
        switch (state) {
            case 1:
                return R.string.station_reported_text;
            case 2:
                return R.string.station_broken_text;
        }
        if (bikes == 0)
            return R.string.station_empty_text;
        if (bikes <= 3)
            return R.string.station_almost_empty_text;
        return R.string.station_working_text;
    }

    @NonNull
    public static String getStateBikesText(@NonNull Context context, int state, int bikes) {
        return context.getString(getStateBikesTextId(state, bikes));
    }

    private static int getMarkerIconId(int state, int bikes) {
        switch (state) {
            case 2:
                return R.drawable.marker_station_broken_empty;
            case 1:
                return R.drawable.marker_station_reported_almost_empty;
        }
        if (bikes == 0)
            return R.drawable.marker_station_broken_empty;
        if (bikes <= 3)
            return R.drawable.marker_station_reported_almost_empty;
        return R.drawable.marker_station;
    }

    @NonNull
    public static Bitmap getMarkerIcon(@NonNull Context context, int state, int bikes) {
        int markerWidth = context.getResources().getDimensionPixelSize(R.dimen.marker_width);
        int markerHeight = context.getResources().getDimensionPixelSize(R.dimen.marker_height);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), getMarkerIconId(state, bikes), options);
        options.inSampleSize = options.outWidth/markerWidth;
        options.inJustDecodeBounds = false;
        Bitmap stateImage = BitmapFactory.decodeResource(context.getResources(), getMarkerIconId(state, bikes), options);
        return Bitmap.createScaledBitmap(stateImage, markerWidth, markerHeight, false);
    }
}
