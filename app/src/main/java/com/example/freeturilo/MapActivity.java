package com.example.freeturilo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.Station;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    List<Marker> markers;
    List<Favourite> favourites;
    List<Station> stations;
    List<Button> actionButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        actionButtons = Arrays.asList(
                findViewById(R.id.report_station_button),
                findViewById(R.id.edit_favourite_button),
                findViewById(R.id.delete_favourite_button));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,
                getResources().getIdentifier("google_map_style",
                "raw", getPackageName())));
        LatLng warsaw = new LatLng(52.23, 21);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(warsaw, 12));
        loadAndShowMarkers();
        map.setOnMapClickListener(this::unfocus);
        map.setOnMapLongClickListener(this::showAddFavouriteDialog);
        map.setOnMarkerClickListener(this::showMarkerInfo);
    }

    private void loadAndShowMarkers() {
        markers = new ArrayList<>();
        stations = Station.loadStations();
        for(Station station : stations) {
            Marker marker = map.addMarker(station.createMarkerOptions(this));
            Objects.requireNonNull(marker).setTag(station);
            markers.add(marker);
        }
        favourites = Favourite.loadFavouritesSafe(this, new ToastExceptionHandler(this, R.string.no_favourites_message));
        for(Favourite favourite : favourites) {
            Marker marker = map.addMarker(favourite.createMarkerOptions(this));
            Objects.requireNonNull(marker).setTag(favourite);
            markers.add(marker);
        }
    }

    private void hideActionButtons() {
        for (Button button : actionButtons) {
            button.setVisibility(View.GONE);
            button.setTag(null);
        }
    }

    private void showActionButtonsForLocation(Location location) {
        if (location instanceof Favourite) {
            findViewById(R.id.edit_favourite_button).setVisibility(View.VISIBLE);
            findViewById(R.id.edit_favourite_button).setTag(location);

            findViewById(R.id.delete_favourite_button).setVisibility(View.VISIBLE);
            findViewById(R.id.delete_favourite_button).setTag(location);
        }
        else if (location instanceof Station)
            findViewById(R.id.report_station_button).setVisibility(View.VISIBLE);
            findViewById(R.id.report_station_button).setTag(location);
    }

    private void unfocus(LatLng latLng) {
        TextView bottomTextPrimary = findViewById(R.id.bottom_panel_primary);
        bottomTextPrimary.setText(getString(R.string.map_caption_text));
        TextView bottomTextSecondary = findViewById(R.id.bottom_panel_secondary);
        bottomTextSecondary.setVisibility(View.GONE);
        hideActionButtons();
    }

    private void showAddFavouriteDialog(LatLng latLng) {
        AddFavouriteDialog dialog = new AddFavouriteDialog(latLng);
        dialog.show(getSupportFragmentManager(), null);
    }

    public void showEditFavouriteDialog(View view) {
        Favourite favourite = Objects.requireNonNull((Favourite) view.getTag());
        EditFavouriteDialog dialog = new EditFavouriteDialog(favourite);
        dialog.show(getSupportFragmentManager(), null);
    }

    public void showDeleteFavouriteDialog(View view) {
        Favourite favourite = Objects.requireNonNull((Favourite) view.getTag());
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.FreeturiloDialogTheme))
                .setMessage(getString(R.string.delete_favourite_message) + " \"" + favourite.name + "\"?")
                .setPositiveButton(R.string.yes_text, (dialog, id) -> deleteFavourite(favourite))
                .setNegativeButton(R.string.cancel_text, null)
                .show();
    }

    private boolean showMarkerInfo(Marker marker) {
        Location location = Objects.requireNonNull((Location) marker.getTag());
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        TextView bottomTextPrimary = findViewById(R.id.bottom_panel_primary);
        bottomTextPrimary.setText(location.getPrimaryText());
        TextView bottomTextSecondary = findViewById(R.id.bottom_panel_secondary);
        bottomTextSecondary.setVisibility(View.VISIBLE);
        bottomTextSecondary.setText(location.getCaption(this));
        hideActionButtons();
        showActionButtonsForLocation(location);
        return true;
    }

    public Marker findMarkerByLocation(Location location) {
        for (Marker marker : markers)
            if (Objects.equals(marker.getTag(), location))
                return marker;
        return null;
    }

    public void addFavourite(Favourite favourite) {
        favourites.add(favourite);
        Marker marker = map.addMarker(favourite.createMarkerOptions(this));
        Objects.requireNonNull(marker).setTag(favourite);
        markers.add(marker);
        showMarkerInfo(marker);
        Favourite.saveFavouritesSafe(this, favourites,
                new ToastExceptionHandler(this, R.string.no_favourites_message));
    }

    public void updateFavourite(Favourite favourite) {
        Marker favouriteMarker = findMarkerByLocation(favourite);
        markers.remove(Objects.requireNonNull(favouriteMarker));
        favouriteMarker.remove();
        Marker marker = map.addMarker(favourite.createMarkerOptions(this));
        Objects.requireNonNull(marker).setTag(favourite);
        markers.add(marker);
        showMarkerInfo(marker);
        Favourite.saveFavouritesSafe(this, favourites,
                new ToastExceptionHandler(this, R.string.no_favourites_message));
    }

    public void deleteFavourite(Favourite favourite) {
        favourites.remove(favourite);
        Marker favouriteMarker = findMarkerByLocation(favourite);
        markers.remove(Objects.requireNonNull(favouriteMarker));
        favouriteMarker.remove();
        Favourite.saveFavouritesSafe(this, favourites,
                new ToastExceptionHandler(this, R.string.no_favourites_message));
        unfocus(null);
    }
}