package com.example.freeturilo.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.example.freeturilo.misc.Callback;
import com.google.android.gms.maps.model.LatLng;

public class AddFavouriteDialog extends FavouriteDialog {
    final LatLng latLng;

    public AddFavouriteDialog(LatLng latLng, Callback<Favourite> positiveCallback) {
        super(positiveCallback);
        this.latLng = latLng;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createBuilder()
                .setTitle(R.string.add_favourite_dialog_title)
                .setPositiveButton(R.string.ok_text, this::onPositiveButton)
                .setNegativeButton(R.string.cancel_text, null)
                .create();
    }

    private void onPositiveButton(DialogInterface dialog, int id) {
        final EditText nameEditText = view.findViewById(R.id.name);
        String name = nameEditText.getText().toString();
        final RadioGroup typeRadioGroup = view.findViewById(R.id.favourite_buttons);
        FavouriteType type = getCheckedType(typeRadioGroup);
        Favourite favourite = new Favourite(name, latLng.latitude, latLng.longitude, type);
        positiveCallback.call(favourite);
    }
}
