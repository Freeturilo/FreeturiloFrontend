package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;

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
    public static String filename = "favourites.json";

    @Override
    public MarkerOptions createMarkerOptions(Context context) {
        LatLng markerPosition = new LatLng(latitude, longitude);
        int markerIconId = R.drawable.marker_other;
        switch (type) {
            case HOME:
                markerIconId = R.drawable.marker_home;
                break;
            case WORK:
                markerIconId = R.drawable.marker_work;
                break;
            case SCHOOL:
                markerIconId = R.drawable.marker_school;
                break;
            case OTHER:
                markerIconId = R.drawable.marker_other;
                break;
        }
        Bitmap markerBitmap = BitmapFactory.decodeResource(context.getResources(), markerIconId);
        Bitmap smallMarker = Bitmap.createScaledBitmap(markerBitmap, 95, 150, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        return new MarkerOptions()
                .position(markerPosition)
                .icon(smallMarkerIcon);
    }

    @Override
    public CharSequence createCaption(Context context) {
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder();
        SpannableString ssName = new SpannableString(name);
        int bigSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_big);
        ssName.setSpan(new AbsoluteSizeSpan(bigSize), 0, name.length(), 0);
        ssBuilder.append(ssName);
        int typeTextId = R.string.favourite_other_text;
        switch (type){
            case HOME:
                typeTextId = R.string.favourite_home_text;
                break;
            case SCHOOL:
                typeTextId = R.string.favourite_school_text;
                break;
            case WORK:
                typeTextId = R.string.favourite_work_text;
                break;
            case OTHER:
                typeTextId = R.string.favourite_other_text;
                break;
        }
        String typeText = context.getResources().getString(typeTextId).toLowerCase(Locale.ROOT);
        String typeHelper = context.getResources().getString(R.string.favourite_helper_text);
        String fullType = typeHelper + typeText;
        SpannableString ssType = new SpannableString(fullType);
        int smallSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_small);
        ssType.setSpan(new AbsoluteSizeSpan(smallSize), 0, fullType.length(), 0);
        ssBuilder.append("\n").append(ssType);
        return ssBuilder;
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
