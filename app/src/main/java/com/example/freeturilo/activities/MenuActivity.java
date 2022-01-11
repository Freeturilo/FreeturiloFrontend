package com.example.freeturilo.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.freeturilo.R;

/**
 * An activity displaying the main menu.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see FreeturiloActivity
 */
public class MenuActivity extends FreeturiloActivity {

    /**
     * Called when this activity is created.
     * <p>
     * Calls {@link FreeturiloActivity#onCreate}. Initializes the layout of
     * this activity and the logo in toolbar for an administrator.
     * @param savedInstanceState    unused parameter, included for
     *                              compatibility with
     *                              {@code AppCompatActivity}
     * @see #initializeLogoForAdmin
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initializeLogoForAdmin();
    }

    /**
     * Starts the {@code MapActivity}. Called after a map button click.
     * @param view          unused parameter, the map button
     * @see MapActivity
     */
    public void goToMap(@NonNull View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the {@code RouteCreateActivity}. Called after a route button
     * click.
     * @param view          unused parameter, the route button
     * @see RouteCreateActivity
     */
    public void goToRouteCreate(@NonNull View view) {
        Intent intent = new Intent(this, RouteCreateActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the {@code HistoryActivity}. Called after a route history button
     * click.
     * @param view          unused parameter, the route history button
     * @see HistoryActivity
     */
    public void goToHistory(@NonNull View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
}