package com.example.freeturilo.storage;

/**
 * An internal storage data transaction.
 * <p>
 * Object of class implementing this interface performs a synchronous storage
 * transaction. Within a successful transaction there could be a payload
 * retrieved. It is also possible for the transaction to throw errors in the
 * form of {@code StorageException}.
 *
 * @param <T> the type of payload retrieved within the transaction or
 *            Boolean if no payload is retrieved
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see StorageException
 */
public interface StorageTransaction<T> {
    /**
     * Synchronously performs the storage transaction.
     *
     * @return a payload retrieved within the transaction or a boolean
     *          if no payload is retrieved
     * @throws StorageException an exception representing an error which
     *                          occurred when performing the transaction
     */
    T go() throws StorageException;
}
