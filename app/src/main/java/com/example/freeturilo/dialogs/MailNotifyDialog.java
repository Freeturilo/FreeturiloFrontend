package com.example.freeturilo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.example.freeturilo.R;
import com.example.freeturilo.activities.AdminActivity;

import java.util.Objects;

public class MailNotifyDialog extends DialogFragment {
    View view;
    AdminActivity adminActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        adminActivity = Objects.requireNonNull((AdminActivity) context);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_mail_notify, null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        SwitchCompat notifySwitch = view.findViewById(R.id.notify_switch);
        notifySwitch.setOnCheckedChangeListener(this::onSwitchChange);
        return new AlertDialog.Builder(requireActivity(), R.style.FreeturiloDialogTheme)
                .setView(view)
                .setTitle(R.string.mail_notify_dialog_title)
                .setPositiveButton(R.string.ok_text, this::onPositiveButton)
                .setNegativeButton(R.string.cancel_text, null)
                .create();
    }

    private void onSwitchChange(CompoundButton buttonView, boolean isChecked) {
        TextView notifyNumberText = view.findViewById(R.id.notify_number_text);
        EditText notifyNumberInput = view.findViewById(R.id.notify_number_input);
        if(!isChecked) {
            notifyNumberText.setTextColor(adminActivity.getColor(R.color.grey));
            notifyNumberInput.setTextColor(adminActivity.getColor(R.color.grey));
            notifyNumberInput.setEnabled(false);
        }
        else {
            notifyNumberText.setTextColor(adminActivity.getColor(R.color.black));
            notifyNumberInput.setTextColor(adminActivity.getColor(R.color.black));
            notifyNumberInput.setEnabled(true);
        }
    }

    private void onPositiveButton(DialogInterface dialog, int id) {
        SwitchCompat notifySwitch = view.findViewById(R.id.notify_switch);
        if (!notifySwitch.isChecked()) {
            adminActivity.setNotifyThreshold(0);
        }
        else {
            EditText notifyNumberInput = view.findViewById(R.id.notify_number_input);
            adminActivity.setNotifyThreshold(Integer.parseInt(notifyNumberInput.getText().toString()));
        }
    }
}
