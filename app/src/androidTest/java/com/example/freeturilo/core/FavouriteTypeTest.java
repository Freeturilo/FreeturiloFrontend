package com.example.freeturilo.core;

import static org.junit.Assert.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.freeturilo.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class FavouriteTypeTest {

    @Parameterized.Parameters
    public static Iterable<Object[]> types() {
        return Arrays.asList(new Object[][] {
                {FavouriteType.HOME, R.string.favourite_home_text, R.drawable.marker_home},
                {FavouriteType.SCHOOL, R.string.favourite_school_text, R.drawable.marker_school},
                {FavouriteType.WORK, R.string.favourite_work_text, R.drawable.marker_work},
                {FavouriteType.OTHER, R.string.favourite_other_text, R.drawable.marker_other}
        });
    }

    private final FavouriteType favouriteType;
    private final int typeTextId;
    private final int typeMarkerId;

    public FavouriteTypeTest(FavouriteType favouriteType, int typeTextId, int typeMarkerId) {
        this.favouriteType = favouriteType;
        this.typeTextId = typeTextId;
        this.typeMarkerId = typeMarkerId;
    }

    @Test
    public void getTypeText() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        String result = FavouriteType.getTypeText(context, favouriteType);

        assertEquals(context.getString(typeTextId), result);
    }

    @Test
    public void getMarkerIcon() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Bitmap expected = BitmapFactory.decodeResource(context.getResources(), typeMarkerId);
        int markerWidth = context.getResources().getDimensionPixelSize(R.dimen.marker_width);
        int markerHeight = context.getResources().getDimensionPixelSize(R.dimen.marker_height);
        expected = Bitmap.createScaledBitmap(expected, markerWidth, markerHeight, false);

        Bitmap result = FavouriteType.getMarkerIcon(context, favouriteType);

        assertTrue(expected.sameAs(result));
        assertEquals(markerWidth, result.getWidth());
        assertEquals(markerHeight, result.getHeight());
    }
}