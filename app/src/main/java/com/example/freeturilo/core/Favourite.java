package com.example.freeturilo.core;

import android.content.Context;

import com.google.android.gms.maps.model.MarkerOptions;
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

public class Favourite extends Location {
    public FavouriteType type;
    public static String filename = "favourites.json";

    @Override
    public MarkerOptions createMarkerOptions() {
        return null;
    }

    public static List<Favourite> loadFavourites(Context context) throws IOException {
        Gson gson = new Gson();
        FileInputStream in = context.openFileInput(filename);
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
    }

    public static void saveFavourites(Context context, List<Favourite> favourites) throws IOException {
        Gson gson = new Gson();
        FileOutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        writer.setIndent("  ");
        writer.beginArray();
        for (Favourite favourite : favourites) {
            gson.toJson(favourite, Favourite.class, writer);
        }
        writer.endArray();
        writer.close();
    }
}
