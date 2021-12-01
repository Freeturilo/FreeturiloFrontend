package com.example.freeturilo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.freeturilo.core.Criterion;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.IdentifiedLocation;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.RouteParameters;
import com.example.freeturilo.core.Station;
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

    private List<AutoCompleteTextView> stop_inputs;
    private List<Location> customLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_create);
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        stop_inputs = new ArrayList<>();
        customLocations = new ArrayList<>();
        customLocations.addAll(Station.loadStations());
        customLocations.addAll(Favourite.loadFavouritesSafe(this, new IgnoreExceptionHandler()));
        AutoCompleteTextView start_input = this.findViewById(R.id.startTextView);
        start_input.addTextChangedListener(new AutoCompleteTextWatcher(start_input, customLocations));
        start_input.setOnItemClickListener((adapterView, view, i, l) -> {
            Object item = adapterView.getItemAtPosition(i);
            start_input.setTag(item);
            if (item instanceof IdentifiedLocation)
                fetchLatLng((IdentifiedLocation) item);
        });
        AutoCompleteTextView end_input = this.findViewById(R.id.endTextView);
        end_input.addTextChangedListener(new AutoCompleteTextWatcher(end_input, customLocations));
        end_input.setOnItemClickListener((adapterView, view, i, l) -> {
            Object item = adapterView.getItemAtPosition(i);
            end_input.setTag(item);
            if (item instanceof IdentifiedLocation)
                fetchLatLng((IdentifiedLocation) item);
        });
    }

    public void addStop(View view1) {
        if (stop_inputs.size() > 2)
            return;
        if (stop_inputs.size() == 2) {
            Button add_stop_button = findViewById(R.id.add_stop_button);
            add_stop_button.setVisibility(View.GONE);
        }
        AutoCompleteTextView input = new AutoCompleteTextView(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        int value1dp = (int) Math.ceil(logicalDensity);
        input.setBackground(AppCompatResources.getDrawable(this, R.color.white));
        input.setTypeface(ResourcesCompat.getFont(this, R.font.quicksand));
        ViewGroup.MarginLayoutParams marginLayoutParams =
                new ViewGroup.MarginLayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        marginLayoutParams.setMargins(0, 10 * value1dp, 0, 0);
        input.setLayoutParams(marginLayoutParams);
        input.setHint(R.string.stop_hint);
        input.setHintTextColor(
                ResourcesCompat.getColor(getResources(), R.color.strong_grey, getTheme()));
        input.setMinimumHeight(50 * value1dp);
        input.setSingleLine();
        input.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_medium));
        input.setThreshold(1);
        input.setPadding(10 * value1dp, 0, 10 * value1dp, 0);
        LinearLayout layout = this.findViewById(R.id.inputs);
        layout.addView(input, stop_inputs.size() + 1);
        stop_inputs.add(input);
        input.addTextChangedListener(new AutoCompleteTextWatcher(input, customLocations));
        input.setOnItemClickListener((adapterView, view, i, l) -> {
                Object item = adapterView.getItemAtPosition(i);
                input.setTag(item);
                if (item instanceof IdentifiedLocation)
                    fetchLatLng((IdentifiedLocation) item);
            });
    }

    public void createRoute(View view) {
        AutoCompleteTextView start_input = this.findViewById(R.id.startTextView);
        AutoCompleteTextView end_input = this.findViewById(R.id.endTextView);
        if (!(start_input.getTag() instanceof Location)) {
            specifyAddress(start_input);
            return;
        }
        Location start = (Location) start_input.getTag();
        if (!(end_input.getTag() instanceof Location)) {
            specifyAddress(end_input);
            return;
        }
        Location end  = (Location) end_input.getTag();
        List<Location> stops = new ArrayList<>();
        for (AutoCompleteTextView stop_input : stop_inputs) {
            if (!(stop_input.getTag() instanceof Location)) {
                specifyAddress(stop_input);
                return;
            }
            stops.add((Location) stop_input.getTag());
        }
        RadioGroup radioGroup = this.findViewById(R.id.criterion_buttons);
        Criterion criterion = getCheckedCriterion(radioGroup);
        RouteParameters parameters = new RouteParameters(start, end, stops, criterion);
        Bundle bundle = new Bundle();
        bundle.putBinder(getString(R.string.route_parameters_intent_name), new ObjectWrapperForBinder(parameters));
        Intent intent = new Intent(this, RouteActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private Criterion getCheckedCriterion(RadioGroup criterionRadioGroup) {
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

    private void fetchLatLng(IdentifiedLocation idLocation) {
        PlacesClient placesClient = Places.createClient(this);
        List<Place.Field> latLngField = Collections.singletonList(Place.Field.LAT_LNG);
        FetchPlaceRequest request = FetchPlaceRequest.builder(idLocation.placeId, latLngField)
                .setSessionToken(idLocation.token).build();
        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            LatLng latLng = Objects.requireNonNull(response.getPlace().getLatLng());
            idLocation.latitude = latLng.latitude;
            idLocation.longitude = latLng.longitude;
        });
    }

    private void specifyAddress(AutoCompleteTextView input) {
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
            String toastMessage = getString(R.string.no_address_to_specify_message)
                    + ": \"" + locationName + "\"";
            Toast toast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        ArrayAdapter<Location> adapter = new ArrayAdapter<>(this,
                R.layout.item_specify_address, locations);
        new AlertDialog.Builder(this, R.style.FreeturiloDialogTheme)
                .setTitle(R.string.specify_address_title)
                .setAdapter(adapter, (dialog, i) -> {
                    input.setText(locations.get(i).toString());
                    input.setTag(locations.get(i));
                    createRoute(input.getRootView().findViewById(R.id.submit_button));
                }).show();
    }
}