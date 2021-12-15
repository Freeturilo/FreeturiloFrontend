package com.example.freeturilo.dialogs;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;
import com.example.freeturilo.connection.APIConnector;
import com.example.freeturilo.connection.APIException;
import com.example.freeturilo.connection.APIHandler;
import com.example.freeturilo.misc.Callback;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.LooperMode;

import javax.net.ssl.HttpsURLConnection;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class MailNotifyDialogTest {

    private boolean positiveCallbackCalled = false;
    private int setThreshold = -1;

    private void positiveCallback(int threshold) {
        positiveCallbackCalled = true;
        setThreshold = threshold;
    }

    private static class apiThreshold extends APIConnector {
        private final int threshold;

        @Override
        public Thread getNotifyThresholdAsync(@Nullable Callback<Integer> callback, @Nullable APIHandler handler) {
            if (callback != null)
                callback.call(threshold);
            return null;
        }

        public apiThreshold(int threshold) {
            super();
            this.threshold = threshold;
        }
    }

    private static class apiThresholdException extends APIConnector {
        @Override
        public Thread getNotifyThresholdAsync(@Nullable Callback<Integer> callback, @Nullable APIHandler handler) {
            if (handler != null)
                handler.handle(new APIException(HttpsURLConnection.HTTP_UNAUTHORIZED));
            return null;
        }
    }

    @Test
    public void setThreshold_onPositiveButton() {
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        DialogFragment dialogFragment = new MailNotifyDialog(new apiThreshold(3), this::positiveCallback);
        dialogFragment.show(activity.getSupportFragmentManager(), null);
        shadowOf(getMainLooper()).idle();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        assertNotNull(dialog);
        EditText notifyThresholdInput = dialog.findViewById(R.id.notify_threshold_input);
        notifyThresholdInput.setText("5");
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.performClick();
        shadowOf(getMainLooper()).idle();

        assertTrue(positiveCallbackCalled);
        assertFalse(dialog.isShowing());
        assertEquals(5, setThreshold);
    }

    @Test
    public void setThresholdToZero_onPositiveButton() {
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        DialogFragment dialogFragment = new MailNotifyDialog(new apiThreshold(3), this::positiveCallback);
        dialogFragment.show(activity.getSupportFragmentManager(), null);
        shadowOf(getMainLooper()).idle();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        assertNotNull(dialog);
        SwitchCompat notifySwitch = dialog.findViewById(R.id.notify_switch);
        notifySwitch.setChecked(false);
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.performClick();
        shadowOf(getMainLooper()).idle();

        assertTrue(positiveCallbackCalled);
        assertFalse(dialog.isShowing());
        assertEquals(0, setThreshold);
    }

    @Test
    public void performsValidation_onPositiveButton() {
        Context context = RuntimeEnvironment.getApplication();
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        DialogFragment dialogFragment = new MailNotifyDialog(new apiThreshold(3), this::positiveCallback);
        dialogFragment.show(activity.getSupportFragmentManager(), null);
        shadowOf(getMainLooper()).idle();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        assertNotNull(dialog);
        EditText notifyThresholdInput = dialog.findViewById(R.id.notify_threshold_input);
        notifyThresholdInput.setText("r5");
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.performClick();
        shadowOf(getMainLooper()).idle();

        assertFalse(positiveCallbackCalled);
        assertTrue(dialog.isShowing());
        assertEquals(context.getString(R.string.threshold_invalid_text), notifyThresholdInput.getError());
    }

    @Test
    public void dismissed_onNegativeButton() {
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        DialogFragment dialogFragment = new MailNotifyDialog(new apiThreshold(3), this::positiveCallback);
        dialogFragment.show(activity.getSupportFragmentManager(), null);
        shadowOf(getMainLooper()).idle();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        assertNotNull(dialog);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        negativeButton.performClick();
        shadowOf(getMainLooper()).idle();

        assertFalse(positiveCallbackCalled);
        assertFalse(dialog.isShowing());
    }

    @Test
    public void dismissed_onException() {
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        DialogFragment dialogFragment = new MailNotifyDialog(new apiThresholdException(), this::positiveCallback);

        dialogFragment.show(activity.getSupportFragmentManager(), null);
        shadowOf(getMainLooper()).idle();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();

        assertNull(dialog);
        assertFalse(positiveCallbackCalled);
    }
}