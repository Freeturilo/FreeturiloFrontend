package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.libraries.places.api.model.AutocompleteSessionToken;

public class IdentifiedLocation extends Location {
    public final String details;
    public final String placeId;
    public final AutocompleteSessionToken token;

    public IdentifiedLocation(String name, String details,
                              String placeId, AutocompleteSessionToken token) {
        super(name);
        this.details = details;
        this.placeId = placeId;
        this.token = token;
    }

    @NonNull
    @Override
    public String getInlineSecondaryText(Context context) {
        return details;
    }
}
