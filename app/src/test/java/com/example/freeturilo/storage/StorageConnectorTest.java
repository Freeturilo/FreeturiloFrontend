package com.example.freeturilo.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.os.Looper;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.core.Criterion;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.json.JsonEquality;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.LooperMode;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class StorageConnectorTest {

    @Test
    public void ensureFavouritesExistAsync_forAbsent() throws InterruptedException {
        Context context = RuntimeEnvironment.getApplication();
        OutputStream outputStream = new ByteArrayOutputStream();
        StorageConnector storageConnector = new StorageConnector(context,
                new MockConnection.Builder(outputStream, "None", true));

        Thread thread = storageConnector.ensureFavouritesExistAsync(null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        JsonElement saved = JsonParser.parseString(outputStream.toString());
        assertTrue(saved.isJsonArray());
        assertEquals(0, saved.getAsJsonArray().size());
    }

    @Test
    public void ensureFavouritesExistAsync_forExisting() throws InterruptedException {
        Context context = RuntimeEnvironment.getApplication();
        OutputStream outputStream = new ByteArrayOutputStream();
        StorageConnector storageConnector = new StorageConnector(context,
                new MockConnection.Builder(outputStream, "None"));

        Thread thread = storageConnector.ensureFavouritesExistAsync(null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        assertTrue(outputStream.toString().isEmpty());
    }

    @Test
    public void loadFavouritesAsync() throws InterruptedException {
        Context context = RuntimeEnvironment.getApplication();
        String filename = "INTERNALfavourites_loaded.json";
        AtomicReference<List<Favourite>> load = new AtomicReference<>();
        StorageConnector storageConnector = new StorageConnector(context,
                new MockConnection.Builder(null, filename));

        Thread thread = storageConnector.loadFavouritesAsync(load::set, null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        List<Favourite> favourites = load.get();
        assertEquals(3, favourites.size());
        assertEquals("Mieszkanie", favourites.get(0).name);
        assertEquals(FavouriteType.SCHOOL, favourites.get(1).type);
    }

    @Test
    public void saveFavouritesAsync() throws InterruptedException {
        Context context = RuntimeEnvironment.getApplication();
        Favourite home = new Favourite("Mieszkanie", 52.2468349, 20.9981223, FavouriteType.HOME);
        Favourite school = new Favourite("SGH", 52.208901, 21.0085756, FavouriteType.SCHOOL);
        List<Favourite> favourites = Arrays.asList(home, school);
        InputStreamReader expectedSavedReader = new InputStreamReader(Objects.requireNonNull(
                getClass().getClassLoader()).getResourceAsStream("INTERNALfavourites_saved.json"));
        JsonElement expectedSaved = JsonParser.parseReader(expectedSavedReader);
        OutputStream outputStream = new ByteArrayOutputStream();
        StorageConnector storageConnector = new StorageConnector(context,
                new MockConnection.Builder(outputStream, "None"));

        Thread thread = storageConnector.saveFavouritesAsync(favourites, null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        JsonElement saved = JsonParser.parseString(outputStream.toString());
        assertTrue(JsonEquality.equals(expectedSaved, saved));
    }

    @Test
    public void ensureHistoryExistsAsync_forAbsent() throws InterruptedException {
        Context context = RuntimeEnvironment.getApplication();
        OutputStream outputStream = new ByteArrayOutputStream();
        StorageConnector storageConnector = new StorageConnector(context,
                new MockConnection.Builder(outputStream, "None", true));

        Thread thread = storageConnector.ensureHistoryExistsAsync(null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        JsonElement saved = JsonParser.parseString(outputStream.toString());
        assertTrue(saved.isJsonArray());
        assertEquals(0, saved.getAsJsonArray().size());
    }

    @Test
    public void ensureHistoryExistsAsync_forExisting() throws InterruptedException {
        Context context = RuntimeEnvironment.getApplication();
        OutputStream outputStream = new ByteArrayOutputStream();
        StorageConnector storageConnector = new StorageConnector(context,
                new MockConnection.Builder(outputStream, "None"));

        Thread thread = storageConnector.ensureHistoryExistsAsync(null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        assertTrue(outputStream.toString().isEmpty());
    }

    @Test
    public void loadHistoryAsync() throws InterruptedException {
        Context context = RuntimeEnvironment.getApplication();
        String filename = "INTERNALhistory_loaded.json";
        AtomicReference<List<RouteParameters>> load = new AtomicReference<>();
        StorageConnector storageConnector = new StorageConnector(context,
                new MockConnection.Builder(null, filename));

        Thread thread = storageConnector.loadHistoryAsync(load::set, null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        List<RouteParameters> history = load.get();
        assertEquals(2, history.size());
        assertEquals(1, history.get(0).stops.size());
        assertEquals("SGH", history.get(0).end.name);
        assertTrue(history.get(0).start instanceof Station);
        assertTrue(history.get(0).end instanceof Favourite);
    }

    @Test
    public void saveHistoryAsync() throws InterruptedException {
        Context context = RuntimeEnvironment.getApplication();
        Location start = new Location("Mieszkanie", 52.266611, 21.045194);
        Favourite end = new Favourite("MiNI", 52.221990, 21.0070651, FavouriteType.SCHOOL);
        Location stop = new Location("Westfield Arkadia", 52.256128, 20.985926);
        List<Location> stops = Collections.singletonList(stop);
        Criterion criterion = Criterion.TIME;
        RouteParameters routeParameters = new RouteParameters(start, end, stops, criterion);
        List<RouteParameters> history = Collections.singletonList(routeParameters);
        InputStreamReader expectedSavedReader = new InputStreamReader(Objects.requireNonNull(
                getClass().getClassLoader()).getResourceAsStream("INTERNALhistory_saved.json"));
        JsonElement expectedSaved = JsonParser.parseReader(expectedSavedReader);
        OutputStream outputStream = new ByteArrayOutputStream();
        StorageConnector storageConnector = new StorageConnector(context,
                new MockConnection.Builder(outputStream, "None"));

        Thread thread = storageConnector.saveHistoryAsync(history, null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        JsonElement saved = JsonParser.parseString(outputStream.toString());
        assertTrue(JsonEquality.equals(expectedSaved, saved));
    }

    @Test
    public void addToHistoryAsync() throws InterruptedException {
        Context context = RuntimeEnvironment.getApplication();
        Location start = new Location("Mieszkanie", 52.266611, 21.045194);
        Favourite end = new Favourite("MiNI", 52.221990, 21.0070651, FavouriteType.SCHOOL);
        Location stop = new Location("Westfield Arkadia", 52.256128, 20.985926);
        List<Location> stops = Collections.singletonList(stop);
        Criterion criterion = Criterion.TIME;
        RouteParameters routeParameters = new RouteParameters(start, end, stops, criterion);
        InputStreamReader expectedSavedReader = new InputStreamReader(Objects.requireNonNull(
                getClass().getClassLoader()).getResourceAsStream("INTERNALhistory_added.json"));
        JsonElement expectedSaved = JsonParser.parseReader(expectedSavedReader);
        OutputStream outputStream = new ByteArrayOutputStream();
        String filename = "INTERNALhistory_loaded.json";
        StorageConnector storageConnector = new StorageConnector(context,
                new MockConnection.Builder(outputStream, filename));

        Thread thread = storageConnector.addToHistoryAsync(routeParameters, null);
        thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        JsonElement saved = JsonParser.parseString(outputStream.toString());
        assertTrue(JsonEquality.equals(expectedSaved, saved));
    }

    @Test
    public void anyMethodAsync_invokesHandlerOnError() throws InterruptedException {
        Context context = RuntimeEnvironment.getApplication();
        StorageConnector storageConnector = new StorageConnector(context, new MockConnection.Builder(true));
        AtomicInteger handlerInvocations = new AtomicInteger();
        StorageHandler handler = e -> { if(e.message.equals("Test")) handlerInvocations.incrementAndGet(); };
        List<Thread> threads = new ArrayList<>();

        threads.add(storageConnector.ensureFavouritesExistAsync(handler));
        threads.add(storageConnector.loadFavouritesAsync(null, handler));

        Favourite home = new Favourite("Mieszkanie", 52.2468349, 20.9981223, FavouriteType.HOME);
        Favourite school = new Favourite("SGH", 52.208901, 21.0085756, FavouriteType.SCHOOL);
        List<Favourite> favourites = Arrays.asList(home, school);
        threads.add(storageConnector.saveFavouritesAsync(favourites, handler));

        threads.add(storageConnector.ensureHistoryExistsAsync(handler));
        threads.add(storageConnector.loadHistoryAsync(null, handler));

        Location start = new Location("Mieszkanie", 52.266611, 21.045194);
        Favourite end = new Favourite("MiNI", 52.221990, 21.0070651, FavouriteType.SCHOOL);
        Location stop = new Location("Westfield Arkadia", 52.256128, 20.985926);
        List<Location> stops = Collections.singletonList(stop);
        Criterion criterion = Criterion.TIME;
        RouteParameters routeParameters = new RouteParameters(start, end, stops, criterion);
        List<RouteParameters> history = Collections.singletonList(routeParameters);
        threads.add(storageConnector.saveHistoryAsync(history, handler));
        threads.add(storageConnector.addToHistoryAsync(routeParameters, handler));

        for (Thread thread : threads)
            thread.join();
        shadowOf(Looper.getMainLooper()).idle();

        assertEquals(threads.size(), handlerInvocations.get());
    }
}