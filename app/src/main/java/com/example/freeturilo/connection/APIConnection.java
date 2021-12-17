package com.example.freeturilo.connection;

import static com.example.freeturilo.json.FreeturiloGson.getFreeturiloDeserializingGson;
import static com.example.freeturilo.json.FreeturiloGson.getFreeturiloSerializingGson;

import android.net.Uri;

import com.example.freeturilo.BuildConfig;
import com.example.freeturilo.misc.AuthTools;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class APIConnection implements Connection {

    private final HttpsURLConnection connection;

    private APIConnection(HttpsURLConnection connection) {
        this.connection = connection;
    }

    public void setRequestProperty(String key, String value) {
        connection.setRequestProperty(key, value);
    }

    public void setDoOutput(boolean doOutput) {
        connection.setDoOutput(doOutput);
    }

    public void setChunkedStreamingMode(int chunkLength) {
        connection.setChunkedStreamingMode(chunkLength);
    }

    public OutputStream getOutputStream() throws APIException {
        try {
            return connection.getOutputStream();
        } catch (IOException exception) {
            disconnect();
            throw new APIException(-1);
        }
    }

    public InputStream getInputStream() throws APIException {
        try {
            return connection.getInputStream();
        } catch (IOException exception) {
            disconnect();
            throw new APIException(-1);
        }
    }

    public <T> void attachRequestBody(T object) throws APIException {
        Gson gson = getFreeturiloSerializingGson();
        setRequestProperty("Content-type", "application/json");
        setDoOutput(true);
        setChunkedStreamingMode(0);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(getOutputStream()));
        gson.toJson(object, object.getClass(), writer);
        try { writer.close(); }
        catch (IOException ignored) {}
    }

    public int getResponseCode() throws APIException {
        try {
            return connection.getResponseCode();
        } catch (IOException exception) {
            disconnect();
            throw new APIException(-1);
        }
    }

    public int retrieveResponseCode() throws APIException {
        int responseCode = getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK)
            return responseCode;
        disconnect();
        throw new APIException(responseCode);
    }

    public <T> T retrieveResponseJsonObject(Class<T> classOfObject) throws APIException {
        Gson gson = getFreeturiloDeserializingGson();
        JsonReader reader = new JsonReader(new InputStreamReader(getInputStream()));
        T object = gson.fromJson(reader, classOfObject);
        try { reader.close(); }
        catch (IOException ignored) {}
        return object;
    }

    public <T> List<T> retrieveResponseJsonList(Class<T> classOfElement) throws APIException {
        Gson gson = getFreeturiloDeserializingGson();
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(getInputStream()));
            List<T> list = new ArrayList<>();
            reader.beginArray();
            while(reader.hasNext())
                list.add(gson.fromJson(reader, classOfElement));
            reader.endArray();
            reader.close();
            return list;
        } catch (IOException exception) {
            disconnect();
            throw new APIException(-1);
        }
    }

    public void disconnect() {
        connection.disconnect();
    }

    public static class Builder implements Connection.Builder {
        String method = null;
        List<String> pathFragments = new ArrayList<>();

        public Connection.Builder newConnection() {
            return new Builder();
        }

        public Connection.Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public Connection.Builder appendPath(String path) {
            this.pathFragments.add(path);
            return this;
        }

        public Connection create() throws APIException {
            HttpsURLConnection connection = null;
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https").authority(BuildConfig.FREETURILO_API_URL);
            for (String pathFragment : pathFragments)
                builder.appendPath(pathFragment);
            try {
                URL url = new URL(builder.build().toString());
                connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod(method);
                if (AuthTools.isLoggedIn())
                    connection.setRequestProperty("api-key", AuthTools.getToken());
                return new APIConnection(connection);
            } catch (IOException exception) {
                if(connection != null)
                    connection.disconnect();
                throw new APIException(-1);
            }
        }
    }
}
