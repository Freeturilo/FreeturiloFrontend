package com.example.freeturilo.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.freeturilo.R;

public class MenuActivity extends FreeturiloActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initializeLogoForAdmin();
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
}