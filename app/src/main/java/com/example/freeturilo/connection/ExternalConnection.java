package com.example.freeturilo.connection;

import java.io.InputStream;
import java.io.OutputStream;

interface ExternalConnection {
    void setRequestProperty(String key, String value);

    void setDoOutput(boolean doOutput);

    void setChunkedStreamingMode(int chunkLength);

    OutputStream getOutputStream() throws APIException;

    InputStream getInputStream() throws APIException;

    int getResponseCode() throws APIException;

    void disconnect();

    interface Builder {
        Builder newConnection();

        Builder setMethod(String method);

        Builder appendPath(String path);

        ExternalConnection create() throws APIException;
    }
}
