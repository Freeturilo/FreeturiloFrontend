package com.example.freeturilo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.freeturilo.connection.API;
import com.example.freeturilo.connection.APIActivityHandler;
import com.example.freeturilo.connection.APIMock;
import com.example.freeturilo.dialogs.AddFavouriteDialog;
import com.example.freeturilo.dialogs.EditFavouriteDialog;
import com.example.freeturilo.R;
import com.example.freeturilo.storage.StorageManager;
import com.example.freeturilo.storage.ToastStorageHandler;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.Station;
import com.example.freeturilo.misc.AuthTools;
import com.example.freeturilo.misc.Synchronizer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity {
    private API api;
    private StorageManager storage;
    private GoogleMap map;
    private List<Marker> markers;
    private List<Favourite> favourites;
    private List<Station> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        api = new APIMock();
        storage = new StorageManager(this);
        SupportMapFragment mapFragment = Objects.requireNonNull((SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map));
        Synchronizer createSynchronizer = new Synchronizer(3, this::showMarkers);
        mapFragment.getMapAsync(googleMap -> onMapReadySync(googleMap, createSynchronizer));
        api.getStationsAsync(retrievedStations ->
                onStationsReadySync(retrievedStations, createSynchronizer),
                new APIActivityHandler(this));
        storage.loadFavouritesAsync(loadedFavourites ->
                onFavouritesReadySync(loadedFavourites, createSynchronizer),
                new ToastStorageHandler(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        unfocus(null);
    }

    private void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,
                getResources().getIdentifier("google_map_style",
                "raw", getPackageName())));
        LatLng warsaw = new LatLng(52.23, 21);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(warsaw, 12));
        map.setOnMapClickListener(this::unfocus);
        map.setOnMapLongClickListener(this::showAddFavouriteDialog);
        map.setOnMarkerClickListener(this::focus);
    }

    private void onMapReadySync(@NonNull GoogleMap googleMap, @NonNull Synchronizer synchronizer) {
        onMapReady(googleMap);
        synchronizer.decrement();
    }

    private void onStationsReady(@NonNull List<Station> retrievedStations) {
        stations = retrievedStations;
    }

    private void onStationsReadySync(@NonNull List<Station> retrievedStations,
                                     @NonNull Synchronizer synchronizer) {
        onStationsReady(retrievedStations);
        synchronizer.decrement();
    }

    private void onFavouritesReady(@NonNull List<Favourite> loadedFavourites) {
        favourites = loadedFavourites;
    }

    private void onFavouritesReadySync(@NonNull List<Favourite> loadedFavourites,
                                       @NonNull Synchronizer synchronizer) {
        onFavouritesReady(loadedFavourites);
        synchronizer.decrement();
    }

    private void showMarkers() {
        markers = new ArrayList<>();
        for(Favourite favourite : favourites) {
            Marker marker = map.addMarker(favourite.createMarkerOptions(this));
            Objects.requireNonNull(marker).setTag(favourite);
            markers.add(marker);
        }
        for (Station station : stations) {
            Marker marker = map.addMarker(station.createMarkerOptions(this));
            Objects.requireNonNull(marker).setTag(station);
            markers.add(marker);
        }
    }

    @Nullable
    private Marker findMarkerByLocation(@NonNull Location location) {
        for (Marker marker : markers)
            if (Objects.equals(marker.getTag(), location))
                return marker;
        return null;
    }

    private void hideActionButton(int buttonId) {
        findViewById(buttonId).setVisibility(View.GONE);
        findViewById(buttonId).setTag(null);
    }

    private void hideActionButtons() {
        hideActionButton(R.id.set_broken_station_button);
        hideActionButton(R.id.set_working_station_button);
        hideActionButton(R.id.report_station_button);
        hideActionButton(R.id.edit_favourite_button);
        hideActionButton(R.id.delete_favourite_button);
    }

    private void showActionButtonFocused(int buttonId, @NonNull Location location) {
        findViewById(buttonId).setVisibility(View.VISIBLE);
        findViewById(buttonId).setTag(location);
    }

    private void showActionButtonsFocused(@NonNull Location location) {
        if (location instanceof Favourite) {
            showActionButtonFocused(R.id.edit_favourite_button, location);
            showActionButtonFocused(R.id.delete_favourite_button, location);
        }
        else if (location instanceof Station) {
            if (AuthTools.isLoggedIn()) {
                Station station = (Station) location;
                if (station.state != 0)
                    showActionButtonFocused(R.id.set_working_station_button, location);
                if (station.state != 2)
                    showActionButtonFocused(R.id.set_broken_station_button, location);
            }
            else
                showActionButtonFocused(R.id.report_station_button, location);
        }
    }

    private void unfocus(@Nullable LatLng latLng) {
        updateBottomPanel(getString(R.string.map_caption_text), null, null);
        hideActionButtons();
    }

    private boolean focus(@NonNull Marker marker) {
        Location location = Objects.requireNonNull((Location) marker.getTag());
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        updateBottomPanel(location.getPrimaryText(),
                location.getSecondaryText(this), location.getTertiaryText(this));
        hideActionButtons();
        showActionButtonsFocused(location);
        return true;
    }

    private void updateBottomPanel(@NonNull String textPrimary, @Nullable String textSecondary,
                                   @Nullable String textTertiary) {
        TextView bottomTextPrimary = findViewById(R.id.bottom_panel_primary);
        View bottomPanelHorizontalLine = findViewById(R.id.bottom_panel_horizontal_line);
        TextView bottomTextSecondary = findViewById(R.id.bottom_panel_secondary);
        TextView bottomTextTertiary = findViewById(R.id.bottom_panel_tertiary);
        bottomTextPrimary.setText(textPrimary);
        if (textSecondary != null || textTertiary != null )
            bottomPanelHorizontalLine.setVisibility(View.VISIBLE);
        else
            bottomPanelHorizontalLine.setVisibility(View.GONE);
        if (textSecondary != null) {
            bottomTextSecondary.setVisibility(View.VISIBLE);
            bottomTextSecondary.setText(textSecondary);
        }
        else
            bottomTextSecondary.setVisibility(View.GONE);
        if (textTertiary != null) {
            bottomTextTertiary.setVisibility(View.VISIBLE);
            bottomTextTertiary.setText(textTertiary);
        }
        else
            bottomTextTertiary.setVisibility(View.GONE);
    }

    private void showAddFavouriteDialog(@NonNull LatLng latLng) {
        AddFavouriteDialog dialog = new AddFavouriteDialog(latLng, this::addFavourite);
        dialog.show(getSupportFragmentManager(), null);
    }

    private void addFavourite(@NonNull Favourite favourite) {
        favourites.add(favourite);
        Marker marker = map.addMarker(favourite.createMarkerOptions(this));
        Objects.requireNonNull(marker).setTag(favourite);
        markers.add(marker);
        focus(marker);
        storage.saveFavouritesAsync(favourites, new ToastStorageHandler(this));
    }

    public void showEditFavouriteDialog(@NonNull View view) {
        Favourite favourite = Objects.requireNonNull((Favourite) view.getTag());
        EditFavouriteDialog dialog = new EditFavouriteDialog(favourite, this::editFavourite);
        dialog.show(getSupportFragmentManager(), null);
    }

    private void editFavourite(@NonNull Favourite favourite) {
        Marker favouriteMarker = Objects.requireNonNull(findMarkerByLocation(favourite));
        markers.remove(favouriteMarker);
        favouriteMarker.remove();
        Marker marker = map.addMarker(favourite.createMarkerOptions(this));
        Objects.requireNonNull(marker).setTag(favourite);
        markers.add(marker);
        focus(marker);
        storage.saveFavouritesAsync(favourites, new ToastStorageHandler(this));
    }

    public void showDeleteFavouriteDialog(@NonNull View view) {
        Favourite favourite = Objects.requireNonNull((Favourite) view.getTag());
        new AlertDialog.Builder(this, R.style.FreeturiloDialogTheme)
                .setMessage(String.format("%s \"%s\"?",
                        getString(R.string.delete_favourite_message), favourite.name))
                .setPositiveButton(R.string.yes_text, (dialog, id) -> deleteFavourite(favourite))
                .setNegativeButton(R.string.cancel_text, null)
                .show();
    }

    private void deleteFavourite(@NonNull Favourite favourite) {
        favourites.remove(favourite);
        Marker favouriteMarker = Objects.requireNonNull(findMarkerByLocation(favourite));
        markers.remove(favouriteMarker);
        favouriteMarker.remove();
        storage.saveFavouritesAsync(favourites, new ToastStorageHandler(this));
        unfocus(null);
    }

    public void showReportStationDialog(@NonNull View view) {
        Station station = Objects.requireNonNull((Station) view.getTag());
        new AlertDialog.Builder(this, R.style.FreeturiloDialogTheme)
                .setMessage(String.format("%s \"%s\"?", getString(R.string.report_station_message), station.name))
                .setPositiveButton(R.string.yes_text, (dialog, id) -> reportStation(station))
                .setNegativeButton(R.string.cancel_text, null)
                .show();
    }

    private void reportStation(@NonNull Station station) {
        api.reportStationAsync(station, new APIActivityHandler(this));
    }

    public void showSetBrokenStationDialog(@NonNull View view) {
        Station station = Objects.requireNonNull((Station) view.getTag());
        new AlertDialog.Builder(this, R.style.FreeturiloDialogTheme)
                .setMessage(String.format("%s \"%s\"?",
                        getString(R.string.set_broken_station_message), station.name))
                .setPositiveButton(R.string.yes_text, (dialog, id) -> setBrokenStation(station))
                .setNegativeButton(R.string.cancel_text, null)
                .show();
    }

    private void setBrokenStation(@NonNull Station station) {
        api.setBrokenStationAsync(station, new APIActivityHandler(this));
    }

    public void showSetWorkingStationDialog(@NonNull View view) {
        Station station = Objects.requireNonNull((Station) view.getTag());
        new AlertDialog.Builder(this, R.style.FreeturiloDialogTheme)
                .setMessage(String.format("%s \"%s\"?",
                        getString(R.string.set_working_station_message), station.name))
                .setPositiveButton(R.string.yes_text, (dialog, id) -> setWorkingStation(station))
                .setNegativeButton(R.string.cancel_text, null)
                .show();
    }

    private void setWorkingStation(@NonNull Station station) {
        api.setWorkingStationAsync(station, new APIActivityHandler(this));
    }
}