package com.example.freeturilo.storage;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.misc.Callback;

/**
 * A runnable performing an asynchronous storage data transaction.
 * <p>
 * Object of this class performs an asynchronous {@link #transaction} with
 * a {@link #callback} and an error {@link #handler}. It can be easily
 * customized and wrapped in a {@code Thread}.
 * @param <T>   the type of payload retrieved within the transaction
 *              or Boolean if no payload is retrieved
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #transaction
 * @see #callback
 * @see #handler
 * @see #run
 * @see #create
 * @see #setCallback
 * @see #setHandler
 * @see #toThread
 * @see Runnable
 * @see StorageTransaction
 * @see Callback
 * @see StorageHandler
 * @see Thread
 * @see StorageConnector
 */
public class StorageRunnable<T> implements Runnable {
    /**
     * Stores the storage data transaction which is performed when this
     * runnable is ran.
     */
    private final StorageTransaction<T> transaction;
    /**
     * Stores the callback which is called in the main thread when the
     * transaction is performed without errors.
     */
    private Callback<T> callback;
    /**
     * Stores the handler which handles errors of the transaction in the main
     * thread.
     */
    private StorageHandler handler;

    /**
     * Class constructor.
     * @param transaction   a storage transaction to be ran within a run of
     *                      this runnable
     */
    public StorageRunnable(@NonNull StorageTransaction<T> transaction) {
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
        } catch (StorageException e) {
            if (handler != null)
                new Handler(Looper.getMainLooper()).post(() -> handler.handle(e));
        }
    }

    /**
     * Creates a new {@code StorageRunnable}.
     * @param transaction       a transaction to be ran within the created
     *                          runnable
     * @param <Q>               the type of payload retrieved within the
     *                          transaction
     * @return                  a runnable running the specified transaction
     */
    @NonNull
    public static <Q> StorageRunnable<Q> create(@NonNull StorageTransaction<Q> transaction) {
        return new StorageRunnable<>(transaction);
    }

    /**
     * Sets the callback of this runnable.
     * @param callback      a callback to be called on successful transaction
     *                      finish
     * @return              this runnable with callback set
     */
    @NonNull
    public StorageRunnable<T> setCallback(@Nullable Callback<T> callback) {
        this.callback = callback;
        return this;
    }

    /**
     * Sets the handler of this runnable.
     * @param handler       a handler to handle transaction errors
     * @return              this runnable with handler set
     */
    @NonNull
    public StorageRunnable<T> setHandler(@Nullable StorageHandler handler) {
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
