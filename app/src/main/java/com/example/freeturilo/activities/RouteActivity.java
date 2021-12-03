package com.example.freeturilo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.freeturilo.misc.ObjectWrapperForBinder;
import com.example.freeturilo.R;
import com.example.freeturilo.core.CriterionTools;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.RouteParameters;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.Locale;
import java.util.Objects;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private RouteParameters routeParameters;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        ObjectWrapperForBinder parameters_wrapper = (ObjectWrapperForBinder) getIntent().getExtras()
                        .getBinder(getString(R.string.route_parameters_intent_name));
        routeParameters = (RouteParameters) parameters_wrapper.getData();
        RouteParameters.addToHistorySafe(this, routeParameters);
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
        map.addMarker(routeParameters.start.createMarkerOptions(this));
        map.addMarker(routeParameters.end.createMarkerOptions(this));
        for (Location stop : routeParameters.stops)
            map.addMarker(stop.createMarkerOptions(this));
        TextView bottomBar = findViewById(R.id.bottom_panel_primary);
        String criterionText = CriterionTools.getCriterionText(this, routeParameters.criterion);
        StringBuilder bottomBarText = new StringBuilder();
        bottomBarText.append(criterionText.substring(0, 1).toUpperCase(Locale.ROOT));
        bottomBarText.append(criterionText.substring(1));
        bottomBarText.append(" ");
        bottomBarText.append(getString(R.string.route_text));
        bottomBar.setText(bottomBarText);
    }
}