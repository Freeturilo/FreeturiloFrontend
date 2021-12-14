package com.example.freeturilo.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.libraries.places.api.model.AutocompleteSessionToken;

public class IdentifiedLocation extends Location {
    public final String details;
    public final String placeId;
    public final AutocompleteSessionToken token;

    public IdentifiedLocation(@NonNull String name, @NonNull String details,
                              @NonNull String placeId, @NonNull AutocompleteSessionToken token) {
        super(name);
        this.details = details;
        this.placeId = placeId;
        this.token = token;
    }

    @NonNull
    @Override
    public String getInlineSecondaryText(@NonNull Context context) {
        return details;
    }
}
