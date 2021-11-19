package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;

import com.example.freeturilo.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Station extends Location {
    public int id;
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

    @Override
    public MarkerOptions createMarkerOptions(Context context) {
        LatLng markerPosition = new LatLng(latitude, longitude);
        int markerIconId = R.drawable.marker_station;
        switch (state) {
            case 0:
                markerIconId = R.drawable.marker_station;
                break;
            case 1:
                markerIconId = R.drawable.marker_station_reported;
                break;
            case 2:
                markerIconId = R.drawable.marker_station_broken;
                break;
        }
        Bitmap markerBitmap = BitmapFactory.decodeResource(context.getResources(), markerIconId);
        Bitmap smallMarker = Bitmap.createScaledBitmap(markerBitmap, 95, 150, false);
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
    public CharSequence createCaption(Context context) {
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder();
        SpannableString ssName = new SpannableString(name);
        int bigSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_big);
        ssName.setSpan(new AbsoluteSizeSpan(bigSize), 0, name.length(), 0);
        ssBuilder.append(ssName);
        String fullDetails = context.getResources().getString(R.string.station_helper_text)
                + bikes + "/" + bikeRacks;
        String textState;
        if (state != 0) {
            if (state == 1)
                textState = context.getResources().getString(R.string.station_reported_text);
            else
                textState = context.getResources().getString(R.string.station_broken_text);
            fullDetails += "\n(" + textState + ")";
        }
        SpannableString ssDetails = new SpannableString(fullDetails);
        int smallSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_small);
        ssDetails.setSpan(new AbsoluteSizeSpan(smallSize), 0, fullDetails.length(), 0);
        ssBuilder.append("\n").append(ssDetails);
        return ssBuilder;
    }

    public void reportBroken() {

    }

    public void setBroken() {

    }

    public void setWorking() {

    }
}
