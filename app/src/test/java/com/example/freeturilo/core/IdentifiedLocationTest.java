package com.example.freeturilo.core;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.libraries.places.api.model.AutocompleteSessionToken;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

@RunWith(AndroidJUnit4.class)
public class IdentifiedLocationTest {

    @Test
    public void getInlineSecondaryText() {
        Context context = RuntimeEnvironment.getApplication();
        IdentifiedLocation identifiedLocation = new IdentifiedLocation("test", "details", "id", AutocompleteSessionToken.newInstance());

        String result = identifiedLocation.getInlineSecondaryText(context);

        assertNotNull(result);
        assertEquals(identifiedLocation.details, result);
    }
}