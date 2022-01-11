package com.example.freeturilo.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.example.freeturilo.connection.APIConnector;
import com.example.freeturilo.dialogs.AddFavouriteDialog;
import com.example.freeturilo.dialogs.EditFavouriteDialog;
import com.example.freeturilo.R;
import com.example.freeturilo.storage.StorageConnector;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An activity displaying a map with marked stations and user-favourite
 * locations.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see FreeturiloActivity
 * @see Station
 * @see Favourite
 */
public class MapActivity extends FreeturiloActivity {
    /**
     * Stores the API used by this activity to perform external data
     * transaction.
     */
    private final API api = new APIConnector();
    /**
     * Stores the storage used by this activity to retrieve user-favourite
     * locations.
     */
    private final StorageConnector storage = new StorageConnector(this);
    /**
     * Stores all station and user-favourite locations' markers displayed on
     * the map.
     */
    private final List<Marker> markers = new ArrayList<>();
    /**
     * Stores the map displayed with this activity.
     */
    private GoogleMap map;
    /**
     * Stores all user-favourite locations.
     */
    private List<Favourite> favourites = new ArrayList<>();
    /**
     * Stores all stations.
     */
    private List<Station> stations = new ArrayList<>();
    /**
     * Stores a launcher for a call to ask for system location module
     * permissions.
     */
    private ActivityResultLauncher<String[]> locationPermissionRequest;
    /**
     * Stores a marker representing the device location on the map.
     */
    private Marker deviceLocationMarker;

    /**
     * Called when this activity is created.
     * <p>
     * Calls {@link FreeturiloActivity#onCreate}. Initializes the layout of
     * this activity and registers this activity for future ask for system
     * location module permissions. Sets {@link #locationPermissionRequest}.
     * @param savedInstanceState    unused parameter, included for
     *                              compatibility with
     *                              {@code AppCompatActivity}
     * @see #registerForActivityResult
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        locationPermissionRequest = registerForActivityResult(new ActivityResultContracts
                .RequestMultiplePermissions(), this::processLocationPermissionsRequestResult);
    }

    /**
     * Called when this activity is resumed.
     * <p>
     * Starts a loading animation and begins asynchronous data transactions:
     * retrieving the {@link #map} from Google Maps API, {@link #stations} from
     * {@link #api} and {@link #favourites} from {@link #storage}.
     * @see #startLoadingAnimation
     * @see API#getStationsAsync
     * @see StorageConnector#loadFavouritesAsync
     * @see SupportMapFragment#getMapAsync
     */
    @Override
    protected void onResume() {
        super.onResume();
        startLoadingAnimation();
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

    /**
     * Called when the activity is paused.
     * <p>
     * Clears focus.
     * @see #unfocus
     */
    @Override
    protected void onPause() {
        super.onPause();
        unfocus(null);
    }

    /**
     * Called when {@link #map} is retrieved.
     * <p>
     * Clears {@link #markers}, sets the {@link #map} and its style, moves map
     * camera to the center of Warsaw, adds map click, long click and marker
     * click listeners, clears focus.
     * @param googleMap         a retrieved map
     * @see #unfocus
     * @see GoogleMap
     */
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
        unfocus(null);
    }

    /**
     * Called when {@link #map} is retrieved.
     * <p>
     * Calls {@link #onMapReady} and decrements {@code Synchronizers}.
     * @param googleMap         a retrieved map
     * @param synchronizers     a list of synchronizers to be decremented
     *                          when {@link #onMapReady} finishes
     * @see #onMapReady
     * @see Synchronizer#decrement
     */
    private void onMapReadySync(@NonNull GoogleMap googleMap,
                                @NonNull Synchronizer ... synchronizers) {
        onMapReady(googleMap);
        for (Synchronizer synchronizer : synchronizers)
            synchronizer.decrement();
    }

    /**
     * Called when {@link #stations} are retrieved.
     * <p>
     * Sets {@link #stations}.
     * @param retrievedStations     a list of all retrieved stations
     * @see Station
     */
    private void onStationsReady(@NonNull List<Station> retrievedStations) {
        stations = retrievedStations;
    }

