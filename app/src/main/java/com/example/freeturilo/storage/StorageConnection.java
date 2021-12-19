package com.example.freeturilo.storage;

import android.content.Context;

import com.example.freeturilo.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

public class StorageConnection implements InternalConnection {
    private final Context context;

    private StorageConnection(Context context) {
        this.context = context;
    }

    @Override
    public InputStream openFileInput(String filename) throws StorageException {
        try {
            return context.openFileInput(filename);
        } catch (FileNotFoundException e) {
            String noFileMessage = context.getString(R.string.no_file_message);
            throw new StorageException(noFileMessage);
        }
    }

    @Override
    public OutputStream openFileOutput(String filename) throws StorageException {
        try {
            return context.openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            String noFileMessage = context.getString(R.string.no_file_message);
            throw new StorageException(noFileMessage);
        }
    }

    @Override
    public boolean checkFileAbsent(String filename) {
        return !context.getFileStreamPath(filename).exists();
    }

    public static class Builder implements InternalConnection.Builder {
        private Context context;

        @Override
        public InternalConnection.Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        @Override
        public InternalConnection create() {
            return new StorageConnection(context);
        }
    }
}
