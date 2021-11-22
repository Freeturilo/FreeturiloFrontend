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
        builder.setView(inflated)
                .setPositiveButton(R.string.ok_text, (dialog, id) -> {
                    final EditText nameEditText = inflated.findViewById(R.id.name);
                    String name = nameEditText.getText().toString();
                    final RadioGroup typeRadioGroup = inflated.findViewById(R.id.favourite_buttons);
                    FavouriteType type = getCheckedType(typeRadioGroup);
                    Favourite favourite = new Favourite(name, latLng.latitude, latLng.longitude, type);
                    mapActivity.addFavourite(favourite);
                })
                .setNegativeButton(R.string.cancel_text, null);
        return builder.create();
    }

    private FavouriteType getCheckedType(RadioGroup typeRadioGroup) {
        int buttonId = typeRadioGroup.getCheckedRadioButtonId();
        final int homeId = R.id.favourite_home_button;
        final int workId = R.id.favourite_work_button;
        final int schoolId = R.id.favourite_school_button;
        switch (buttonId) {
            case homeId:
                return FavouriteType.HOME;
            case workId:
                return FavouriteType.WORK;
            case schoolId:
                return FavouriteType.SCHOOL;
            default:
                return FavouriteType.OTHER;
        }
    }
}
