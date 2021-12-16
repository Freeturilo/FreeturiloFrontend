package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class Station extends Location {
    public final int id;
    public int bikeRacks;
    public int bikes;
    public int state;

    public Station(String name, double latitude, double longitude,
                   int id, int bikeRacks, int bikes, int state){
        super(name, latitude, longitude);
        this.id = id;
        this.bikeRacks = bikeRacks;
        this.bikes = bikes;
        this.state = state;
    }

    @NonNull
    @Override
    public MarkerOptions createMarkerOptions(@NonNull Context context) {
        return new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(StationStateBikes.getMarkerIcon(context, state, bikes)));
    }

    @Nullable
    @Override
    public String getSecondaryText(@NonNull Context context) {
        String stationHelperText = context.getString(R.string.station_helper_text).toLowerCase(Locale.ROOT);
        String stateText = StationStateBikes.getStateBikesText(context, state, bikes);
        if (state == 0 && bikes > 3)
            return stationHelperText;
        else
            return String.format("%s (%s)", stationHelperText, stateText);
    }

    @Nullable
    @Override
    public String getTertiaryText(@NonNull Context context) {
        String availabilityHelperText = context.getString(R.string.bikes_availability_text);
        String availabilityText = String.format(Locale.ROOT, "%d/%d", bikes, bikes + bikeRacks);
        return String.format("%s: %s", availabilityHelperText, availabilityText);
    }

    @NonNull
    @Override
    public String getInlineSecondaryText(@NonNull Context context) {
        return String.format("%s, %s", context.getString(R.string.station_helper_text),
                StationStateBikes.getStateBikesText(context, state, bikes));
    }
}