    /**
     * Called when {@link #stations} are retrieved.
     * <p>
     * Calls {@link #onStationsReady} and decrements a {@code Synchronizer}.
     * @param retrievedStations     a list of all retrieved stations
     * @param synchronizer          a synchronizer to be decremented when
     *                              {@link #onStationsReady} finishes
     * @see #onStationsReady
     * @see Synchronizer#decrement
     */
    private void onStationsReadySync(@NonNull List<Station> retrievedStations,
                                     @NonNull Synchronizer synchronizer) {
        onStationsReady(retrievedStations);
        synchronizer.decrement();
    }

    /**
     * Called when {@link #favourites} are retrieved.
     * <p>
     * Sets {@link #favourites}.
     * @param loadedFavourites      a list of all retrieved user-favourite
     *                              locations
     * @see Favourite
     */
    private void onFavouritesReady(@NonNull List<Favourite> loadedFavourites) {
        favourites = loadedFavourites;
    }

    /**
     * Called when {@link #favourites} are retrieved.
     * <p>
     * Calls {@link #onStationsReady} and decrements a {@code Synchronizer}.
     * @param loadedFavourites      a list of all retrieved user-favourite
     * @param synchronizer          a synchronizer to be decremented when
     *                              {@link #onFavouritesReady} finishes
     * @see #onFavouritesReady
     * @see Synchronizer#decrement
     */
    private void onFavouritesReadySync(@NonNull List<Favourite> loadedFavourites,
                                       @NonNull Synchronizer synchronizer) {
        onFavouritesReady(loadedFavourites);
        synchronizer.decrement();
    }

