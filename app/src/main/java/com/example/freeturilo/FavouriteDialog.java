package com.example.freeturilo;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.freeturilo.core.FavouriteType;

import java.util.Objects;

public class FavouriteDialog extends DialogFragment {
    View view;
    MapActivity mapActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mapActivity = Objects.requireNonNull((MapActivity) context);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_favourite, null);
    }

    AlertDialog.Builder createBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mapActivity, R.style.FreeturiloDialogTheme);
        return builder.setView(view);
    }

    View findButtonByType(FavouriteType type) {
        switch (type) {
            case HOME:
                return view.findViewById(R.id.favourite_home_button);
            case SCHOOL:
                return view.findViewById(R.id.favourite_school_button);
            case WORK:
                return view.findViewById(R.id.favourite_work_button);
            default:
                return view.findViewById(R.id.favourite_other_button);
        }
    }

    FavouriteType getCheckedType(RadioGroup typeRadioGroup) {
        int buttonId = typeRadioGroup.getCheckedRadioButtonId();
        final int homeId = R.id.favourite_home_button;
        final int schoolId = R.id.favourite_school_button;
        final int workId = R.id.favourite_work_button;
        switch (buttonId) {
            case homeId:
                return FavouriteType.HOME;
            case schoolId:
                return FavouriteType.SCHOOL;
            case workId:
                return FavouriteType.WORK;
            default:
                return FavouriteType.OTHER;
        }
    }
}
