package com.example.freeturilo.connection;

import android.net.Uri;

import com.example.freeturilo.BuildConfig;
import com.example.freeturilo.misc.AuthTools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class APIConnection implements ExternalConnection {

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

    public int getResponseCode() throws APIException {
        try {
            return connection.getResponseCode();
        } catch (IOException exception) {
            disconnect();
            throw new APIException(-1);
        }
    }

    public void disconnect() {
        connection.disconnect();
    }

    public static class Builder implements ExternalConnection.Builder {
        private String method = null;
        private final List<String> pathFragments = new ArrayList<>();

        public ExternalConnection.Builder newConnection() {
            return new Builder();
        }

        public ExternalConnection.Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public ExternalConnection.Builder appendPath(String path) {
            this.pathFragments.add(path);
            return this;
        }

        public ExternalConnection create() throws APIException {
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
