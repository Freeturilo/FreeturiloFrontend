package com.example.freeturilo.core;

import android.content.Context;
import android.location.Address;
import android.text.SpannableStringBuilder;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.io.Serializable;

public class Location implements Serializable {
    public String name;
    public Double latitude;
    public Double longitude;
    public SpannableStringBuilder autoCompletePredictionText;

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

    public CharSequence createCaption(Context context) {
        return null;
    }

    public void setAutoCompletePredictionText(Context context) {}

    public void setAutoCompletePredictionTextWithPrediction(Context context,
                                                            AutocompletePrediction prediction) {}

    public static Location fromAddress(Address address) throws IllegalStateException {
        int maxAddressLineIndex = address.getMaxAddressLineIndex();
        if (maxAddressLineIndex == -1)
            throw new IllegalStateException();
        Location location = new Location(address.getAddressLine(0),
                address.getLatitude(), address.getLongitude());
        location.autoCompletePredictionText
                = new SpannableStringBuilder(address.getAddressLine(0));
        return location;
    }

    @NonNull
    @Override
    public String toString() {
        return autoCompletePredictionText.toString();
    }
}
