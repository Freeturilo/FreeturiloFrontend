package com.example.freeturilo.core;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.gms.maps.model.MarkerOptions;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LocationTest {

    @Test
    public void createMarkerOptions() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Location location = new Location("test", 49, 51);

        MarkerOptions result = location.createMarkerOptions(context);

        assertNull(result.getIcon());
        assertEquals(location.latitude, result.getPosition().latitude, 0.000001);
        assertEquals(location.longitude, result.getPosition().longitude, 0.000001);
    }

    @Test
    public void getPrimaryText() {
        Location location = new Location("test", 49, 51);

        String result = location.getPrimaryText();

        assertEquals(location.name, result);
    }

    @Test
    public void getSecondaryText() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Location location = new Location("test", 49, 51);

        String result = location.getSecondaryText(context);

        assertNull(result);
    }

    @Test
    public void getTertiaryText() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Location location = new Location("test", 49, 51);

        String result = location.getTertiaryText(context);

        assertNull(result);
    }

    @Test
    public void getInlineSecondaryText() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Location location = new Location("test", 49, 51);

        String result = location.getInlineSecondaryText(context);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testToString() {
        Location location = new Location("test", 49, 51);

        String result = location.toString();

        assertEquals(location.getPrimaryText(), result);
    }
}