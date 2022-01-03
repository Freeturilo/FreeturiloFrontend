package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;

/**
 * An identified geographical location.
 * Object of this class represents a real-world location identified with
 * a Google Maps identifier. It is always obtained from an autocomplete
 * prediction within an autocomplete session. It encapsulates the
 * Google Maps identifier ({@link #placeId}), {@link #name} and
 * {@link #details} of the location and {@link #token} of the autocomplete
 * session within which the location has been obtained. Values of its
 * geographical coordinates fields ({@link #latitude} and {@link #longitude})
 * are always equal to NaN.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #details
 * @see #placeId
 * @see #token
 * @see Location
 * @see AutocompletePrediction
 * @see AutocompleteSessionToken
 */
public class IdentifiedLocation extends Location {
    /**
     * Stores detailed description of this location.
     */
    public final String details;
    /**
     * Stores this location's Google Maps identifier.
     */
    public final String placeId;
    /**
     * Stores a token of the autocomplete session which was the source of this
     * location.
     */
    public final AutocompleteSessionToken token;

    /**
     * Class constructor. Initializes {@link #name} and {@link #details} as
     * primary and secondary text of the prediction respectively. Initializes
     * {@link #latitude} and {@link #longitude} both as NaN.
     * @param prediction    an autocomplete prediction on which this location
     *                      is based
     * @param token         the token of an autocomplete session which was
     *                      source of the prediction
     */
    public IdentifiedLocation(@NonNull AutocompletePrediction prediction,
                              @NonNull AutocompleteSessionToken token) {
        super(prediction.getPrimaryText(null).toString(), Double.NaN, Double.NaN);
        this.details = prediction.getSecondaryText(null).toString();
        this.placeId = prediction.getPlaceId();
        this.token = token;
    }

    /**
     * Gets a composite description of details of this location. Contains
     * {@link #details}.
     * @param context   the context of the application providing all global
     *                  information
     * @return          a string providing details of this location to put in
     *                  one line
     * @see Context
     */
    @NonNull
    @Override
    public String getInlineSecondaryText(@NonNull Context context) {
        return details;
    }
}
