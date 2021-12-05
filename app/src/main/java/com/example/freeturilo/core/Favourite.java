package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.handlers.ExceptionHandler;
import com.example.freeturilo.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.Locale;

public class Favourite extends Location {
    public FavouriteType type;

    public Favourite(@NonNull String name, double latitude,
                     double longitude, @NonNull FavouriteType type) {
        super(name, latitude, longitude);
        this.type = type;
    }

    @NonNull
    @Override
    public MarkerOptions createMarkerOptions(@NonNull Context context) {
        LatLng markerPosition = new LatLng(latitude, longitude);
        return new MarkerOptions()
                .position(markerPosition)
                .icon(createMarkerIcon(context));
    }

    @Nullable
    @Override
    public String getSecondaryText(@NonNull Context context) {
        String helperText = context.getString(R.string.favourite_helper_text).toLowerCase(Locale.ROOT);
        String typeText = FavouriteTypeTools.getTypeText(context, type);
        return String.format("%s - %s", helperText, typeText);
    }

    @Nullable
    @Override
    public String getTertiaryText(@NonNull Context context) {
        return null;
    }

    @NonNull
    @Override
    public String getInlineSecondaryText(@NonNull Context context) {
        return String.format("%s, %s", context.getString(R.string.favourite_helper_text),
                FavouriteTypeTools.getTypeText(context, type));
    }

    @NonNull
    private BitmapDescriptor createMarkerIcon(@NonNull Context context) {
        Bitmap markerBitmap = FavouriteTypeTools.getMarkerIcon(context, type);
        int markerWidth = context.getResources().getDimensionPixelSize(R.dimen.marker_width);
        int markerHeight = context.getResources().getDimensionPixelSize(R.dimen.marker_height);
        Bitmap smallMarker =
                Bitmap.createScaledBitmap(markerBitmap, markerWidth, markerHeight, false);
        return BitmapDescriptorFactory.fromBitmap(smallMarker);
    }

    @NonNull
    private static List<Favourite> loadFavourites(@NonNull Context context) throws IOException {
        Gson gson = new Gson();
        FileInputStream in = context.openFileInput(context.getString(R.string.favourites_filename));
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

    @NonNull
    public static List<Favourite> loadFavouritesSafe(@NonNull Context context,
                                                     @Nullable ExceptionHandler handler) {
        try {
            return Favourite.loadFavourites(context);
        }
        catch (IOException exception) {
            if (handler != null)
                handler.handle();
            return new ArrayList<>();
        }
    }

    private static void saveFavourites(@NonNull Context context,
                                       @NonNull List<Favourite> favourites)
            throws IOException {
        Gson gson = new Gson();
        FileOutputStream out = context.openFileOutput(context.getString(R.string.favourites_filename), Context.MODE_PRIVATE);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        writer.setIndent("  ");
        writer.beginArray();
        for (Favourite favourite : favourites)
            gson.toJson(favourite, Favourite.class, writer);
        writer.endArray();
        writer.close();
    }

    public static void saveFavouritesSafe(@NonNull Context context,
                                          @NonNull List<Favourite> favourites,
                                          @Nullable ExceptionHandler handler)
    {
        try {
            saveFavourites(context, favourites);
        }
        catch (IOException exception) {
            if (handler != null)
                handler.handle();
        }
    }
}
