package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Toast;

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

    public Favourite(String name, double latitude, double longitude, FavouriteType type) {
        super(name, latitude, longitude);
        this.type = type;
    }

    @Override
    public MarkerOptions createMarkerOptions(Context context) {
        LatLng markerPosition = new LatLng(latitude, longitude);
        return new MarkerOptions()
                .position(markerPosition)
                .icon(createMarkerIcon(context));
    }

    @Override
    public CharSequence createCaption(Context context) {
        SpannableString ssName = new SpannableString(name);
        int bigSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_big);
        ssName.setSpan(new AbsoluteSizeSpan(bigSize), 0, name.length(), 0);
        String typeText = createTypeText(context);
        String typeHelper = context.getString(R.string.favourite_helper_text).toLowerCase(Locale.ROOT);
        String fullType = typeHelper + " - " + typeText;
        SpannableString ssType = new SpannableString(fullType);
        int smallSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_small);
        ssType.setSpan(new AbsoluteSizeSpan(smallSize), 0, fullType.length(), 0);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(ssName).append("\n").append(ssType);
        return builder;
    }

    @Override
    public void setAutoCompletePredictionText(Context context) {
        SpannableString ssPrimary = new SpannableString(name);
        ssPrimary.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), 0);
        SpannableString ssSecondary = new SpannableString(
                ", " + context.getString(R.string.favourite_helper_text) + ", " + createTypeText(context));
        ssSecondary.setSpan(new ForegroundColorSpan(context.getColor(R.color.grey)),
                0, ssSecondary.length(), 0);
        int smallSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_small);
        ssSecondary.setSpan(new AbsoluteSizeSpan(smallSize), 0, ssSecondary.length(), 0);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(ssPrimary).append(ssSecondary);
        autoCompletePredictionText = builder;
    }

    private String createTypeText(Context context) {
        switch (type){
            case HOME:
                return context.getString(R.string.favourite_home_text);
            case SCHOOL:
                return context.getString(R.string.favourite_school_text);
            case WORK:
                return context.getString(R.string.favourite_work_text);
            default:
                return context.getString(R.string.favourite_other_text);
        }
    }

    private BitmapDescriptor createMarkerIcon(Context context) {
        int markerIconId;
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
            default:
                markerIconId = R.drawable.marker_other;
                break;
        }
        Bitmap markerBitmap = BitmapFactory.decodeResource(context.getResources(), markerIconId);
        int markerWidth = context.getResources().getDimensionPixelSize(R.dimen.marker_width);
        int markerHeight = context.getResources().getDimensionPixelSize(R.dimen.marker_height);
        Bitmap smallMarker =
                Bitmap.createScaledBitmap(markerBitmap, markerWidth, markerHeight, false);
        return BitmapDescriptorFactory.fromBitmap(smallMarker);
    }

    public static void createFavouritesFile(Context context) {
        try {
            FileInputStream in = context.openFileInput(
                    context.getString(R.string.favourites_filename));
            in.close();
        } catch (IOException ignored) { }
    }

    public static List<Favourite> loadFavourites(Context context) throws IOException {
        Gson gson = new Gson();
        FileInputStream in = context.openFileInput(
                context.getString(R.string.favourites_filename));
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

    public static List<Favourite> loadFavouritesSafe(Context context) {
        try {
            return Favourite.loadFavourites(context);
        }
        catch (IOException exception) {
            Toast toast = Toast.makeText(context.getApplicationContext(),
                    R.string.no_favourites_message, Toast.LENGTH_SHORT);
            toast.show();
            return new ArrayList<>();
        }
    }

    public static void saveFavourites(Context context, List<Favourite> favourites)
            throws IOException {
        Gson gson = new Gson();
        FileOutputStream out = context.openFileOutput(
                context.getString(R.string.favourites_filename), Context.MODE_PRIVATE);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        writer.setIndent("  ");
        writer.beginArray();
        for (Favourite favourite : favourites) {
            gson.toJson(favourite, Favourite.class, writer);
        }
        writer.endArray();
        writer.close();
    }

    public static void saveFavouritesSafe(Context context, List<Favourite> favourites)
    {
        try {
            saveFavourites(context, favourites);
        }
        catch (IOException exception)
        {
            Toast toast = Toast.makeText(context.getApplicationContext(),
                    R.string.no_favourites_message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
