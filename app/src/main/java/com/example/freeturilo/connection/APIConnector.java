package com.example.freeturilo.connection;

import static com.example.freeturilo.json.FreeturiloGson.getFreeturiloDeserializingGson;
import static com.example.freeturilo.json.FreeturiloGson.getFreeturiloSerializingGson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.core.Route;
import com.example.freeturilo.core.RouteFragment;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.core.SystemState;
import com.example.freeturilo.misc.AuthCredentials;
import com.example.freeturilo.misc.Callback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

public class APIConnector implements API {

    private final ExternalConnection.Builder builder;

    public APIConnector() {
        this.builder = new APIConnection.Builder();
    }

    public APIConnector(@NonNull ExternalConnection.Builder builder) {
        this.builder = builder;
    }

    private <T> void attachRequestBody(@NonNull ExternalConnection connection, @NonNull T object) throws APIException {
        Gson gson = getFreeturiloSerializingGson();
        connection.setRequestProperty("Content-type", "application/json");
        connection.setDoOutput(true);
        connection.setChunkedStreamingMode(0);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(connection.getOutputStream()));
        gson.toJson(object, object.getClass(), writer);
        try { writer.close(); }
        catch (IOException ignored) {}
    }

    private int retrieveResponseCode(@NonNull ExternalConnection connection) throws APIException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK)
            return responseCode;
        connection.disconnect();
        throw new APIException(responseCode);
    }

    @NonNull
    private <T> T retrieveResponseJsonObject(@NonNull ExternalConnection connection, @NonNull Type typeOfObject) throws APIException {
        Gson gson = getFreeturiloDeserializingGson();
        JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
        T object = gson.fromJson(reader, typeOfObject);
        try { reader.close(); }
        catch (IOException ignored) {}
        return object;
    }

    @NonNull
    private List<Station> getStations() throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("GET")
                .appendPath("station").create();
        retrieveResponseCode(connection);
        List<Station> stations = retrieveResponseJsonObject(connection,
                new TypeToken<List<Station>>(){}.getType());
        connection.disconnect();
        return stations;
    }

    @NonNull
    private Integer reportStation(@NonNull Station station) throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("POST")
                .appendPath("station").appendPath(String.valueOf(station.id))
                .appendPath("report").create();
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    @NonNull
    private Integer setBrokenStation(@NonNull Station station) throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("POST")
                .appendPath("station").appendPath(String.valueOf(station.id))
                .appendPath("broken").create();
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    @NonNull
    private Integer setWorkingStation(@NonNull Station station) throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("POST")
                .appendPath("station").appendPath(String.valueOf(station.id))
                .appendPath("working").create();
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    @NonNull
    private String postUser(@NonNull AuthCredentials authCredentials) throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("POST")
                .appendPath("user").create();
        attachRequestBody(connection, authCredentials);
        retrieveResponseCode(connection);
        String token = retrieveResponseJsonObject(connection, String.class);
        connection.disconnect();
        return token;
    }

    @NonNull
    private Route getRoute(@NonNull RouteParameters routeParameters) throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("POST")
                .appendPath("route").create();
        attachRequestBody(connection, routeParameters);
        retrieveResponseCode(connection);
        List<RouteFragment> routeFragments = retrieveResponseJsonObject(connection,
                new TypeToken<List<RouteFragment>>(){}.getType());
        Route route = new Route(routeFragments, routeParameters);
        connection.disconnect();
        return route;
    }

    @NonNull
    private SystemState getState() throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("GET")
                .appendPath("app").appendPath("state").create();
        retrieveResponseCode(connection);
        SystemState systemState = retrieveResponseJsonObject(connection, SystemState.class);
        connection.disconnect();
        return systemState;
    }

    @NonNull
    private Integer postState(@NonNull SystemState systemState) throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("POST")
                .appendPath("app").appendPath("state").appendPath(systemState.toString()).create();
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    private int getNotifyThreshold() throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("GET")
                .appendPath("app").appendPath("notify").create();
        retrieveResponseCode(connection);
        int threshold = retrieveResponseJsonObject(connection, Integer.class);
        connection.disconnect();
        return threshold;
    }

    @NonNull
    private Integer postNotifyThreshold(int threshold) throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("POST")
                .appendPath("app").appendPath("notify")
                .appendPath(String.valueOf(threshold)).create();
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    @NonNull
    public Thread getStationsAsync(@Nullable Callback<List<Station>> callback, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(this::getStations).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread reportStationAsync(@NonNull Station station, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> reportStation(station)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread setBrokenStationAsync(@NonNull Station station, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> setBrokenStation(station)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread setWorkingStationAsync(@NonNull Station station, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> setWorkingStation(station)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread postUserAsync(@NonNull AuthCredentials authCredentials,
                              @Nullable Callback<String> callback, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> postUser(authCredentials)).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread getRouteAsync(@NonNull RouteParameters routeParameters,
                              @Nullable Callback<Route> callback, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> getRoute(routeParameters)).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread getStateAsync(@Nullable Callback<SystemState> callback, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(this::getState).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread postStateAsync(@NonNull SystemState systemState, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> postState(systemState)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread getNotifyThresholdAsync(@Nullable Callback<Integer> callback, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(this::getNotifyThreshold).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread postNotifyThresholdAsync(int threshold, @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> postNotifyThreshold(threshold)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }
}
