package com.example.freeturilo.connection;

import com.example.freeturilo.core.Route;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.core.SystemState;
import com.example.freeturilo.misc.AuthCredentials;
import com.example.freeturilo.misc.Callback;

import java.util.List;

public interface API {
    Thread getStationsAsync(Callback<List<Station>> callback, APIHandler handler);

    Thread reportStationAsync(Station station, APIHandler handler);

    Thread setBrokenStationAsync(Station station, APIHandler handler);

    Thread setWorkingStationAsync(Station station, APIHandler handler);

    Thread postUserAsync(AuthCredentials authCredentials, Callback<String> callback, APIHandler handler);

    Thread getRouteAsync(RouteParameters routeParameters, Callback<Route> callback, APIHandler handler);

    Thread getStateAsync(Callback<SystemState> callback, APIHandler handler);

    Thread postStateAsync(SystemState systemState, APIHandler handler);

    Thread getNotifyThresholdAsync(Callback<Integer> callback, APIHandler handler);

    Thread postNotifyThresholdAsync(int threshold, APIHandler handler);
}
