package com.example.freeturilo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.freeturilo.core.Favourite;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    List<Favourite> favourites;

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
        LatLng warsaw = new LatLng(52.23, 21);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(warsaw, 12));
        try {
            favourites = Favourite.loadFavourites(getApplicationContext());
            for(Favourite favourite : favourites)
                map.addMarker(favourite.createMarkerOptions());
        }
        catch (IOException exception) {
            favourites = new ArrayList<>();
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.no_favourites_message, Toast.LENGTH_SHORT);
            toast.show();
        }
        map.setOnMapLongClickListener(this::addFavourite);
    }

    private void addFavourite(LatLng latLng) {

    }
}