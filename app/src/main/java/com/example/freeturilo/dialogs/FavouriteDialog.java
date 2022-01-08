package com.example.freeturilo.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.example.freeturilo.misc.Callback;
import com.example.freeturilo.misc.Validation;

/**
 * A dialog for managing favourite locations.
 * <p>
 * This is an abstract class. Object of a class that is child to this class is
 * used to manage a {@link #favourite} location through an {@code AlertDialog}.
 * The dialog shows a {@link #view} customized for favourite locations
 * management. When its positive button is clicked, it should eventually call
 * the {@link #positiveCallback}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #view
 * @see #positiveCallback
 * @see #onAttach
 * @see #onCreateDialog
 * @see #onShow
 * @see #onPositiveButton
 * @see #validate
 * @see #findButtonByType
 * @see #getCheckedType
 * @see DialogFragment
 * @see AlertDialog
 */
public abstract class FavouriteDialog extends DialogFragment {
    /**
     * A favourite location managed with this dialog.
     */
    protected final Favourite favourite;
    /**
     * Stores the view shown within this dialog. Null until the dialog is
     * attached.
     */
    protected View view;
    /**
     * Stores the resource id of the title of this dialog.
     */
    protected int titleResourceId;
    /**
     * Stores the resource id of the text of the positive button.
     */
    protected int positiveTextResourceId;
    /**
     * Stores the resource id of the text of the negative button.
     */
    protected int negativeTextResourceId;
    /**
     * Stores the callback that is called after clicking the positive button
     * with the managed favourite location as an argument.
     */
    protected final Callback<Favourite> positiveCallback;

    /**
     * Class constructor.
     * <p>
     * Sets default values to {@link #titleResourceId}, {@link #positiveTextResourceId} and
     * {@link #negativeTextResourceId}.
     * @param favourite         a favourite location to be managed with this
     *                          dialog
     * @param positiveCallback  a callback which is called after this dialogs
     *                          finishes with a positive button click
     */
    protected FavouriteDialog(Favourite favourite, @NonNull Callback<Favourite> positiveCallback) {
        super();
        this.favourite = favourite;
        this.titleResourceId = R.string.favourite_dialog_title;
        this.positiveTextResourceId = R.string.ok_text;
        this.negativeTextResourceId = R.string.cancel_text;
        this.positiveCallback = positiveCallback;
    }

    /**
     * Attaches this dialog and sets {@link #view}.
     * @param context       the context of the application providing all global
     *                      information
     */
    @SuppressLint("InflateParams")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_favourite, null);
    }

    /**
     * Creates this dialog. When overridden, should call super.onCreateDialog.
     * @param savedInstanceState    unused parameter, included for
     *                              compatibility with {@code DialogFragment}
     * @return                      the created {@code AlertDialog}
     *                              representing this dialog
     */
    @NonNull
    @Override
    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.FreeturiloDialogTheme)
                .setView(view)
                .setTitle(titleResourceId)
                .setPositiveButton(positiveTextResourceId, null)
                .setNegativeButton(negativeTextResourceId, null)
                .create();
        dialog.setOnShowListener(this::onShow);
        return dialog;
    }

    /**
     * Shows this dialog.
     * @param dialog        the shown dialog
     */
    protected void onShow(@NonNull DialogInterface dialog) {
        Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener((view) -> onPositiveButton(dialog));
    }

    /**
     * Handles a positive button click.
     * <p>
     * Validates input data of this dialog. If validation passes, the managed
     * {@link #favourite} is updated with input data, {@link #positiveCallback}
     * is called and this dialog is dismissed.
     * @param dialog        the shown dialog
     * @see #validate
     */
    private void onPositiveButton(@NonNull DialogInterface dialog) {
        if (validate()) {
            EditText nameEditText = view.findViewById(R.id.name);
            favourite.name = nameEditText.getText().toString();
            favourite.type = getCheckedType();
            positiveCallback.call(favourite);
            dialog.dismiss();
        }
    }

    /**
     * Validates input data of this dialog.
     * <p>
     * If the name input is empty, a validation error occurs.
     * @return              a boolean indicating whether validation has passed
     */
    protected boolean validate() {
        EditText nameEditText = view.findViewById(R.id.name);
        if (Validation.isEmpty(nameEditText)) {
            Validation.setInputError(requireContext(), nameEditText, R.string.name_empty_text);
            return false;
        }
        return true;
    }

    /**
     * Gets the {@code RadioButton} of this dialog that corresponds to a
     * {@code FavouriteType}.
     * @param type      a favourite type
     * @return          a radio button of this dialog that corresponds to the
     *                  favourite type
     */
    @NonNull
    RadioButton findButtonByType(@NonNull FavouriteType type) {
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

    /**
     * Gets the {@code FavouriteType} that has been chosen with radio buttons
     * of this dialog.
     * @return      the favourite type represented by the checked radio button
     */
    @NonNull
    FavouriteType getCheckedType() {
        RadioGroup typeRadioGroup = view.findViewById(R.id.favourite_buttons);
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
