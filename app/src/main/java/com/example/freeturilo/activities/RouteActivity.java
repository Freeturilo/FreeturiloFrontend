package com.example.freeturilo.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.freeturilo.connection.API;
import com.example.freeturilo.connection.APIMock;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteActivity extends AppCompatActivity {
    private GoogleMap map;
    private Route route;
    private List<LatLng> path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        API api = new APIMock();
        ObjectWrapperForBinder parameters_wrapper =
                (ObjectWrapperForBinder) getIntent().getExtras().getBinder(getString(R.string.route_parameters_intent_name));
        RouteParameters routeParameters = (RouteParameters) parameters_wrapper.getData();
        RouteParameters.addToHistorySafe(this, routeParameters);
        SupportMapFragment mapFragment = Objects.requireNonNull((SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map));
        Synchronizer createSynchronizer = new Synchronizer(2, this::showRoute);
        mapFragment.getMapAsync(googleMap -> onMapReadySync(googleMap, createSynchronizer));
        api.getRouteAsync(routeParameters,
                retrievedRoute -> onRouteReadySync(retrievedRoute, createSynchronizer), null);
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
        map.setOnMarkerClickListener(this::focus);
    }

    private void onMapReadySync(@NonNull GoogleMap googleMap, @NonNull Synchronizer synchronizer) {
        onMapReady(googleMap);
        synchronizer.decrement();
    }

    private void onRouteReady(@NonNull Route retrievedRoute) {
        route = retrievedRoute;
        path = new ArrayList<>();
        for (DirectionsLeg leg : route.directionsRoute.legs)
            for (DirectionsStep step : leg.steps) {
                List<com.google.maps.model.LatLng> decodedPath = step.polyline.decodePath();
                for (com.google.maps.model.LatLng point : decodedPath)
                    path.add(new LatLng(point.lat, point.lng));
            }
    }

    private void onRouteReadySync(@NonNull Route retrievedRoute, @NonNull Synchronizer synchronizer) {
        onRouteReady(retrievedRoute);
        synchronizer.decrement();
    }

    private void showRoute() {
        PolylineOptions opts = new PolylineOptions().addAll(path).color(getColor(R.color.purple_first));
        map.addPolyline(opts);
        LatLng southwest = new LatLng(route.directionsRoute.bounds.southwest.lat, route.directionsRoute.bounds.southwest.lng);
        LatLng northeast = new LatLng(route.directionsRoute.bounds.northeast.lat, route.directionsRoute.bounds.northeast.lng);
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        for (Location location : route.waypoints) {
            Marker marker = map.addMarker(location.createMarkerOptions(this));
            Objects.requireNonNull(marker).setTag(location);
        }
        updateBottomPanel(route.getPrimaryText(this),
                route.getSecondaryText(this), route.getTertiaryText());
    }

    private void unfocus(@Nullable LatLng latLng) {
        if (route != null)
            updateBottomPanel(route.getPrimaryText(this),
                    route.getSecondaryText(this), route.getTertiaryText());
        else
            updateBottomPanel(getString(R.string.route_caption_text), null, null);
    }

    private boolean focus(@NonNull Marker marker) {
        Location location = Objects.requireNonNull((Location) marker.getTag());
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        updateBottomPanel(location.getPrimaryText(),
                location.getSecondaryText(this), location.getTertiaryText(this));
        return true;
    }

    private void updateBottomPanel(@NonNull String textPrimary, @Nullable String textSecondary, @Nullable String textTertiary) {
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