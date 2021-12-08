package com.example.freeturilo.core;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RouteParameters {
    public final Location start;
    public final Location end;
    public final List<Location> stops;
    public final Criterion criterion;

    public RouteParameters(@NonNull Location start, @NonNull Location end,
                           @NonNull List<Location> stops, @NonNull Criterion criterion){
        this.start = start;
        this.end = end;
        this.stops = new ArrayList<>(stops);
        this.criterion = criterion;
    }
}
