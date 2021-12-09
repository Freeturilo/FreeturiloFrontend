package com.example.freeturilo.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.freeturilo.R;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class StationStateTest {

    @Parameterized.Parameters
    public static Iterable<Object[]> states() {
        return Arrays.asList(new Object[][] {
                {0, R.string.station_working_text, R.drawable.marker_station},
                {1, R.string.station_reported_text, R.drawable.marker_station_reported},
                {2, R.string.station_broken_text, R.drawable.marker_station_broken}
        });
    }

    private final int stationState;
    private final int stateTextId;
    private final int stateMarkerId;

    public StationStateTest(int stationState, int stateTextId, int stateMarkerId) {
        this.stationState = stationState;
        this.stateTextId = stateTextId;
        this.stateMarkerId = stateMarkerId;
    }

    @Test
    public void getStateText() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        String result = StationState.getStateText(context, stationState);

        Assert.assertEquals(context.getString(stateTextId), result);
    }

    @Test
    public void getMarkerIcon() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Bitmap expected = BitmapFactory.decodeResource(context.getResources(), stateMarkerId);
        int markerWidth = context.getResources().getDimensionPixelSize(R.dimen.marker_width);
        int markerHeight = context.getResources().getDimensionPixelSize(R.dimen.marker_height);
        expected = Bitmap.createScaledBitmap(expected, markerWidth, markerHeight, false);

        Bitmap result = StationState.getMarkerIcon(context, stationState);

        assertTrue(expected.sameAs(result));
        assertEquals(markerWidth, result.getWidth());
        assertEquals(markerHeight, result.getHeight());
    }
}