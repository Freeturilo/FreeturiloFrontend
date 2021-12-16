package com.example.freeturilo.core;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.freeturilo.R;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class StationStateBikesTest {

    @ParameterizedRobolectricTestRunner.Parameters
    public static Iterable<Object[]> states() {
        return Arrays.asList(new Object[][] {
                {0, R.string.station_working_text},
                {1, R.string.station_reported_text},
                {2, R.string.station_broken_text}
        });
    }

    private final int stationState;
    private final int stateTextId;

    public StationStateBikesTest(int stationState, int stateTextId) {
        this.stationState = stationState;
        this.stateTextId = stateTextId;
    }

    @Test
    public void getStateText() {
        Context context = RuntimeEnvironment.getApplication();

        String result = StationStateBikes.getStateBikesText(context, stationState, 10);

        Assert.assertEquals(context.getString(stateTextId), result);
    }

    @Test
    public void getMarkerIcon() {
        Context context = RuntimeEnvironment.getApplication();
        int markerWidth = context.getResources().getDimensionPixelSize(R.dimen.marker_width);
        int markerHeight = context.getResources().getDimensionPixelSize(R.dimen.marker_height);

        Bitmap result = StationStateBikes.getMarkerIcon(context, stationState, 10);

        assertEquals(markerWidth, result.getWidth());
        assertEquals(markerHeight, result.getHeight());
    }
}