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

/**
 * A service for internal storage data transaction.
 * <p>
 * Object of this class connects to internal storage and handles data
 * transactions of the application. It encapsulates a {@link #builder} that
 * is used to build connections to the internal storage.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #FAVOURITES_FILE
 * @see #HISTORY_FILE
 * @see #context
 * @see #builder
 * @see #ensureFileExist
 * @see #saveToFile
 * @see #loadFromFile
 * @see #saveFavourites
 * @see #loadFavourites
 * @see #saveHistory
 * @see #loadHistory
 * @see #addToHistory
 * @see StorageRunnable
 * @see StorageConnection
 */
public class StorageConnector {
    /**
     * Stores name of the internal storage file that contains a serialized list
     * of favourite locations.
     * @see Favourite
     */
    private static final String FAVOURITES_FILE = "favourites.json";
    /**
     * Stores name of the internal storage file that contains a serialized
     * history of calculated routes.
     * @see RouteParameters
     */
    private static final String HISTORY_FILE = "history.json";
    /**
     * Stores the context of the application providing all global information.
     */
    private final Context context;
    /**
     * Stores a builder of connections to the internal storage.
     */
    private final InternalConnection.Builder builder;

    /**
     * Class constructor. Sets {@code builder} to the default value
     * {@code new StorageConnection.Builder()}.
     * @param context       the context of the application providing all global
     *                      information
     */
    public StorageConnector(Context context) {
        this.context = context;
        builder = new StorageConnection.Builder();
    }

    /**
     * Class constructor.
     * @param context       the context of the application providing all global
     *                      information
     * @param builder       an internal connection builder that will be used by
     *                      this connector to build connections to the internal
     *                      storage
     */
    public StorageConnector(Context context, InternalConnection.Builder builder) {
        this.context = context;
        this.builder = builder;
    }

    /**
     * Ensures a file exists in internal storage.
     * <p>
     * Creates a storage connection, checks if the target file is absent, and
     * if it is, creates it with serialized default content.
     * @param filename          a string equal to the name of the checked file
     * @param defaultContent    an object to serialize to JSON and write to the
     *                          file if it is created
     * @param <T>               the type of the default content object
     * @return                  true
     * @throws StorageException an exception representing an error which
     *                          occurred when creating the file with default
     *                          content
     */
    @NonNull
    private <T> Boolean ensureFileExist(@NonNull String filename, @NonNull T defaultContent) throws StorageException {
        InternalConnection connection = builder.setContext(context).create();
        if (connection.checkFileAbsent(filename))
            saveToFile(filename, defaultContent);
        return true;
    }

    /**
     * Saves content to an internal storage file.
     * <p>
     * Creates a storage connection, opens the target file and overrides its
     * content with a serialized object.
     * @param filename          a string equal to the name of the target file
     * @param object            an object to serialize to JSON and write to the
     *                          file
     * @param <T>               the type of the saved object
     * @throws StorageException an exception representing an error which
     *                          occurred when writing the object to the file
     */
    private <T> void saveToFile(@NonNull String filename, @NonNull T object) throws StorageException {
        Gson gson = getFreeturiloSerializingGson();
        InternalConnection connection = builder.setContext(context).create();
        OutputStream out = connection.openFileOutput(filename);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        gson.toJson(object, object.getClass(), writer);
        try { writer.close(); }
        catch (IOException ignored) {}
    }

    /**
     * Retrieves content from an internal storage file.
     * <p>
     * Creates a storage connection, opens the source file, reads its content
     * and deserializes it to an object.
     * @param filename      a string equal to the name of the source file
     * @param typeOfObject  the type of the retrieved object
     * @param <T>           the type of the retrieved object
     * @return              an object retrieved over the connection and
     *                      deserialized from JSON
     * @throws StorageException an exception representing an error which
     *                          occurred when retrieving the object from the
     *                          file
     */
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

    /**
     * Saves a list of favourite locations to the {@code FAVOURITES_FILE}.
     * @param favourites        a list of all user-favourite locations
     * @return                  a boolean equal to true
     * @throws StorageException an exception representing an error in data
     *                          transaction
     */
    @NonNull
    private Boolean saveFavourites(@NonNull List<Favourite> favourites) throws StorageException {
        saveToFile(FAVOURITES_FILE, favourites);
        return true;
    }

    /**
     * Loads the list of favourite locations from the {@code FAVOURITES_FILE}.
     * @return                  the list of all user-favourite locations
     * @throws StorageException an exception representing an error in data
     *                          transaction
     */
    @NonNull
    private List<Favourite> loadFavourites() throws StorageException {
        return loadFromFile(FAVOURITES_FILE, new TypeToken<List<Favourite>>(){}.getType());
    }

