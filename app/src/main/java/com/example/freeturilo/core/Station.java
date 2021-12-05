package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class Station extends Location {
    public final int id;
    public final int bikeRacks;
    public final int bikes;
    public final int state;

    public Station(String name, double latitude, double longitude,
                   int id, int bikeRacks, int bikes, int state){
        super(name, latitude, longitude);
        this.id = id;
        this.bikeRacks = bikeRacks;
        this.bikes = bikes;
        this.state = state;
    }

    @Override
    public MarkerOptions createMarkerOptions(Context context) {
        LatLng markerPosition = new LatLng(latitude, longitude);
        Bitmap markerBitmap = StationStateTools.getMarkerIcon(context, state);
        int markerWidth = context.getResources().getDimensionPixelSize(R.dimen.marker_width);
        int markerHeight = context.getResources().getDimensionPixelSize(R.dimen.marker_height);
        Bitmap smallMarker =
                Bitmap.createScaledBitmap(markerBitmap, markerWidth, markerHeight, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        return new MarkerOptions()
                .position(markerPosition)
                .icon(smallMarkerIcon);
    }

    @Nullable
    @Override
    public String getSecondaryText(Context context) {
        return context.getString(R.string.station_helper_text);
    }

    @Nullable
    @Override
    public String getTertiaryText(Context context) {
        String availabilityHelperText = context.getString(R.string.bikes_availability_text);
        String availabilityText = String.format(Locale.ROOT, "%d/%d", bikes, bikeRacks);
        String stateText = String.format("(%s)", StationStateTools.getStateText(context, state));
        if (state == 0)
            return String.format("%s: %s", availabilityHelperText, availabilityText);
        else
            return String.format("%s: %s\n%s", availabilityHelperText, availabilityText, stateText);
    }

    @NonNull
    @Override
    public String getInlineSecondaryText(Context context) {
        return String.format("%s, %s", context.getString(R.string.station_helper_text),
                StationStateTools.getStateText(context, state));
    }
}
