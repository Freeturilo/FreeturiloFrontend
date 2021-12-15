package com.example.freeturilo.connection;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.activities.ErrorActivity;
import com.example.freeturilo.core.ErrorType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import javax.net.ssl.HttpsURLConnection;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class APIActivityHandlerTest {

    @Test
    public void handle_forActivityDoNotFinish() {
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        APIActivityHandler apiActivityHandler = new APIActivityHandler(activity, false);
        APIException apiException = new APIException(HttpsURLConnection.HTTP_UNAUTHORIZED);

        apiActivityHandler.handle(apiException);
        shadowOf(getMainLooper()).idle();

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent intent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(intent);
        assertEquals(ErrorActivity.class, shadowIntent.getIntentClass());
        assertEquals(ErrorType.getType(HttpsURLConnection.HTTP_UNAUTHORIZED),
                intent.getSerializableExtra(ErrorActivity.ERROR_TYPE_INTENT));
        assertFalse(activity.isFinishing());
    }

    @Test
    public void handle_forActivityFinish() {
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        APIActivityHandler apiActivityHandler = new APIActivityHandler(activity, true);
        APIException apiException = new APIException(HttpsURLConnection.HTTP_UNAUTHORIZED);

        apiActivityHandler.handle(apiException);
        shadowOf(getMainLooper()).idle();

        ShadowActivity shadowActivity = shadowOf(activity);
        Intent intent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(intent);
        assertEquals(ErrorActivity.class, shadowIntent.getIntentClass());
        assertEquals(ErrorType.getType(HttpsURLConnection.HTTP_UNAUTHORIZED),
                intent.getSerializableExtra(ErrorActivity.ERROR_TYPE_INTENT));
        assertTrue(activity.isFinishing());
    }
}