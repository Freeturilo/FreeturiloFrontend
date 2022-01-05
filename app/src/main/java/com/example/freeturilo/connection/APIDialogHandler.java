package com.example.freeturilo.connection;

import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.freeturilo.core.ErrorType;

/**
 * A handler for errors in Freeturilo API transactions compatible with dialogs.
 * <p>
 * Object of this class handles errors occurring in Freeturilo API transactions
 * that have been performed with a {@link #dialog}.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #dialog
 * @see #dismissDialog
 * @see #handle
 * @see APIHandler
 * @see APIException
 * @see APIRunnable
 * @see DialogFragment
 */
public class APIDialogHandler implements APIHandler {
    /**
     * Stores the dialog which has been used to perform the transaction.
     */
    private final DialogFragment dialog;
    /**
     * Stores the flag defining whether to dismiss the dialog after handling
     * an error.
     */
    private final boolean dismissDialog;

    /**
     * Class constructor.
     * @param dialog            a dialog fragment which has been used to
     *                          perform the Freeturilo API transaction
     * @param dismissDialog     a boolean defining whether to dismiss the
     *                          dialog after handling an error.
     */
    public APIDialogHandler(DialogFragment dialog, boolean dismissDialog) {
        this.dialog = dialog;
        this.dismissDialog = dismissDialog;
    }

    /**
     * Handles an error in a Freeturilo API transaction.
     * <p>
     * Displays a {@code Toast} message customized for the
     * {@code APIException}. Then dismisses the {@code dialog} if
     * {@code dismissDialog} is set to true.
     * @param error     an error that occurred in an API transaction
     * @see #dialog
     * @see #dismissDialog
     * @see Toast
     * @see ErrorType
     * @see APIException
     */
    public void handle(APIException error) {
        ErrorType errorType = ErrorType.getType(error.responseCode);
        String errorText = ErrorType.getTypeText(dialog.requireContext(), errorType);
        Toast.makeText(dialog.requireContext().getApplicationContext(), errorText, Toast.LENGTH_SHORT).show();
        if (dismissDialog)
            dialog.dismiss();
    }
}
