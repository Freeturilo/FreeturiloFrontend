package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.example.freeturilo.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Station extends Location {
    public int id;
    public int bikeRacks;
    public int bikes;
    public int state;

    public Station(String name, double latitude, double longitude,
                   int id, int bikeRacks, int bikes, int state){
        super(name, latitude, longitude);
        this.id = id;
        this.bikeRacks = bikeRacks;
        this.bikes = bikes;
        this.state = state;
    }

    @Override
    public MarkerOptions createMarkerOptions(Context context) {
        LatLng markerPosition = new LatLng(latitude, longitude);
        Bitmap markerBitmap = StationStateTools.getMarkerIcon(context, state);
        int markerWidth = context.getResources().getDimensionPixelSize(R.dimen.marker_width);
        int markerHeight = context.getResources().getDimensionPixelSize(R.dimen.marker_height);
        Bitmap smallMarker =
                Bitmap.createScaledBitmap(markerBitmap, markerWidth, markerHeight, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
        return new MarkerOptions()
                .position(markerPosition)
                .icon(smallMarkerIcon);
    }

    public static List<Station> loadStations() {
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(new Station("Dewajtis - UKSW", 52.296298, 20.958358,
                1, 30, 28, 0));
        stations.add(new Station("Metro Młociny", 52.290974, 20.929556,
                2, 30, 18, 1));
        stations.add(new Station("Rondo ONZ", 52.232628, 20.997123,
                3, 29, 24, 2));
        return stations;
    }

    @Override
    public CharSequence createCaption(Context context) {
        SpannableString ssName = new SpannableString(name);
        int bigSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_big);
        ssName.setSpan(new AbsoluteSizeSpan(bigSize), 0, name.length(), 0);
        String fullDetails = context.getString(R.string.station_helper_text)
                + "\n" + context.getString(R.string.bikes_availability_text)
                + ": " + bikes + "/" + bikeRacks;
        if (state != 0)
            fullDetails += "\n(" + StationStateTools.getStateText(context, state) + ")";
        SpannableString ssDetails = new SpannableString(fullDetails);
        int smallSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_small);
        ssDetails.setSpan(new AbsoluteSizeSpan(smallSize), 0, ssDetails.length(), 0);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(ssName).append("\n").append(ssDetails);
        return builder;
    }

    @Override
    public void setAutoCompletePredictionText(Context context) {
        SpannableString ssPrimary = new SpannableString(name);
        ssPrimary.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), 0);
        SpannableString ssSecondary = new SpannableString(
                ", " + context.getString(R.string.station_helper_text)
                        + ", " + StationStateTools.getStateText(context, state));
        ssSecondary.setSpan(new ForegroundColorSpan(context.getColor(R.color.grey)),
                0, ssSecondary.length(), 0);
        int smallSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_small);
        ssSecondary.setSpan(new AbsoluteSizeSpan(smallSize), 0, ssSecondary.length(), 0);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(ssPrimary).append(ssSecondary);
        autoCompletePredictionText = builder;
    }

    public void reportBroken() {

    }

    public void setBroken() {

    }

    public void setWorking() {

    }
}
