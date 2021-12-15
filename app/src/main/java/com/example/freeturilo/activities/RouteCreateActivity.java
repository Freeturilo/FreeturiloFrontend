package com.example.freeturilo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.freeturilo.connection.API;
import com.example.freeturilo.connection.APIConnector;
import com.example.freeturilo.misc.AutoCompleteTextWatcher;
import com.example.freeturilo.misc.ObjectWrapperForBinder;
import com.example.freeturilo.R;
import com.example.freeturilo.core.Criterion;
import com.example.freeturilo.core.IdentifiedLocation;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.misc.Validation;
import com.example.freeturilo.storage.StorageManager;
import com.example.freeturilo.storage.ToastStorageHandler;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RouteCreateActivity extends AppCompatActivity {
    private final StorageManager storage = new StorageManager(this);
    private final API api = new APIConnector();
    private AutoCompleteTextView startInput;
    private AutoCompleteTextView endInput;
    private final List<AutoCompleteTextView> stopInputs = new ArrayList<>();
    private final List<Location> customLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_create);
        startInput = this.findViewById(R.id.startTextView);
        endInput = this.findViewById(R.id.endTextView);
        initializeAutocompleteInput(startInput, R.string.start_point_hint);
        initializeAutocompleteInput(endInput, R.string.end_point_hint);
    }

    private void initializeAutocompleteInput(@NonNull AutoCompleteTextView input,
                                             int hintResourceId) {
        input.setHint(hintResourceId);
        input.addTextChangedListener(new AutoCompleteTextWatcher(this, input, customLocations));
        input.setOnItemClickListener((adapterView, view, i, l) ->
                assignLocationAndHideKeyboard(input, (Location) adapterView.getItemAtPosition(i)));
        input.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard((AutoCompleteTextView) v);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        api.getStationsAsync(customLocations::addAll, null);
        storage.loadFavouritesAsync(customLocations::addAll, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        customLocations.clear();
    }

    public void addStop(@NonNull View view) {
        if (stopInputs.size() > 2)
            return;
        if (stopInputs.size() == 2) {
            Button addStopButton = findViewById(R.id.add_stop_button);
            addStopButton.setVisibility(View.GONE);
        }
        LinearLayout autocompleteInputs = this.findViewById(R.id.autocomplete_inputs);
        AutoCompleteTextView stopInput = (AutoCompleteTextView) getLayoutInflater()
                .inflate(R.layout.input_autocomplete, autocompleteInputs, false);
        autocompleteInputs.addView(stopInput, stopInputs.size() + 1);
        stopInputs.add(stopInput);
        initializeAutocompleteInput(stopInput, R.string.stop_hint);
    }

    private boolean validate() {
        boolean valid = true;
        List<AutoCompleteTextView> inputs = new ArrayList<>();
        inputs.add(startInput);
        inputs.addAll(stopInputs);
        inputs.add(endInput);
        for (AutoCompleteTextView input : inputs) {
            if (Validation.isEmpty(input)) {
                valid = false;
                Validation.setInputError(this, input, R.string.autocomplete_empty_error_text);
            }
        }
        return valid;
    }

    public void createRoute(@NonNull View view) {
        if(!validate())
            return;
        if (!(startInput.getTag() instanceof Location)) {
            specifyAddress(startInput);
            return;
        }
        Location start = (Location) startInput.getTag();
        if (!(endInput.getTag() instanceof Location)) {
            specifyAddress(endInput);
            return;
        }
        Location end  = (Location) endInput.getTag();
        List<Location> stops = new ArrayList<>();
        for (AutoCompleteTextView stopInput : stopInputs) {
            if (!(stopInput.getTag() instanceof Location)) {
                specifyAddress(stopInput);
                return;
            }
            stops.add((Location) stopInput.getTag());
        }
        RadioGroup radioGroup = this.findViewById(R.id.criterion_buttons);
        Criterion criterion = getCheckedCriterion(radioGroup);
        RouteParameters routeParameters = new RouteParameters(start, end, stops, criterion);
        storage.addToHistoryAsync(routeParameters, new ToastStorageHandler(this));
        Bundle bundle = new Bundle();
        bundle.putBinder(RouteActivity.ROUTE_PARAMETERS_INTENT, new ObjectWrapperForBinder(routeParameters));
        Intent intent = new Intent(this, RouteActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @NonNull
    private Criterion getCheckedCriterion(@NonNull RadioGroup criterionRadioGroup) {
        int buttonId = criterionRadioGroup.getCheckedRadioButtonId();
        final int costId = R.id.criterion_cost_button;
        final int hybridId = R.id.criterion_hybrid_button;
        switch (buttonId) {
            case costId:
                return Criterion.COST;
            case hybridId:
                return Criterion.HYBRID;
            default:
                return Criterion.TIME;
        }
    }

    private void fetchLatLng(@NonNull IdentifiedLocation identifiedLocation) {
        PlacesClient placesClient = Places.createClient(this);
        List<Place.Field> latLngField = Collections.singletonList(Place.Field.LAT_LNG);
        FetchPlaceRequest request = FetchPlaceRequest
                .builder(identifiedLocation.placeId, latLngField)
                .setSessionToken(identifiedLocation.token).build();
        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            LatLng latLng = Objects.requireNonNull(response.getPlace().getLatLng());
            identifiedLocation.latitude = latLng.latitude;
            identifiedLocation.longitude = latLng.longitude;
        });
    }

    private void specifyAddress(@NonNull AutoCompleteTextView input) {
        String locationName = input.getText().toString();
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(locationName,
                    5, 52.03, 20.80,
                    52.36, 21.30 );
        } catch (IOException exception) {
            return;
        }
        List<Location> locations = new ArrayList<>();
        for (Address address : addresses)
            if (address.getMaxAddressLineIndex() >= 0)
                locations.add(new Location(address.getAddressLine(0),
                            address.getLatitude(), address.getLongitude()));
        if(locations.isEmpty()) {
            Validation.setInputError(this, input,
                    R.string.autocomplete_not_found_error_text);
            return;
        }
        ArrayAdapter<Location> adapter = new ArrayAdapter<>(this,
                R.layout.item_specify_address, locations);
        new AlertDialog.Builder(this, R.style.FreeturiloDialogTheme)
                .setTitle(R.string.specify_address_title)
                .setAdapter(adapter, (dialog, i) -> {
                    input.setText(locations.get(i).toString());
                    assignLocation(input, locations.get(i));
                    createRoute(input.getRootView().findViewById(R.id.submit_button));
                }).show();
    }

    private void assignLocation(@NonNull AutoCompleteTextView input,
                                @NonNull Location location) {
        input.setTag(location);
        if (location instanceof IdentifiedLocation)
            fetchLatLng((IdentifiedLocation) location);
        input.setTextColor(getColor(R.color.purple_first));
        input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_autocomplete_assigned, 0, 0, 0);
    }

    private void hideKeyboard(@NonNull AutoCompleteTextView input) {
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        input.clearFocus();
    }

    private void assignLocationAndHideKeyboard(@NonNull AutoCompleteTextView input,
                                               @NonNull Location location) {
        assignLocation(input, location);
        hideKeyboard(input);
    }

    public void clearLocationAssignment(@NonNull AutoCompleteTextView input) {
        if(input.getTag() != null) {
            input.setTag(null);
            input.setTextColor(getColor(R.color.black));
            input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_autocomplete_unassigned,
                    0, 0, 0);
        }
    }
}