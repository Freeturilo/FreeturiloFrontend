package com.example.freeturilo.core;

import com.google.maps.model.DirectionsRoute;

import java.util.List;

public class Route {
    public DirectionsRoute directionsRoute;
    public List<Location> waypoints;
    public double cost;
    public RouteParameters parameters;

    public static Route loadRoute(RouteParameters parameters) {
        return null;
    }

    public String getLength() {
        return null;
    }

    public String getTime() {
        return null;
    }

    public String getCost() {
        return null;
    }
}
