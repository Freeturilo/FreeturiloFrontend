package com.example.freeturilo.connection;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;
import static org.robolectric.shadow.api.Shadow.extract;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.core.ErrorType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.LooperMode;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowToast;

import javax.net.ssl.HttpsURLConnection;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class APIDialogHandlerTest {

    @Implements(DialogFragment.class)
    public static class ShadowDialogFragment {
        @RealObject private DialogFragment dialogFragment;
        boolean hasBeenDismissed = false;

        @Implementation
        public void dismiss() {
            Shadow.directlyOn(dialogFragment, DialogFragment.class, "dismiss");
            hasBeenDismissed = true;
        }
    }

    @Test
    @Config(shadows={ShadowDialogFragment.class})
    public void handle_forDialogDoNotDismiss() {
        Context context = RuntimeEnvironment.getApplication();
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().get();
        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.show(activity.getSupportFragmentManager(), null);
        activity.getSupportFragmentManager().executePendingTransactions();
        APIDialogHandler apiDialogHandler = new APIDialogHandler(dialogFragment, false);
        APIException apiException = new APIException(HttpsURLConnection.HTTP_UNAUTHORIZED);
        ErrorType errorType = ErrorType.getType(HttpsURLConnection.HTTP_UNAUTHORIZED);
        String errorText = ErrorType.getTypeText(context, errorType);

        apiDialogHandler.handle(apiException);
        shadowOf(getMainLooper()).idle();

        assertEquals(errorText, ShadowToast.getTextOfLatestToast());
        assertEquals(Toast.LENGTH_SHORT, ShadowToast.getLatestToast().getDuration());
        ShadowDialogFragment shadowDialogFragment = extract(dialogFragment);
        assertFalse(shadowDialogFragment.hasBeenDismissed);
    }

    @Test
    @Config(shadows={ShadowDialogFragment.class})
    public void handle_forDialogDismiss() {
        Context context = RuntimeEnvironment.getApplication();
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().get();
        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.show(activity.getSupportFragmentManager(), null);
        activity.getSupportFragmentManager().executePendingTransactions();
        APIDialogHandler apiDialogHandler = new APIDialogHandler(dialogFragment, true);
        APIException apiException = new APIException(HttpsURLConnection.HTTP_UNAUTHORIZED);
        ErrorType errorType = ErrorType.getType(HttpsURLConnection.HTTP_UNAUTHORIZED);
        String errorText = ErrorType.getTypeText(context, errorType);

        apiDialogHandler.handle(apiException);
        shadowOf(getMainLooper()).idle();

        assertEquals(errorText, ShadowToast.getTextOfLatestToast());
        assertEquals(Toast.LENGTH_SHORT, ShadowToast.getLatestToast().getDuration());
        ShadowDialogFragment shadowDialogFragment = extract(dialogFragment);
        assertTrue(shadowDialogFragment.hasBeenDismissed);
    }
}