    /**
     * Saves the history of calculated routes to the {@code HISTORY_FILE}.
     * @param history           a list of all route parameters used before by
     *                          user
     * @return                  a boolean equal to true
     * @throws StorageException an exception representing an error in data
     *                          transaction
     */
    private Boolean saveHistory(@NonNull List<RouteParameters> history) throws StorageException {
        saveToFile(HISTORY_FILE, history);
        return true;
    }

    /**
     * Loads the history of calculated routes from the {@code HISTORY_FILE}.
     * @return                  the list of all route parameters used before by
     *                          user
     * @throws StorageException an exception representing an error in data
     *                          transaction
     */
    @NonNull
    private List<RouteParameters> loadHistory() throws StorageException {
        return loadFromFile(HISTORY_FILE, new TypeToken<List<RouteParameters>>(){}.getType());
    }

    /**
     * Adds a parameters bundle to the history of calculated routes stored in
     * the {@code HISTORY_FILE}.
     * @param routeParameters   a bundle of parameters used to calculate a
     *                          route
     * @return                  a boolean equal to true
     * @throws StorageException an exception representing an error in data
     *                          transaction
     */
    @NonNull
    private Boolean addToHistory(@NonNull RouteParameters routeParameters) throws StorageException {
        List<RouteParameters> history = new ArrayList<>();
        try { history = loadHistory(); }
        catch (StorageException ignored) {}
        history.add(0, routeParameters);
        saveToFile(HISTORY_FILE, history);
        return true;
    }

    /**
     * Asynchronously ensures the {@code FAVOURITES_FILE} exists in internal
     * storage. If the file is absent, creates it with a JSON-serialized empty
     * list as initial content.
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    @NonNull
    public Thread ensureFavouritesExistAsync(@Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(() -> ensureFileExist(FAVOURITES_FILE, new ArrayList<Favourite>())).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    /**
     * Asynchronously saves a list of favourite locations to the
     * {@code FAVOURITES_FILE}.
     * @param favourites    a list of all user-favourite locations
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    @NonNull
    public Thread saveFavouritesAsync(@NonNull List<Favourite> favourites, @Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(() -> saveFavourites(favourites)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    /**
     * Asynchronously loads the list of favourite locations from the
     * {@code FAVOURITES_FILE}. The payload of this transaction contains the
     * list of all user-favourite locations.
     * @param callback      a method to be called when the transaction finishes
     *                      correctly, takes the payload as a parameter
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    @NonNull
    public Thread loadFavouritesAsync(@Nullable Callback<List<Favourite>> callback, @Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(this::loadFavourites).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    /**
     * Asynchronously ensures the {@code HISTORY_FILE} exists in internal
     * storage. If the file is absent, creates it with a JSON-serialized empty
     * list as initial content.
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    @NonNull
    public Thread ensureHistoryExistsAsync(@Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(() -> ensureFileExist(HISTORY_FILE, new ArrayList<RouteParameters>())).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    /**
     * Asynchronously saves the history of calculated routes to the
     * {@code HISTORY_FILE}.
     * @param history       a list of all route parameters used before by
     *                      user
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    @NonNull
    public Thread saveHistoryAsync(@NonNull List<RouteParameters> history, @Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(() -> saveHistory(history)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    /**
     * Asynchronously loads the history of calculated routes from the
     * {@code HISTORY_FILE}. The payload of this transaction contains the list
     * of all route parameters used before by user.
     * @param callback      a method to be called when the transaction finishes
     *                      correctly, takes the payload as a parameter
     * @param handler       a handler to handle potential errors of the
     *                      transaction
     * @return              a dedicated thread that handles the transaction
     */
    @NonNull
    public Thread loadHistoryAsync(@Nullable Callback<List<RouteParameters>> callback, @Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(this::loadHistory).setCallback(callback).setHandler(handler).toThread();
        thread.start();
        return thread;
    }

    /**
     * Asynchronously adds a parameters bundle to the history of calculated
     * routes stored in the {@code HISTORY_FILE}.
     * @param routeParameters   a bundle of parameters used to calculate a
     *                          route
     * @param handler           a handler to handle potential errors of the
     *                          transaction
     * @return                  a dedicated thread that handles the transaction
     */
    @NonNull
    public Thread addToHistoryAsync(@NonNull RouteParameters routeParameters, @Nullable StorageHandler handler) {
        Thread thread = StorageRunnable.create(() -> addToHistory(routeParameters)).setHandler(handler).toThread();
        thread.start();
        return thread;
    }
}
