package com.example.freeturilo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

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
import android.widget.ImageButton;
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
import com.example.freeturilo.storage.StorageConnector;
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

/**
 * An activity displaying the route create view.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see FreeturiloActivity
 * @see RouteParameters
 */
public class RouteCreateActivity extends FreeturiloActivity {
    /**
     * Stores the storage used by this activity to retrieve user-favourite
     * locations.
     */
    private final StorageConnector storage = new StorageConnector(this);
    /**
     * Stores the API used by this activity to retrieve all stations.
     */
    private final API api = new APIConnector();
    /**
     * Stores the input for the starting point of the created route.
     */
    private AutoCompleteTextView startInput;
    /**
     * Stores the input for the ending point of the created route.
     */
    private AutoCompleteTextView endInput;
    /**
     * Stores a list of inputs for stops of the created route.
     */
    private final List<AutoCompleteTextView> stopInputs = new ArrayList<>();
    /**
     * Stores a list of user-favourite locations and stations to add to
     * suggested locations when performing input autocompletion.
     */
    private final List<Location> customLocations = new ArrayList<>();

    /**
     * Called when this activity is created.
     * <p>
     * Calls {@link FreeturiloActivity#onCreate}. Initializes the layout of
     * this activity, sets {@link #startInput}, {@link #endInput} and
     * initializes them.
     * @param savedInstanceState    unused parameter, included for
     *                              compatibility with
     *                              {@code AppCompatActivity}
     * @see #initializeAutocompleteInput
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_create);
        startInput = this.findViewById(R.id.start_input);
        endInput = this.findViewById(R.id.end_input);
        initializeAutocompleteInput(startInput, R.string.start_input_hint);
        initializeAutocompleteInput(endInput, R.string.end_input_hint);
    }

    /**
     * Initializes an input for a route point (start, end or a stop).
     * <p>
     * Sets hint of the input, its {@code AutoCompleteTextWatcher}, sets a
     * listener for a dropdown suggestion click and a listener for an IME
     * keyboard button click.
     * @param input             the initialized input
     * @param hintResourceId    an integer equal to the resource id of the
     *                          hint of this input
     * @see AutoCompleteTextWatcher
     */
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

    /**
     * Called when this activity is resumed.
     * <p>
     * Sends a request for a list of all stations state to {@link #api}. Starts
     * the retrieval of all user-favourite locations from {@link #storage}.
     * When stations are received they are added to {@link #customLocations}.
     * The same happens to favourites when they are retrieved.
     *
     * @see API#getStationsAsync
     * @see StorageConnector#loadFavouritesAsync
     */
    @Override
    protected void onResume() {
        super.onResume();
        api.getStationsAsync(customLocations::addAll, null);
        storage.loadFavouritesAsync(customLocations::addAll, null);
    }

    /**
     * Called when this activity is paused.
     * <p>
     * Clears {@link #customLocations}.
     */
    @Override
    protected void onPause() {
        super.onPause();
        customLocations.clear();
    }

