package com.example.freeturilo.core;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class Station extends Location {
    public int id;
    public int bikeRacks;
    public int bikes;
    public int state;

    @Override
    public MarkerOptions createMarkerOptions() {
        return null;
    }

    public static List<Station> loadStations() {
        return null;
    }

    public void reportBroken() {

    }

    public void setBroken() {

    }

    public void setWorking() {

    }
}
