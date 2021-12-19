package com.example.freeturilo.storage;

import static com.example.freeturilo.json.FreeturiloGson.getFreeturiloDeserializingGson;
import static com.example.freeturilo.json.FreeturiloGson.getFreeturiloSerializingGson;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.misc.Callback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StorageConnector {
    private static final String FAVOURITES_FILE = "favourites.json";
    private static final String HISTORY_FILE = "history.json";
    private final Context context;
    private final InternalConnection.Builder builder;

    public StorageConnector(Context context) {
        this.context = context;
        builder = new StorageConnection.Builder();
    }

    public StorageConnector(Context context, InternalConnection.Builder builder) {
        this.context = context;
        this.builder = builder;
    }

    @NonNull
    private <T> Boolean ensureFileExist(@NonNull String filename, @NonNull T defaultContent) throws StorageException {
        InternalConnection connection = builder.setContext(context).create();
        if (connection.checkFileAbsent(filename))
            saveToFile(filename, defaultContent);
        return true;
    }

    private <T> void saveToFile(@NonNull String filename, @NonNull T object) throws StorageException {
        Gson gson = getFreeturiloSerializingGson();
        InternalConnection connection = builder.setContext(context).create();
        OutputStream out = connection.openFileOutput(filename);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        gson.toJson(object, object.getClass(), writer);
        try { writer.close(); }
        catch (IOException ignored) {}
    }

    @NonNull
    private <T> T loadFromFile(@NonNull String filename, @NonNull Type typeOfObject) throws StorageException {
        Gson gson = getFreeturiloDeserializingGson();
        InternalConnection connection = builder.setContext(context).create();
        InputStream in = connection.openFileInput(filename);
        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        T object = gson.fromJson(reader, typeOfObject);
        try { reader.close(); }
        catch (IOException ignored) {}
        return object;
    }

    @NonNull
    private Boolean saveFavourites(@NonNull List<Favourite> favourites) throws StorageException {
        saveToFile(FAVOURITES_FILE, favourites);
        return true;
    }

    @NonNull
    private List<Favourite> loadFavourites() throws StorageException {
        return loadFromFile(FAVOURITES_FILE, new TypeToken<List<Favourite>>(){}.getType());
    }

    private Boolean saveHistory(@NonNull List<RouteParameters> history) throws StorageException {
        saveToFile(HISTORY_FILE, history);
        return true;
    }

    @NonNull
    private List<RouteParameters> loadHistory() throws StorageException {
        return loadFromFile(HISTORY_FILE, new TypeToken<List<RouteParameters>>(){}.getType());
    }

    @NonNull
    private Boolean addToHistory(@NonNull RouteParameters routeParameters) throws StorageException {
        List<RouteParameters> history = new ArrayList<>();
        try { history = loadHistory(); }
        catch (StorageException ignored) {}
        history.add(0, routeParameters);
        saveToFile(HISTORY_FILE, history);
        return true;
    }

    @NonNull
    public Thread ensureFavouritesExistAsync(@Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(() -> ensureFileExist(FAVOURITES_FILE, new ArrayList<Favourite>())).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread loadFavouritesAsync(@Nullable Callback<List<Favourite>> callback, @Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(this::loadFavourites).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread saveFavouritesAsync(@NonNull List<Favourite> favourites, @Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(() -> saveFavourites(favourites)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread ensureHistoryExistsAsync(@Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(() -> ensureFileExist(HISTORY_FILE, new ArrayList<RouteParameters>())).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread loadHistoryAsync(@Nullable Callback<List<RouteParameters>> callback, @Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(this::loadHistory).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread saveHistoryAsync(@NonNull List<RouteParameters> history, @Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(() -> saveHistory(history)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    @NonNull
    public Thread addToHistoryAsync(@NonNull RouteParameters routeParameters, @Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(() -> addToHistory(routeParameters)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }
}
