package com.example.freeturilo.connection;

import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.freeturilo.core.ErrorType;

public class APIDialogHandler implements APIHandler {
    private final DialogFragment dialog;
    private final boolean dismissDialog;

    public APIDialogHandler(DialogFragment dialog, boolean dismissDialog) {
        this.dialog = dialog;
        this.dismissDialog = dismissDialog;
    }

    @Override
    public void handle(APIException e) {
        ErrorType errorType = ErrorType.getType(e.responseCode);
        String errorText = ErrorType.getTypeText(dialog.requireContext(), errorType);
        Toast.makeText(dialog.requireContext().getApplicationContext(), errorText, Toast.LENGTH_SHORT).show();
        if (dismissDialog)
            dialog.dismiss();
    }
}
