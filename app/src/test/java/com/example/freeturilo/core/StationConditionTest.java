package com.example.freeturilo.core;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.freeturilo.R;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class StationConditionTest {

    @ParameterizedRobolectricTestRunner.Parameters
    public static Iterable<Object[]> states() {
        return Arrays.asList(new Object[][] {
                {0, 0, R.string.station_empty_text, R.drawable.marker_station_broken_empty},
                {1, 0, R.string.station_reported_text, R.drawable.marker_station_reported_almost_empty},
                {2, 0, R.string.station_broken_text, R.drawable.marker_station_broken_empty},
                {0, 2, R.string.station_almost_empty_text, R.drawable.marker_station_reported_almost_empty},
                {1, 2, R.string.station_reported_text, R.drawable.marker_station_reported_almost_empty},
                {2, 2, R.string.station_broken_text, R.drawable.marker_station_broken_empty},
                {0, 5, R.string.station_working_text, R.drawable.marker_station},
                {1, 5, R.string.station_reported_text, R.drawable.marker_station_reported_almost_empty},
                {2, 5, R.string.station_broken_text, R.drawable.marker_station_broken_empty}
        });
    }

    private final Station station;
    private final int stateTextId;
    private final int stateMarkerId;

    public StationConditionTest(int stationState, int stationBikes, int stateTextId, int stateMarkerId) {
        this.station = new Station("Test", 49, 51,
                                1, 20, stationBikes, stationState);
        this.stateTextId = stateTextId;
        this.stateMarkerId = stateMarkerId;
    }

    private Bitmap getBitmap(Context context, int stateMarkerId) {
        int markerWidth = context.getResources().getDimensionPixelSize(R.dimen.marker_width);
        int markerHeight = context.getResources().getDimensionPixelSize(R.dimen.marker_height);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), stateMarkerId, options);
        options.inSampleSize = options.outWidth/markerWidth;
        options.inJustDecodeBounds = false;
        Bitmap stateImage = BitmapFactory.decodeResource(context.getResources(), stateMarkerId, options);
        return Bitmap.createScaledBitmap(stateImage, markerWidth, markerHeight, false);
    }

    @Test
    public void getStateText() {
        Context context = RuntimeEnvironment.getApplication();

        String result = StationCondition.getConditionText(context, station);

        Assert.assertEquals(context.getString(stateTextId), result);
    }

    @Test
    public void getMarkerIcon() {
        Context context = RuntimeEnvironment.getApplication();

        Bitmap result = StationCondition.getMarkerIcon(context, station);

        assertTrue(getBitmap(context, stateMarkerId).sameAs(result));
    }
}