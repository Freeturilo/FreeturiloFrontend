package com.example.freeturilo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.example.freeturilo.misc.Callback;
import com.google.android.gms.maps.model.LatLng;

public class AddFavouriteDialog extends FavouriteDialog {
    private final LatLng latLng;

    public AddFavouriteDialog(@NonNull LatLng latLng, @NonNull Callback<Favourite> positiveCallback) {
        super(positiveCallback);
        this.latLng = latLng;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = createBuilder()
                .setTitle(R.string.add_favourite_dialog_title)
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
            String name = nameEditText.getText().toString();
            RadioGroup typeRadioGroup = view.findViewById(R.id.favourite_buttons);
            FavouriteType type = getCheckedType(typeRadioGroup);
            Favourite favourite = new Favourite(name, latLng.latitude, latLng.longitude, type);
            positiveCallback.call(favourite);
            dialog.dismiss();
        }
    }
}
