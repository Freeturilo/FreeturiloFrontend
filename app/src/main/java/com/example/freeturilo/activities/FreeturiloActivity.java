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

/**
 * A parent activity for all activities of the application.
 * <p>
 * This is an abstract class. Object of a child class of this class is an
 * activity within the application.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see AppCompatActivity
 */
public abstract class FreeturiloActivity extends AppCompatActivity {

    /**
     * Called when this activity is created. When overridden should call
     * super.onCreate.
     * <p>
     * Initializes Google Maps Places API for further requests. Ensures that
     * the favourites file and the history file exist in Internal Storage.
     * @param savedInstanceState    unused parameter, included for
     *                              compatibility with
     *                              {@code AppCompatActivity}
     * @see Places
     * @see StorageConnector
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        StorageConnector storage = new StorageConnector(this);
        storage.ensureFavouritesExistAsync(new ToastStorageHandler(this));
        storage.ensureHistoryExistsAsync(new ToastStorageHandler(this));
    }

    /**
     * Sets a listener for long click of the application logo to start the
     * {@code LoginActivity} (or {@code AdminActivity} if user is logged in).
     * Requires that toolbar is included in the layout of this activity.
     *
     * @see #goToAdmin
     */
    protected void initializeLogoForAdmin() {
        ImageView logo = findViewById(R.id.toolbar).findViewById(R.id.logo);
        logo.setOnLongClickListener(this::goToAdmin);
    }

    /**
     * Starts the {@code LoginActivity} (or {@code AdminActivity} if user is
     * logged in).
     * @param view          unused parameter, an imageview representing the
     *                      application logo
     * @return              a boolean equal to true
     * @see LoginActivity
     * @see AdminActivity
     */
    protected boolean goToAdmin(@NonNull View view) {
        Intent intent;
        if (AuthTools.isLoggedIn())
            intent = new Intent(this, AdminActivity.class);
        else
            intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        return true;
    }

    /**
     * Shows the animated loading icon. Requires that toolbar is included in
     * the layout of this activity.
     */
    protected void startLoadingAnimation() {
        ProgressBar progressBar = findViewById(R.id.loading);
        progressBar.setAlpha(0.0f);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.animate().alpha(1.0f).setDuration(500);
    }

    /**
     * Hides the animated loading icon. Requires that toolbar is included in
     * the layout of this activity.
     */
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