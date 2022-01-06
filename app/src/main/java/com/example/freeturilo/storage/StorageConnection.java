package com.example.freeturilo.storage;

import android.content.Context;

import com.example.freeturilo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An internal storage connection.
 * <p>
 * Object of this class performs low-level operations on device's internal
 * storage files.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #context
 * @see InternalConnection
 * @see Builder
 * @see Context
 */
public class StorageConnection implements InternalConnection {
    /**
     * Stores the context of the application used to perform data transaction
     * with internal storage.
     */
    private final Context context;

    /**
     * Class constructor.
     * @param context       the context of the application providing all global
     *                      information
     */
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

    /**
     * Builder of internal storage connections.
     * <p>
     * Object of class implementing this interface builds internal storage
     * connections with custom context.
     *
     * @author Mikołaj Terzyk
     * @version 1.0.0
     * @see #context
     * @see InternalConnection.Builder
     * @see StorageConnection
     */
    public static class Builder implements InternalConnection.Builder {
        /**
         * Stores the context for the created storage connection.
         */
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
