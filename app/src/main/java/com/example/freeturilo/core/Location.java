package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    @NonNull
    public String getPrimaryText() {
        return name;
    }

    @Nullable
    public String getSecondaryText(Context context) {
        return null;
    }

    @Nullable
    public String getTertiaryText(Context context) {
        return null;
    }

    @NonNull
    public String getInlineSecondaryText(Context context) {
        return "";
    }

    @NonNull
    @Override
    public String toString() {
        return getPrimaryText();
    }
}
