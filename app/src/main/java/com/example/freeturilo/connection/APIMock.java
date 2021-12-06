package com.example.freeturilo.connection;

import android.util.Log;

import com.example.freeturilo.core.Route;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.core.SystemState;
import com.example.freeturilo.misc.AuthTools;
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

    private void randomWait() {
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(1800) + 200);
        } catch (InterruptedException ignored) {}
    }

    private Integer responseAuthorized() throws APIException {
        if(AuthTools.isLoggedIn())
            return HttpsURLConnection.HTTP_OK;
        else
            throw new APIException(HttpsURLConnection.HTTP_UNAUTHORIZED);
    }

    private List<Station> getStations() {
        randomWait();
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(new Station("Dewajtis - UKSW", 52.296298, 20.958358,
                1, 30, 28, 0));
        stations.add(new Station("Metro Młociny", 52.290974, 20.929556,
                2, 30, 18, 1));
        stations.add(new Station("Rondo ONZ", 52.232628, 20.997123,
                3, 29, 24, 2));
        Log.d("MockAPI", "getStations");
        return stations;
    }

    private Integer reportStation(Station station) {
        randomWait();
        Log.d("MockAPI", "reportStation");
        return HttpsURLConnection.HTTP_OK;
    }

    private Integer setBrokenStation(Station station) throws APIException {
        randomWait();
        Log.d("MockAPI", "setBrokenStation");
        return responseAuthorized();
    }

    private Integer setWorkingStation(Station station) throws APIException {
        randomWait();
        Log.d("MockAPI", "setWorkingStation");
        return responseAuthorized();
    }

    private String postUser(String email, String password) {
        randomWait();
        Log.d("MockAPI", "postUser");
        return "API_MOCK_TOKEN";
    }

    private Route getRoute(RouteParameters routeParameters) {
        randomWait();
        Log.d("MockAPI", "getRoute");
        Route route = new Route();
        route.cost = 0;
        route.parameters = routeParameters;
        route.waypoints = new ArrayList<>();
        route.waypoints.add(routeParameters.start);
        route.waypoints.add(routeParameters.end);
        route.directionsRoute = new DirectionsRoute();
        route.directionsRoute.bounds = new Bounds();
        double westLatitude = Math.min(routeParameters.start.latitude, routeParameters.end.latitude);
        double eastLatitude = Math.max(routeParameters.start.latitude, routeParameters.end.latitude);
        double southLongitude = Math.min(routeParameters.start.longitude, routeParameters.end.longitude);
        double northLongitude = Math.max(routeParameters.start.longitude, routeParameters.end.longitude);
        route.directionsRoute.bounds.southwest = new LatLng(westLatitude, southLongitude);
        route.directionsRoute.bounds.northeast = new LatLng(eastLatitude, northLongitude);
        route.directionsRoute.legs = new DirectionsLeg[1];
        route.directionsRoute.legs[0] = new DirectionsLeg();
        route.directionsRoute.legs[0].distance = new Distance();
        route.directionsRoute.legs[0].distance.inMeters = Math.round(Math.sqrt(
                Math.pow((eastLatitude - westLatitude) * 111000, 2)
                + Math.pow((northLongitude - southLongitude) * 111000, 2)));
        route.directionsRoute.legs[0].duration = new Duration();
        route.directionsRoute.legs[0].duration.inSeconds = Math.round(
                route.directionsRoute.legs[0].distance.inMeters / 5.55);
        route.directionsRoute.legs[0].steps = new DirectionsStep[1];
        route.directionsRoute.legs[0].steps[0] = new DirectionsStep();
        route.directionsRoute.legs[0].steps[0].distance = route.directionsRoute.legs[0].distance;
        route.directionsRoute.legs[0].steps[0].duration = route.directionsRoute.legs[0].duration;
        List<LatLng> startAndEnd = new ArrayList<>();
        startAndEnd.add(new LatLng(routeParameters.start.latitude, routeParameters.start.longitude));
        startAndEnd.add(new LatLng(routeParameters.end.latitude, routeParameters.end.longitude));
        route.directionsRoute.legs[0].steps[0].polyline = new EncodedPolyline(startAndEnd);
        return route;
    }

    private SystemState getState() {
        randomWait();
        Log.d("MockAPI", "getState");
        return SystemState.DEMO;
    }

    private Integer postState(SystemState systemState) throws APIException {
        randomWait();
        Log.d("MockAPI", "postState " + systemState.toString());
        return responseAuthorized();
    }

    private int getNotifyThreshold() {
        randomWait();
        Log.d("MockAPI", "getNotifyThreshold");
        return 10;
    }

    private Integer postNotifyThreshold(int threshold) throws APIException {
        randomWait();
        Log.d("MockAPI", "postNotifyThreshold " + threshold);
        return responseAuthorized();
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
