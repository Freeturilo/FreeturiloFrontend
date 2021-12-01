package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

public class Location implements Serializable {
    public String name;
    public Double latitude;
    public Double longitude;

    public Location(String name) {
        this.name = name;
    }

    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MarkerOptions createMarkerOptions(Context context) {
        LatLng markerPosition = new LatLng(latitude, longitude);
        return new MarkerOptions().position(markerPosition);
    }

    public String getCaption(Context context) {
        return "";
    }

    public String getPrimaryText() {
        return name;
    }

    public String getSecondaryText(Context context) {
        return "";
    }

    @NonNull
    @Override
    public String toString() {
        return getPrimaryText();
    }
}
