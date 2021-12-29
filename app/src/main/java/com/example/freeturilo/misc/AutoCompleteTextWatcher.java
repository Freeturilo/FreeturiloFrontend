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

public class AutoCompleteTextWatcher implements TextWatcher {

    private final RouteCreateActivity activity;
    private final AutocompleteSessionToken token;
    private final AutoCompleteTextView input;
    private final List<Location> customLocations;
    private CancellationTokenSource cancellationTokenSource;

    public AutoCompleteTextWatcher(@NonNull RouteCreateActivity activity,
                                   @NonNull AutoCompleteTextView input,
                                   @NonNull List<Location> customLocations) {
        this.activity = activity;
        this.input = input;
        this.customLocations = customLocations;
        this.token = AutocompleteSessionToken.newInstance();
    }

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
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                IdentifiedLocation idPrediction = new IdentifiedLocation(
                        prediction.getPrimaryText(null).toString(),
                        prediction.getSecondaryText(null).toString(),
                        prediction.getPlaceId(), token);
                autoComplete.add(idPrediction);
            }
            input.setAdapter(adapter);
            adapter.addAll(autoComplete);
            adapter.notifyDataSetChanged();
            if (input.hasFocus())
                input.showDropDown();
        });
    }

    @Override
    public void beforeTextChanged(@NonNull CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(@NonNull Editable editable) {}
}
