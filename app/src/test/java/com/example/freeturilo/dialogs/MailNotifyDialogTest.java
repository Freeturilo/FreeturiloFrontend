package com.example.freeturilo.dialogs;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;
import com.example.freeturilo.activities.AdminActivity;
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
        public void getNotifyThresholdAsync(@Nullable Callback<Integer> callback, @Nullable APIHandler handler) {
            if (callback != null)
                callback.call(threshold);
        }

        public apiThreshold(int threshold) {
            super();
            this.threshold = threshold;
        }
    }

    private static class apiThresholdException extends APIConnector {
        @Override
        public void getNotifyThresholdAsync(@Nullable Callback<Integer> callback, @Nullable APIHandler handler) {
            if (handler != null)
                handler.handle(new APIException(HttpsURLConnection.HTTP_UNAUTHORIZED));
        }
    }

    @Test
    public void setThreshold_onPositiveButton() {
        AdminActivity adminActivity = Robolectric.buildActivity(AdminActivity.class).create().start().get();
        DialogFragment dialogFragment = new MailNotifyDialog(new apiThreshold(3), this::positiveCallback);
        dialogFragment.show(adminActivity.getSupportFragmentManager(), null);
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
        AdminActivity adminActivity = Robolectric.buildActivity(AdminActivity.class).create().start().get();
        DialogFragment dialogFragment = new MailNotifyDialog(new apiThreshold(3), this::positiveCallback);
        dialogFragment.show(adminActivity.getSupportFragmentManager(), null);
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
        AdminActivity adminActivity = Robolectric.buildActivity(AdminActivity.class).create().start().get();
        DialogFragment dialogFragment = new MailNotifyDialog(new apiThreshold(3), this::positiveCallback);
        dialogFragment.show(adminActivity.getSupportFragmentManager(), null);
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
        AdminActivity adminActivity = Robolectric.buildActivity(AdminActivity.class).create().start().get();
        DialogFragment dialogFragment = new MailNotifyDialog(new apiThreshold(3), this::positiveCallback);
        dialogFragment.show(adminActivity.getSupportFragmentManager(), null);
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
        AdminActivity adminActivity = Robolectric.buildActivity(AdminActivity.class).create().start().get();
        DialogFragment dialogFragment = new MailNotifyDialog(new apiThresholdException(), this::positiveCallback);

        dialogFragment.show(adminActivity.getSupportFragmentManager(), null);
        shadowOf(getMainLooper()).idle();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();

        assertNull(dialog);
        assertFalse(positiveCallbackCalled);
    }
}