package com.example.freeturilo.misc;

import com.example.freeturilo.connection.APIConnector;
import com.example.freeturilo.connection.APIRunnable;
import com.example.freeturilo.storage.StorageConnector;
import com.example.freeturilo.storage.StorageRunnable;

/**
 * A callback called after an asynchronous data transaction.
 * <p>
 * Object of this class is used as a callback ran in the main thread after
 * an asynchronous data transaction finishes successfully.
 * @param <T>       type of the transaction payload if any is received or
 *                  retrieved, usually {@code Boolean} or {@code Integer}
 *                  if not
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see APIRunnable
 * @see APIConnector
 * @see StorageRunnable
 * @see StorageConnector
 */
public interface Callback<T> {
    /**
     * Calls this callback.
     * @param result    payload of the data transaction or anything if no
     *                  payload is received or retrieved
     */
    void call(T result);
}
