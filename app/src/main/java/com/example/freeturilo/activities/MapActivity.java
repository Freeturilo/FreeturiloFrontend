package com.example.freeturilo.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapActivity extends AppCompatActivity {
    private final API api = new APIMock();
    private final StorageManager storage = new StorageManager(this);
    private final List<Marker> markers = new ArrayList<>();
    private GoogleMap map;
    private List<Favourite> favourites = new ArrayList<>();
    private List<Station> stations = new ArrayList<>();
    private ActivityResultLauncher<String[]> locationPermissionRequest;
    private Marker deviceLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        locationPermissionRequest = registerForActivityResult(new ActivityResultContracts
                .RequestMultiplePermissions(), this::processLocationPermissionsRequestResult);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Synchronizer stationsSynchronizer =
                new Synchronizer(2, this::showStationMarkers);
        Synchronizer favouritesSynchronizer =
                new Synchronizer(2, this::showFavouriteMarkers);
        SupportMapFragment mapFragment = Objects.requireNonNull((SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(googleMap ->
                onMapReadySync(googleMap, stationsSynchronizer, favouritesSynchronizer));
        api.getStationsAsync(retrievedStations ->
                        onStationsReadySync(retrievedStations, stationsSynchronizer),
                new APIActivityHandler(this, true));
        storage.loadFavouritesAsync(loadedFavourites ->
                        onFavouritesReadySync(loadedFavourites, favouritesSynchronizer),
                new ToastStorageHandler(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unfocus(null);
    }

    private void onMapReady(@NonNull GoogleMap googleMap) {
        markers.clear();
        map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,
                getResources().getIdentifier("google_map_style",
                "raw", getPackageName())));
        LatLng warsaw = new LatLng(52.23, 21);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(warsaw, 12));
        map.setOnMapLongClickListener(this::showAddFavouriteDialog);
        map.setOnMapClickListener(this::unfocus);
        map.setOnMarkerClickListener(this::focus);
        showActionButtons(null);
    }

    private void onMapReadySync(@NonNull GoogleMap googleMap,
                                @NonNull Synchronizer ... synchronizers) {
        onMapReady(googleMap);
        for (Synchronizer synchronizer : synchronizers)
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

    private void showStationMarkers() {
        for (Station station : stations) {
            Marker marker = map.addMarker(station.createMarkerOptions(this));
            Objects.requireNonNull(marker).setTag(station);
            markers.add(marker);
        }
    }

    private void showFavouriteMarkers() {
        for(Favourite favourite : favourites) {
            Marker marker = map.addMarker(favourite.createMarkerOptions(this));
            Objects.requireNonNull(marker).setTag(favourite);
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
        hideActionButton(R.id.device_location_button);
        hideActionButton(R.id.set_broken_station_button);
        hideActionButton(R.id.set_working_station_button);
        hideActionButton(R.id.report_station_button);
        hideActionButton(R.id.edit_favourite_button);
        hideActionButton(R.id.delete_favourite_button);
    }

    private void showActionButton(int buttonId, @Nullable Location location) {
        findViewById(buttonId).setVisibility(View.VISIBLE);
        findViewById(buttonId).setTag(location);
    }

    private void showActionButtons(@Nullable Location location) {
        if (location == null) {
            showActionButton(R.id.device_location_button, null);
        }
        else if (location instanceof Favourite) {
            showActionButton(R.id.edit_favourite_button, location);
            showActionButton(R.id.delete_favourite_button, location);
        }
        else if (location instanceof Station) {
            if (AuthTools.isLoggedIn()) {
                Station station = (Station) location;
                if (station.state != 0)
                    showActionButton(R.id.set_working_station_button, location);
                if (station.state != 2)
                    showActionButton(R.id.set_broken_station_button, location);
            }
            else
                showActionButton(R.id.report_station_button, location);
        }
    }

    private void unfocus(@Nullable LatLng latLng) {
        updateBottomPanel(getString(R.string.map_caption_text), null, null);
        hideActionButtons();
        showActionButtons(null);
    }

    private boolean focus(@NonNull Marker marker) {
        map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        if (marker.getTag() == null) {
            unfocus(null);
            return false;
        } else {
            Location location = (Location) marker.getTag();
            updateBottomPanel(location.getPrimaryText(),
                    location.getSecondaryText(this), location.getTertiaryText(this));
            hideActionButtons();
            showActionButtons(location);
            return true;
        }
    }

    private void updateBottomPanel(@NonNull String textPrimary, @Nullable String textSecondary,
                                   @Nullable String textTertiary) {
        TextView bottomTextPrimary = findViewById(R.id.bottom_panel_primary);
        View bottomPanelHorizontalLine = findViewById(R.id.bottom_panel_horizontal_line);
        TextView bottomTextSecondary = findViewById(R.id.bottom_panel_secondary);
        TextView bottomTextTertiary = findViewById(R.id.bottom_panel_tertiary);
        bottomTextPrimary.setText(textPrimary);
        if (textSecondary != null || textTertiary != null)
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
        map.stopAnimation();
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
        map.stopAnimation();
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
        map.stopAnimation();
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
        map.stopAnimation();
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
        map.stopAnimation();
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

    public void showLocationPermissionsDialog(@NonNull View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            showLocationServicesDialog();
        }
        else {
            locationPermissionRequest.launch(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    private void processLocationPermissionsRequestResult(Map<String, Boolean> result) {
        Boolean fineLocationGranted =
                result.get(Manifest.permission.ACCESS_FINE_LOCATION);
        Boolean coarseLocationGranted =
                result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
        if ((fineLocationGranted != null && fineLocationGranted) ||
                (coarseLocationGranted != null && coarseLocationGranted)) {
            showLocationServicesDialog();
        }
    }

    private void showLocationServicesDialog() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (LocationManagerCompat.isLocationEnabled(locationManager))
            locateDevice();
        else {
            map.stopAnimation();
            new AlertDialog.Builder(this, R.style.FreeturiloDialogTheme)
                    .setMessage(R.string.start_location_services_message)
                    .setPositiveButton(R.string.ok_text, (dialog, id) -> locateDevice())
                    .setNegativeButton(R.string.cancel_text, null)
                    .show();
        }
    }

    @SuppressLint("MissingPermission")
    private void locateDevice() {
        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, this::showDeviceLocation);
    }

    private void showDeviceLocation(@Nullable android.location.Location location) {
        if (location != null) {
            LatLng markerPosition = new LatLng(location.getLatitude(), location.getLongitude());
            Bitmap markerBitmap =
                    BitmapFactory.decodeResource(getResources(), R.drawable.marker_device_location);
            int markerSize = getResources().getDimensionPixelSize(R.dimen.small_marker_size);
            Bitmap smallMarker =
                    Bitmap.createScaledBitmap(markerBitmap, markerSize, markerSize, false);
            MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .anchor((float) 0.5, (float) 0.5)
                    .position(markerPosition);
            if (deviceLocationMarker != null)
                deviceLocationMarker.remove();
            deviceLocationMarker = map.addMarker(markerOptions);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 14));
        }
    }
}