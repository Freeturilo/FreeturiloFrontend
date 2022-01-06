package com.example.freeturilo.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * An external API connection.
 * <p>
 * Object of class implementing this interface performs low-level operations in
 * an I/O connection model.
 * <p>
 * This interface should be treated as an adapter. It has been designed to
 * support HTTP/HTTPS connections but could also be used for other types of I/O
 * connections (it is used with {@code FileStreams} for testing purposes). For
 * feasibility of understanding the methods will be described as if they were
 * methods used for HTTP/HTTPS connections.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #setRequestProperty
 * @see #setDoOutput
 * @see #setChunkedStreamingMode
 * @see #getOutputStream
 * @see #getInputStream
 * @see #getResponseCode
 * @see #disconnect
 * @see Builder
 * @see APIConnection
 * @see HttpsURLConnection
 * @see HttpURLConnection
 */
interface ExternalConnection {
    /**
     * Sets a request property.
     * @param key       a string equal to property name
     * @param value     a string equal to property value
     */
    void setRequestProperty(String key, String value);

    /**
     * Sets a flag that indicates whether an object is attached to the request
     * of this connection.
     * @param doOutput  a boolean defining whether an object is or will be
     *                  attached to this connection's request
     */
    void setDoOutput(boolean doOutput);

    /**
     * Sets length of a chunk in chunked streaming mode.
     * @param chunkLength   an integer equal to the length of a chunk in
     *                      chunked streaming mode
     */
    void setChunkedStreamingMode(int chunkLength);

    /**
     * Gets the output stream of this connection which can be later used to
     * attach an object to the request of this connection.
     * @return              a writeable output stream
     * @throws APIException an exception representing an error which occurred
     *                      when trying to retrieve the output stream
     */
    OutputStream getOutputStream() throws APIException;

    /**
     * Gets the input stream of this connection which can be later used to
     * retrieve an object from the response of this connection.
     * @return              a readable input stream
     * @throws APIException an exception representing an error which occurred
     *                      when trying to retrieve the input stream
     */
    InputStream getInputStream() throws APIException;

    /**
     * Sends the request of this connection and gets the response code from the
     * response.
     * @return              an integer equal to the response code of this
     *                      connection's response
     * @throws APIException an exception representing an error which occurred
     *                      when trying to send the request or receive the
     *                      response
     */
    int getResponseCode() throws APIException;

    /**
     * Ends and disposes the connection.
     */
    void disconnect();

    /**
     * Builder of external connections.
     * <p>
     * Object of class implementing this interface builds I/O connections with
     * custom paths and request methods.
     * <p>
     * This interface should be treated as a factory for external connections.
     * It has been designed to support HTTP/HTTPS connections but could also be
     * used for other types of I/O connections (it is used with
     * {@code FileStreams} for testing purposes). For feasibility of
     * understanding the methods will be described as if they were methods
     * used for HTTP/HTTPS connections.
     *
     * @author Mikołaj Terzyk
     * @version 1.0.0
     * @see #newConnection
     * @see #setMethod
     * @see #appendPath
     * @see #create
     * @see ExternalConnection
     * @see APIConnection.Builder
     */
    interface Builder {
        /**
         * Creates a new builder.
         * @return      a builder with reset request method and path
         */
        Builder newConnection();

        /**
         * Sets the request method for the created connection.
         * @param method    a string equal to the name of a request method
         * @return          this builder with set request method
         */
        Builder setMethod(String method);

        /**
         * Appends a path fragment to the current path prepared for the created
         * connection.
         * @param path      a string equal to the appended path fragment
         * @return          this builder with updated path
         */
        Builder appendPath(String path);

        /**
         * Creates an external connection.
         * @return          the created connection with specified path and
         *                  request method
         * @throws APIException an exception representing an error which occurred
         *                      when trying to create the external connection
         */
        ExternalConnection create() throws APIException;
    }
}
