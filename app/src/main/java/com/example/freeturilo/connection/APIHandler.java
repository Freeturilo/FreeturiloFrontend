package com.example.freeturilo.connection;

/**
 * A handler for errors in API transactions.
 * <p>
 * Object of a class implementing this interface handles transaction errors
 * in the form of {@code APIException} in the {@link #handle} method.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #handle
 * @see APIException
 * @see APIRunnable
 */
public interface APIHandler {
    /**
     * Handles an error in an API transaction.
     * @param error     an error that occurred in an API transaction
     */
    void handle(APIException error);
}
