package com.example.freeturilo.core;

import android.content.Context;
import android.text.SpannableString;

import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

public class Location {
    public String name;
    public double latitude;
    public double longitude;

    public MarkerOptions createMarkerOptions(Context context) {
        return null;
    }

    public CharSequence createCaption(Context context) {
        return null;
    }
}
