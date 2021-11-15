package com.example.freeturilo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    List<Favourite> favourites;
    List<Station> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setSupportActionBar(findViewById(R.id.toolbar));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
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
        showFavourites();
        showStations();
        map.setOnMapClickListener(this::unfocus);
        map.setOnMapLongClickListener(this::showFavouriteDialog);
        map.setOnMarkerClickListener(this::showMarkerInfo);
    }

    private void showStations() {
        stations = Station.loadStations();
        for(Station station : stations) {
            Marker marker = map.addMarker(station.createMarkerOptions(this));
            Objects.requireNonNull(marker).setTag(station);
        }
    }

    private void unfocus(LatLng latLng) {
        TextView bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setText(getString(R.string.map_caption_text));
    }

    private void showFavourites() {
        try {
            favourites = Favourite.loadFavourites(getApplicationContext());
        }
        catch (IOException exception) {
            favourites = new ArrayList<>();
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.no_favourites_message, Toast.LENGTH_SHORT);
            toast.show();
        }
        finally {
            for(Favourite favourite : favourites) {
                Marker marker = map.addMarker(favourite.createMarkerOptions(this));
                Objects.requireNonNull(marker).setTag(favourite);
            }
        }
    }

    private void showFavouriteDialog(LatLng latLng) {
        FavouriteDialogFragment dialog = new FavouriteDialogFragment();
        dialog.latLng = latLng;
        dialog.show(getSupportFragmentManager(), "FavouriteDialogFragment");
    }

    private boolean showMarkerInfo(Marker marker) {
        Location location = Objects.requireNonNull((Location) marker.getTag());
        LatLng latLng = new LatLng(location.latitude, location.longitude);
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        TextView bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setText(location.createCaption(this));
        return true;
    }

    public void addFavourite(Favourite favourite) {
        favourites.add(favourite);
        try {
            Favourite.saveFavourites(this, favourites);
        }
        catch (IOException exception) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.no_favourites_message, Toast.LENGTH_SHORT);
            toast.show();
        }
        finally {
            Marker marker = map.addMarker(favourite.createMarkerOptions(this));
            Objects.requireNonNull(marker).setTag(favourite);
        }
    }
}