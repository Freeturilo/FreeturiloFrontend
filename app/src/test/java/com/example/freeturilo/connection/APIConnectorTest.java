package com.example.freeturilo.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.os.Looper;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.core.Criterion;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.Route;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.core.SystemState;
import com.example.freeturilo.json.JsonEquality;
import com.example.freeturilo.misc.AuthCredentials;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class APIConnectorTest {

    @Test
    public void getStationsAsync() throws InterruptedException {
        APIConnector apiConnector = new APIConnector(new MockConnection.Builder(200, null));
        AtomicReference<List<Station>> response = new AtomicReference<>();

        Thread thread = apiConnector.getStationsAsync(response::set, null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(6, response.get().size());
        assertEquals(2585893, response.get().get(0).id);
    }

    @Test
    public void reportStationAsync() throws InterruptedException {
        Station station = new Station("Test", 49, 51, 1, 0, 0, 0);
        APIConnector apiConnector = new APIConnector(new MockConnection.Builder(200, null));
        AtomicBoolean handlerInvoked = new AtomicBoolean(false);

        Thread thread = apiConnector.reportStationAsync(station, (e) -> handlerInvoked.set(true));
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        assertFalse(handlerInvoked.get());
    }

    @Test
    public void setBrokenStationAsync() throws InterruptedException {
        Station station = new Station("Test", 49, 51, 1, 0, 0, 0);
        APIConnector apiConnector = new APIConnector(new MockConnection.Builder(200, null));
        AtomicBoolean handlerInvoked = new AtomicBoolean(false);

        Thread thread = apiConnector.setBrokenStationAsync(station, (e) -> handlerInvoked.set(true));
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        assertFalse(handlerInvoked.get());
    }

    @Test
    public void setWorkingStationAsync() throws InterruptedException {
        Station station = new Station("Test", 49, 51, 1, 0, 0, 0);
        APIConnector apiConnector = new APIConnector(new MockConnection.Builder(200, null));
        AtomicBoolean handlerInvoked = new AtomicBoolean(false);

        Thread thread = apiConnector.setWorkingStationAsync(station, (e) -> handlerInvoked.set(true));
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        assertFalse(handlerInvoked.get());
    }

    @Test
    public void postUserAsync() throws InterruptedException {
        AuthCredentials authCredentials = new AuthCredentials("user@example.com", "password");
        InputStreamReader expectedRequestReader = new InputStreamReader(Objects.requireNonNull(
                getClass().getClassLoader()).getResourceAsStream("POSTuser_request.json"));
        JsonObject expectedRequest = JsonParser.parseReader(expectedRequestReader).getAsJsonObject();
        OutputStream requestOutputStream = new ByteArrayOutputStream();
        String expectedResponse = "uiqwehpdfiuhgq3pq9";
        AtomicReference<String> response = new AtomicReference<>();
        APIConnector apiConnector = new APIConnector(new MockConnection.Builder(200, requestOutputStream));

        Thread thread = apiConnector.postUserAsync(authCredentials, response::set, null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        JsonObject request = JsonParser.parseString(requestOutputStream.toString()).getAsJsonObject();
        assertTrue(JsonEquality.equals(expectedRequest, request));
        assertEquals(expectedResponse, response.get());
    }

    @Test
    public void getRouteAsync() throws InterruptedException {
        Location start = new Location("Mieszkanie", 52.266611, 21.045194);
        Favourite end = new Favourite("MiNI", 52.221990, 21.0070651, FavouriteType.SCHOOL);
        Location stop = new Location("Westfield Arkadia", 52.256128, 20.985926);
        List<Location> stops = Collections.singletonList(stop);
        Criterion criterion = Criterion.TIME;
        RouteParameters routeParameters = new RouteParameters(start, end, stops, criterion);
        InputStreamReader expectedRequestReader = new InputStreamReader(Objects.requireNonNull(
                getClass().getClassLoader()).getResourceAsStream("POSTroute_request.json"));
        JsonElement expectedRequest = JsonParser.parseReader(expectedRequestReader);
        OutputStream requestOutputStream = new ByteArrayOutputStream();
        AtomicReference<Route> response = new AtomicReference<>();
        APIConnector apiConnector = new APIConnector(new MockConnection.Builder(200, requestOutputStream));

        Thread thread = apiConnector.getRouteAsync(routeParameters, response::set, null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        JsonElement request = JsonParser.parseString(requestOutputStream.toString());
        Route route = response.get();
        assertTrue(JsonEquality.equals(expectedRequest, request));
        assertEquals("Mieszkanie", route.parameters.start.name);
        assertTrue(route.parameters.end instanceof Favourite);
        assertEquals(Criterion.TIME, route.parameters.criterion);
        assertEquals(4, route.fragments.size());
        assertEquals(0, route.fragments.get(0).cost, 0.000001);
        assertEquals(52.2665971, route.fragments.get(0).directionsRoute.bounds.northeast.lat, 0.000001);
        assertEquals(1, route.fragments.get(1).directionsRoute.legs.length);
        assertEquals(17, route.fragments.get(1).directionsRoute.legs[0].steps.length);
    }

    @Test
    public void getStateAsync() throws InterruptedException {
        AtomicReference<SystemState> response = new AtomicReference<>();
        APIConnector apiConnector = new APIConnector(new MockConnection.Builder(200, null));

        Thread thread = apiConnector.getStateAsync(response::set, null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(SystemState.DEMO, response.get());
    }

    @Test
    public void postStateAsync() throws InterruptedException {
        SystemState systemState = SystemState.STARTED;
        APIConnector apiConnector = new APIConnector(new MockConnection.Builder(200, null));
        AtomicBoolean handlerInvoked = new AtomicBoolean(false);

        Thread thread = apiConnector.postStateAsync(systemState, (e) -> handlerInvoked.set(true));
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        assertFalse(handlerInvoked.get());
    }

    @Test
    public void getNotifyThresholdAsync() throws InterruptedException {
        AtomicInteger response = new AtomicInteger();
        APIConnector apiConnector = new APIConnector(new MockConnection.Builder(200, null));

        Thread thread = apiConnector.getNotifyThresholdAsync(response::set, null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(7, response.get());
    }

    @Test
    public void postNotifyThresholdAsync() throws InterruptedException {
        int threshold = 8;
        APIConnector apiConnector = new APIConnector(new MockConnection.Builder(200, null));
        AtomicBoolean handlerInvoked = new AtomicBoolean(false);

        Thread thread = apiConnector.postNotifyThresholdAsync(threshold, (e) -> handlerInvoked.set(true));
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        assertFalse(handlerInvoked.get());
    }

    @Test
    public void anyEndpointAsync_invokesHandlerOnErrorCode() throws InterruptedException {
        int responseCode = 333;
        APIConnector apiConnector = new APIConnector(new MockConnection.Builder(responseCode, new ByteArrayOutputStream()));
        AtomicInteger handlerInvocations = new AtomicInteger();
        APIHandler handler = e -> { if(e.responseCode == responseCode) handlerInvocations.incrementAndGet(); };
        List<Thread> threads = new ArrayList<>();

        threads.add(apiConnector.getStationsAsync(null, handler));

        Station station = new Station("Test", 49, 51, 1, 0, 0, 0);
        threads.add(apiConnector.reportStationAsync(station, handler));
        threads.add(apiConnector.setBrokenStationAsync(station, handler));
        threads.add(apiConnector.setWorkingStationAsync(station, handler));

        AuthCredentials authCredentials = new AuthCredentials("user@example.com", "password");
        threads.add(apiConnector.postUserAsync(authCredentials, null, handler));

        Location start = new Location("Mieszkanie", 52.266611, 21.045194);
        Favourite end = new Favourite("MiNI", 52.221990, 21.0070651, FavouriteType.SCHOOL);
        Location stop = new Location("Westfield Arkadia", 52.256128, 20.985926);
        List<Location> stops = Collections.singletonList(stop);
        Criterion criterion = Criterion.TIME;
        RouteParameters routeParameters = new RouteParameters(start, end, stops, criterion);
        threads.add(apiConnector.getRouteAsync(routeParameters, null, handler));

        threads.add(apiConnector.getStateAsync(null, handler));
        threads.add(apiConnector.postStateAsync(SystemState.STARTED, handler));
        threads.add(apiConnector.getNotifyThresholdAsync(null, handler));
        threads.add(apiConnector.postNotifyThresholdAsync(8, handler));

        for (Thread thread : threads)
            thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(threads.size(), handlerInvocations.get());
    }
}