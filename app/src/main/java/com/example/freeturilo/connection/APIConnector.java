package com.example.freeturilo.connection;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.BuildConfig;
import com.example.freeturilo.core.Criterion;
import com.example.freeturilo.core.Route;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.core.SystemState;
import com.example.freeturilo.misc.AuthCredentials;
import com.example.freeturilo.misc.AuthTools;
import com.example.freeturilo.misc.Callback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class APIConnector implements API {

    private static class CriterionSerializer implements JsonSerializer<Criterion> {
        @Override
        public JsonElement serialize(Criterion src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.ordinal());
        }
    }

    private static class CriterionDeserializer implements JsonDeserializer<Criterion> {
        @Override
        public Criterion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Criterion.values()[json.getAsJsonPrimitive().getAsInt()];
        }
    }

    private static class SystemStateDeserializer implements JsonDeserializer<SystemState> {
        @Override
        public SystemState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return SystemState.values()[json.getAsJsonPrimitive().getAsInt()];
        }
    }

    private static class DistanceDeserializer implements JsonDeserializer<Distance> {
        @Override
        public Distance deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            Distance distance = new Distance();
            distance.humanReadable = object.get("text").getAsString();
            distance.inMeters = object.get("value").getAsLong();
            return distance;
        }
    }

    private static class DurationDeserializer implements JsonDeserializer<Duration> {
        @Override
        public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            Duration duration = new Duration();
            duration.humanReadable = object.get("text").getAsString();
            duration.inSeconds = object.get("value").getAsLong();
            return duration;
        }
    }


    @NonNull
    private URL createURL(@NonNull String ... pathFragments) throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(BuildConfig.FREETURILO_API_URL);
        for (String pathFragment : pathFragments)
            builder.appendPath(pathFragment);
        return new URL(builder.build().toString());
    }

    @NonNull
    private HttpsURLConnection createConnection(@NonNull String method,
                                                @NonNull String ... pathFragments) throws APIException {
        HttpsURLConnection connection = null;
        try {
            URL url = createURL(pathFragments);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            if (AuthTools.isLoggedIn())
                connection.setRequestProperty("api-key", AuthTools.getToken());
            return connection;
        } catch (IOException ignored) {
            if(connection != null)
                connection.disconnect();
            throw new APIException(-1);
        }
    }

    private <T> void attachRequestBody(@NonNull HttpsURLConnection connection,
                                       @NonNull T object) throws APIException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Criterion.class, new CriterionSerializer());
        Gson gson = gsonBuilder.create();
        connection.setRequestProperty("Content-type", "application/json");
        connection.setDoOutput(true);
        connection.setChunkedStreamingMode(0);
        try {
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(connection.getOutputStream()));
            gson.toJson(object, object.getClass(), writer);
            writer.close();
        } catch (IOException exception) {
            connection.disconnect();
            throw new APIException(-1);
        }
    }

    private int retrieveResponseCode(@NonNull HttpsURLConnection connection) throws APIException {
        int responseCode;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException exception) {
            connection.disconnect();
            throw new APIException(-1);
        }
        if (responseCode != 200) {
            connection.disconnect();
            throw new APIException(responseCode);
        }
        return responseCode;
    }

    @NonNull
    private <T> T retrieveResponseJsonObject(@NonNull HttpsURLConnection connection,
                                             @NonNull Class<T> classOfObject) throws APIException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Criterion.class, new CriterionDeserializer());
        gsonBuilder.registerTypeAdapter(SystemState.class, new SystemStateDeserializer());
        gsonBuilder.registerTypeAdapter(Distance.class, new DistanceDeserializer());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationDeserializer());
        Gson gson = gsonBuilder.create();
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
            T object = gson.fromJson(reader, classOfObject);
            reader.close();
            return object;
        } catch (IOException exception) {
            connection.disconnect();
            throw new APIException(-1);
        }
    }

    @NonNull
    private <T> List<T> retrieveResponseJsonList(@NonNull HttpsURLConnection connection,
                                                 @NonNull Class<T> classOfElement) throws APIException {
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
            List<T> list = new ArrayList<>();
            reader.beginArray();
            while(reader.hasNext())
                list.add(gson.fromJson(reader, classOfElement));
            reader.endArray();
            reader.close();
            return list;
        } catch (IOException exception) {
            connection.disconnect();
            throw new APIException(-1);
        }
    }

    @NonNull
    private List<Station> getStations() throws APIException {
        HttpsURLConnection connection = createConnection("GET", "station");
        retrieveResponseCode(connection);
        List<Station> stations = retrieveResponseJsonList(connection, Station.class);
        connection.disconnect();
        return stations;
    }

    @NonNull
    private Integer reportStation(@NonNull Station station) throws APIException {
        HttpsURLConnection connection = createConnection("POST",
                "station", String.valueOf(station.id), "report");
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    @NonNull
    private Integer setBrokenStation(@NonNull Station station) throws APIException {
        HttpsURLConnection connection = createConnection("POST",
                "station", String.valueOf(station.id), "broken");
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    @NonNull
    private Integer setWorkingStation(@NonNull Station station) throws APIException {
        HttpsURLConnection connection = createConnection("POST",
                "station", String.valueOf(station.id), "working");
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    @NonNull
    private String postUser(@NonNull AuthCredentials authCredentials) throws APIException {
        HttpsURLConnection connection = createConnection("POST", "user");
        attachRequestBody(connection, authCredentials);
        retrieveResponseCode(connection);
        String token = retrieveResponseJsonObject(connection, String.class);
        connection.disconnect();
        return token;
    }

    @NonNull
    private Route getRoute(@NonNull RouteParameters routeParameters) throws APIException {
        HttpsURLConnection connection = createConnection("POST", "route");
        attachRequestBody(connection, routeParameters);
        retrieveResponseCode(connection);
        Route route = retrieveResponseJsonObject(connection, Route.class);
        connection.disconnect();
        return route;
    }

    @NonNull
    private SystemState getState() throws APIException {
        HttpsURLConnection connection = createConnection("GET", "app", "state");
        retrieveResponseCode(connection);
        SystemState systemState = retrieveResponseJsonObject(connection, SystemState.class);
        connection.disconnect();
        return systemState;
    }

    @NonNull
    private Integer postState(@NonNull SystemState systemState) throws APIException {
        HttpsURLConnection connection = createConnection("POST",
                "app", "state", systemState.toString());
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    private int getNotifyThreshold() throws APIException {
        HttpsURLConnection connection = createConnection("GET", "app", "notify");
        retrieveResponseCode(connection);
        int threshold = retrieveResponseJsonObject(connection, Integer.class);
        connection.disconnect();
        return threshold;
    }

    @NonNull
    private Integer postNotifyThreshold(int threshold) throws APIException {
        HttpsURLConnection connection = createConnection("POST",
                "app", "notify", String.valueOf(threshold));
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    @Override
    public void getStationsAsync(@Nullable Callback<List<Station>> callback, @Nullable APIHandler handler) {
        APIRunnable.create(this::getStations).setCallback(callback).setHandler(handler).startThread();
    }

    @Override
    public void reportStationAsync(@NonNull Station station, @Nullable APIHandler handler) {
        APIRunnable.create(() -> reportStation(station)).setHandler(handler).startThread();
    }

    @Override
    public void setBrokenStationAsync(@NonNull Station station, @Nullable APIHandler handler) {
        APIRunnable.create(() -> setBrokenStation(station)).setHandler(handler).startThread();
    }

    @Override
    public void setWorkingStationAsync(@NonNull Station station, @Nullable APIHandler handler) {
        APIRunnable.create(() -> setWorkingStation(station)).setHandler(handler).startThread();
    }

    @Override
    public void postUserAsync(@NonNull AuthCredentials authCredentials,
                              @Nullable Callback<String> callback, @Nullable APIHandler handler) {
        APIRunnable.create(() -> postUser(authCredentials)).setCallback(callback).setHandler(handler).startThread();
    }

    @Override
    public void getRouteAsync(@NonNull RouteParameters routeParameters,
                              @Nullable Callback<Route> callback, @Nullable APIHandler handler) {
        APIRunnable.create(() -> getRoute(routeParameters)).setCallback(callback).setHandler(handler).startThread();
    }

    @Override
    public void getStateAsync(@Nullable Callback<SystemState> callback, @Nullable APIHandler handler) {
        APIRunnable.create(this::getState).setCallback(callback).setHandler(handler).startThread();
    }

    @Override
    public void postStateAsync(@NonNull SystemState systemState, @Nullable APIHandler handler) {
        APIRunnable.create(() -> postState(systemState)).setHandler(handler).startThread();
    }

    @Override
    public void getNotifyThresholdAsync(@Nullable Callback<Integer> callback, @Nullable APIHandler handler) {
        APIRunnable.create(this::getNotifyThreshold).setCallback(callback).setHandler(handler).startThread();
    }

    @Override
    public void postNotifyThresholdAsync(int threshold, @Nullable APIHandler handler) {
        APIRunnable.create(() -> postNotifyThreshold(threshold)).setHandler(handler).startThread();
    }
}
