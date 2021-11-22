package com.example.freeturilo.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RouteParameters implements Serializable {
    public Location start;
    public Location end;
    public List<Location> stops;
    public Criterion criterion;

    public RouteParameters(Location start, Location end, List<Location> stops, Criterion criterion){
        this.start = start;
        this.end = end;
        this.stops = new ArrayList<>(stops);
        this.criterion = criterion;
    }
}
