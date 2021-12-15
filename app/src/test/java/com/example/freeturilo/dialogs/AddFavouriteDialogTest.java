package com.example.freeturilo.dialogs;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class AddFavouriteDialogTest {

    private boolean positiveCallbackCalled = false;
    private Favourite createdFavourite;

    private void positiveCallback(Favourite favourite) {
        positiveCallbackCalled = true;
        createdFavourite = favourite;
    }

    @Test
    public void createsFavourite_onPositiveButton() {
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        LatLng latLng = new LatLng(49, 51);
        AddFavouriteDialog dialogFragment = new AddFavouriteDialog(latLng, this::positiveCallback);
        dialogFragment.show(activity.getSupportFragmentManager(), null);
        shadowOf(getMainLooper()).idle();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        assertNotNull(dialog);
        EditText nameEditText = dialog.findViewById(R.id.name);
        nameEditText.setText("test");
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.performClick();
        shadowOf(getMainLooper()).idle();

        assertTrue(positiveCallbackCalled);
        assertFalse(dialog.isShowing());
        assertNotNull(createdFavourite);
        assertEquals(latLng.latitude, createdFavourite.latitude, 0.000001);
        assertEquals(latLng.longitude, createdFavourite.longitude, 0.000001);
        assertEquals("test", createdFavourite.name);
        assertEquals(FavouriteType.HOME, createdFavourite.type);
    }

    @Test
    public void createsFavouriteWithType_onPositiveButton() {
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        LatLng latLng = new LatLng(49, 51);
        AddFavouriteDialog dialogFragment = new AddFavouriteDialog(latLng, this::positiveCallback);
        dialogFragment.show(activity.getSupportFragmentManager(), null);
        shadowOf(getMainLooper()).idle();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        assertNotNull(dialog);
        EditText nameEditText = dialog.findViewById(R.id.name);
        nameEditText.setText("test");
        RadioButton typeButton = dialog.findViewById(R.id.favourite_school_button);
        typeButton.setChecked(true);
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.performClick();
        shadowOf(getMainLooper()).idle();

        assertTrue(positiveCallbackCalled);
        assertFalse(dialog.isShowing());
        assertNotNull(createdFavourite);
        assertEquals(FavouriteType.SCHOOL, createdFavourite.type);
    }

    @Test
    public void performsValidation_onPositiveButton() {
        Context context = RuntimeEnvironment.getApplication();
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        LatLng latLng = new LatLng(49, 51);
        AddFavouriteDialog dialogFragment = new AddFavouriteDialog(latLng, this::positiveCallback);
        dialogFragment.show(activity.getSupportFragmentManager(), null);
        shadowOf(getMainLooper()).idle();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        assertNotNull(dialog);
        EditText nameEditText = dialog.findViewById(R.id.name);
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.performClick();
        shadowOf(getMainLooper()).idle();

        assertFalse(positiveCallbackCalled);
        assertTrue(dialog.isShowing());
        assertEquals(context.getString(R.string.name_empty_text), nameEditText.getError());
    }

    @Test
    public void dismissed_onNegativeButton() {
        AppCompatActivity activity = Robolectric.buildActivity(AppCompatActivity.class).create().start().get();
        LatLng latLng = new LatLng(49, 51);
        AddFavouriteDialog dialogFragment = new AddFavouriteDialog(latLng, this::positiveCallback);
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
}