package com.example.freeturilo.misc;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import com.example.freeturilo.activities.RouteCreateActivity;
import com.example.freeturilo.core.IdentifiedLocation;
import com.example.freeturilo.core.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A text watcher of inputs with suggestions retrieved from Google Maps Places
 * API.
 * <p>
 * Object of this class shows dropdown of an {@code AutoCompleteTextView} in
 * the {@code RouteCreateActivity} with {@code Location} suggestions retrieved
 * from Google Maps Places API with the textview content. This
 * {@code TextWatcher} updates the dropdown content everytime the textview text
 * changes. It includes up to 2 stations or user-favourite places in the shown
 * suggestions.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #activity
 * @see #token
 * @see #input
 * @see #customLocations
 * @see #cancellationTokenSource
 * @see #matchCustomLocations
 * @see #onTextChanged
 * @see TextWatcher
 */
public class AutoCompleteTextWatcher implements TextWatcher {
    /**
     * Stores the route create activity within which the input is displayed.
     */
    private final RouteCreateActivity activity;
    /**
     * Stores a token of the Google Maps Places API Autocomplete Session within
     * which the suggestions are retrieved.
     */
    private final AutocompleteSessionToken token;
    /**
     * Stores the input for the content of which suggestions are shown.
     */
    private final AutoCompleteTextView input;
    /**
     * Stores a list of stations and user-favourite locations that can be shown
     * as suggestions for the input's autocompletion.
     */
    private final List<Location> customLocations;
    /**
     * Stores an object used for cancelling a yet unanswered request for
     * suggestions to Google Maps Places API when a new request is sent.
     */
    private CancellationTokenSource cancellationTokenSource;

    /**
     * Class constructor.
     * <p>
     * Instantiates the Autocomplete Session {@link #token}.
     * @param activity          an activity within which the input is displayed
     * @param input             an input for which suggestion retrieval will be
     *                          performed
     * @param customLocations   a list of custom locations that can be used as
     *                          suggestions for the input's autocompletion
     */
    public AutoCompleteTextWatcher(@NonNull RouteCreateActivity activity,
                                   @NonNull AutoCompleteTextView input,
                                   @NonNull List<Location> customLocations) {
        this.activity = activity;
        this.input = input;
        this.customLocations = customLocations;
        this.token = AutocompleteSessionToken.newInstance();
    }

    /**
     * Matches up to 2 locations from {@link #customLocations} based on a
     * query.
     * <p>
     * A location can be matched if its name starts with
     * the query (case-insensitively).
     * @param query     a string equal to a query to which the locations will
     *                  be matched
     * @return          a list of matched locations
     */
    private List<Location> matchCustomLocations(String query) {
        List<Location> matched = new ArrayList<>();
        for (Location location : customLocations)
            if (location.name.toLowerCase(Locale.ROOT).startsWith(query.toLowerCase(Locale.ROOT))) {
                matched.add(location);
                if (matched.size() == 2)
                    break;
            }
        return matched;
    }

    /**
     * Shows Google Maps Places API suggestions for autocompletion of the text
     * in the {@link #input}.
     * <p>
     * Does not show suggestions if the text change is performed as a part of
     * autocompletion. Cancels yet unanswered request for suggestions if one
     * has been sent before. Sends a request for suggestions within Warsaw, PL
     * to Google Maps Places API, and, when retrieved, shows the input's
     * dropdown with those suggestions.
     */
    @Override
    public void onTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {
        if(input.isPerformingCompletion()) return;
        activity.clearLocationAssignment(input);
        if (cancellationTokenSource != null)
            cancellationTokenSource.cancel();
        String query = charSequence.toString().replaceAll("^[ \t]+|[ \t]+$", "");
        ArrayList<Location> autoComplete = new ArrayList<>(matchCustomLocations(query));
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(52.03, 20.80),
                new LatLng(52.36, 21.30)
        );
        this.cancellationTokenSource = new CancellationTokenSource();
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationRestriction(bounds)
                .setCountries("PL")
                .setSessionToken(token)
                .setQuery(query)
                .setCancellationToken(cancellationTokenSource.getToken())
                .build();
        PlacesClient placesClient = Places.createClient(input.getContext());
        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            AutoCompleteAdapter adapter = new AutoCompleteAdapter(input.getContext(), new ArrayList<>());
            for (AutocompletePrediction prediction : response.getAutocompletePredictions())
                autoComplete.add(new IdentifiedLocation(prediction, token));
            input.setAdapter(adapter);
            adapter.addAll(autoComplete);
            adapter.notifyDataSetChanged();
            if (input.getWindowToken() != null && input.hasFocus())
                input.showDropDown();
        });
    }

    @Override
    public void beforeTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(@NonNull Editable editable) {}
}
