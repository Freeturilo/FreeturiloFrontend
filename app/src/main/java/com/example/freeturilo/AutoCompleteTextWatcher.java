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
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AutoCompleteTextWatcher implements TextWatcher {

    final public AutocompleteSessionToken token;
    final private AutoCompleteTextView input;
    final private List<Location> customLocations;

    public AutoCompleteTextWatcher(AutoCompleteTextView input, List<Location> customLocations) {
        this.input = input;
        this.customLocations = new ArrayList<>(customLocations);
        this.token = AutocompleteSessionToken.newInstance();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String query = charSequence.toString();
        if (query.length() < input.getThreshold())
            return;
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
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                IdentifiedLocation idPrediction = new IdentifiedLocation(
                        prediction.getPrimaryText(null).toString(),
                        prediction.getPlaceId(), token);
                idPrediction.setAutoCompletePredictionTextWithPrediction(input.getContext(), prediction);
                autoComplete.add(idPrediction);
            }
            AutoCompleteAdapter adapter = new AutoCompleteAdapter(input.getContext(), autoComplete);
            input.setAdapter(adapter);
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {}
}
