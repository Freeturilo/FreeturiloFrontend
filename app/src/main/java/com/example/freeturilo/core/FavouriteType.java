package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;
import com.google.android.gms.maps.model.Marker;

/**
 * Type of a {@code Favourite} location.
 * <p>
 * Values of this enum represent different types of user-favourite locations.
 * There are multiple static methods declared within the enum that help
 * represent a type with text or a {@code Marker}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #HOME
 * @see #SCHOOL
 * @see #WORK
 * @see #OTHER
 * @see #getTypeTextId
 * @see #getTypeText
 * @see #getMarkerIconId
 * @see #getMarkerIcon
 * @see Favourite
 */
public enum FavouriteType {
    /**
     * Represents favourite locations defined as homes.
     */
    HOME,
    /**
     * Represents favourite locations defined as schools.
     */
    SCHOOL,
    /**
     * Represents favourite locations defined as work.
     */
    WORK,
    /**
     * Represents all favourite locations that don't fit
     * other categories.
     */
    OTHER;

    /**
     * Gets the resource identifier of the text representation of
     * a {@code FavouriteType}.
     * @param favouriteType     a favourite type
     * @return                  an integer identifying the text representing
     *                          the favourite type
     */
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

    /**
     * Gets the text representation of a {@code FavouriteType}.
     * @param context           the context of the application providing all
     *                          global information
     * @param favouriteType     a favourite type
     * @return                  a string representing the favourite type
     * @see Context
     */
    @NonNull
    public static String getTypeText(@NonNull Context context, @NonNull FavouriteType favouriteType) {
        return context.getString(getTypeTextId(favouriteType));
    }

    /**
     * Gets the resource identifier of the icon of a marker representing a
     * {@code FavouriteType}.
     * @param favouriteType     a favourite type
     * @return                  an integer identifying the icon of a marker
     *                          representing the favourite type
     * @see Marker
     */
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

    /**
     * Gets the icon of a marker representing a {@code FavouriteType}.
     * @param context           the context of the application providing all
     *                          global information
     * @param favouriteType     a favourite type
     * @return                  a bitmap containing the icon
     * @see Context
     */
    @NonNull
    public static Bitmap getMarkerIcon(@NonNull Context context, @NonNull FavouriteType favouriteType) {
        Bitmap favouriteImage = BitmapFactory.decodeResource(context.getResources(), getMarkerIconId(favouriteType));
        int markerWidth = context.getResources().getDimensionPixelSize(R.dimen.marker_width);
        int markerHeight = context.getResources().getDimensionPixelSize(R.dimen.marker_height);
        return Bitmap.createScaledBitmap(favouriteImage, markerWidth, markerHeight, false);
    }
}
