package com.example.freeturilo.core;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.freeturilo.R;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.MarkerOptions;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

@RunWith(AndroidJUnit4.class)
public class FavouriteTest {

    @Test
    public void createMarkerOptions() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        MapsInitializer.initialize(context);
        Favourite favourite = new Favourite("test", 49, 51, FavouriteType.SCHOOL);

        MarkerOptions result = favourite.createMarkerOptions(context);

        assertNotNull(result.getIcon());
    }

    @Test
    public void getSecondaryText() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Favourite favourite = new Favourite("test", 49, 51, FavouriteType.SCHOOL);

        String result = favourite.getSecondaryText(context);

        assertNotNull(result);
        assertTrue(result.contains(context.getString(R.string.favourite_helper_text).toLowerCase(Locale.ROOT)));
        assertTrue(result.contains(FavouriteType.getTypeText(context, favourite.type)));
        assertFalse(result.contains("\n"));
    }

    @Test
    public void getTertiaryText() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Favourite favourite = new Favourite("test", 49, 51, FavouriteType.SCHOOL);

        String result = favourite.getTertiaryText(context);

        assertNull(result);
    }

    @Test
    public void getInlineSecondaryText() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Favourite favourite = new Favourite("test", 49, 51, FavouriteType.SCHOOL);

        String result = favourite.getInlineSecondaryText(context);

        assertNotNull(result);
        assertTrue(result.contains(context.getString(R.string.favourite_helper_text)));
        assertTrue(result.contains(FavouriteType.getTypeText(context, favourite.type)));
        assertFalse(result.contains("\n"));
    }
}