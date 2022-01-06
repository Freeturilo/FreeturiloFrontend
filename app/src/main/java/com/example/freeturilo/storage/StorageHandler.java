package com.example.freeturilo.storage;

/**
 * A handler for errors in storage transactions.
 * <p>
 * Object of a class implementing this interface handles transaction errors
 * in the form of {@code StorageException} in the {@link #handle} method.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #handle
 * @see StorageException
 * @see StorageRunnable
 */
public interface StorageHandler {
    /**
     * Handles an error in a storage transaction.
     * @param error     an error that occurred in a storage transaction
     */
    void handle(StorageException error);
}
