package com.example.freeturilo.connection;

import java.io.OutputStream;
import java.util.List;

interface Connection {
    void setRequestProperty(String key, String value);

    void setDoOutput(boolean doOutput);

    void setChunkedStreamingMode(int chunkLength);

    OutputStream getOutputStream() throws APIException;

    <T> void attachRequestBody(T object) throws APIException;

    int getResponseCode() throws APIException;

    int retrieveResponseCode() throws APIException;

    <T> T retrieveResponseJsonObject(Class<T> classOfObject) throws APIException;

    <T> List<T> retrieveResponseJsonList(Class<T> classOfElement) throws APIException;

    void disconnect();

    interface Builder {
        Builder newConnection();

        Builder setMethod(String method);

        Builder appendPath(String path);

        Connection create() throws APIException;
    }
}
