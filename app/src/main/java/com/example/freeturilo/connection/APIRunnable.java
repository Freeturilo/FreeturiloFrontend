package com.example.freeturilo.connection;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.misc.Callback;

/**
 * A runnable performing an asynchronous API data transaction.
 * <p>
 * Object of this class performs an asynchronous {@link #transaction} with
 * a {@link #callback} and an error {@link #handler}. It can be easily
 * customized and wrapped in a {@code Thread}.
 * @param <T>   the type of payload received within the transaction
 *              or Integer if no payload is received
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see Runnable
 * @see APITransaction
 * @see Callback
 * @see APIHandler
 * @see Thread
 * @see APIConnector
 */
public class APIRunnable<T> implements Runnable {
    /**
     * Stores the API data transaction which is performed when this runnable is
     * ran.
     */
    private final APITransaction<T> transaction;
    /**
     * Stores the callback which is called in the main thread when the
     * transaction is performed without errors.
     */
    private Callback<T> callback;
    /**
     * Stores the handler which handles errors of the transaction in the main
     * thread.
     */
    private APIHandler handler;

    /**
     * Class constructor.
     * @param transaction   an API transaction to be ran within a run of
     *                      this runnable
     */
    private APIRunnable(@NonNull APITransaction<T> transaction) {
        this.transaction = transaction;
    }

    /**
     * Runs the {@code transaction} synchronously. If set, calls the
     * {@code callback} in the main thread on successful transaction finish.
     * Otherwise, if set, handles transaction errors with {@code handler} in
     * the main thread.
     */
    @Override
    public void run() {
        try {
            T result = transaction.go();
            if (callback != null)
                new Handler(Looper.getMainLooper()).post(() -> callback.call(result));
        } catch (APIException e) {
            if (handler != null)
                new Handler(Looper.getMainLooper()).post(() -> handler.handle(e));
        }
    }

    /**
     * Creates a new {@code APIRunnable}.
     * @param transaction       a transaction to be ran within the created
     *                          runnable
     * @param <Q>               the type of payload received within the
     *                          transaction
     * @return                  a runnable running the specified transaction
     */
    @NonNull
    public static <Q> APIRunnable<Q> create(@NonNull APITransaction<Q> transaction) {
        return new APIRunnable<>(transaction);
    }

    /**
     * Sets the callback of this runnable.
     * @param callback      a callback to be called on successful transaction
     *                      finish
     * @return              this runnable with callback set
     */
    @NonNull
    public APIRunnable<T> setCallback(@Nullable Callback<T> callback) {
        this.callback = callback;
        return this;
    }

    /**
     * Sets the handler of this runnable.
     * @param handler       a handler to handle transaction errors
     * @return              this runnable with handler set
     */
    @NonNull
    public APIRunnable<T> setHandler(@Nullable APIHandler handler) {
        this.handler = handler;
        return this;
    }

    /**
     * Wraps this runnable in a thread.
     * @return              a thread with this runnable set as the target
     * @see Thread
     */
    @NonNull
    public Thread toThread() {
        return new Thread(this);
    }

}
