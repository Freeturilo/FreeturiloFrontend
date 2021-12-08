package com.example.freeturilo.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import com.example.freeturilo.misc.ValidationTools;

public class MailNotifyDialog extends DialogFragment {
    private View view;
    private final API api;
    private final Callback<Integer> positiveCallback;

    public MailNotifyDialog(API api, Callback<Integer> positiveCallback) {
        this.api = api;
        this.positiveCallback = positiveCallback;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_mail_notify, null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        api.getNotifyThresholdAsync(this::onThresholdReady, new APIDialogHandler(this, true));
        SwitchCompat notifySwitch = view.findViewById(R.id.notify_switch);
        notifySwitch.setOnCheckedChangeListener(this::onSwitchChange);
        Dialog dialog = new AlertDialog.Builder(requireContext(), R.style.FreeturiloDialogTheme)
                .setView(view)
                .setTitle(R.string.mail_notify_dialog_title)
                .setPositiveButton(R.string.ok_text, null)
                .setNegativeButton(R.string.cancel_text, null)
                .create();
        dialog.setOnShowListener(this::onShow);
        return dialog;
    }

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

    private void onShow(@NonNull DialogInterface dialog) {
        Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener((view) -> onPositiveButton(dialog));
    }

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

    private boolean validate() {
        SwitchCompat notifySwitch = view.findViewById(R.id.notify_switch);
        EditText notifyThresholdInput = view.findViewById(R.id.notify_threshold_input);
        if (!notifySwitch.isChecked() || ValidationTools.hasInteger(notifyThresholdInput)) return true;
        ValidationTools.setInputError(requireContext(), notifyThresholdInput, R.string.threshold_invalid_text);
        return false;
    }
}
