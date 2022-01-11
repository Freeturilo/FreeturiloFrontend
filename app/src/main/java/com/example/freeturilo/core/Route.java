package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A calculated bicycle route.
 * <p>
 * Object of this class represents a calculated NextBike bicycle route
 * composed of fragments. It encapsulates the collection of {@link #fragments}
 * and the {@link #parameters} of calculation of the route. There are multiple
 * methods declared within the class that help represent a route with text and
 * properties.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see RouteFragment
 * @see RouteParameters
 */
public class Route {
    /**
     * Stores a collection of this route's fragments.
     */
    public List<RouteFragment> fragments;
    /**
     * Stores the parameters with which this route has been calculated.
     */
    public RouteParameters parameters;

    /**
     * Class constructor.
     * @param fragments     a list of fragments that this route consists of
     * @param parameters    a bundle of parameters of this route's calculation
     */
    public Route(List<RouteFragment> fragments, RouteParameters parameters) {
        this.fragments = fragments;
        this.parameters = parameters;
    }

    /**
     * Gets a list of the waypoints of this route.
     * @return          a list of locations that this route passes in order
     *                  that they are visited in
     */
    public List<Location> getWaypoints() {
        List<Location> waypoints = new ArrayList<>();
        for(RouteFragment fragment : fragments)
            waypoints.add(fragment.getStart());
        waypoints.add(fragments.get(fragments.size() - 1).getEnd());
        return waypoints;
    }

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
     * its properties: distance, time and cost.
     * @param context   the context of the application providing all global
     *                  information
     * @return          a string providing description of this route
     */
    @NonNull
    public String getSecondaryText(@NonNull Context context) {
        String distanceHelper = context.getString(R.string.distance_helper_text);
        String timeHelper = context.getString(R.string.time_helper_text);
        String costHelper = context.getString(R.string.cost_helper_text);
        return String.format("%s: %s | %s: %s | %s: %s",
                distanceHelper, getDistance(), timeHelper, getTime(), costHelper, getCost());
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
        for (RouteFragment fragment : fragments) {
            String fragmentDetails = String.format("%s | %s | %s",
                    fragment.getDistance(), fragment.getTime(), fragment.getCost());
            builder.append(fragment.getStart().name).append("\n")
                    .append("☇ ").append(fragmentDetails).append("\n");
        }
        builder.append(fragments.get(fragments.size() - 1).getEnd().name);
        return builder.toString();
    }

    /**
     * Gets the distance of this route.
     * @return          a long equal to this route's distance in meters
     */
    public long getDistanceInMeters() {
        long distanceInMeters = 0;
        for (RouteFragment fragment : fragments)
            distanceInMeters += fragment.getDistanceInMeters();
        return distanceInMeters;
    }

    /**
     * Gets the text representation of the distance of this route.
     * @return          a string containing this route's distance in meters
     *                  when shorter than a kilometer and in kilometers
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
     * Gets the duration of this route.
     * @return      a long equal to this route's duration in seconds
     */
    public long getTimeInSeconds() {
        long timeInSeconds = 0;
        for (RouteFragment fragment : fragments)
            timeInSeconds += fragment.getTimeInSeconds();
        return timeInSeconds;
    }

    /**
     * Gets the text representation of the duration of this route.
     * @return          a string containing this route's duration in seconds
     *                  when shorter than a minute and in minutes otherwise
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
     * Gets the cost of this route.
     * @return          a double equal to this route's cost in pln
     */
    public double getCostInPLN() {
        double costInPLN = 0;
        for (RouteFragment fragment : fragments)
            costInPLN += fragment.getCostInPLN();
        return costInPLN;
    }

    /**
     * Gets the text representation of the cost of this route.
     * @return          a string containing this route's cost in pln
     */
    @NonNull
    public String getCost() {
        return String.format(Locale.ROOT, "%.2f zł", getCostInPLN());
    }

    /**
     * Gets the geographical bounds of this route.
     * @return          a bounds object representing this route's bounds
     */
    @NonNull
    public LatLngBounds getBounds() {
        LatLngBounds fullBounds = fragments.get(0).getBounds();
        for (RouteFragment fragment : fragments) {
            LatLngBounds bounds = fragment.getBounds();
            fullBounds = fullBounds.including(bounds.southwest);
            fullBounds = fullBounds.including(bounds.northeast);
        }
        return fullBounds;
    }

    /**
     * Gets the path of this route.
     * @return          a dense, ordered list of points on this route
     */
    @NonNull
    public List<LatLng> getPath() {
        List<LatLng> path = new ArrayList<>();
        for (RouteFragment fragment : fragments)
            path.addAll(fragment.getPath());
        return path;
    }
}
