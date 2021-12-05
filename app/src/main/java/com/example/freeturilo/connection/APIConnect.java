package com.example.freeturilo.connection;

import android.net.Uri;

import com.example.freeturilo.BuildConfig;
import com.example.freeturilo.core.Route;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.misc.AuthTools;
import com.example.freeturilo.misc.Callback;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class APIConnect implements API {

    private URL createURL(String ... pathFragments) throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(BuildConfig.FREETURILO_API_URL);
        for (String pathFragment : pathFragments)
            builder.appendPath(pathFragment);
        return new URL(builder.build().toString());
    }

    private HttpsURLConnection createConnection(String method, String ... pathFragments) throws APIException {
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

    private <T> void attachRequestBody(HttpsURLConnection connection, T object) throws APIException {
        Gson gson = new Gson();
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

    private int retrieveResponseCode(HttpsURLConnection connection) throws APIException {
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

    private <T> T retrieveResponseJsonObject(HttpsURLConnection connection, Class<T> classOfObject) throws APIException {
        Gson gson = new Gson();
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

    private <T> List<T> retrieveResponseJsonList(HttpsURLConnection connection, Class<T> classOfElement) throws APIException {
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

    private List<Station> getStations() throws APIException {
        HttpsURLConnection connection = createConnection("GET", "station");
        retrieveResponseCode(connection);
        List<Station> stations = retrieveResponseJsonList(connection, Station.class);
        connection.disconnect();
        return stations;
    }

    private Integer reportStation(Station station) throws APIException {
        HttpsURLConnection connection = createConnection("POST",
                "station", String.valueOf(station.id), "report");
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    private Integer setBrokenStation(Station station) throws APIException {
        HttpsURLConnection connection = createConnection("POST",
                "station", String.valueOf(station.id), "broken");
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    private Integer setWorkingStation(Station station) throws APIException {
        HttpsURLConnection connection = createConnection("POST",
                "station", String.valueOf(station.id), "working");
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    private String postUser(String email, String password) throws APIException {
        HttpsURLConnection connection = createConnection("POST", "user");
        connection.setRequestProperty("email", email);
        connection.setRequestProperty("password", password);
        String token = retrieveResponseJsonObject(connection, String.class);
        connection.disconnect();
        return token;
    }

    private Route getRoute(RouteParameters routeParameters) throws APIException {
        HttpsURLConnection connection = createConnection("POST", "route");
        attachRequestBody(connection, routeParameters);
        Route route = retrieveResponseJsonObject(connection, Route.class);
        connection.disconnect();
        return route;
    }

    private Integer postStateStop() throws APIException {
        HttpsURLConnection connection = createConnection("POST", "app", "stop");
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    private Integer postStateStart() throws APIException {
        HttpsURLConnection connection = createConnection("POST", "app", "start");
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    private Integer postStateDemo() throws APIException {
        HttpsURLConnection connection = createConnection("POST", "app", "demo");
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
    }

    private Integer postNotifyThreshold(int threshold) throws APIException {
        HttpsURLConnection connection = createConnection("POST",
                "app", "notify", String.valueOf(threshold));
        int responseCode = retrieveResponseCode(connection);
        connection.disconnect();
        return responseCode;
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
