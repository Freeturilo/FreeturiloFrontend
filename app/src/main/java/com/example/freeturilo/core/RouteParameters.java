package com.example.freeturilo.core;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A bundle of route calculation parameters.
 * Object of this class represents a bundle of parameters used in calculation
 * of a NextBike bicycle route. It encapsulates the {@link #start},
 * {@link #end} and {@link #stops} of the route and the {@link #criterion} of
 * the route's calculation.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #start
 * @see #end
 * @see #stops
 * @see #criterion
 * @see Route
 */
public class RouteParameters {
    /**
     * Stores the route's starting location.
     */
    public final Location start;
    /**
     * Stores the route's end location.
     */
    public final Location end;
    /**
     * Stores the route's stops in visiting order.
     */
    public final List<Location> stops;
    /**
     * Stores the criterion of the route's calculation.
     */
    public final Criterion criterion;

    /**
     * Class constructor.
     * @param start         a location in which the route should start
     * @param end           a location in which the route should end
     * @param stops         locations through which the route should go
     * @param criterion     a criterion of the route's calculation
     */
    public RouteParameters(@NonNull Location start, @NonNull Location end,
                           @NonNull List<Location> stops, @NonNull Criterion criterion){
        this.start = start;
        this.end = end;
        this.stops = new ArrayList<>(stops);
        this.criterion = criterion;
    }
}
