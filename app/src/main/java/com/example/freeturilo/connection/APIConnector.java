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

/**
 * A service for Freeturilo API data transaction.
 * <p>
 * Object of this class connects to Freeturilo API and handles data
 * transactions of the application. It encapsulates a {@link #builder} that
 * is used to build connections to various Freeturilo API endpoints.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see API
 * @see APIRunnable
 * @see ExternalConnection
 */
public class APIConnector implements API {
    /**
     * Stores a builder of external connections to Freeturilo API endpoints.
     */
    private final ExternalConnection.Builder builder;

    /**
     * Class constructor. Sets {@code builder} to the default value
     * {@code new APIConnection.Builder()}.
     */
    public APIConnector() {
        this.builder = new APIConnection.Builder();
    }

    /**
     * Class constructor.
     * @param builder       an external connection builder that will be used by
     *                      this connector to build connections to Freeturilo
     *                      API endpoints
     */
    public APIConnector(@NonNull ExternalConnection.Builder builder) {
        this.builder = builder;
    }

    /**
     * Attaches an object to a request within an API connection.
     * <p>
     * Prepares an API connection to accept output payload, serializes an
     * object to JSON and writes it to the output stream within the connection.
     * @param connection    an external connection over which the object is
     *                      sent
     * @param object        an object to serialize to JSON and send over the
     *                      connection
     * @param <T>           the type of the sent object
     * @throws APIException an exception representing an error which occurred
     *                      when sending the object over the connection
     */
    private <T> void attachRequestBody(@NonNull ExternalConnection connection,
                                       @NonNull T object) throws APIException {
        Gson gson = getFreeturiloSerializingGson();
        connection.setRequestProperty("Content-type", "application/json");
        connection.setDoOutput(true);
        connection.setChunkedStreamingMode(0);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(connection.getOutputStream()));
        gson.toJson(object, object.getClass(), writer);
        try { writer.close(); }
        catch (IOException ignored) {}
    }

    /**
     * Retrieves the response code from a response within an API connection.
     * <p>
     * Sends a request over an API connection and retrieves its response code.
     * If the code is not HTTP OK (200), disconnects. This method should always
     * be used to submit a request prepared within a connection.
     * @param connection        an external connection which is the source of
     *                          the response code
     * @return                  an integer equal to the response code if it is
     *                          equal to HTTP OK (200)
     * @throws APIException     an exception representing an error which is
     *                          indicated by the retrieved response code (if
     *                          not equal to 200)
     */
    private int retrieveResponseCode(@NonNull ExternalConnection connection) throws APIException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK)
            return responseCode;
        connection.disconnect();
        throw new APIException(responseCode);
    }

    /**
     * Retrieves an object from a response within an API connection.
     * <p>
     * Reads, deserializes and returns an object from the input stream within
     * an API connection.
     * @param connection    an external connection which is the source of the
     *                      object
     * @param typeOfObject  the type of the retrieved object
     * @param <T>           the type of the retrieved object
     * @return              an object retrieved over the connection and
     *                      deserialized from JSON
     * @throws APIException an exception representing an error which occurred
     *                      when retrieving the object over the connection
     */
    @NonNull
    private <T> T retrieveResponseJsonObject(@NonNull ExternalConnection connection,
                                             @NonNull Type typeOfObject) throws APIException {
        Gson gson = getFreeturiloDeserializingGson();
        JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
        T object = gson.fromJson(reader, typeOfObject);
        try { reader.close(); }
        catch (IOException ignored) {}
        return object;
    }

    /**
     * Gets a list of all bike stations.
     * @return              a list of all NextBike bike stations
     * @throws APIException an exception representing error that occurred in
     *                      the API transaction
     */
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

    /**
     * Reports a bike station.
     * @param station       a station to be reported
     * @return              an integer equal to the response code of the
     *                      transaction
     * @throws APIException an exception representing error that occurred in
     *                      the API transaction
     */
    @NonNull
    private Integer reportStation(@NonNull Station station) throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("POST")
                .appendPath("station").appendPath(String.valueOf(station.id))
                .appendPath("report").create();
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    /**
     * Sets a bike station state to broken.
     * @param station       the station to be set as broken
     * @return              an integer equal to the response code of the
     *                      transaction
     * @throws APIException an exception representing error that occurred in
     *                      the API transaction
     */
    @NonNull
    private Integer setBrokenStation(@NonNull Station station) throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("POST")
                .appendPath("station").appendPath(String.valueOf(station.id))
                .appendPath("broken").create();
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    /**
     * Sets a bike station state to working.
     * @param station       the station to be set as working
     * @return              an integer equal to the response code of the
     *                      transaction
     * @throws APIException an exception representing error that occurred in
     *                      the API transaction
     */
    @NonNull
    private Integer setWorkingStation(@NonNull Station station) throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("POST")
                .appendPath("station").appendPath(String.valueOf(station.id))
                .appendPath("working").create();
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    /**
     * Performs an administrator authentication.
     * @param authCredentials   a bundle of authentication credentials for
     *                          an administrator account
     * @return                  a string equal to an auth token
     * @throws APIException an exception representing error that occurred in
     *                      the API transaction
     */
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

    /**
     * Calculates a route with given parameters
     * @param routeParameters   a bundle of parameters to be used in route
     *                          calculation
     * @return                  a route calculated with given parameters
     * @throws APIException an exception representing error that occurred in
     *                      the API transaction
     */
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

    /**
     * Gets the system state.
     * @return              the current system state
     * @throws APIException an exception representing error that occurred in
     *                      the API transaction
     */
    @NonNull
    private SystemState getState() throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("GET")
                .appendPath("app").appendPath("state").create();
        retrieveResponseCode(connection);
        SystemState systemState = retrieveResponseJsonObject(connection, SystemState.class);
        connection.disconnect();
        return systemState;
    }

    /**
     * Updates the system state.
     * @param systemState   the value of system state to be set
     * @return              an integer equal to the response code of the
     *                      transaction
     * @throws APIException an exception representing error that occurred in
     *                      the API transaction
     */
    @NonNull
    private Integer postState(@NonNull SystemState systemState) throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("POST")
                .appendPath("app").appendPath("state").appendPath(systemState.toString()).create();
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    /**
     * Gets the threshold for administrator mail notifications.
     * @return              an integer equal to the value of the
     *                      threshold
     * @throws APIException an exception representing error that occurred in
     *                      the API transaction
     */
    private int getNotifyThreshold() throws APIException {
        ExternalConnection connection = builder.newConnection().setMethod("GET")
                .appendPath("app").appendPath("notify").create();
        retrieveResponseCode(connection);
        int threshold = retrieveResponseJsonObject(connection, Integer.class);
        connection.disconnect();
        return threshold;
    }

    /**
     * Updates the threshold for administrator mail notifications.
     * @param threshold     the value of the threshold to be set
     * @return              an integer equal to the response code of the
     *                      transaction
     * @throws APIException an exception representing error that occurred in
     *                      the API transaction
     */
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
    public Thread getStationsAsync(@Nullable Callback<List<Station>> callback,
                                   @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(this::getStations)
                                    .setCallback(callback)
                                    .setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread reportStationAsync(@NonNull Station station,
                                     @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> reportStation(station))
                                    .setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread setBrokenStationAsync(@NonNull Station station,
                                        @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> setBrokenStation(station))
                                    .setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread setWorkingStationAsync(@NonNull Station station,
                                         @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> setWorkingStation(station))
                                    .setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread postUserAsync(@NonNull AuthCredentials authCredentials,
                                @Nullable Callback<String> callback,
                                @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> postUser(authCredentials))
                                    .setCallback(callback)
                                    .setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread getRouteAsync(@NonNull RouteParameters routeParameters,
                                @Nullable Callback<Route> callback,
                                @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> getRoute(routeParameters))
                                    .setCallback(callback)
                                    .setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread getStateAsync(@Nullable Callback<SystemState> callback,
                                @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(this::getState)
                                    .setCallback(callback)
                                    .setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread postStateAsync(@NonNull SystemState systemState,
                                 @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> postState(systemState))
                                    .setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread getNotifyThresholdAsync(@Nullable Callback<Integer> callback,
                                          @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(this::getNotifyThreshold)
                                    .setCallback(callback)
                                    .setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread postNotifyThresholdAsync(int threshold,
                                           @Nullable APIHandler handler) {
        Thread thread = APIRunnable.create(() -> postNotifyThreshold(threshold))
                                    .setHandler(handler).toThread();
        thread.start();
        return thread;
    }
}
