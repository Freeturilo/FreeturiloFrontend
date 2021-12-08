package com.example.freeturilo.storage;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.misc.Callback;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    private static final String FAVOURITES_FILE = "favourites.json";
    private static final String HISTORY_FILE = "history.json";
    private final Context context;

    public StorageManager(Context context) {
        this.context = context;
    }

    @NonNull
    private Boolean ensureFavouritesExist() throws StorageException {
        if (!context.getFileStreamPath(FAVOURITES_FILE).exists())
            try {
                saveFavourites(new ArrayList<>());
            } catch (StorageException e) {
                throw new StorageException(context.getString(R.string.no_initialize_favourites_message));
            }
        return true;
    }

    @NonNull
    private List<Favourite> loadFavourites() throws StorageException {
        try {
            Gson gson = new Gson();
            FileInputStream in = context.openFileInput(FAVOURITES_FILE);
            JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            List<Favourite> favourites = new ArrayList<>();
            reader.beginArray();
            while (reader.hasNext()) {
                Favourite favourite = gson.fromJson(reader, Favourite.class);
                favourites.add(favourite);
            }
            reader.endArray();
            reader.close();
            return favourites;
        } catch (IOException exception) {
            String noFavouritesMessage = context.getString(R.string.no_favourites_message);
            throw new StorageException(noFavouritesMessage);
        }
    }

    @NonNull
    private Boolean saveFavourites(@NonNull List<Favourite> favourites) throws StorageException {
        try {
            Gson gson = new Gson();
            FileOutputStream out = context.openFileOutput(FAVOURITES_FILE, Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
            writer.setIndent("  ");
            writer.beginArray();
            for (Favourite favourite : favourites)
                gson.toJson(favourite, Favourite.class, writer);
            writer.endArray();
            writer.close();
            return true;
        } catch (IOException exception) {
            String noFavouritesMessage = context.getString(R.string.no_favourites_message);
            throw new StorageException(noFavouritesMessage);
        }
    }

    @NonNull
    private Boolean ensureHistoryExists() throws StorageException {
        if (!context.getFileStreamPath(HISTORY_FILE).exists())
            try {
                saveHistory(new ArrayList<>());
            } catch (StorageException e) {
                throw new StorageException(context.getString(R.string.no_initialize_history_message));
            }
        return true;
    }

    @NonNull
    private List<RouteParameters> loadHistory() throws StorageException {
        try {
            Gson gson = new Gson();
            FileInputStream in = context.openFileInput(HISTORY_FILE);
            JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            List<RouteParameters> history = new ArrayList<>();
            reader.beginArray();
            while (reader.hasNext()) {
                RouteParameters parameters = gson.fromJson(reader, RouteParameters.class);
                history.add(parameters);
            }
            reader.endArray();
            reader.close();
            return history;
        } catch (IOException exception) {
            String noHistoryMessage = context.getString(R.string.no_history_message);
            throw new StorageException(noHistoryMessage);
        }
    }

    @NonNull
    private Boolean saveHistory(@NonNull List<RouteParameters> history) throws StorageException {
        try {
            Gson gson = new Gson();
            FileOutputStream out = context.openFileOutput(HISTORY_FILE, Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
            writer.setIndent("  ");
            writer.beginArray();
            for (RouteParameters routeParameters : history) {
                gson.toJson(routeParameters, RouteParameters.class, writer);
            }
            writer.endArray();
            writer.close();
            return true;
        } catch (IOException exception) {
            String noHistoryMessage = context.getString(R.string.no_history_message);
            throw new StorageException(noHistoryMessage);
        }
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
