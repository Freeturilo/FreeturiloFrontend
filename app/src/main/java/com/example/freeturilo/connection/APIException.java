package com.example.freeturilo.connection;

/**
 * An error within an API transaction.
 * <p>
 * Object of this class is thrown within an {@code APITransaction} with a
 * {@link #responseCode}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see Exception
 * @see APITransaction
 */
public class APIException extends Exception {
    /**
     * Stores the response code which indicates the cause of the transaction's
     * failure.
     */
    public final int responseCode;

    /**
     * Class constructor.
     * @param responseCode      an integer indicating the cause of the failure
     *                          of the transaction which was the source of this
     *                          exception
     */
    public APIException(int responseCode) {
        this.responseCode = responseCode;
    }
}
