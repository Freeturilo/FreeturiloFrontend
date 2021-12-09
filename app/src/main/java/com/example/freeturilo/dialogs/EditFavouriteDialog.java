package com.example.freeturilo.dialogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.misc.Callback;

public class EditFavouriteDialog extends FavouriteDialog {
    private final Favourite favourite;

    public EditFavouriteDialog(@NonNull Favourite favourite, @NonNull Callback<Favourite> positiveCallback) {
        super(positiveCallback);
        this.favourite = favourite;
    }

    @NonNull
    @Override
    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        EditText nameEditText = view.findViewById(R.id.name);
        nameEditText.setText(favourite.name);
        RadioButton typeRadioButton = (RadioButton) findButtonByType(favourite.type);
        typeRadioButton.setChecked(true);
        AlertDialog dialog = createBuilder()
                .setTitle(R.string.edit_favourite_dialog_title)
                .setPositiveButton(R.string.ok_text, null)
                .setNegativeButton(R.string.cancel_text, null)
                .create();
        dialog.setOnShowListener(this::onShow);
        return dialog;
    }

    private void onShow(@NonNull DialogInterface dialog) {
        Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener((view) -> onPositiveButton(dialog));
    }

    private void onPositiveButton(@NonNull DialogInterface dialog) {
        if (validate()) {
            EditText nameEditText = view.findViewById(R.id.name);
            favourite.name = nameEditText.getText().toString();
            RadioGroup typeRadioGroup = view.findViewById(R.id.favourite_buttons);
            favourite.type = getCheckedType(typeRadioGroup);
            positiveCallback.call(favourite);
            dialog.dismiss();
        }
    }
}
