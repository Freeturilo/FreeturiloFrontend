package com.example.freeturilo.dialogs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.misc.Callback;

/**
 * A dialog for editing favourite locations.
 * <p>
 * Object of this class is used to edit a {@code Favourite} location.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see FavouriteDialog
 */
public class EditFavouriteDialog extends FavouriteDialog {

    /**
     * Class constructor.
     * @param favourite         a favourite location to be edited
     * @param positiveCallback  a callback which is called after this dialogs
     *                          finishes with a positive button click
     */
    public EditFavouriteDialog(@NonNull Favourite favourite, @NonNull Callback<Favourite> positiveCallback) {
        super(favourite, positiveCallback);
        this.titleResourceId = R.string.edit_favourite_dialog_title;
        this.positiveTextResourceId = R.string.ok_text;
        this.negativeTextResourceId = R.string.cancel_text;
    }

    /**
     * Creates this dialog. Sets its inputs to match properties of
     * {@link #favourite}.
     * @param savedInstanceState    unused parameter, included for
     *                              compatibility with {@code DialogFragment}
     * @return                      the created {@code AlertDialog}
     *                              representing this dialog
     */
    @NonNull
    @Override
    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        EditText nameEditText = view.findViewById(R.id.name);
        nameEditText.setText(favourite.name);
        RadioButton typeRadioButton = findButtonByType(favourite.type);
        typeRadioButton.setChecked(true);
        return super.onCreateDialog(savedInstanceState);
    }
}
