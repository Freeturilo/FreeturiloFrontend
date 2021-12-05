package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.handlers.ExceptionHandler;
import com.example.freeturilo.R;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RouteParameters implements Serializable {
    public final Location start;
    public final Location end;
    public final List<Location> stops;
    public final Criterion criterion;

    public RouteParameters(@NonNull Location start, @NonNull Location end,
                           @NonNull List<Location> stops, @NonNull Criterion criterion){
        this.start = start;
        this.end = end;
        this.stops = new ArrayList<>(stops);
        this.criterion = criterion;
    }

    @NonNull
    private static List<RouteParameters> loadHistory(@NonNull Context context) throws IOException
    {
        Gson gson = new Gson();
        FileInputStream in = context.openFileInput(context.getString(R.string.history_filename));
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
    }

    @NonNull
    public static List<RouteParameters> loadHistorySafe(@NonNull Context context,
                                                        @Nullable ExceptionHandler handler)
    {
        try {
            return RouteParameters.loadHistory(context);
        }
        catch (IOException exception) {
            if (handler != null)
                handler.handle();
            return new ArrayList<>();
        }
    }

    private static void saveHistory(@NonNull Context context,
                                    @NonNull List<RouteParameters> history) throws IOException
    {
        Gson gson = new Gson();
        FileOutputStream out = context.openFileOutput(context.getString(R.string.history_filename), Context.MODE_PRIVATE);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        writer.setIndent("  ");
        writer.beginArray();
        for (RouteParameters routeParameters : history) {
            gson.toJson(routeParameters, RouteParameters.class, writer);
        }
        writer.endArray();
        writer.close();
    }

    public static void saveHistorySafe(@NonNull Context context,
                                       @NonNull List<RouteParameters> history,
                                       @Nullable ExceptionHandler handler)
    {
        try {
            saveHistory(context, history);
        }
        catch (IOException exception) {
            if (handler != null)
                handler.handle();
        }
    }

    public static void addToHistorySafe(@NonNull Context context,
                                        @NonNull RouteParameters routeParameters,
                                        @Nullable ExceptionHandler handler)
    {
        List<RouteParameters> history = loadHistorySafe(context, null);
        history.add(0, routeParameters);
        saveHistorySafe(context, history, handler);
    }
}
