package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;

import java.util.List;
import java.util.Locale;

/**
 * A calculated bike route.
 * Object of this class represents a calculated NextBike bicycle route with
 * cost and waypoints. It encapsulates the {@link #cost}, {@link #parameters},
 * {@link #waypoints} and the corresponding Google Maps route
 * ({@link #directionsRoute}) of the route. There are multiple methods declared
 *  within the class that help represent a location with text and properties.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #cost
 * @see #parameters
 * @see #waypoints
 * @see #directionsRoute
 * @see #getPrimaryText
 * @see #getSecondaryText
 * @see #getTertiaryText
 * @see #getTime()
 * @see #getLength()
 * @see #getCost()
 * @see RouteParameters
 * @see DirectionsRoute
 */
public class Route {
    /**
     * Stores this route's corresponding Google Maps route.
     */
    public DirectionsRoute directionsRoute;
    /**
     * Stores a collection of this route's waypoints including
     * the start and the end.
     */
    public List<Location> waypoints;
    /**
     * Stores the cost of covering this route with NextBike.
     */
    public double cost;
    /**
     * Stores the parameters used to calculate this route.
     */
    public RouteParameters parameters;

    /**
     * Gets a short basic description of this route containing information
     * about its criterion.
     * @param context   the context of the application providing all global
     *                  information
     * @return          a string providing basic description of this route
     */
    @NonNull
    public String getPrimaryText(@NonNull Context context) {
        String criterionText = Criterion.getCriterionText(context, parameters.criterion);
        return String.format("%s%s %s",
                criterionText.substring(0, 1).toUpperCase(Locale.ROOT), criterionText.substring(1),
                context.getString(R.string.route_caption_text).toLowerCase(Locale.ROOT));
    }

    /**
     * Gets a short description of this route containing information about
     * its properties: length, time and cost.
     * @param context   the context of the application providing all global
     *                  information
     * @return          a string providing description of this route
     */
    @NonNull
    public String getSecondaryText(@NonNull Context context) {
        String lengthHelper = context.getString(R.string.length_helper_text);
        String timeHelper = context.getString(R.string.time_helper_text);
        String costHelper = context.getString(R.string.cost_helper_text);
        return String.format("%s: %s | %s: %s | %s: %s",
                lengthHelper, getLength(), timeHelper, getTime(), costHelper, getCost());
    }

    /**
     * Gets a long description of additional details of this route: its
     * waypoints and time spans between them.
     * @return          a short string providing additional details of this
     *                  route
     */
    @NonNull
    public String getTertiaryText() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < waypoints.size() - 1; i++)
            builder.append(waypoints.get(i).name).append("\n");
        builder.append(waypoints.get(waypoints.size() - 1).name);
        return builder.toString();
    }

    /**
     * Gets the text representation of the length of this route in the metric
     * system.
     * @return          a string containing this route's length in meters when
     *                  shorter than a kilometer and in kilometers otherwise
     */
    @NonNull
    private String getLength() {
        long lengthInMeters = 0;
        for (DirectionsLeg leg : directionsRoute.legs)
            lengthInMeters += leg.distance.inMeters;
        return getLength(lengthInMeters);
    }

    /**
     * Gets the text representation of a length
     * @param lengthInMeters    a long of a value equal to a length in meters
     * @return                  a string containing the length in meters when
     *                          shorter than a kilometer and in kilometers
     *                          otherwise
     */
    @NonNull
    private String getLength(long lengthInMeters) {
        if (lengthInMeters < 1000)
            return String.format(Locale.ROOT, "%d m", lengthInMeters);
        else {
            double lengthInKilometers = lengthInMeters / 1000.0;
            return String.format(Locale.ROOT,"%.1f km", lengthInKilometers);
        }
    }

    /**
     * Gets the text representation of the duration of this route
     * @return          a string containing this route's duration in seconds
     *                  when shorter than a minute and in minutes otherwise
     */
    @NonNull
    private String getTime() {
        long timeInSeconds = 0;
        for (DirectionsLeg leg : directionsRoute.legs)
            timeInSeconds += leg.duration.inSeconds;
        return getTime(timeInSeconds);
    }

    /**
     * Gets the text representation of a duration
     * @param timeInSeconds     a long of a value equal to a duration in
     *                          seconds
     * @return                  a string containing the duration in seconds
     *                          when shorter than a minute and in minutes
     *                          otherwise
     */
    @NonNull
    private String getTime(long timeInSeconds) {
        if (timeInSeconds < 60)
            return "<1 min";
        else {
            long timeInMinutes = timeInSeconds / 60;
            return String.format(Locale.ROOT, "%d min", timeInMinutes);
        }
    }

    /**
     * Gets the text representation of the cost of this route in zł
     * @return          a string containing this route's cost in zł
     */
    @NonNull
    private String getCost() {
        return String.format(Locale.ROOT, "%.2f zł", cost);
    }
}
