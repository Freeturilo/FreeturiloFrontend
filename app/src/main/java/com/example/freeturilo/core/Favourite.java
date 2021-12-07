package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
        String typeText = FavouriteType.getTypeText(context, type);
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
                FavouriteType.getTypeText(context, type));
    }

    @NonNull
    private BitmapDescriptor createMarkerIcon(@NonNull Context context) {
        Bitmap markerBitmap = FavouriteType.getMarkerIcon(context, type);
        int markerWidth = context.getResources().getDimensionPixelSize(R.dimen.marker_width);
        int markerHeight = context.getResources().getDimensionPixelSize(R.dimen.marker_height);
        Bitmap smallMarker =
                Bitmap.createScaledBitmap(markerBitmap, markerWidth, markerHeight, false);
        return BitmapDescriptorFactory.fromBitmap(smallMarker);
    }
}
