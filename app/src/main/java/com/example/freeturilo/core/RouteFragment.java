package com.example.freeturilo.core;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.model.Bounds;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A bicycle route fragment.
 * <p>
 * Object of this class represents a calculated NextBike bicycle route's
 * fragment with its details. It encapsulates the {@link #cost},
 * {@link #waypoints}, {@link #parameters} of calculation and the corresponding
 * Google Maps route ({@link #directionsRoute}) of the route fragment.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see RouteParameters
 * @see DirectionsRoute
 */
public class RouteFragment {
    /**
     * Stores this route fragment's corresponding Google Maps route.
     */
    public DirectionsRoute directionsRoute;
    /**
     * Stores a collection of two locations, the start and the end of this
     * route fragment, in this order.
     */
    public List<Location> waypoints;
    /**
     * Stores the cost of covering this route fragment with a NextBike bicycle.
     */
    public double cost;
    /**
     * Stores the parameters used to calculate the route of which this is a
     * fragment.
     */
    public RouteParameters parameters;

    /**
     * Gets the start of this route fragment.
     * @return      a location that starts this route fragment
     */
    public Location getStart() {
        return waypoints.get(0);
    }

    /**
     * Gets the end of this route fragment.
     * @return      a location that ends this route fragment
     */
    public Location getEnd() {
        return waypoints.get(waypoints.size() - 1);
    }

    /**
     * Gets the distance of this route fragment.
     * @return      a long equal to this route fragment's distance in meters
     */
    public long getDistanceInMeters() {
        long distanceInMeters = 0;
        for (DirectionsLeg leg : directionsRoute.legs)
            distanceInMeters += leg.distance.inMeters;
        return distanceInMeters;
    }

    /**
     * Gets the text representation of the distance of this route fragment.
     * @return          a string containing this route fragment's distance in
     *                  meters when shorter than a kilometer and in kilometers
     *                  otherwise
     */
    @NonNull
    public String getDistance() {
        long distanceInMeters = getDistanceInMeters();
        if (distanceInMeters < 1000)
            return String.format(Locale.ROOT, "%d m", distanceInMeters);
        else {
            double distanceInKilometers = distanceInMeters / 1000.0;
            return String.format(Locale.ROOT,"%.1f km", distanceInKilometers);
        }
    }

    /**
     * Gets the duration of this route fragment.
     * @return      a long equal to this route fragment's duration in seconds
     */
    public long getTimeInSeconds() {
        long timeInSeconds = 0;
        for (DirectionsLeg leg : directionsRoute.legs)
            timeInSeconds += leg.duration.inSeconds;
        return timeInSeconds;
    }

    /**
     * Gets the text representation of the duration of this route fragment.
     * @return          a string containing this route fragment's duration in
     *                  seconds when shorter than a minute and in minutes
     *                  otherwise
     */
    @NonNull
    public String getTime() {
        long timeInSeconds = getTimeInSeconds();
        if (timeInSeconds < 60)
            return "<1 min";
        else {
            long timeInMinutes = timeInSeconds / 60;
            return String.format(Locale.ROOT, "%d min", timeInMinutes);
        }
    }

    /**
     * Gets the cost of this route fragment.
     * @return      a double equal to this route fragment's duration in pln
     */
    public double getCostInPLN() {
        return cost;
    }

    /**
     * Gets the text representation of the cost of this route fragment.
     * @return          a string containing this route fragment's cost in pln
     */
    @NonNull
    public String getCost() {
        return String.format(Locale.ROOT, "%.2f zł", getCostInPLN());
    }

    /**
     * Gets the geographical bounds of this route fragment.
     * @return          a bounds object representing this route fragment's
     *                  bounds
     */
    public LatLngBounds getBounds() {
        Bounds bounds = directionsRoute.bounds;
        LatLng southwest = new LatLng(bounds.southwest.lat, bounds.southwest.lng);
        LatLng northeast = new LatLng(bounds.northeast.lat, bounds.northeast.lng);
        return new LatLngBounds(southwest, northeast);
    }

    /**
     * Gets the path of this route fragment.
     * @return          a dense, ordered list of points on this fragment
     */
    public List<LatLng> getPath() {
        List<LatLng> path = new ArrayList<>();
        for (DirectionsLeg leg : directionsRoute.legs) {
            for (DirectionsStep step : leg.steps) {
                List<com.google.maps.model.LatLng> decodedPath = step.polyline.decodePath();
                for (com.google.maps.model.LatLng point : decodedPath)
                    path.add(new LatLng(point.lat, point.lng));
            }
        }
        return path;
    }
}
