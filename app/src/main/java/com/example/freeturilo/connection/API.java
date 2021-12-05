package com.example.freeturilo.connection;

import com.example.freeturilo.core.Route;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.misc.Callback;

import java.util.List;

public interface API {
    void getStationsAsync(Callback<List<Station>> callback, APIHandler handler);

    void reportStationAsync(Station station, APIHandler handler);

    void setBrokenStationAsync(Station station, APIHandler handler);

    void setWorkingStationAsync(Station station, APIHandler handler);

    void postUserAsync(String email, String password, Callback<String> callback, APIHandler handler);

    void getRouteAsync(RouteParameters routeParameters, Callback<Route> callback, APIHandler handler);

    void postStateStopAsync(APIHandler handler);

    void postStateStartAsync(APIHandler handler);

    void postStateDemoAsync(APIHandler handler);

    void postNotifyThresholdAsync(int threshold, APIHandler handler);
}
