package com.example.freeturilo.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.example.freeturilo.R;
import com.example.freeturilo.connection.API;
import com.example.freeturilo.connection.APIDialogHandler;
import com.example.freeturilo.misc.Callback;
import com.example.freeturilo.misc.Validation;

/**
 * A dialog for managing mail notifications.
 * <p>
 * Object of this class is used to manage the threshold of reports of a station
 * at which an administrator receives a mail notification. This dialog shows a
 * {@link #view} customized for mail notifications management. When created,
 * retrieves the current value of the threshold from {@link #api}. When its
 * positive button is clicked, it should eventually call the
 * {@link #positiveCallback}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see DialogFragment
 */
public class MailNotifyDialog extends DialogFragment {
    /**
     * Stores the view shown within this dialog. Null until the dialog is
     * attached.
     */
    private View view;
    /**
     * Stores the API used by this dialog to retrieve current value of the
     * threshold.
     */
    private final API api;
    /**
     * Stores the callback that is called after clicking the positive button
     * with the set threshold value as an argument.
     */
    private final Callback<Integer> positiveCallback;

    /**
     * Class constructor.
     * @param api               the API used to retrieve
     * @param positiveCallback  a callback which is called after this dialogs
     *                          finishes with a positive button click
     */
    public MailNotifyDialog(API api, Callback<Integer> positiveCallback) {
        this.api = api;
        this.positiveCallback = positiveCallback;
    }

    /**
     * Called when this dialog is attached. Sets {@link #view}.
     * @param context       the context of the application providing all global
     *                      information
     */
    @SuppressLint("InflateParams")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_mail_notify, null);
    }

    /**
     * Called when this dialog is created.
     * <p>
     * Builds this dialog and initiates retrieval of current value of the
     * threshold from {@link #api}.
     * @param savedInstanceState    unused parameter, included for
     *                              compatibility with {@code DialogFragment}
     * @return                      the created {@code AlertDialog}
     *                              representing this dialog
     */
    @NonNull
    @Override
    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        api.getNotifyThresholdAsync(this::onThresholdReady, new APIDialogHandler(this, true));
        SwitchCompat notifySwitch = view.findViewById(R.id.notify_switch);
        notifySwitch.setOnCheckedChangeListener(this::onSwitchChange);
        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.FreeturiloDialogTheme)
                .setView(view)
                .setTitle(R.string.mail_notify_dialog_title)
                .setPositiveButton(R.string.ok_text, null)
                .setNegativeButton(R.string.cancel_text, null)
                .create();
        dialog.setOnShowListener(this::onShow);
        return dialog;
    }

    /**
     * Adjusts inputs of this dialog to the retrieved value of the threshold.
     * @param threshold     current value of the threshold retrieved from api
     */
    private void onThresholdReady(int threshold) {
        if (getContext() == null) return;
        EditText notifyThresholdInput = view.findViewById(R.id.notify_threshold_input);
        SwitchCompat notifySwitch = view.findViewById(R.id.notify_switch);
        if (threshold > 0) {
            notifySwitch.setChecked(true);
            notifyThresholdInput.setText(String.valueOf(threshold));
        }
        notifySwitch.setEnabled(true);
    }

    /**
     * Called when this dialog is shown.
     * @param dialog        the shown dialog
     */
    private void onShow(@NonNull DialogInterface dialog) {
        Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener((view) -> onPositiveButton(dialog));
    }

    /**
     * Called when the switch is changed.
     * @param buttonView        the changed switch
     * @param isChecked         a boolean indicating whether the switch is
     *                          checked after the change
     */
    private void onSwitchChange(@NonNull CompoundButton buttonView, boolean isChecked) {
        TextView notifyThresholdText = view.findViewById(R.id.notify_threshold_text);
        EditText notifyThresholdInput = view.findViewById(R.id.notify_threshold_input);
        if(!isChecked) {
            notifyThresholdText.setTextColor(requireContext().getColor(R.color.grey));
            notifyThresholdInput.setTextColor(requireContext().getColor(R.color.grey));
            notifyThresholdInput.setEnabled(false);
        }
        else {
            notifyThresholdText.setTextColor(requireContext().getColor(R.color.black));
            notifyThresholdInput.setTextColor(requireContext().getColor(R.color.black));
            notifyThresholdInput.setEnabled(true);
        }
    }

    /**
     * Called when the positive button is clicked.
     * <p>
     * Validates input data of this dialog. If validation passes, the
     * {@link #positiveCallback} is called with set threshold value and this
     * dialog is dismissed.
     * @param dialog        the shown dialog
     * @see #validate
     */
    private void onPositiveButton(@NonNull DialogInterface dialog) {
        if (validate()) {
            SwitchCompat notifySwitch = view.findViewById(R.id.notify_switch);
            if (notifySwitch.isChecked()) {
                EditText notifyThresholdInput = view.findViewById(R.id.notify_threshold_input);
                positiveCallback.call(Integer.parseInt(notifyThresholdInput.getText().toString()));
            } else {
                positiveCallback.call(0);
            }
            dialog.dismiss();
        }
    }

    /**
     * Validates input data of this dialog.
     * <p>
     * If the switch is checked and threshold input does not contain a positive
     * integer, a validation error occurs.
     * @return              a boolean indicating whether validation has passed
     */
    private boolean validate() {
        SwitchCompat notifySwitch = view.findViewById(R.id.notify_switch);
        EditText notifyThresholdInput = view.findViewById(R.id.notify_threshold_input);
        if (!notifySwitch.isChecked() || Validation.hasPositiveInteger(notifyThresholdInput))
            return true;
        Validation.setInputError(requireContext(), notifyThresholdInput, R.string.threshold_invalid_text);
        return false;
    }
}
