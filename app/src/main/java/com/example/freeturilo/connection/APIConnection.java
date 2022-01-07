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

/**
 * A Freeturilo API connection.
 * <p>
 * Object of this class performs low-level operations on an HTTPS connection
 * with Freeturilo API.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #connection
 * @see ExternalConnection
 * @see Builder
 * @see HttpsURLConnection
 */
public class APIConnection implements ExternalConnection {
    /**
     * Stores the HTTPS connection used to perform data transaction with
     * Freeturilo API.
     */
    private final HttpsURLConnection connection;

    /**
     * Class constructor.
     * @param connection    an HTTPS connection to be used to perform data
     *                      transaction with an endpoint of Freeturilo API
     */
    private APIConnection(HttpsURLConnection connection) {
        this.connection = connection;
    }

    /**
     * Sets a request header of this connection.
     * @param key       a string equal to property name
     * @param value     a string equal to property value
     * @see HttpsURLConnection#setRequestProperty
     */
    public void setRequestProperty(String key, String value) {
        connection.setRequestProperty(key, value);
    }

    /**
     * Sets a flag that indicates whether the request of this connection
     * has a body.
     * @param doOutput  a boolean defining whether an object is or will be
     *                  attached to this connection's request
     * @see HttpsURLConnection#setDoOutput
     */
    public void setDoOutput(boolean doOutput) {
        connection.setDoOutput(doOutput);
    }

    /**
     * Sets length of a chunk in chunked streaming mode.
     * @param chunkLength   an integer equal to the length of a chunk in
     *                      chunked streaming mode
     * @see HttpsURLConnection#setChunkedStreamingMode
     */
    public void setChunkedStreamingMode(int chunkLength) {
        connection.setChunkedStreamingMode(chunkLength);
    }

    /**
     * Gets the output stream of this connection which can be later used to
     * define request body of this connection.
     * @return              a writeable output stream
     * @throws APIException an exception representing an error which occurred
     *                      when trying to retrieve the output stream
     * @see HttpsURLConnection#getOutputStream
     */
    public OutputStream getOutputStream() throws APIException {
        try {
            return connection.getOutputStream();
        } catch (IOException exception) {
            disconnect();
            throw new APIException(-1);
        }
    }

    /**
     * Gets the input stream of this connection which can be later used to
     * retrieve the payload of this connection
     * @return              a readable input stream
     * @throws APIException an exception representing an error which occurred
     *                      when trying to retrieve the input stream
     * @see HttpsURLConnection#getInputStream
     */
    public InputStream getInputStream() throws APIException {
        try {
            return connection.getInputStream();
        } catch (IOException exception) {
            disconnect();
            throw new APIException(-1);
        }
    }

    /**
     * Gets the response code from the response of this connection.
     * @return              an integer equal to the response code of this
     *                      connection's response
     * @throws APIException an exception representing an error which occurred
     *                      when trying to retrieve the response code
     * @see HttpsURLConnection#getResponseCode
     */
    public int getResponseCode() throws APIException {
        try {
            return connection.getResponseCode();
        } catch (IOException exception) {
            disconnect();
            throw new APIException(-1);
        }
    }

    /**
     * Ends the connection.
     */
    public void disconnect() {
        connection.disconnect();
    }

    /**
     * Builder of Freeturilo API connections.
     * <p>
     * Object of class implementing this interface builds Freeturilo API
     * connections with custom paths and request methods.
     *
     * @author Mikołaj Terzyk
     * @version 1.0.0
     * @see #method
     * @see #pathFragments
     * @see ExternalConnection.Builder
     * @see APIConnection
     */
    public static class Builder implements ExternalConnection.Builder {
        /**
         * Stores the request method for the created API connection.
         */
        private String method = null;
        /**
         * Stores fragments of the path to the endpoint of the created API
         * connection.
         */
        private final List<String> pathFragments = new ArrayList<>();

        /**
         * Creates a new builder.
         * @return      a builder with reset request method and path
         */
        public ExternalConnection.Builder newConnection() {
            return new Builder();
        }

        /**
         * Sets the request method of the created connection.
         * @param method    a string equal to the name of a request method
         * @return          this builder with set request method
         */
        public ExternalConnection.Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        /**
         * Appends a path fragment to the current path prepared for the created
         * connection.
         * @param path      a string equal to the appended path fragment
         * @return          this builder with updated path
         */
        public ExternalConnection.Builder appendPath(String path) {
            this.pathFragments.add(path);
            return this;
        }

        /**
         * Creates a Freeturilo API connection.
         * <p>
         * Creates an {@code HttpsURLConnection} to Freeturilo API with
         * specified path, request method and auth token (if present) as
         * header. Wraps the {@code HttpsURLConnection} in an
         * {@code APIConnection}.
         * @return              the created connection with specified path and
         *                      request method
         * @throws APIException an exception representing an error which
         *                      occurred when trying to create the Freeturilo
         *                      API connection
         * @see URL#openConnection
         */
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
