package com.example.freeturilo.connection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.core.Route;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.core.SystemState;
import com.example.freeturilo.misc.AuthCredentials;
import com.example.freeturilo.misc.Callback;

import java.util.List;

public class APIConnector implements API {

    private final Connection.Builder builder;

    public APIConnector() {
        this.builder = new APIConnection.Builder();
    }

    public APIConnector(Connection.Builder builder) {
        this.builder = builder;
    }

    @NonNull
    private List<Station> getStations() throws APIException {
        Connection connection = builder.newConnection().setMethod("GET")
                .appendPath("station").create();
        connection.retrieveResponseCode();
        List<Station> stations = connection.retrieveResponseJsonList(Station.class);
        connection.disconnect();
        return stations;
    }

    @NonNull
    private Integer reportStation(@NonNull Station station) throws APIException {
        Connection connection = builder.newConnection().setMethod("POST")
                .appendPath("station").appendPath(String.valueOf(station.id))
                .appendPath("report").create();
        int responseCode = connection.retrieveResponseCode();
        connection.disconnect();
        return responseCode;
    }

    @NonNull
    private Integer setBrokenStation(@NonNull Station station) throws APIException {
        Connection connection = builder.newConnection().setMethod("POST")
                .appendPath("station").appendPath(String.valueOf(station.id))
                .appendPath("broken").create();
        int responseCode = connection.retrieveResponseCode();
        connection.disconnect();
        return responseCode;
    }

    @NonNull
    private Integer setWorkingStation(@NonNull Station station) throws APIException {
        Connection connection = builder.newConnection().setMethod("POST")
                .appendPath("station").appendPath(String.valueOf(station.id))
                .appendPath("working").create();
        int responseCode = connection.retrieveResponseCode();
        connection.disconnect();
        return responseCode;
    }

    @NonNull
    private String postUser(@NonNull AuthCredentials authCredentials) throws APIException {
        Connection connection = builder.newConnection().setMethod("POST")
                .appendPath("user").create();
        connection.attachRequestBody(authCredentials);
        connection.retrieveResponseCode();
        String token = connection.retrieveResponseJsonObject(String.class);
        connection.disconnect();
        return token;
    }

    @NonNull
    private Route getRoute(@NonNull RouteParameters routeParameters) throws APIException {
        Connection connection = builder.newConnection().setMethod("POST")
                .appendPath("route").create();
        connection.attachRequestBody(routeParameters);
        connection.retrieveResponseCode();
        Route route = connection.retrieveResponseJsonObject(Route.class);
        connection.disconnect();
        return route;
    }

    @NonNull
    private SystemState getState() throws APIException {
        Connection connection = builder.newConnection().setMethod("GET")
                .appendPath("app").appendPath("state").create();
        connection.retrieveResponseCode();
        SystemState systemState = connection.retrieveResponseJsonObject(SystemState.class);
        connection.disconnect();
        return systemState;
    }

    @NonNull
    private Integer postState(@NonNull SystemState systemState) throws APIException {
        Connection connection = builder.newConnection().setMethod("POST")
                .appendPath("app").appendPath("state").appendPath(systemState.toString()).create();
        int responseCode = connection.retrieveResponseCode();
        connection.disconnect();
        return responseCode;
    }

    private int getNotifyThreshold() throws APIException {
        Connection connection = builder.newConnection().setMethod("GET")
                .appendPath("app").appendPath("notify").create();
        connection.retrieveResponseCode();
        int threshold = connection.retrieveResponseJsonObject(Integer.class);
        connection.disconnect();
        return threshold;
    }

    @NonNull
    private Integer postNotifyThreshold(int threshold) throws APIException {
        Connection connection = builder.newConnection().setMethod("POST")
                .appendPath("app").appendPath("notify")
                .appendPath(String.valueOf(threshold)).create();
        int responseCode = connection.retrieveResponseCode();
        connection.disconnect();
        return responseCode;
    }

    @Override
    public Thread getStationsAsync(@Nullable Callback<List<Station>> callback, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(this::getStations).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @Override
    public Thread reportStationAsync(@NonNull Station station, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> reportStation(station)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @Override
    public Thread setBrokenStationAsync(@NonNull Station station, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> setBrokenStation(station)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @Override
    public Thread setWorkingStationAsync(@NonNull Station station, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> setWorkingStation(station)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @Override
    public Thread postUserAsync(@NonNull AuthCredentials authCredentials,
                              @Nullable Callback<String> callback, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> postUser(authCredentials)).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @Override
    public Thread getRouteAsync(@NonNull RouteParameters routeParameters,
                              @Nullable Callback<Route> callback, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> getRoute(routeParameters)).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @Override
    public Thread getStateAsync(@Nullable Callback<SystemState> callback, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(this::getState).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @Override
    public Thread postStateAsync(@NonNull SystemState systemState, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> postState(systemState)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @Override
    public Thread getNotifyThresholdAsync(@Nullable Callback<Integer> callback, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(this::getNotifyThreshold).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @Override
    public Thread postNotifyThresholdAsync(int threshold, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> postNotifyThreshold(threshold)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }
}
