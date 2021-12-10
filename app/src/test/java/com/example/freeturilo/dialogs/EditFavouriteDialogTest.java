package com.example.freeturilo.dialogs;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;
import com.example.freeturilo.activities.MapActivity;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class EditFavouriteDialogTest {

    private boolean positiveCallbackCalled = false;
    private Favourite editedFavourite;

    private void positiveCallback(Favourite favourite) {
        positiveCallbackCalled = true;
        editedFavourite = favourite;
    }

    @Test
    public void editsFavourite_onPositiveButton() {
        MapActivity mapActivity = Robolectric.buildActivity(MapActivity.class).create().start().get();
        Favourite favourite = new Favourite("test", 49, 51, FavouriteType.WORK);
        EditFavouriteDialog dialogFragment = new EditFavouriteDialog(favourite, this::positiveCallback);
        dialogFragment.show(mapActivity.getSupportFragmentManager(), null);
        shadowOf(getMainLooper()).idle();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        assertNotNull(dialog);
        EditText nameEditText = dialog.findViewById(R.id.name);
        nameEditText.setText("new test");
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.performClick();
        shadowOf(getMainLooper()).idle();

        assertTrue(positiveCallbackCalled);
        assertFalse(dialog.isShowing());
        assertEquals(favourite, editedFavourite);
        assertEquals(favourite.latitude, editedFavourite.latitude, 0.000001);
        assertEquals(favourite.longitude, editedFavourite.longitude, 0.000001);
        assertEquals("new test", editedFavourite.name);
        assertEquals(FavouriteType.WORK, editedFavourite.type);
    }

    @Test
    public void editsFavouriteWithType_onPositiveButton() {
        MapActivity mapActivity = Robolectric.buildActivity(MapActivity.class).create().start().get();
        Favourite favourite = new Favourite("test", 49, 51, FavouriteType.WORK);
        EditFavouriteDialog dialogFragment = new EditFavouriteDialog(favourite, this::positiveCallback);
        dialogFragment.show(mapActivity.getSupportFragmentManager(), null);
        shadowOf(getMainLooper()).idle();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        assertNotNull(dialog);
        EditText nameEditText = dialog.findViewById(R.id.name);
        nameEditText.setText("new test");
        RadioButton typeButton = dialog.findViewById(R.id.favourite_school_button);
        typeButton.setChecked(true);
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.performClick();
        shadowOf(getMainLooper()).idle();

        assertTrue(positiveCallbackCalled);
        assertFalse(dialog.isShowing());
        assertEquals(favourite, editedFavourite);
        assertEquals(favourite.latitude, editedFavourite.latitude, 0.000001);
        assertEquals(favourite.longitude, editedFavourite.longitude, 0.000001);
        assertEquals("new test", editedFavourite.name);
        assertEquals(FavouriteType.SCHOOL, editedFavourite.type);
    }

    @Test
    public void performsValidation_onPositiveButton() {
        Context context = RuntimeEnvironment.getApplication();
        MapActivity mapActivity = Robolectric.buildActivity(MapActivity.class).create().start().get();
        Favourite favourite = new Favourite("test", 49, 51, FavouriteType.WORK);
        EditFavouriteDialog dialogFragment = new EditFavouriteDialog(favourite, this::positiveCallback);
        dialogFragment.show(mapActivity.getSupportFragmentManager(), null);
        shadowOf(getMainLooper()).idle();
        AlertDialog dialog = (AlertDialog) dialogFragment.getDialog();
        assertNotNull(dialog);
        EditText nameEditText = dialog.findViewById(R.id.name);
        nameEditText.setText("");
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.performClick();
        shadowOf(getMainLooper()).idle();

        assertFalse(positiveCallbackCalled);
        assertTrue(dialog.isShowing());
        assertEquals(context.getString(R.string.name_empty_text), nameEditText.getError());
        assertEquals("test", favourite.name);
    }

    @Test
    public void dismissed_onNegativeButton() {
        MapActivity mapActivity = Robolectric.buildActivity(MapActivity.class).create().start().get();
        Favourite favourite = new Favourite("test", 49, 51, FavouriteType.WORK);
        EditFavouriteDialog dialogFragment = new EditFavouriteDialog(favourite, this::positiveCallback);
        dialogFragment.show(mapActivity.getSupportFragmentManager(), null);
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