package com.example.freeturilo.core;

import android.content.Context;
import android.text.SpannableString;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class Station extends Location {
    public int id;
    public int bikeRacks;
    public int bikes;
    public int state;

    @Override
    public MarkerOptions createMarkerOptions(Context context) {
        return null;
    }

    public static List<Station> loadStations() {
        return null;
    }

    @Override
    public CharSequence createCaption(Context context) {
        return null;
    }

    public void reportBroken() {

    }

    public void setBroken() {

    }

    public void setWorking() {

    }
}
