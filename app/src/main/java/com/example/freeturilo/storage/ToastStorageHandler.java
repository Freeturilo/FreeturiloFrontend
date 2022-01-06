package com.example.freeturilo.storage;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * A handler for errors in internal storage transactions that displays a toast
 * message.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #context
 * @see #handle
 * @see StorageHandler
 * @see StorageException
 * @see StorageRunnable
 * @see DialogFragment
 */
public class ToastStorageHandler implements StorageHandler {
    /**
     * Stores the context of the application providing all global information.
     */
    private final Context context;

    /**
     * Class constructor.
     * @param context   the context of the application providing all global
     *                  information
     */
    public ToastStorageHandler(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Handles an error in an internal storage transaction.
     * <p>
     * Displays a {@code Toast} message of the {@code StorageException}.
     * @param error     an error that occurred in a storage transaction
     * @see #context
     * @see Toast
     * @see StorageException
     * @see StorageException#message
     */
    @Override
    public void handle(StorageException error) {
        Toast.makeText(context.getApplicationContext(), error.message, Toast.LENGTH_SHORT).show();
    }
}