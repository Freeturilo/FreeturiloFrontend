package com.example.freeturilo.connection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class MockConnection implements ExternalConnection {
    private final int responseCode;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    private MockConnection(int responseCode, @Nullable InputStream inputStream, @Nullable OutputStream outputStream) {
        this.responseCode = responseCode;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void setRequestProperty(@Nullable String key, @Nullable String value) {}

    @Override
    public void setDoOutput(boolean doOutput) {}

    @Override
    public void setChunkedStreamingMode(int chunkLength) {}

    @Nullable
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Nullable
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public void disconnect() {}

    public static class Builder implements ExternalConnection.Builder {
        private String method = null;
        private final StringBuilder pathBuilder = new StringBuilder();
        private final int responseCode;
        private final OutputStream outputStream;

        public Builder(int responseCode, @Nullable OutputStream outputStream) {
            this.responseCode = responseCode;
            this.outputStream = outputStream;
        }

        @NonNull
        public ExternalConnection.Builder newConnection() {
            return new Builder(responseCode, outputStream);
        }

        @NonNull
        public ExternalConnection.Builder setMethod(@NonNull String method) {
            this.method = method;
            return this;
        }

        @NonNull
        public ExternalConnection.Builder appendPath(@NonNull String path) {
            pathBuilder.append(path);
            return this;
        }

        @NonNull
        public ExternalConnection create() {
            String fileName = pathBuilder.insert(0, method).append("_response.json").toString();
            InputStream inputStream = Objects.requireNonNull(getClass().getClassLoader()).getResourceAsStream(fileName);
            return new MockConnection(responseCode, inputStream, outputStream);
        }
    }
}
