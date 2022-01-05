package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

/**
 * A bike station.
 * <p>
 * Object of this class represents a NextBike station. It encapsulates
 * the {@link #name}, {@link #latitude}, {@link #longitude}, NextBike
 * identifier ({@link #id}), number of free bike racks ({@link #bikeRacks}),
 * number of bikes ({@link #bikes}) and {@link #state} of the station.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #id
 * @see #bikeRacks
 * @see #bikes
 * @see #state
 * @see Location
 * @see FavouriteType
 */
public class Station extends Location {
    /**
     * Stores this station's NextBike identifier.
     */
    public final int id;
    /**
     * Stores the number of free bike racks at this station.
     */
    public int bikeRacks;
    /**
     * Stores the number of available bikes at this station.
     */
    public int bikes;
    /**
     * Stores the number defining the current state of this station.
     * When equals 0, the station is working correctly. When equals 1,
     * the station has been reported. When equals 2, the station is broken.
     */
    public int state;

    /**
     * Class constructor.
     * @param name      a string describing this station
     * @param latitude  a double equal to the north–south position of this
     *                  station
     * @param longitude a double equal to the east-west position of this
     *                  station
     * @param id        an integer equal to the NextBike identifier of this
     *                  station
     * @param bikeRacks an integer equal to the number of free bike racks
     *                  available at this station
     * @param bikes     an integer equal to the number of bikes available
     *                  at this station
     * @param state     an integer defining this station's current state
     * @see #state
     */
    public Station(String name, double latitude, double longitude,
                   int id, int bikeRacks, int bikes, int state){
        super(name, latitude, longitude);
        this.id = id;
        this.bikeRacks = bikeRacks;
        this.bikes = bikes;
        this.state = state;
    }

    /**
     * Creates options for a {@link Marker} representing this location.
     * Defines geographical positioning and a custom icon of the Marker.
     * @param context   the context of the application providing all global
     *                  information
     * @return          options for creating a Marker representing this
     *                  location
     * @see #bikes
     * @see #state
     * @see StationCondition
     * @see Marker
     * @see Context
     * @see MarkerOptions
     */
    @NonNull
    @Override
    public MarkerOptions createMarkerOptions(@NonNull Context context) {
        return new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(StationCondition.getMarkerIcon(context, this)));
    }

    /**
     * Gets a short description of details of this station
     * containing information about its condition.
     * @return          a short string providing details of this station
     *
     * @see Context
     */
    @Nullable
    @Override
    public String getSecondaryText(@NonNull Context context) {
        String stationHelperText = context.getString(R.string.station_helper_text).toLowerCase(Locale.ROOT);
        String stateText = StationCondition.getConditionText(context, this);
        if (state == 0 && bikes > 3)
            return stationHelperText;
        else
            return String.format("%s (%s)", stationHelperText, stateText);
    }

    /**
     * Gets a short description of additional details of this station
     * containing information about its numbers of bikes and bike racks.
     * @return          a short string providing additional details of this
     *                  station
     * @see Context
     */
    @Nullable
    @Override
    public String getTertiaryText(@NonNull Context context) {
        String availabilityHelperText = context.getString(R.string.bikes_availability_text);
        String availabilityText = String.format(Locale.ROOT, "%d/%d", bikes, bikes + bikeRacks);
        return String.format("%s: %s", availabilityHelperText, availabilityText);
    }

    /**
     * Gets a composite description of details of this location containing
     * information about its condition.
     * @param context   the context of the application providing all global
     *                  information
     * @return          a string providing details of this station to put in
     *                  one line
     * @see Context
     */
    @NonNull
    @Override
    public String getInlineSecondaryText(@NonNull Context context) {
        return String.format("%s, %s", context.getString(R.string.station_helper_text),
                StationCondition.getConditionText(context, this));
    }
}