    /**
     * Creates, initializes and displays a stop input. Called when the add stop
     * button is clicked.
     * <p>
     * Hides the add stop button if it's the third stop input added.
     * @param view      unused parameter, the add stop button
     */
    public void addStop(@NonNull View view) {
        if (stopInputs.size() == 2) {
            Button addStopButton = findViewById(R.id.add_stop_button);
            addStopButton.setVisibility(View.GONE);
        }
        LinearLayout autocompleteInputs = this.findViewById(R.id.autocomplete_inputs);
        LinearLayout stopInputWithButton = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.input_autocomplete_with_button, autocompleteInputs, false);
        AutoCompleteTextView stopInput = stopInputWithButton.findViewById(R.id.autocomplete_input);
        autocompleteInputs.addView(stopInputWithButton, stopInputs.size() + 1);
        stopInputs.add(stopInput);
        initializeAutocompleteInput(stopInput, R.string.stop_hint);
        ImageButton imageButton = stopInputWithButton.findViewById(R.id.remove_input_button);
        imageButton.setOnClickListener((v) -> removeStop(stopInputWithButton));
    }

    /**
     * Removes a stop input. Called when the remove stop button is clicked.
     * <p>
     * Shows the add stop button if it's hidden.
     * @param stopInputWithButton       a linear layout containing the stop
     *                                  input and the remove stop button
     */
    public void removeStop(@NonNull LinearLayout stopInputWithButton) {
        if (stopInputs.size() == 3) {
            Button addStopButton = findViewById(R.id.add_stop_button);
            addStopButton.setVisibility(View.VISIBLE);
        }
        AutoCompleteTextView stopInput = stopInputWithButton.findViewById(R.id.autocomplete_input);
        stopInputs.remove(stopInput);
        stopInputWithButton.setVisibility(View.GONE);
        LinearLayout autocompleteInputs = this.findViewById(R.id.autocomplete_inputs);
        autocompleteInputs.removeView(stopInputWithButton);
    }

    /**
     * Validates input data of the route create form.
     * <p>
     * Checks if all inputs are not empty. If one of them is empty, a
     * validation error occurs.
     * @return              a boolean indicating whether validation has passed
     */
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

    /**
     * Creates route. Called when the create button is clicked.
     * <p>
     * Validates input data of the view. If validation passes, checks if all
     * inputs have a location assigned. If not, for inputs without a defined
     * location, asks the user to specify the location with a dialog. After
     * all inputs have locations specified and assigned, starts the
     * {@code RouteActivity} with defined {@code RouteParameters} and adds the
     * parameters to route history.
     * @param view          unused parameter, the create button
     * @see #validate
     * @see #specifyAddress
     * @see StorageConnector#addToHistoryAsync
     */
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
        Criterion criterion = getCheckedCriterion();
        RouteParameters routeParameters = new RouteParameters(start, end, stops, criterion);
        storage.addToHistoryAsync(routeParameters, new ToastStorageHandler(this));
        Bundle bundle = new Bundle();
        bundle.putBinder(RouteActivity.ROUTE_PARAMETERS_INTENT, new ObjectWrapperForBinder(routeParameters));
        Intent intent = new Intent(this, RouteActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Gets the criterion for which a corresponding criterion button is
     * checked.
     * @return          a criterion that has been chosen with the criterion
     *                  buttons
     */
    @NonNull
    private Criterion getCheckedCriterion() {
        RadioGroup criterionRadioGroup = findViewById(R.id.criterion_buttons);
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

    /**
     * Asynchronously fetches geographical coordinates of a location identified
     * with a Google Maps token from the Google Maps Places API.
     * @param identifiedLocation        a location identified with a Google
     *                                  Maps token
     */
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

    /**
     * Attempts to find addresses within Warsaw that match the location name
     * in input. Uses a {@code Geocoder}. If any addresses are found, shows
     * a dialog for user to specify the correct address. If no addresses are
     * found, sets a validation error of the input.
     * @param input         an input containing the location name with no
     *                      location assigned
     * @see Geocoder#getFromLocationName
     */
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

    /**
     * Assigns a location to an input. Called when one of autocompletion
     * suggestions of the input is chosen.
     * <p>
     * Sets the tag of the input to the location. If location has been obtained
     * with Google Maps API, fetches its geographical coordinates. Sets UI to
     * indicate that location has been assigned.
     * @param input         an input
     * @param location      a location to be assigned to the input
     * @see #fetchLatLng
     */
    private void assignLocation(@NonNull AutoCompleteTextView input,
                                @NonNull Location location) {
        input.setTag(location);
        if (location instanceof IdentifiedLocation)
            fetchLatLng((IdentifiedLocation) location);
        input.setTextColor(getColor(R.color.purple_first));
        input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_autocomplete_assigned, 0, 0, 0);
    }

    /**
     * Clears focus from an input and hides keyboard. Called when one of
     * autocompletion suggestions of the input is chosen.
     * @param input         an input with focus
     */
    private void hideKeyboard(@NonNull AutoCompleteTextView input) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        input.clearFocus();
    }

    /**
     * Assigns a location to an input, clears its focus and hides keyboard.
     * Called when one of autocompletion suggestions of the input is chosen.
     * @param input         an input with focus
     * @param location      a location to be assigned to the input
     * @see #assignLocation
     * @see #hideKeyboard
     */
    private void assignLocationAndHideKeyboard(@NonNull AutoCompleteTextView input,
                                               @NonNull Location location) {
        assignLocation(input, location);
        hideKeyboard(input);
    }

    /**
     * Clears location assignment of an input. Called when text of an input
     * with location assigned changes.
     * <p>
     * If input has location assigned, deletes the assignment and sets UI to
     * indicate location has not been assigned to the input.
     * @param input         an input
     */
    public void clearLocationAssignment(@NonNull AutoCompleteTextView input) {
        if(input.getTag() != null) {
            input.setTag(null);
            input.setTextColor(getColor(R.color.black));
            input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_autocomplete_unassigned,
                    0, 0, 0);
        }
    }
}