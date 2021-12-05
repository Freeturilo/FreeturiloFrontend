package com.example.freeturilo.connection;

import android.util.Log;

import com.example.freeturilo.core.Route;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.misc.AuthTools;
import com.example.freeturilo.misc.Callback;

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

    private Integer responseAuthorized() {
        if(AuthTools.isLoggedIn())
            return HttpsURLConnection.HTTP_OK;
        else
            return HttpsURLConnection.HTTP_UNAUTHORIZED;
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

    private Integer setBrokenStation(Station station) {
        randomWait();
        Log.d("MockAPI", "setBrokenStation");
        return responseAuthorized();
    }

    private Integer setWorkingStation(Station station) {
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
        return new Route();
    }

    private Integer postStateStop() {
        randomWait();
        Log.d("MockAPI", "postStateStop");
        return responseAuthorized();
    }

    private Integer postStateStart() {
        randomWait();
        Log.d("MockAPI", "postStateStart");
        return responseAuthorized();
    }

    private Integer postStateDemo() {
        randomWait();
        Log.d("MockAPI", "postStateDemo");
        return responseAuthorized();
    }

    private Integer postNotifyThreshold(int threshold) throws APIException {
        randomWait();
        Log.d("MockAPI", "postNotifyThreshold");
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
    public void postStateStopAsync(APIHandler handler) {
        APIRunnable.create(this::postStateStop).setHandler(handler).startThread();
    }

    @Override
    public void postStateStartAsync(APIHandler handler) {
        APIRunnable.create(this::postStateStart).setHandler(handler).startThread();
    }

    @Override
    public void postStateDemoAsync(APIHandler handler) {
        APIRunnable.create(this::postStateDemo).setHandler(handler).startThread();
    }

    @Override
    public void postNotifyThresholdAsync(int threshold, APIHandler handler) {
        APIRunnable.create(() -> postNotifyThreshold(threshold)).setHandler(handler).startThread();
    }
}
