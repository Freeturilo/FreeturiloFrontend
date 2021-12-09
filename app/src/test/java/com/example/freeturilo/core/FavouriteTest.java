package com.example.freeturilo.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import java.util.Locale;

@RunWith(AndroidJUnit4.class)
public class FavouriteTest {

    @Test
    public void getSecondaryText() {
        Context context = RuntimeEnvironment.getApplication();
        Favourite favourite = new Favourite("test", 49, 51, FavouriteType.SCHOOL);

        String result = favourite.getSecondaryText(context);

        assertNotNull(result);
        assertTrue(result.contains(context.getString(R.string.favourite_helper_text).toLowerCase(Locale.ROOT)));
        assertTrue(result.contains(FavouriteType.getTypeText(context, favourite.type)));
        assertFalse(result.contains("\n"));
    }

    @Test
    public void getTertiaryText() {
        Context context = RuntimeEnvironment.getApplication();
        Favourite favourite = new Favourite("test", 49, 51, FavouriteType.SCHOOL);

        String result = favourite.getTertiaryText(context);

        assertNull(result);
    }

    @Test
    public void getInlineSecondaryText() {
        Context context = RuntimeEnvironment.getApplication();
        Favourite favourite = new Favourite("test", 49, 51, FavouriteType.SCHOOL);

        String result = favourite.getInlineSecondaryText(context);

        assertNotNull(result);
        assertTrue(result.contains(context.getString(R.string.favourite_helper_text)));
        assertTrue(result.contains(FavouriteType.getTypeText(context, favourite.type)));
        assertFalse(result.contains("\n"));
    }
}