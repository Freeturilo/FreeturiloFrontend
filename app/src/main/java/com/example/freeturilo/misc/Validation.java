package com.example.freeturilo.misc;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Patterns;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.freeturilo.R;

import java.util.Objects;

/**
 * A collection of static methods used for input validation.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see EditText
 */
public class Validation {

    /**
     * Sets error of an input with a custom icon and message.
     * @param context   the context of the application providing all global
     *                  information
     * @param input     the text input with unvalidated data
     * @param messageId the resource identifier of the error message
     */
    public static void setInputError(@NonNull Context context, @NonNull EditText input, int messageId) {
        Drawable icon = Objects.requireNonNull(AppCompatResources
                .getDrawable(context, R.drawable.icon_validation_error));
        int size = input.getMeasuredHeight() / 2;
        icon.setBounds(0, 0, size, size);
        String message = context.getString(messageId);
        input.setError(message, icon);
    }

    /**
     * Checks if an input is empty.
     * @param input     a text input
     * @return          a boolean indicating whether the input is empty
     */
    public static boolean isEmpty(@NonNull EditText input) {
        return input.getText().toString().isEmpty();
    }

    /**
     * Checks if an input has a positive integer as content.
     * @param input     a text input
     * @return          a boolean indicating whether content of the input is a
     *                  positive integer
     */
    public static boolean hasPositiveInteger(@NonNull EditText input) {
        try {
            int value = Integer.parseInt(input.getText().toString());
            return value > 0;
        }
        catch (NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Checks if an input has an email address as content.
     * @param input     a text input
     * @return          a boolean indicating whether content of the input is a
     *                  valid email address
     */
    public static boolean hasEmail(@NonNull EditText input) {
        return Patterns.EMAIL_ADDRESS.matcher(input.getText().toString()).matches();
    }
}
