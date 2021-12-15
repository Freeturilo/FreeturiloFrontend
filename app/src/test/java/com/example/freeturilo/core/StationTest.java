package com.example.freeturilo.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import java.util.Locale;

@RunWith(AndroidJUnit4.class)
public class StationTest {

    @Test
    public void getSecondaryText_Working() {
        Context context = RuntimeEnvironment.getApplication();
        Station station = new Station("test", 49, 51, 45, 25, 10, 0);

        String result = station.getSecondaryText(context);

        assertNotNull(result);
        assertTrue(result.contains(context.getString(R.string.station_helper_text).toLowerCase(Locale.ROOT)));
        assertFalse(result.contains(StationState.getStateText(context, station.state)));
        assertFalse(result.contains("\n"));
    }

    @Test
    public void getSecondaryText_Reported() {
        Context context = RuntimeEnvironment.getApplication();
        Station station = new Station("test", 49, 51, 45, 25, 10, 1);

        String result = station.getSecondaryText(context);

        assertNotNull(result);
        assertTrue(result.contains(context.getString(R.string.station_helper_text).toLowerCase(Locale.ROOT)));
        assertTrue(result.contains(StationState.getStateText(context, station.state)));
        assertFalse(result.contains("\n"));
    }

    @Test
    public void getSecondaryText_Broken() {
        Context context = RuntimeEnvironment.getApplication();
        Station station = new Station("test", 49, 51, 45, 25, 10, 2);

        String result = station.getSecondaryText(context);

        assertNotNull(result);
        assertTrue(result.contains(context.getString(R.string.station_helper_text).toLowerCase(Locale.ROOT)));
        assertTrue(result.contains(StationState.getStateText(context, station.state)));
        assertFalse(result.contains("\n"));
    }

    @Test
    public void getTertiaryText() {
        Context context = RuntimeEnvironment.getApplication();
        Station station = new Station("test", 49, 51, 45, 25, 10, 0);

        String result = station.getTertiaryText(context);

        assertNotNull(result);
        assertTrue(result.contains(context.getString(R.string.bikes_availability_text)));
        assertTrue(result.contains(String.valueOf(station.bikes)));
        assertTrue(result.contains(String.valueOf(station.bikes + station.bikeRacks)));
        assertFalse(result.contains("\n"));
    }

    @Test
    public void getInlineSecondaryText() {
        Context context = RuntimeEnvironment.getApplication();
        Station station = new Station("test", 49, 51, 45, 25, 10, 0);

        String result = station.getInlineSecondaryText(context);

        assertNotNull(result);
        assertTrue(result.contains(context.getString(R.string.station_helper_text)));
        assertTrue(result.contains(StationState.getStateText(context, station.state)));
        assertFalse(result.contains("\n"));
    }
}