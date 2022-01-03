package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;
import com.google.android.gms.maps.model.Marker;

/**
 * Collection of static methods that help represent a {@code Station} with text
 * or a {@code Marker}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #getConditionTextId
 * @see #getConditionText
 * @see #getMarkerIconId
 * @see #getMarkerIcon
 * @see Marker
 * @see Station#state
 * @see Station#bikes
 */
public class StationCondition {

    /**
     * Gets the resource identifier of the text representation of a
     * {@code Station}'s condition.
     * @param station   a station
     * @return          an integer identifying the text representing the
     *                  station's condition
     */
    private static int getConditionTextId(Station station) {
        switch (station.state) {
            case 1:
                return R.string.station_reported_text;
            case 2:
                return R.string.station_broken_text;
        }
        if (station.bikes == 0)
            return R.string.station_empty_text;
        if (station.bikes <= 3)
            return R.string.station_almost_empty_text;
        return R.string.station_working_text;
    }

    /**
     * Gets the text representation of a {@code Station}'s condition.
     * @param context   the context of the application providing all
     *                  global information
     * @param station   a station
     * @return          a string representing the station's condition
     * @see Context
     */
    @NonNull
    public static String getConditionText(@NonNull Context context, Station station) {
        return context.getString(getConditionTextId(station));
    }

    /**
     * Gets the resource identifier of the icon of a marker representing a
     * {@code Station}.
     * @param station   a station
     * @return          an integer identifying the icon of a marker
     *                  representing the station
     */
    private static int getMarkerIconId(Station station) {
        switch (station.state) {
            case 2:
                return R.drawable.marker_station_broken_empty;
            case 1:
                return R.drawable.marker_station_reported_almost_empty;
        }
        if (station.bikes == 0)
            return R.drawable.marker_station_broken_empty;
        if (station.bikes <= 3)
            return R.drawable.marker_station_reported_almost_empty;
        return R.drawable.marker_station;
    }

    /**
     * Gets the icon of a marker representing a {@code Station} (based on its
     * state and bikes).
     * @param context   the context of the application providing all
     *                  global information
     * @param station   a station
     * @return          a bitmap containing the icon
     * @see Context
     */
    @NonNull
    public static Bitmap getMarkerIcon(@NonNull Context context, Station station) {
        int markerWidth = context.getResources().getDimensionPixelSize(R.dimen.marker_width);
        int markerHeight = context.getResources().getDimensionPixelSize(R.dimen.marker_height);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), getMarkerIconId(station), options);
        options.inSampleSize = options.outWidth/markerWidth;
        options.inJustDecodeBounds = false;
        Bitmap stateImage = BitmapFactory.decodeResource(context.getResources(),
                getMarkerIconId(station), options);
        return Bitmap.createScaledBitmap(stateImage, markerWidth, markerHeight, false);
    }
}
