package com.example.freeturilo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class FavouriteDialogFragment extends DialogFragment {
    MapActivity mapActivity;
    LatLng latLng;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mapActivity = Objects.requireNonNull((MapActivity) context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.FavouriteDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View inflated = inflater.inflate(R.layout.dialog_fragment_favourite, null);
        final EditText nameEditText = inflated.findViewById(R.id.name);
        final RadioGroup typeRadioGroup = inflated.findViewById(R.id.favourite_buttons);
        builder.setView(inflated)
                .setPositiveButton(R.string.ok_text, (dialog, id) -> {
                    Favourite favourite = new Favourite();
                    favourite.name = nameEditText.getText().toString();
                    favourite.latitude = latLng.latitude;
                    favourite.longitude = latLng.longitude;
                    favourite.type = FavouriteType.OTHER;
                    int buttonId = typeRadioGroup.getCheckedRadioButtonId();
                    if (buttonId == R.id.favourite_home_button)
                        favourite.type = FavouriteType.HOME;
                    else if (buttonId == R.id.favourite_work_button)
                        favourite.type = FavouriteType.WORK;
                    else if (buttonId == R.id.favourite_school_button)
                        favourite.type = FavouriteType.SCHOOL;
                    else if (buttonId == R.id.favourite_other_button)
                        favourite.type = FavouriteType.OTHER;
                    mapActivity.addFavourite(favourite);
                })
                .setNegativeButton(R.string.cancel_text, null);
        return builder.create();
    }
}
