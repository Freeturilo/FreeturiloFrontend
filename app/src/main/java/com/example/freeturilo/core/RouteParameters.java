package com.example.freeturilo.core;

import android.content.Context;

import com.example.freeturilo.handlers.ExceptionHandler;
import com.example.freeturilo.handlers.IgnoreExceptionHandler;
import com.example.freeturilo.R;
import com.example.freeturilo.handlers.ToastExceptionHandler;
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

    public RouteParameters(Location start, Location end, List<Location> stops, Criterion criterion){
        this.start = start;
        this.end = end;
        this.stops = new ArrayList<>(stops);
        this.criterion = criterion;
    }

    private static List<RouteParameters> loadHistory(Context context) throws IOException
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

    public static List<RouteParameters> loadHistorySafe(Context context, ExceptionHandler handler)
    {
        try {
            return RouteParameters.loadHistory(context);
        }
        catch (IOException exception) {
            handler.handle();
            return new ArrayList<>();
        }
    }

    private static void saveHistory(Context context, List<RouteParameters> history) throws IOException
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

    public static void saveHistorySafe(Context context, List<RouteParameters> history, ExceptionHandler handler)
    {
        try {
            saveHistory(context, history);
        }
        catch (IOException exception) {
            handler.handle();
        }
    }

    public static void addToHistorySafe(Context context, RouteParameters routeParameters)
    {
        List<RouteParameters> history = loadHistorySafe(context, new IgnoreExceptionHandler());
        history.add(0, routeParameters);
        saveHistorySafe(context, history, new ToastExceptionHandler(context, R.string.no_history_message));
    }
}
