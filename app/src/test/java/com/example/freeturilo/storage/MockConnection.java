package com.example.freeturilo.storage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class MockConnection implements InternalConnection {
    private final boolean fileAbsent;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final boolean error;

    private MockConnection(@Nullable OutputStream outputStream, @Nullable InputStream inputStream, boolean fileAbsent) {
        this.fileAbsent = fileAbsent;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.error = false;
    }

    private MockConnection(boolean error) {
        this.fileAbsent = true;
        this.inputStream = null;
        this.outputStream = null;
        this.error = error;
    }

    @Override
    public InputStream openFileInput(String filename) throws StorageException {
        if (error)
            throw new StorageException("Test");
        return inputStream;
    }

    @Override
    public OutputStream openFileOutput(String filename) throws StorageException {
        if (error)
            throw new StorageException("Test");
        return outputStream;
    }

    @Override
    public boolean checkFileAbsent(String filename) {
        return fileAbsent;
    }

    public static class Builder implements InternalConnection.Builder {
        private final boolean fileAbsent;
        private final String inputFileName;
        private final OutputStream outputStream;
        private final boolean error;

        public Builder(@Nullable OutputStream outputStream, @NonNull String inputFileName) {
            this.fileAbsent = false;
            this.inputFileName = inputFileName;
            this.outputStream = outputStream;
            this.error = false;
        }

        public Builder(@Nullable OutputStream outputStream, @NonNull String inputFileName, boolean fileAbsent) {
            this.fileAbsent = fileAbsent;
            this.inputFileName = inputFileName;
            this.outputStream = outputStream;
            this.error = false;
        }

        public Builder(boolean error) {
            this.fileAbsent = true;
            this.inputFileName = "None";
            this.outputStream = null;
            this.error = error;
        }

        @NonNull
        public InternalConnection.Builder setContext(Context context) {
            if (error)
                return new Builder(true);
            return new Builder(outputStream, inputFileName, fileAbsent);
        }

        @NonNull
        public InternalConnection create() {
            InputStream inputStream = Objects.requireNonNull(getClass().getClassLoader()).getResourceAsStream(inputFileName);
            if (error)
                return new MockConnection(true);
            return new MockConnection(outputStream, inputStream, fileAbsent);
        }
    }
}
