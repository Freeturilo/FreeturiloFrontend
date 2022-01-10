package com.example.freeturilo.misc;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An executor of a callback after asynchronous methods finish.
 * <p>
 * An object of this class executes a synchronous {@link #callback} when a set
 * of synchronous or asynchronous methods decrement its {@link #counter} to
 * zero. It is usually used when multiple asynchronous methods are ran at once
 * and all their results are then used for a synchronous method. An example of
 * this scenario is simultaneously downloading a map and locations to be placed
 * on the map. After the download the locations get synchronously placed on the
 * map.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #counter
 * @see #callback
 * @see #decrement
 * @see AtomicInteger
 */
public class Synchronizer {
    /**
     * Stores the counter that is decremented by methods when they finish.
     */
    private final AtomicInteger counter;
    /**
     * Stores the method to be called when counter reaches zero.
     */
    private final VoidCallback callback;

    /**
     * Class constructor.
     * @param initialCounter    an integer equal to the initial value of the
     *                          counter, usually equal to the number of methods
     *                          that decrement the counter after finishing
     * @param callback          a method to be called when counter reaches zero
     * @see #decrement
     */
    public Synchronizer(int initialCounter, @NonNull VoidCallback callback) {
        counter = new AtomicInteger(initialCounter);
        this.callback = callback;
    }

    /**
     * Decrements the {@link #counter} and calls callback if it reaches zero.
     */
    public void decrement() {
        if (counter.decrementAndGet() == 0)
            callback.call();
    }
}
