package com.example.freeturilo.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;
import com.google.maps.model.Bounds;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@RunWith(AndroidJUnit4.class)
public class RouteTest {

    private Route route;

    @Test
    public void getPrimaryText() {
        Context context = RuntimeEnvironment.getApplication();

        String result = route.getPrimaryText(context);

        assertNotNull(result);
        assertTrue(result.toLowerCase(Locale.ROOT).contains(context.getString(R.string.route_caption_text).toLowerCase(Locale.ROOT)));
        assertTrue(result.toLowerCase(Locale.ROOT).contains(Criterion.getCriterionText(context, route.parameters.criterion).toLowerCase(Locale.ROOT)));
    }

    @Test
    public void getSecondaryText() {
        Context context = RuntimeEnvironment.getApplication();
        long lengthInMeters = 0;
        for (DirectionsLeg leg : route.directionsRoute.legs)
            lengthInMeters += leg.distance.inMeters;
        double lengthInKilometers = lengthInMeters / 1000.0;

        long timeInSeconds = 0;
        for (DirectionsLeg leg : route.directionsRoute.legs)
            timeInSeconds += leg.duration.inSeconds;
        long timeInMinutes = timeInSeconds / 60;

        String result = route.getSecondaryText(context);

        assertNotNull(result);
        assertTrue(result.contains(context.getString(R.string.length_helper_text)));
        assertTrue(result.contains(String.format(Locale.ROOT,"%.1f km", lengthInKilometers)));
        assertTrue(result.contains(context.getString(R.string.time_helper_text)));
        assertTrue(result.contains(String.format(Locale.ROOT, "%d min", timeInMinutes)));
        assertTrue(result.contains(context.getString(R.string.cost_helper_text)));
        assertTrue(result.contains("0.00 zł"));
        assertFalse(result.contains("\n"));
    }

    @Test
    public void getTertiaryText() {
        String result = route.getTertiaryText();

        assertTrue(result.contains(route.parameters.start.name));
        for (Location stop : route.parameters.stops)
            assertTrue(result.contains(stop.name));
        assertTrue(result.contains(route.parameters.end.name));
    }

    @Before
    public void setUp() {
        Favourite start = new Favourite("start", 49, 51, FavouriteType.SCHOOL);
        Station end = new Station("end", 50, 52, 45, 25, 10, 0);
        List<Location> stops = Arrays.asList(
                new Location("stop1", 49, 52),
                new Location("stop2", 50, 51)
        );
        Criterion criterion = Criterion.TIME;
        RouteParameters routeParameters = new RouteParameters(start, end, stops, criterion);
        route = new Route();
        route.cost = 0;
        route.parameters = routeParameters;
        route.waypoints = new ArrayList<>();
        route.waypoints.add(routeParameters.start);
        route.waypoints.addAll(routeParameters.stops);
        route.waypoints.add(routeParameters.end);
        route.directionsRoute = new DirectionsRoute();
        route.directionsRoute.bounds = new Bounds();
        double westLatitude = Math.min(routeParameters.start.latitude, routeParameters.end.latitude);
        double eastLatitude = Math.max(routeParameters.start.latitude, routeParameters.end.latitude);
        double southLongitude = Math.min(routeParameters.start.longitude, routeParameters.end.longitude);
        double northLongitude = Math.max(routeParameters.start.longitude, routeParameters.end.longitude);
        route.directionsRoute.bounds.southwest = new LatLng(westLatitude, southLongitude);
        route.directionsRoute.bounds.northeast = new LatLng(eastLatitude, northLongitude);
        route.directionsRoute.legs = new DirectionsLeg[route.waypoints.size() - 1];
        for (int i = 0; i < route.waypoints.size() - 1; i++) {
            route.directionsRoute.legs[i] = new DirectionsLeg();
            route.directionsRoute.legs[i].distance = new Distance();
            route.directionsRoute.legs[i].distance.inMeters =
                    Math.round(Math.sqrt(Math.pow((route.waypoints.get(i + 1).latitude - route.waypoints.get(i).latitude) * 111000, 2)
                            + Math.pow((route.waypoints.get(i + 1).longitude - route.waypoints.get(i).longitude) * 111000, 2)));
            route.directionsRoute.legs[i].duration = new Duration();
            route.directionsRoute.legs[i].duration.inSeconds =
                    Math.round(route.directionsRoute.legs[i].distance.inMeters / 5.55);
            route.directionsRoute.legs[i].steps = new DirectionsStep[1];
            route.directionsRoute.legs[i].steps[0] = new DirectionsStep();
            route.directionsRoute.legs[i].steps[0].distance = route.directionsRoute.legs[i].distance;
            route.directionsRoute.legs[i].steps[0].duration = route.directionsRoute.legs[i].duration;
            List<LatLng> startAndEnd = new ArrayList<>();
            startAndEnd.add(new LatLng(route.waypoints.get(i).latitude, route.waypoints.get(i).longitude));
            startAndEnd.add(new LatLng(route.waypoints.get(i + 1).latitude, route.waypoints.get(i + 1).longitude));
            route.directionsRoute.legs[i].steps[0].polyline = new EncodedPolyline(startAndEnd);
        }
    }
}