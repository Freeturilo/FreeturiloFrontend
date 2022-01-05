package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A named geographical location.
 * <p>
 * Object of this class represents a distinguished real-world location and
 * encapsulates information about its {@link #name}, {@link #latitude} and
 * {@link #longitude}. There are multiple methods declared within the class
 * that help represent a location with text or a map marker.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #name
 * @see #latitude
 * @see #longitude
 * @see #getPrimaryText
 * @see #getSecondaryText
 * @see #getInlineSecondaryText
 * @see #getTertiaryText
 * @see #createMarkerOptions
 */
public class Location {
    /**
     * Stores the name given to this location.
     */
    @NonNull public String name;
    /**
     * Stores the latitude (north–south position) of this location.
     */
    @NonNull public Double latitude;
    /**
     * Stores the longitude (east-west position) of this location.
     */
    @NonNull public Double longitude;

    /**
     * Class constructor.
     * @param name      a string describing this location
     * @param latitude  a double equal to the north–south position of this
     *                  location
     * @param longitude a double equal to the east-west position of this
     *                  location
     */
    public Location(@NonNull String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Creates options for a {@code Marker} representing this location.
     * Defines geographical positioning of the Marker.
     * @param context   the context of the application providing all global
     *                  information
     * @return          options for creating a Marker representing this
     *                  location
     * @see #latitude
     * @see #longitude
     * @see Marker
     * @see Context
     * @see MarkerOptions
     */
    @NonNull
    public MarkerOptions createMarkerOptions(@NonNull Context context) {
        LatLng markerPosition = new LatLng(latitude, longitude);
        return new MarkerOptions().position(markerPosition);
    }

    /**
     * Gets a short basic description of this location.
     * @return          a short string defining this location in general
     * @see #name
     */
    @NonNull
    public String getPrimaryText() {
        return name;
    }

    /**
     * Gets a short description of details of this location.
     * @param context   the context of the application providing all global
     *                  information
     * @return          a short string providing details of this location
     */
    @Nullable
    public String getSecondaryText(@NonNull Context context) {
        return null;
    }

    /**
     * Gets a short description of additional details of this location.
     * @param context   the context of the application providing all global
     *                  information
     * @return          a short string providing additional details of this
     *                  location
     */
    @Nullable
    public String getTertiaryText(@NonNull Context context) {
        return null;
    }

    /**
     * Gets a composite description of details of this location.
     * @param context   the context of the application providing all global
     *                  information
     * @return          a string providing details of this location to put in
     *                  one line
     * @see Context
     */
    @NonNull
    public String getInlineSecondaryText(@NonNull Context context) {
        return "";
    }

    /**
     * Generates text representation of this location. Identical to
     * {@code getPrimaryText}.
     * @return          a string representing this location
     * @see #getPrimaryText
     */
    @NonNull
    @Override
    public String toString() {
        return getPrimaryText();
    }
}
