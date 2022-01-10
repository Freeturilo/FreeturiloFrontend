package com.example.freeturilo.misc;

/**
 * A callback called after an asynchronous data transaction.
 * <p>
 * Object of this class is used as a callback ran in the main thread after
 * an asynchronous data transaction finishes successfully.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see Synchronizer
 */
public interface VoidCallback {
    /**
     * Calls this callback.
     */
    void call();
}
