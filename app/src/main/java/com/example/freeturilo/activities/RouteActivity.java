package com.example.freeturilo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.freeturilo.connection.API;
import com.example.freeturilo.connection.APIActivityHandler;
import com.example.freeturilo.connection.APIConnector;
import com.example.freeturilo.core.Route;
import com.example.freeturilo.misc.ObjectWrapperForBinder;
import com.example.freeturilo.R;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.misc.Synchronizer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Objects;

/**
 * An activity displaying a created route on a map.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see FreeturiloActivity
 * @see Route
 */
public class RouteActivity extends FreeturiloActivity {
    /**
     * Stores the name of the {@code RouteParameters} in the {@code Intent}
     * used to create the route of this activity.
     */
    public static final String ROUTE_PARAMETERS_INTENT = "route_parameters";
    /**
     * Stores the API used by this activity to retrieve a route.
     */
    private final API api = new APIConnector();
    /**
     * Stores the map displayed with this activity.
     */
    private GoogleMap map;
    /**
     * Stores the parameters used to create route to display on the map.
     */
    private RouteParameters routeParameters;
    /**
     * Stores the route created and displayed on the map.
     */
    private Route route;
    /**
     * Stores a flag indicating whether the details about route fragments
     * have been fully shown on the bottom panel.
     */
    private boolean routeDetailsShown = false;

