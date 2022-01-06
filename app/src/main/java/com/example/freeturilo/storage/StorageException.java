package com.example.freeturilo.storage;

import androidx.annotation.NonNull;

/**
 * An error within a storage transaction.
 * <p>
 * Object of this class is thrown within a {@code StorageTransaction} with an
 * error {@link #message}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #message
 * @see Exception
 * @see StorageTransaction
 */
public class StorageException extends Exception {
    /**
     * Stores the message which indicates the cause of the transaction's
     * failure.
     */
    public final String message;

    /**
     * Class constructor.
     * @param message      a string indicating the cause of the failure of the
     *                     transaction which was the source of this exception
     */
    public StorageException(@NonNull String message) {
        this.message = message;
    }
}
