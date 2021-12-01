package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.freeturilo.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
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

    public static List<Station> loadStations() {
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(new Station("Dewajtis - UKSW", 52.296298, 20.958358,
                1, 30, 28, 0));
        stations.add(new Station("Metro Młociny", 52.290974, 20.929556,
                2, 30, 18, 1));
        stations.add(new Station("Rondo ONZ", 52.232628, 20.997123,
                3, 29, 24, 2));
        return stations;
    }

    @Override
    public String getCaption(Context context) {
        String helperText = context.getString(R.string.station_helper_text);
        String availabilityHelperText = context.getString(R.string.bikes_availability_text);
        String availabilityText = String.format(Locale.ROOT, "%d/%d", bikes, bikeRacks);
        String stateText = String.format("(%s)", StationStateTools.getStateText(context, state));
        if (state == 0)
            return String.format("%s\n%s: %s", helperText, availabilityHelperText,
                    availabilityText);
        else
            return String.format("%s\n%s: %s\n%s", helperText, availabilityHelperText,
                    availabilityText, stateText);
    }

    @Override
    public String getSecondaryText(Context context) {
        return context.getString(R.string.station_helper_text)
                + ", " + StationStateTools.getStateText(context, state);
    }
}
