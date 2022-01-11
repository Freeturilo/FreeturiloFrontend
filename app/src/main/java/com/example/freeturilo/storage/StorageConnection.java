package com.example.freeturilo.storage;

import android.content.Context;

import com.example.freeturilo.R;

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
     * Stores the filename of the file which is the source or the target of
     * data transaction performed within this connection.
     */
    private final String filename;

    /**
     * Class constructor.
     * @param context       the context of the application providing all global
     *                      information
     */
    private StorageConnection(String filename, Context context) {
        this.filename = filename;
        this.context = context;
    }

    /**
     * Gets the output stream of this connection which can be later used to
     * write to the internal storage file of this connection.
     * @throws StorageException an exception representing an error which
     *                          occurred when trying to retrieve the output
     *                          stream
     */
    @Override
    public OutputStream openFileOutput() throws StorageException {
        try {
            return context.openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            String noFileMessage = context.getString(R.string.no_file_message);
            throw new StorageException(noFileMessage);
        }
    }

    /**
     * Gets the input stream of this connection which can be later used to read
     * content of the internal storage file of this connection.
     * @return                  a readable input stream
     * @throws StorageException an exception representing an error which
     *                          occurred when trying to retrieve the input
     *                          stream
     * @see Context#openFileInput
     */
    @Override
    public InputStream openFileInput() throws StorageException {
        try {
            return context.openFileInput(filename);
        } catch (FileNotFoundException e) {
            String noFileMessage = context.getString(R.string.no_file_message);
            throw new StorageException(noFileMessage);
        }
    }

    /**
     * Checks if the internal storage file of this connection is absent in
     * internal storage.
     * @return                  a boolean defining whether the specified file
     *                          is absent
     */
    @Override
    public boolean checkFileAbsent() {
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
     * @see InternalConnection.Builder
     * @see StorageConnection
     */
    public static class Builder implements InternalConnection.Builder {
        /**
         * Stores the context for the created storage connection.
         */
        private Context context;
        /**
         * Stores the filename of the file of the created storage connection.
         */
        private String filename;

        /**
         * Sets the context for the created connection.
         * @param context   the context of the application providing all global
         *                  information
         * @return          this builder with set context
         */
        @Override
        public InternalConnection.Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        /**
         * Sets the filename for the created connection.
         * @param filename  a string equal to the name of the file to be used
         *                  by the created connection.
         * @return          this builder with set filename
         */
        @Override
        public InternalConnection.Builder setFilename(String filename) {
            this.filename = filename;
            return this;
        }

        /**
         * Creates an internal storage connection.
         * @return          the created connection with specified context
         */
        @Override
        public InternalConnection create() {
            return new StorageConnection(filename, context);
        }
    }
}
