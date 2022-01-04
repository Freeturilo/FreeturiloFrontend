package com.example.freeturilo.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;
import com.google.android.gms.maps.model.LatLngBounds;
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
    public void getWaypoints() {
        List<Location> waypoints = route.getWaypoints();

        for(int i = 0; i < route.fragments.size(); i++)
            assertEquals(route.fragments.get(i).getStart(), waypoints.get(i));
        assertEquals(route.fragments.get(route.fragments.size() - 1).getEnd(),
                waypoints.get(waypoints.size() - 1));
    }

    @Test
    public void getPrimaryText() {
        Context context = RuntimeEnvironment.getApplication();

        String result = route.getPrimaryText(context);

        assertNotNull(result);
        assertTrue(result.toLowerCase(Locale.ROOT).contains(context.getString(R.string.route_caption_text).toLowerCase(Locale.ROOT)));
        assertTrue(result.toLowerCase(Locale.ROOT).contains(Criterion
                .getCriterionText(context, route.parameters.criterion).toLowerCase(Locale.ROOT)));
    }

    @Test
    public void getSecondaryText() {
        Context context = RuntimeEnvironment.getApplication();

        String result = route.getSecondaryText(context);

        assertNotNull(result);
        assertTrue(result.contains(context.getString(R.string.distance_helper_text)));
        assertTrue(result.contains(route.getDistance()));
        assertTrue(result.contains(context.getString(R.string.time_helper_text)));
        assertTrue(result.contains(route.getTime()));
        assertTrue(result.contains(context.getString(R.string.cost_helper_text)));
        assertTrue(result.contains(route.getCost()));
    }

    @Test
    public void getTertiaryText() {
        String result = route.getTertiaryText();

        for(Location waypoint : route.getWaypoints())
            assertTrue(result.contains(waypoint.name));
        for(RouteFragment fragment : route.fragments) {
            assertTrue(result.contains(fragment.getCost()));
            assertTrue(result.contains(fragment.getTime()));
            assertTrue(result.contains(fragment.getDistance()));
        }
    }

    @Test
    public void getBounds() {
        LatLngBounds bounds = route.getBounds();

        for (RouteFragment fragment : route.fragments) {
            LatLngBounds fragmentBounds = fragment.getBounds();
            assertTrue(bounds.contains(fragmentBounds.northeast));
            assertTrue(bounds.contains(fragmentBounds.southwest));
        }
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
        List<RouteFragment> routeFragments = new ArrayList<>();
        List<Location> waypoints = new ArrayList<>();
        waypoints.add(routeParameters.start);
        waypoints.addAll(routeParameters.stops);
        waypoints.add(routeParameters.end);
        for(int i = 0; i < waypoints.size() - 1; i++) {
            RouteFragment fragment = new RouteFragment();
            fragment.cost = 0;
            fragment.parameters = routeParameters;
            fragment.waypoints = new ArrayList<>();
            fragment.waypoints.add(waypoints.get(i));
            fragment.waypoints.add(waypoints.get(i + 1));
            fragment.directionsRoute = new DirectionsRoute();
            fragment.directionsRoute.bounds = new Bounds();
            Location fragmentStart = fragment.waypoints.get(0);
            Location fragmentEnd = fragment.waypoints.get(1);
            double westLatitude = Math.min(fragmentStart.latitude, fragmentEnd.latitude);
            double eastLatitude = Math.max(fragmentStart.latitude, fragmentEnd.latitude);
            double southLongitude = Math.min(fragmentStart.longitude, fragmentEnd.longitude);
            double northLongitude = Math.max(fragmentStart.longitude, fragmentEnd.longitude);
            fragment.directionsRoute.bounds.southwest = new LatLng(westLatitude, southLongitude);
            fragment.directionsRoute.bounds.northeast = new LatLng(eastLatitude, northLongitude);
            fragment.directionsRoute.legs = new DirectionsLeg[1];
            fragment.directionsRoute.legs[0] = new DirectionsLeg();
            fragment.directionsRoute.legs[0].distance = new Distance();
            fragment.directionsRoute.legs[0].distance.inMeters =
                    Math.round(Math.sqrt(Math.pow((fragmentEnd.latitude - fragmentStart.latitude) * 111000, 2)
                            + Math.pow((fragmentEnd.longitude - fragmentStart.longitude) * 111000, 2)));
            fragment.directionsRoute.legs[0].duration = new Duration();
            fragment.directionsRoute.legs[0].duration.inSeconds =
                    Math.round(fragment.directionsRoute.legs[0].distance.inMeters / 5.55);
            fragment.directionsRoute.legs[0].steps = new DirectionsStep[1];
            fragment.directionsRoute.legs[0].steps[0] = new DirectionsStep();
            fragment.directionsRoute.legs[0].steps[0].distance = fragment.directionsRoute.legs[0].distance;
            fragment.directionsRoute.legs[0].steps[0].duration = fragment.directionsRoute.legs[0].duration;
            List<LatLng> startAndEnd = new ArrayList<>();
            startAndEnd.add(new LatLng(fragmentStart.latitude, fragmentStart.longitude));
            startAndEnd.add(new LatLng(fragmentEnd.latitude, fragmentEnd.longitude));
            fragment.directionsRoute.legs[0].steps[0].polyline = new EncodedPolyline(startAndEnd);
            routeFragments.add(fragment);
        }
        route = new Route(routeFragments, routeParameters);
    }
}