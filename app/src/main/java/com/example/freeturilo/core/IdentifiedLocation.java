package com.example.freeturilo.core;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.example.freeturilo.R;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;

public class IdentifiedLocation extends Location {
    public String placeId;
    public AutocompleteSessionToken token;

    public IdentifiedLocation(String name, String placeId, AutocompleteSessionToken token) {
        super(name);
        this.placeId = placeId;
        this.token = token;
    }

    @Override
    public void setAutoCompletePredictionTextWithPrediction(Context context,
                                                            AutocompletePrediction prediction) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(prediction.getPrimaryText(new StyleSpan(Typeface.BOLD)));
        SpannableString secondaryText =
                new SpannableString(", " + prediction.getSecondaryText(null));
        secondaryText.setSpan(new ForegroundColorSpan(context.getColor(R.color.grey)),
                0, secondaryText.length(), 0);
        int smallSize = context.getResources().getDimensionPixelSize(R.dimen.text_size_small);
        secondaryText.setSpan(new AbsoluteSizeSpan(smallSize), 0, secondaryText.length(), 0);
        builder.append(secondaryText);
        autoCompletePredictionText = builder;
    }
}
