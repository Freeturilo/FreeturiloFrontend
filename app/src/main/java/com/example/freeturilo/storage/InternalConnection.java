package com.example.freeturilo.storage;

import android.content.Context;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An internal file-based connection.
 * <p>
 * Object of class implementing this interface performs low-level operations
 * in an I/O connection model based on files.
 * <p>
 * This interface should be treated as an adapter. It has been designed to
 * support internal storage file connections but could also be used for other
 * types of I/O file connections (it is used with project resource files for
 * testing purposes). For feasibility of understanding the methods will be
 * described as if they were methods used for internal storage connections.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #openFileInput
 * @see #openFileOutput
 * @see #checkFileAbsent
 * @see Builder
 * @see StorageConnection
 * @see Context#openFileInput
 * @see Context#openFileOutput
 * @see File#exists
 */
public interface InternalConnection {
    /**
     * Gets the output stream of this connection which can be later used to
     * write to the file of this connection.
     * @return                  a writeable output stream
     * @throws StorageException an exception representing an error which occurred
     *                          when trying to retrieve the output stream
     */
    OutputStream openFileOutput() throws StorageException;

    /**
     * Gets the input stream of this connection which can be later used to read
     * content of the file of this connection.
     * @return                  a readable input stream
     * @throws StorageException an exception representing an error which occurred
     *                          when trying to retrieve the input stream
     */
    InputStream openFileInput() throws StorageException;

    /**
     * Checks if the file of this connection is absent.
     * @return                  a boolean defining whether the specified file
     *                          is absent
     */
    boolean checkFileAbsent();

    /**
     * Builder of internal connections.
     * <p>
     * Object of class implementing this interface builds I/O connections
     * with custom context.
     * <p>
     * This interface should be treated as a factory for internal connections.
     * It has been designed to support internal storage connections but could
     * also be used for other types of I/O file-based connections (it is used
     * with project resource files for testing purposes). For feasibility of
     * understanding the methods will be described as if they were methods
     * used for internal storage connections.
     *
     * @author Mikołaj Terzyk
     * @version 1.0.0
     * @see #setContext
     * @see #setFilename
     * @see #create
     * @see InternalConnection
     * @see StorageConnection.Builder
     */
    interface Builder {
        /**
         * Sets the context for the created connection.
         * @param context   the context of the application providing all global
         *                  information
         * @return          this builder with set context
         */
        Builder setContext(Context context);

        /**
         * Sets the filename for the created connection.
         * @param filename  a string equal to the name of the file to be used
         *                  by the created connection.
         * @return          this builder with set filename
         */
        Builder setFilename(String filename);

        /**
         * Creates an internal connection.
         * @return          the created connection with specified context
         */
        InternalConnection create();
    }
}
