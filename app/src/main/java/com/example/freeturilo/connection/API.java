package com.example.freeturilo.connection;

import com.example.freeturilo.core.Route;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.core.SystemState;
import com.example.freeturilo.misc.AuthCredentials;
import com.example.freeturilo.misc.Callback;

import java.util.List;

/**
 * A service for API data transaction.
 * <p>
 * Object of a class implementing this interface connects to an API and handles
 * external data transactions of the application. Every method within this
 * interface should be implemented as asynchronous and return the thread which
 * handles the method's logic. An {@code APIHandler} can be provided (to handle
 * potential transaction errors) in every method. A {@code Callback} can be
 * provided (to handle received payload) in all methods that are expected to
 * receive a payload. It is recommended to use {@code APIRunnable} for the
 * method's implementation as it makes it easy to use {@code APIHandler} and
 * {@code Callback} classes.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see Thread
 * @see Callback
 * @see APIHandler
 */
public interface API {
    /**
     * Asynchronously gets a list of all bike stations. The payload of
     * this transaction should contain the list of all bike stations.
     * @param callback      a method to be called when the transaction finishes
     *                      correctly, takes the payload as a parameter
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    Thread getStationsAsync(Callback<List<Station>> callback, APIHandler handler);

    /**
     * Asynchronously reports a bike station.
     * @param station       the station to be reported
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    Thread reportStationAsync(Station station, APIHandler handler);

    /**
     * Asynchronously sets a bike station state to broken. This transaction
     * should only be accessible to an administrator.
     * @param station       the station to be set as broken
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    Thread setBrokenStationAsync(Station station, APIHandler handler);

    /**
     * Asynchronously sets a bike station state to working. This transaction
     * should only be accessible to an administrator.
     * @param station       the station to be set as working
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    Thread setWorkingStationAsync(Station station, APIHandler handler);

    /**
     * Asynchronously performs an administrator authentication. The payload of
     * this transaction should contain an auth token to save in AuthTools.
     * @param authCredentials   a bundle of authentication credentials for
     *                          an administrator account
     * @param callback          a method to be called when the transaction
     *                          finishes correctly, takes the payload as a
     *                          parameter
     * @param handler           a handler to handle potential errors of the
     *                          transaction
     * @return                  a dedicated thread that handles the transaction
     */
    Thread postUserAsync(AuthCredentials authCredentials, Callback<String> callback, APIHandler handler);

    /**
     * Asynchronously calculates a route with given parameters. The payload of
     * this transaction should contain the calculated route.
     * @param routeParameters   a bundle of parameters to be used in route
     *                          calculation
     * @param callback          a method to be called when the transaction
     *                          finishes correctly, takes the payload as a
     *                          parameter
     * @param handler           a handler to handle potential errors of the
     *                          transaction
     * @return                  a dedicated thread that handles the transaction
     */
    Thread getRouteAsync(RouteParameters routeParameters, Callback<Route> callback, APIHandler handler);

    /**
     * Asynchronously gets the system state. This transaction should only be
     * accessible to an administrator. The payload of this transaction should
     * contain the current system state.
     * @param callback      a method to be called when the transaction
     *                      finishes correctly, takes the payload as a
     *                      parameter
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    Thread getStateAsync(Callback<SystemState> callback, APIHandler handler);

    /**
     * Asynchronously updates the system state. This transaction should only be
     * accessible to an administrator.
     * @param systemState   the value of system state to be set
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    Thread postStateAsync(SystemState systemState, APIHandler handler);

    /**
     * Asynchronously gets the threshold for administrator mail notifications.
     * This method should only be accessible to an administrator. The payload
     * of this transaction should contain the value of the threshold.
     * @param callback      a method to be called when the transaction
     *                      finishes correctly, takes the payload as a
     *                      parameter
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    Thread getNotifyThresholdAsync(Callback<Integer> callback, APIHandler handler);

    /**
     * Asynchronously updates the threshold for administrator mail
     * notifications. This method should only be accessible to an
     * administrator.
     * @param threshold     the value of the threshold to be set
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    Thread postNotifyThresholdAsync(int threshold, APIHandler handler);
}
