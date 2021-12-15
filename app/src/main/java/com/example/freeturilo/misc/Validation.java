package com.example.freeturilo.misc;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Patterns;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.freeturilo.R;

import java.util.Objects;

public class Validation {

    public static void setInputError(@NonNull Context context, @NonNull EditText input, int messageId) {
        Drawable icon = Objects.requireNonNull(AppCompatResources
                .getDrawable(context, R.drawable.icon_validation_error));
        int size = input.getMeasuredHeight() / 2;
        icon.setBounds(0, 0, size, size);
        String message = context.getString(messageId);
        input.setError(message, icon);
    }

    public static boolean isEmpty(@NonNull EditText input) {
        return input.getText().toString().isEmpty();
    }

    public static boolean hasInteger(@NonNull EditText input) {
        try {
            Integer.parseInt(input.getText().toString());
            return true;
        }
        catch (NumberFormatException exception) {
            return false;
        }
    }

    public static boolean hasEmail(@NonNull EditText input) {
        return Patterns.EMAIL_ADDRESS.matcher(input.getText().toString()).matches();
    }
}
