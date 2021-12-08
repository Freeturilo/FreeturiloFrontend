package com.example.freeturilo.connection;

import com.example.freeturilo.core.Route;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.core.SystemState;
import com.example.freeturilo.misc.Callback;
import com.google.maps.model.Bounds;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class APIMock implements API {

    private static int MOCK_THRESHOLD = 10;
    private static ArrayList<Station> MOCK_STATIONS = null;
    private static SystemState MOCK_SYSTEM_STATE = SystemState.STARTED;

    private void randomWait() {
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(1800) + 200);
        } catch (InterruptedException ignored) {}
    }

    private List<Station> getStations() {
        randomWait();
        if(MOCK_STATIONS == null) {
            MOCK_STATIONS = new ArrayList<>();
            MOCK_STATIONS.add(new Station("Dewajtis - UKSW", 52.296298, 20.958358,
                    1, 30, 28, 0));
            MOCK_STATIONS.add(new Station("Metro Młociny", 52.290974, 20.929556,
                    2, 30, 18, 1));
            MOCK_STATIONS.add(new Station("Rondo ONZ", 52.232628, 20.997123,
                    3, 29, 24, 2));
        }
        return MOCK_STATIONS;
    }

    private Integer reportStation(Station station) {
        randomWait();
        station.state = 1;
        return HttpsURLConnection.HTTP_OK;
    }

    private Integer setBrokenStation(Station station) {
        randomWait();
        station.state = 2;
        return HttpsURLConnection.HTTP_OK;
    }

    private Integer setWorkingStation(Station station) {
        randomWait();
        station.state = 0;
        return HttpsURLConnection.HTTP_OK;
    }

    private String postUser(String email, String password) {
        randomWait();
        return email + password + "_TOKEN";
    }

    private Route getRoute(RouteParameters routeParameters) {
        randomWait();
        Route route = new Route();
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
        return route;
    }

    private SystemState getState() {
        randomWait();
        return MOCK_SYSTEM_STATE;
    }

    private Integer postState(SystemState systemState) {
        randomWait();
        MOCK_SYSTEM_STATE = systemState;
        return HttpsURLConnection.HTTP_OK;
    }

    private int getNotifyThreshold() {
        randomWait();
        return MOCK_THRESHOLD;
    }

    private Integer postNotifyThreshold(int threshold) {
        randomWait();
        MOCK_THRESHOLD = threshold;
        return HttpsURLConnection.HTTP_OK;
    }

    @Override
    public void getStationsAsync(Callback<List<Station>> callback, APIHandler handler) {
        APIRunnable.create(this::getStations).setCallback(callback).setHandler(handler).startThread();
    }

    @Override
    public void reportStationAsync(Station station, APIHandler handler) {
        APIRunnable.create(() -> reportStation(station)).setHandler(handler).startThread();
    }

    @Override
    public void setBrokenStationAsync(Station station, APIHandler handler) {
        APIRunnable.create(() -> setBrokenStation(station)).setHandler(handler).startThread();
    }

    @Override
    public void setWorkingStationAsync(Station station, APIHandler handler) {
        APIRunnable.create(() -> setWorkingStation(station)).setHandler(handler).startThread();
    }

    @Override
    public void postUserAsync(String username, String password, Callback<String> callback, APIHandler handler) {
        APIRunnable.create(() -> postUser(username, password)).setCallback(callback).setHandler(handler).startThread();
    }

    @Override
    public void getRouteAsync(RouteParameters routeParameters, Callback<Route> callback, APIHandler handler) {
        APIRunnable.create(() -> getRoute(routeParameters)).setCallback(callback).setHandler(handler).startThread();
    }

    @Override
    public void getStateAsync(Callback<SystemState> callback, APIHandler handler) {
        APIRunnable.create(this::getState).setCallback(callback).setHandler(handler).startThread();
    }

    @Override
    public void postStateAsync(SystemState systemState, APIHandler handler) {
        APIRunnable.create(() -> postState(systemState)).setHandler(handler).startThread();
    }

    @Override
    public void getNotifyThresholdAsync(Callback<Integer> callback, APIHandler handler) {
        APIRunnable.create(this::getNotifyThreshold).setCallback(callback).setHandler(handler).startThread();
    }

    @Override
    public void postNotifyThresholdAsync(int threshold, APIHandler handler) {
        APIRunnable.create(() -> postNotifyThreshold(threshold)).setHandler(handler).startThread();
    }
}
