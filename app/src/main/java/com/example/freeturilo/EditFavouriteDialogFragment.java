package com.example.freeturilo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.example.freeturilo.core.Favourite;

public class EditFavouriteDialogFragment extends FavouriteDialogFragment {
    Favourite favourite;

    public EditFavouriteDialogFragment(Favourite favourite) {
        super();
        this.favourite = favourite;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        EditText nameEditText = view.findViewById(R.id.name);
        nameEditText.setText(favourite.name);
        RadioButton typeRadioButton = (RadioButton) findButtonByType(favourite.type);
        typeRadioButton.setChecked(true);
        return createBuilder()
                .setTitle(R.string.edit_favourite_dialog_title)
                .setPositiveButton(R.string.ok_text, this::onPositiveButton)
                .setNegativeButton(R.string.cancel_text, null)
                .create();
    }

    private void onPositiveButton(DialogInterface dialog, int id) {
        final EditText nameEditText = view.findViewById(R.id.name);
        favourite.name = nameEditText.getText().toString();
        final RadioGroup typeRadioGroup = view.findViewById(R.id.favourite_buttons);
        favourite.type = getCheckedType(typeRadioGroup);
        mapActivity.updateFavourite(favourite);
    }
}