    /**
     * Called when this activity is created.
     * <p>
     * Calls {@link FreeturiloActivity#onCreate}. Initializes the layout of
     * this activity. Retrieves and sets {@link #routeParameters} passed to
     * this activity with the intent.
     * @param savedInstanceState    unused parameter, included for
     *                              compatibility with
     *                              {@code AppCompatActivity}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        ObjectWrapperForBinder parameters_wrapper =
                (ObjectWrapperForBinder) getIntent().getExtras().getBinder(ROUTE_PARAMETERS_INTENT);
        routeParameters = (RouteParameters) parameters_wrapper.getData();
    }

    /**
     * Called when this activity is resumed.
     * <p>
     * Starts a loading animation and begins asynchronous data transactions:
     * retrieving the {@link #map} from Google Maps API and retrieving a route
     * from {@link #api}.
     * @see #startLoadingAnimation
     * @see API#getRouteAsync
     * @see SupportMapFragment#getMapAsync
     */
    @Override
    protected void onResume() {
        super.onResume();
        SupportMapFragment mapFragment = Objects.requireNonNull((SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map));
        startLoadingAnimation();
        Synchronizer routeSynchronizer = new Synchronizer(2, this::showRoute);
        mapFragment.getMapAsync(googleMap -> onMapReadySync(googleMap, routeSynchronizer));
        api.getRouteAsync(routeParameters,
                retrievedRoute -> onRouteReadySync(retrievedRoute, routeSynchronizer),
                new APIActivityHandler(this, true));
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
     * Sets the {@link #map} and its style, moves map camera to the center of
     * Warsaw, adds map click and marker click listeners, clears focus.
     * @param googleMap         a retrieved map
     * @see #unfocus
     * @see GoogleMap
     */
    private void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,
                getResources().getIdentifier("google_map_style",
                        "raw", getPackageName())));
        LatLng warsaw = new LatLng(52.23, 21);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(warsaw, 12));
        map.setOnMapClickListener(this::unfocus);
        map.setOnMarkerClickListener(this::focus);
        unfocus(null);
    }

    /**
     * Called when {@link #map} is retrieved.
     * <p>
     * Calls {@link #onMapReady} and decrements a {@code Synchronizer}.
     * @param googleMap         a retrieved map
     * @param synchronizer      a synchronizer to be decremented when
     *                          {@link #onMapReady} finishes
     * @see #onMapReady
     * @see Synchronizer#decrement
     */
    private void onMapReadySync(@NonNull GoogleMap googleMap,
                                @NonNull Synchronizer synchronizer) {
        onMapReady(googleMap);
        synchronizer.decrement();
    }

    /**
     * Called when {@link #route} is retrieved.
     * <p>
     * Sets {@link #route}.
     * @param retrievedRoute     a created and retrieved route
     * @see Route
     */
    private void onRouteReady(@NonNull Route retrievedRoute) {
        route = retrievedRoute;
    }

    /**
     * Called when {@link #route} is retrieved.
     * <p>
     * Calls {@link #onRouteReady} and decrements a {@code Synchronizer}.
     * @param retrievedRoute        a created and retrieved route
     * @param synchronizer          a synchronizer to be decremented when
     *                              {@link #onRouteReady} finishes
     * @see #onRouteReady
     * @see Synchronizer#decrement
     */
    private void onRouteReadySync(@NonNull Route retrievedRoute,
                                  @NonNull Synchronizer synchronizer) {
        onRouteReady(retrievedRoute);
        synchronizer.decrement();
    }

    /**
     * Draws the {@link #route} on the {@link #map}. Called when both
     * {@link #route} and {@link #map} have been retrieved (via a
     * {@code Synchronizer}).
     * <p>
     * Draws a polyline of the route on the map. Places a marker for each
     * waypoint of the route, shows route details on the bottom panel, moves
     * map camera to show the whole route and stops the loading animation.
     * @see #stopLoadingAnimation
     * @see #updateBottomPanelToRoute
     * @see GoogleMap#addPolyline
     * @see GoogleMap#addMarker
     */
    private void showRoute() {
        PolylineOptions opts = new PolylineOptions()
                .addAll(route.getPath()).color(getColor(R.color.purple_first));
        map.addPolyline(opts);
        for (Location location : route.getWaypoints()) {
            Marker marker = map.addMarker(location.createMarkerOptions(this));
            Objects.requireNonNull(marker).setTag(location);
        }
        updateBottomPanelToRoute();
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(route.getBounds(), 100));
        stopLoadingAnimation();
    }

    /**
     * Clears focus.
     * <p>
     * Shows route details on the bottom panel.
     * @param latLng        unused parameter, a point on the map clicked to
     *                      unfocus or null if focus clear has not been
     *                      obtained with a map click
     * @see #updateBottomPanelToRoute
     */
    private void unfocus(@Nullable LatLng latLng) {
        updateBottomPanelToRoute();
    }

    private boolean focus(@NonNull Marker marker) {
        Location location = Objects.requireNonNull((Location) marker.getTag());
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        updateBottomPanelToLocation(location);
        return true;
    }

    /**
     * Enables showing and hiding detailed information about route fragments
     * on the bottom panel by clicking it.
     */
    private void enableRouteDetailsExtension() {
        LinearLayout bottomPanel = findViewById(R.id.bottom_panel);
        bottomPanel.setOnClickListener((view) -> switchRouteDetails());
    }

    /**
     * Disables showing and hiding detailed information about route fragments
     * on the bottom panel by clicking it.
     */
    private void disableRouteDetailsExtension() {
        LinearLayout bottomPanel = findViewById(R.id.bottom_panel);
        bottomPanel.setOnClickListener(null);
    }

    /**
     * Shows detailed information about route fragments on the bottom panel if
     * hidden and hides if shown.
     * @see #routeDetailsShown
     * @see #updateBottomPanelToRoute()
     */
    private void switchRouteDetails() {
        routeDetailsShown = !routeDetailsShown;
        updateBottomPanelToRoute();
    }

    /**
     * Shows details of a location on the bottom panel. Disables route details
     * extension.
     * @param location      a location
     * @see #updateBottomPanel
     * @see #disableRouteDetailsExtension
     */
    private void updateBottomPanelToLocation(Location location) {
        disableRouteDetailsExtension();
        updateBottomPanel(location.getPrimaryText(),
                location.getSecondaryText(this), location.getTertiaryText(this));
    }

    /**
     * Shows details of the route on the bottom panel. Enables route details
     * extension.
     * @see #updateBottomPanel
     * @see #enableRouteDetailsExtension
     */
    private void updateBottomPanelToRoute() {
        if (route != null) {
            if (routeDetailsShown)
                updateBottomPanel(route.getPrimaryText(this),
                        route.getSecondaryText(this),
                        route.getTertiaryText());
            else
                updateBottomPanel(route.getPrimaryText(this),
                        route.getSecondaryText(this),
                        getString(R.string.route_details_helper));
            enableRouteDetailsExtension();
        }
        else
            updateBottomPanel(getString(R.string.route_caption_text),
                    null, null);
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
}