package com.example.freeturilo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.freeturilo.BuildConfig;
import com.example.freeturilo.R;
import com.example.freeturilo.misc.AuthTools;
import com.google.android.libraries.places.api.Places;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        ImageView logo = findViewById(R.id.toolbar).findViewById(R.id.logo);
        logo.setOnLongClickListener(this::goToAdmin);
    }

    public void goToMap(@NonNull View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void goToRouteCreate(@NonNull View view) {
        Intent intent = new Intent(this, RouteCreateActivity.class);
        startActivity(intent);
    }

    public void goToHistory(@NonNull View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    private boolean goToAdmin(@NonNull View view) {
        Intent intent;
        if (AuthTools.isLoggedIn())
            intent = new Intent(this, AdminActivity.class);
        else
            intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        return true;
    }
}