package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

/**
 * A user-favourite geographical location.
 * <p>
 * Object of this class represents a real-world location that has been
 * selected as favourite by the application user. It encapsulates the
 * {@link #name}, {@link #latitude}, {@link #longitude} and {@link #type}
 * of the location.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see Location
 * @see FavouriteType
 */
public class Favourite extends Location {
    /**
     * Stores the enum type of this location.
     */
    public FavouriteType type;

    /**
     * Class constructor.
     * @param name      a string describing this location
     * @param latitude  a double equal to the north–south position of this
     *                  location
     * @param longitude a double equal to the east-west position of this
     *                  location
     * @param type      an enum value defining the type of this favourite
     *                  location
     */
    public Favourite(@NonNull String name, double latitude,
                     double longitude, @NonNull FavouriteType type) {
        super(name, latitude, longitude);
        this.type = type;
    }

    /**
     * Creates options for a {@code Marker} representing this location.
     * Defines geographical positioning and a custom icon of the Marker.
     * @param context   the context of the application providing all global
     *                  information
     * @return          options for creating a Marker representing this
     *                  location
     * @see #type
     * @see FavouriteType
     * @see Marker
     * @see Context
     * @see MarkerOptions
     */
    @NonNull
    @Override
    public MarkerOptions createMarkerOptions(@NonNull Context context) {
        return super.createMarkerOptions(context)
                .icon(BitmapDescriptorFactory.fromBitmap(FavouriteType.getMarkerIcon(context, type)));
    }

    /**
     * Gets a short description of details of this favourite location
     * containing information about its type.
     * @param context   the context of the application providing all global
     *                  information
     * @return          a short string providing details of this location
     * @see Context
     */
    @Nullable
    @Override
    public String getSecondaryText(@NonNull Context context) {
        String helperText = context.getString(R.string.favourite_helper_text).toLowerCase(Locale.ROOT);
        String typeText = FavouriteType.getTypeText(context, type);
        return String.format("%s - %s", helperText, typeText);
    }

    /**
     * Gets a short description of additional details of this favourite
     * location.
     * @param context   the context of the application providing all global
     *                  information
     * @return          a short string providing additional details of this
     *                  location
     * @see Context
     */
    @Nullable
    @Override
    public String getTertiaryText(@NonNull Context context) {
        return null;
    }

    /**
     * Gets a composite description of details of this favourite location
     * containing information about its type.
     * @param context   the context of the application providing all global
     *                  information
     * @return          a string providing details of this location to put in
     *                  one line
     * @see Context
     */
    @NonNull
    @Override
    public String getInlineSecondaryText(@NonNull Context context) {
        return String.format("%s, %s", context.getString(R.string.favourite_helper_text),
                FavouriteType.getTypeText(context, type));
    }
}
