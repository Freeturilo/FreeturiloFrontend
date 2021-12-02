package com.example.freeturilo;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import com.example.freeturilo.core.IdentifiedLocation;
import com.example.freeturilo.core.Location;
import com.google.android.gms.maps.model.LatLng;
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

    final private RouteCreateActivity activity;
    final public AutocompleteSessionToken token;
    final private AutoCompleteTextView input;
    final private List<Location> customLocations;

    public AutoCompleteTextWatcher(RouteCreateActivity activity, AutoCompleteTextView input,
                                   List<Location> customLocations) {
        this.activity = activity;
        this.input = input;
        this.customLocations = new ArrayList<>(customLocations);
        this.token = AutocompleteSessionToken.newInstance();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        activity.clearLocationAssignment(input);
        String query = charSequence.toString().replaceAll("^[ \t]+|[ \t]+$", "");
        ArrayList<Location> autoComplete = new ArrayList<>();
        for (Location location : customLocations)
            if (location.name.toLowerCase(Locale.ROOT)
                    .contains(query.toLowerCase(Locale.ROOT)))
                autoComplete.add(location);
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(52.03, 20.80),
                new LatLng(52.36, 21.30)
        );
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setLocationRestriction(bounds)
                .setCountries("PL")
                .setSessionToken(token)
                .setQuery(query)
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
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {}
}
