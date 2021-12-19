package com.example.freeturilo.storage;

import static com.example.freeturilo.json.FreeturiloGson.getFreeturiloDeserializingGson;
import static com.example.freeturilo.json.FreeturiloGson.getFreeturiloSerializingGson;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
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
    private Boolean ensureFavouritesExist() throws StorageException {
        InternalConnection connection = builder.setContext(context).create();
        if (connection.checkFileAbsent(FAVOURITES_FILE))
            try {
                saveFavourites(new ArrayList<>());
            } catch (StorageException e) {
                throw new StorageException(context.getString(R.string.no_initialize_favourites_message));
            }
        return true;
    }

    @NonNull
    private List<Favourite> loadFavourites() throws StorageException {
        Gson gson = getFreeturiloDeserializingGson();
        InternalConnection connection = builder.setContext(context).create();
        InputStream in = connection.openFileInput(FAVOURITES_FILE);
        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        List<Favourite> favourites = gson.fromJson(reader, new TypeToken<List<Favourite>>(){}.getType());
        try { reader.close(); }
        catch (IOException ignored) {}
        return favourites;
    }

    @NonNull
    private Boolean saveFavourites(@NonNull List<Favourite> favourites) throws StorageException {
        Gson gson = getFreeturiloSerializingGson();
        InternalConnection connection = builder.setContext(context).create();
        OutputStream out = connection.openFileOutput(FAVOURITES_FILE);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        gson.toJson(favourites, new TypeToken<List<Favourite>>(){}.getType(), writer);
        try { writer.close(); }
        catch (IOException ignored) {}
        return true;
    }

    @NonNull
    private Boolean ensureHistoryExists() throws StorageException {
        InternalConnection connection = builder.setContext(context).create();
        if (connection.checkFileAbsent(HISTORY_FILE))
            try {
                saveHistory(new ArrayList<>());
            } catch (StorageException e) {
                throw new StorageException(context.getString(R.string.no_initialize_history_message));
            }
        return true;
    }

    @NonNull
    private List<RouteParameters> loadHistory() throws StorageException {
        Gson gson = getFreeturiloDeserializingGson();
        InternalConnection connection = builder.setContext(context).create();
        InputStream in = connection.openFileInput(HISTORY_FILE);
        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        List<RouteParameters> history = gson.fromJson(reader, new TypeToken<List<RouteParameters>>(){}.getType());
        try { reader.close(); }
        catch (IOException ignored) {}
        return history;
    }

    @NonNull
    private Boolean saveHistory(@NonNull List<RouteParameters> history) throws StorageException {
        Gson gson = getFreeturiloSerializingGson();
        InternalConnection connection = builder.setContext(context).create();
        OutputStream out = connection.openFileOutput(HISTORY_FILE);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        gson.toJson(history, new TypeToken<List<RouteParameters>>(){}.getType(), writer);
        try { writer.close(); }
        catch (IOException ignored) {}
        return true;
    }

    @NonNull
    private Boolean addToHistory(@NonNull RouteParameters routeParameters) throws StorageException {
        List<RouteParameters> history = new ArrayList<>();
        try { history = loadHistory(); }
        catch (StorageException ignored) {}
        history.add(0, routeParameters);
        saveHistory(history);
        return true;
    }

    public void ensureFavouritesExistAsync(@Nullable StorageHandler handler) {
        StorageRunnable.create(this::ensureFavouritesExist).setHandler(handler).startThread();
    }

    public void loadFavouritesAsync(@Nullable Callback<List<Favourite>> callback, @Nullable StorageHandler handler) {
        StorageRunnable.create(this::loadFavourites).setCallback(callback).setHandler(handler).startThread();
    }

    public void saveFavouritesAsync(@NonNull List<Favourite> favourites, @Nullable StorageHandler handler) {
        StorageRunnable.create(() -> saveFavourites(favourites)).setHandler(handler).startThread();
    }

    public void ensureHistoryExistsAsync(@Nullable StorageHandler handler) {
        StorageRunnable.create(this::ensureHistoryExists).setHandler(handler).startThread();
    }

    public void loadHistoryAsync(@Nullable Callback<List<RouteParameters>> callback, @Nullable StorageHandler handler) {
        StorageRunnable.create(this::loadHistory).setCallback(callback).setHandler(handler).startThread();
    }

    public void saveHistoryAsync(@NonNull List<RouteParameters> history, @Nullable StorageHandler handler) {
        StorageRunnable.create(() -> saveHistory(history)).setHandler(handler).startThread();
    }

    public void addToHistoryAsync(@NonNull RouteParameters routeParameters, @Nullable StorageHandler handler) {
        StorageRunnable.create(() -> addToHistory(routeParameters)).setHandler(handler).startThread();
    }
}
