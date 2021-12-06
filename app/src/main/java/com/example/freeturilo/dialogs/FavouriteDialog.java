package com.example.freeturilo.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.example.freeturilo.misc.Callback;
import com.example.freeturilo.misc.ValidationTools;

public class FavouriteDialog extends DialogFragment {
    protected View view;
    final protected Callback<Favourite> positiveCallback;

    public FavouriteDialog(@NonNull Callback<Favourite> positiveCallback) {
        super();
        this.positiveCallback = positiveCallback;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_favourite, null);
    }

    boolean validate() {
        EditText nameEditText = view.findViewById(R.id.name);
        if (ValidationTools.isEmpty(nameEditText)) {
            ValidationTools.setInputError(requireContext(), nameEditText, R.string.name_empty_text);
            return false;
        }
        return true;
    }

    @NonNull
    AlertDialog.Builder createBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.FreeturiloDialogTheme);
        return builder.setView(view);
    }

    @NonNull
    View findButtonByType(@NonNull FavouriteType type) {
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

    @NonNull
    FavouriteType getCheckedType(@NonNull RadioGroup typeRadioGroup) {
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
