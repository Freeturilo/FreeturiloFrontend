package com.example.freeturilo.connection;

/**
 * An external API data transaction.
 * <p>
 * Object of class implementing this interface performs a synchronous API
 * transaction. Within a successful transaction there could be a payload
 * received. It is also possible for the transaction to throw errors in the
 * form of {@code APIException}.
 *
 * @param <T> the type of payload received within the transaction or
 *            Integer if no payload is received
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #go
 * @see APIException
 */
public interface APITransaction<T> {
    /**
     * Synchronously performs the API transaction.
     *
     * @return a payload received within the transaction or an integer
     * response code if no payload is received
     * @throws APIException an exception representing an error which
     *                      occurred when performing the transaction
     */
    T go() throws APIException;
}