    /**
     * Places and displays markers of {@link #stations} on the {@link #map}.
     * Called when both {@link #stations} and {@link #map} have been retrieved
     * (via a {@code Synchronizer}).
     * <p>
     * Places a marker for each station on the map, assigns a station to each
     * marker and populates {@link #markers}. Moves map camera to show all
     * station markers and stops the loading animation.
     * @see #stopLoadingAnimation
     * @see GoogleMap#addMarker
     */
    private void showStationMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Station station : stations) {
            Marker marker = map.addMarker(station.createMarkerOptions(this));
            Objects.requireNonNull(marker).setTag(station);
            markers.add(marker);
            builder.include(new LatLng(station.latitude, station.longitude));
        }
        if (!stations.isEmpty())
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
        stopLoadingAnimation();
    }

    /**
     * Places and displays markers of {@link #favourites} on the {@link #map}.
     * Called when both {@link #favourites} and {@link #map} have been
     * retrieved (via a {@code Synchronizer}).
     * <p>
     * Places a marker for each favourite on the map, assigns a favourite to
     * each marker and populates {@link #markers}.
     * @see GoogleMap#addMarker
     */
    private void showFavouriteMarkers() {
        for(Favourite favourite : favourites) {
            Marker marker = map.addMarker(favourite.createMarkerOptions(this));
            Objects.requireNonNull(marker).setTag(favourite);
            markers.add(marker);
        }
    }

    /**
     * Finds a marker that has a location assigned.
     * <p>
     * For each marker in {@link #markers} compares its tag to a location.
     * @param location      a location
     * @return              a marker that has the location assigned or null
     *                      if no such marker is found
     */
    @Nullable
    private Marker findMarkerByLocation(@NonNull Location location) {
        for (Marker marker : markers)
            if (Objects.equals(marker.getTag(), location))
                return marker;
        return null;
    }

    /**
     * Hides an action button.
     * <p>
     * Changes visibility of the action button and removes its location
     * assignment.
     * @param buttonId      an integer equal to the resource identifier of an
     *                      action button
     */
    private void hideActionButton(int buttonId) {
        findViewById(buttonId).setVisibility(View.GONE);
        findViewById(buttonId).setTag(null);
    }

    /**
     * Hides all action buttons.
     * <p>
     * Changes visibility of all action buttons and removes all of their
     * location assignments.
     * @see #hideActionButton
     */
    private void hideActionButtons() {
        hideActionButton(R.id.device_location_button);
        hideActionButton(R.id.set_broken_station_button);
        hideActionButton(R.id.set_working_station_button);
        hideActionButton(R.id.report_station_button);
        hideActionButton(R.id.edit_favourite_button);
        hideActionButton(R.id.delete_favourite_button);
    }

    /**
     * Shows an action button.
     * <p>
     * Changes visibility of an action button and assigns a location to it.
     * @param buttonId      an integer equal to the resource identifier of an
     *                      action button
     * @param location      a location to be assigned to the button
     */
    private void showActionButton(int buttonId, @Nullable Location location) {
        findViewById(buttonId).setVisibility(View.VISIBLE);
        findViewById(buttonId).setTag(location);
    }

    /**
     * Shows action buttons for a given focus.
     * <p>
     * If focus is cleared, shows a device location button. If focus is on
     * a user-favourite location, shows an edit favourite and a delete
     * favourite button. If focus is on a station, appropriate station
     * state buttons. Assigns the location having focus to shown buttons.
     * @param location      a location that has focus or null if focus
     *                      is cleared
     * @see #showActionButton
     */
    private void showActionButtons(@Nullable Location location) {
        if (location == null) {
            showActionButton(R.id.device_location_button, null);
        }
        else if (location instanceof Favourite) {
            showActionButton(R.id.edit_favourite_button, location);
            showActionButton(R.id.delete_favourite_button, location);
        }
        else if (location instanceof Station) {
            Station station = (Station) location;
            if (AuthTools.isLoggedIn()) {
                if (station.state != 0)
                    showActionButton(R.id.set_working_station_button, location);
                if (station.state != 2)
                    showActionButton(R.id.set_broken_station_button, location);
            }
            else {
                if (station.state != 2)
                    showActionButton(R.id.report_station_button, location);
            }
        }
    }

    /**
     * Clears focus.
     * <p>
     * Updates bottom panel to be in default state and shows default action
     * buttons.
     * @param latLng        unused parameter, a point on the map clicked to
     *                      unfocus or null if focus clear has not been
     *                      obtained with a map click
     * @see #updateBottomPanel
     * @see #showActionButtons
     */
    private void unfocus(@Nullable LatLng latLng) {
        updateBottomPanel(getString(R.string.map_caption_text), null, null);
        hideActionButtons();
        showActionButtons(null);
    }

    /**
     * Changes focus after a map {@code Marker} click.
     * <p>
     * Moves map camera to show a marker. If marker has no location assigned,
     * clears focus. Else, updates bottom panel to show the location's details
     * and shows action buttons for the location.
     * @param marker        a clicked marker
     * @return              a boolean indicating whether the focus has been
     *                      given to a location
     * @see #unfocus
     * @see #updateBottomPanel
     * @see #showActionButtons
     */
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

    /**
     * Shows a text content on the bottom panel.
     * <p>
     * Sets primary, secondary and tertiary text on the bottom panel. If one of
     * the specified secondary and tertiary texts is not null, shows a
     * horizontal rule between where secondary and tertiary texts would be.
     * @param textPrimary       a string equal to the main, large text on the
     *                          bottom panel
     * @param textSecondary     a string equal to the secondary, small text
     *                          on the bottom panel or null if no text should
     *                          be set as secondary
     * @param textTertiary      a string equal to the tertiary, small text
     *                          on the bottom panel or null if no text should
     *                          be set as tertiary
     */
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

    /**
     * Shows a dialog for adding a favourite location. Called when a point
     * on the {@link #map} is long-clicked.
     * @param latLng        a point on the map
     * @see #addFavourite
     * @see AddFavouriteDialog
     */
    private void showAddFavouriteDialog(@NonNull LatLng latLng) {
        map.stopAnimation();
        AddFavouriteDialog dialog = new AddFavouriteDialog(latLng, this::addFavourite);
        dialog.show(getSupportFragmentManager(), null);
    }

    /**
     * Adds a favourite location. Called when the {@code AddFavouriteDialog}
     * finishes with a positive button click.
     * <p>
     * Adds the favourite location to {@link #favourites}, places and displays
     * its marker on the {@link #map}. Populates {@link #markers}, gives the
     * favourite location focus and saves favourites to {@link #storage}.
     * @param favourite     an added favourite location
     * @see #focus
     * @see #showAddFavouriteDialog
     * @see AddFavouriteDialog
     * @see StorageConnector#saveFavouritesAsync
     */
    private void addFavourite(@NonNull Favourite favourite) {
        favourites.add(favourite);
        Marker marker = map.addMarker(favourite.createMarkerOptions(this));
        Objects.requireNonNull(marker).setTag(favourite);
        markers.add(marker);
        focus(marker);
        storage.saveFavouritesAsync(favourites, new ToastStorageHandler(this));
    }

    /**
     * Shows a dialog for editing a favourite location. Called when the edit
     * favourite action button is clicked.
     * @param view          an edit favourite action button with a favourite
     *                      location assigned
     * @see #editFavourite
     * @see EditFavouriteDialog
     */
    public void showEditFavouriteDialog(@NonNull View view) {
        map.stopAnimation();
        Favourite favourite = Objects.requireNonNull((Favourite) view.getTag());
        EditFavouriteDialog dialog = new EditFavouriteDialog(favourite, this::editFavourite);
        dialog.show(getSupportFragmentManager(), null);
    }

    /**
     * Edits a favourite location. Called when the {@code EditFavouriteDialog}
     * finishes with a positive button click.
     * <p>
     * Updates the marker with the favourite location assigned. Gives the
     * favourite location focus and saves favourites to {@link #storage}.
     * @param favourite     an edited favourite location
     * @see #focus
     * @see #showEditFavouriteDialog
     * @see EditFavouriteDialog
     * @see StorageConnector#saveFavouritesAsync
     */
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

    /**
     * Shows a confirmation dialog for deleting a favourite location. Called
     * when the delete favourite action button is clicked.
     * @param view          a delete favourite action button with a favourite
     *                      location assigned
     * @see #deleteFavourite
     * @see AlertDialog
     */
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

    /**
     * Deletes a favourite location. Called when the {@code AlertDialog} for
     * deleting a favourite location finishes with a positive button click.
     * <p>
     * Removes the marker with the favourite location assigned. Clears focus
     * and saves favourites to {@link #storage}.
     * @param favourite     an edited favourite location
     * @see #unfocus
     * @see #showDeleteFavouriteDialog
     * @see StorageConnector#saveFavouritesAsync
     */
    private void deleteFavourite(@NonNull Favourite favourite) {
        favourites.remove(favourite);
        Marker favouriteMarker = Objects.requireNonNull(findMarkerByLocation(favourite));
        markers.remove(favouriteMarker);
        favouriteMarker.remove();
        storage.saveFavouritesAsync(favourites, new ToastStorageHandler(this));
        unfocus(null);
    }

    /**
     * Shows a confirmation dialog for reporting a station. Called when the
     * report station action button is clicked.
     * @param view          a report station action button with a station
     *                      assigned
     * @see #reportStation
     * @see AlertDialog
     */
    public void showReportStationDialog(@NonNull View view) {
        map.stopAnimation();
        Station station = Objects.requireNonNull((Station) view.getTag());
        new AlertDialog.Builder(this, R.style.FreeturiloDialogTheme)
                .setMessage(String.format("%s \"%s\"?", getString(R.string.report_station_message), station.name))
                .setPositiveButton(R.string.yes_text, (dialog, id) -> reportStation(station))
                .setNegativeButton(R.string.cancel_text, null)
                .show();
    }

    /**
     * Reports a station. Called when the {@code AlertDialog} for reporting
     * a station finishes with a positive button click.
     * <p>
     * Updates the marker with the station assigned. Gives the station focus
     * and sends a station report request to {@link #api}.
     * @param station     a reported station
     * @see #focus
     * @see #showReportStationDialog
     * @see API#reportStationAsync
     */
    private void reportStation(@NonNull Station station) {
        api.reportStationAsync(station, new APIActivityHandler(this, false));
        Marker stationMarker = Objects.requireNonNull(findMarkerByLocation(station));
        markers.remove(stationMarker);
        stationMarker.remove();
        station.state = 1;
        Marker marker = map.addMarker(station.createMarkerOptions(this));
        Objects.requireNonNull(marker).setTag(station);
        markers.add(marker);
        focus(marker);
    }

    /**
     * Shows a confirmation dialog for setting a station state to broken.
     * Called when the set station broken action button is clicked.
     * @param view          a set station broken action button with a station
     *                      assigned
     * @see #setBrokenStation
     * @see AlertDialog
     */
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

    /**
     * Sets a station state to broken. Called when the {@code AlertDialog} for
     * setting a station state to broken finishes with a positive button click.
     * <p>
     * Updates the marker with the station assigned. Gives the station focus
     * and sends a station broken request to {@link #api}.
     * @param station     a station set to broken
     * @see #focus
     * @see #showSetBrokenStationDialog
     * @see API#setBrokenStationAsync
     */
    private void setBrokenStation(@NonNull Station station) {
        api.setBrokenStationAsync(station, new APIActivityHandler(this, false));
        Marker stationMarker = Objects.requireNonNull(findMarkerByLocation(station));
        markers.remove(stationMarker);
        stationMarker.remove();
        station.state = 2;
        Marker marker = map.addMarker(station.createMarkerOptions(this));
        Objects.requireNonNull(marker).setTag(station);
        markers.add(marker);
        focus(marker);
    }

    /**
     * Shows a confirmation dialog for setting a station state to working.
     * Called when the set station working action button is clicked.
     * @param view          a set station working action button with a station
     *                      assigned
     * @see #setWorkingStation
     * @see AlertDialog
     */
    public void showSetWorkingStationDialog(@NonNull View view) {
        map.stopAnimation();
        Station station = Objects.requireNonNull((Station) view.getTag());
        new AlertDialog.Builder(this, R.style.FreeturiloDialogTheme)
                .setMessage(String.format("%s \"%s\"?",
                        getString(R.string.set_working_station_message), station.name))
                .setPositiveButton(R.string.yes_text, (dialog, id) -> setWorkingStation(station))
                .setNegativeButton(R.string.cancel_text, null)
                .show();
    }

    /**
     * Sets a station state to working. Called when the {@code AlertDialog} for
     * setting a station state to working finishes with a positive button
     * click.
     * <p>
     * Updates the marker with the station assigned. Gives the station focus
     * and sends a station working request to {@link #api}.
     * @param station     a station set to working
     * @see #focus
     * @see #showSetWorkingStationDialog
     * @see API#setWorkingStationAsync
     */
    private void setWorkingStation(@NonNull Station station) {
        api.setWorkingStationAsync(station, new APIActivityHandler(this, false));
        Marker stationMarker = Objects.requireNonNull(findMarkerByLocation(station));
        markers.remove(stationMarker);
        stationMarker.remove();
        station.state = 0;
        Marker marker = map.addMarker(station.createMarkerOptions(this));
        Objects.requireNonNull(marker).setTag(station);
        markers.add(marker);
        focus(marker);
    }

    /**
     * Shows a dialog requesting system location module permissions if they
     * have not been yet granted. Else, shows a location services dialog or
     * locates the device. Called when the device location action button is
     * clicked.
     * @param view          unused parameter, the device location action button
     * @see #showLocationServicesDialog()
     * @see #locationPermissionRequest
     * @see #processLocationPermissionsRequestResult
     */
    public void showLocationPermissionsDialog(@NonNull View view) {
        map.stopAnimation();
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

    /**
     * Processes location module permissions request result and shows a
     * location services dialog if granted. Called when the device location
     * action button is clicked, after location module permissions dialog
     * finishes.
     * @param result        a map with booleans assigned to permissions,
     *                      indicating whether each of them was granted
     * @see #showLocationServicesDialog
     */
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

    /**
     * Shows a dialog requesting turning on the system location module if
     * necessary. Else, locates the device. Called when the device location
     * action button is clicked and location module permissions are granted.
     * @see #locateDevice()
     */
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

    /**
     * Starts a retrieval of device location. Called when the device location
     * action button is clicked, location module permissions are granted and
     * the module is turned on.
     * @see #showDeviceLocation
     */
    @SuppressLint("MissingPermission")
    private void locateDevice() {
        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, this::showDeviceLocation);
    }

    /**
     * Shows device location on the {@link #map}. Called when device location
     * is retrieved.
     * <p>
     * Places a device location marker on the map and moves map camera to show
     * the marker.
     * @param location      a retrieved device location
     */
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