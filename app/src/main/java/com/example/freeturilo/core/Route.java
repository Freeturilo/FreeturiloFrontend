package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;

import java.util.List;
import java.util.Locale;

public class Route {
    public DirectionsRoute directionsRoute;
    public List<Location> waypoints;
    public double cost;
    public RouteParameters parameters;

    @NonNull
    public String getPrimaryText(@NonNull Context context) {
        String criterionText = CriterionTools.getCriterionText(context, parameters.criterion);
        return String.format("%s%s %s",
                criterionText.substring(0, 1).toUpperCase(Locale.ROOT), criterionText.substring(1),
                context.getString(R.string.route_caption_text).toLowerCase(Locale.ROOT));
    }

    @NonNull
    public String getSecondaryText(@NonNull Context context) {
        String lengthHelper = context.getString(R.string.length_helper_text);
        String timeHelper = context.getString(R.string.time_helper_text);
        String costHelper = context.getString(R.string.cost_helper_text);
        return String.format("%s: %s | %s: %s | %s: %s",
                lengthHelper, getLength(), timeHelper, getTime(), costHelper, getCost());
    }

    @NonNull
    public String getTertiaryText() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < waypoints.size() - 1; i++) {
            builder.append(waypoints.get(i).name).append("\n");
            builder.append(getTime(directionsRoute.legs[i].duration.inSeconds)).append("\n");
        }
        builder.append(waypoints.get(waypoints.size() - 1).name);
        return builder.toString();
    }

    @NonNull
    private String getLength() {
        long lengthInMeters = 0;
        for (DirectionsLeg leg : directionsRoute.legs)
            lengthInMeters += leg.distance.inMeters;
        return getLength(lengthInMeters);
    }

    @NonNull
    private String getLength(long lengthInMeters) {
        if (lengthInMeters < 1000)
            return String.format(Locale.ROOT, "%d m", lengthInMeters);
        else {
            double lengthInKilometers = lengthInMeters / 1000.0;
            return String.format(Locale.ROOT,"%.1f km", lengthInKilometers);
        }
    }

    @NonNull
    private String getTime() {
        long timeInSeconds = 0;
        for (DirectionsLeg leg : directionsRoute.legs)
            timeInSeconds += leg.duration.inSeconds;
        return getTime(timeInSeconds);
    }

    @NonNull
    private String getTime(long timeInSeconds) {
        if (timeInSeconds < 60)
            return "<1 min";
        else {
            long timeInMinutes = timeInSeconds / 60;
            return String.format(Locale.ROOT, "%d min", timeInMinutes);
        }
    }

    @NonNull
    private String getCost() {
        return String.format(Locale.ROOT, "%.2f zł", cost);
    }
}
