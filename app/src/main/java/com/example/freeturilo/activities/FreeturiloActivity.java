package com.example.freeturilo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.freeturilo.BuildConfig;
import com.example.freeturilo.R;
import com.example.freeturilo.misc.AuthTools;
import com.example.freeturilo.storage.StorageConnector;
import com.example.freeturilo.storage.ToastStorageHandler;
import com.google.android.libraries.places.api.Places;

public class FreeturiloActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        StorageConnector storage = new StorageConnector(this);
        storage.ensureFavouritesExistAsync(new ToastStorageHandler(this));
        storage.ensureHistoryExistsAsync(new ToastStorageHandler(this));
    }

    protected void initializeLogoForAdmin() {
        ImageView logo = findViewById(R.id.toolbar).findViewById(R.id.logo);
        logo.setOnLongClickListener(this::goToAdmin);
    }

    protected boolean goToAdmin(@NonNull View view) {
        Intent intent;
        if (AuthTools.isLoggedIn())
            intent = new Intent(this, AdminActivity.class);
        else
            intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        return true;
    }

    protected void startLoadingAnimation() {
        ProgressBar progressBar = findViewById(R.id.loading);
        progressBar.setAlpha(0.0f);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.animate().alpha(1.0f).setDuration(500);
    }

    protected void stopLoadingAnimation() {
        ProgressBar progressBar = findViewById(R.id.loading);
        progressBar.setAlpha(1.0f);
        progressBar.animate().alpha(0.0f)
                .setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) { progressBar.setVisibility(View.GONE); }
        });
    }
}