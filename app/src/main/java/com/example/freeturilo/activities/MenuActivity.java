package com.example.freeturilo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.freeturilo.R;
import com.example.freeturilo.misc.AuthTools;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ImageView logo = findViewById(R.id.toolbar).findViewById(R.id.logo);
        logo.setOnLongClickListener(this::goToAdmin);
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void goToRouteCreate(View view) {
        Intent intent = new Intent(this, RouteCreateActivity.class);
        startActivity(intent);
    }

    public void goToHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public boolean goToAdmin(View view) {
        Intent intent;
        if (AuthTools.isLoggedIn())
            intent = new Intent(this, AdminActivity.class);
        else
            intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        return true;
    }
}