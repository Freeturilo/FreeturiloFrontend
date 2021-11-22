package com.example.freeturilo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.freeturilo.core.Favourite;

import java.util.Objects;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Favourite.createFavouritesFile(this);
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void goToRouteCreate(View view) {
        Intent intent = new Intent(this, RouteCreateActivity.class);
        startActivity(intent);
    }
}