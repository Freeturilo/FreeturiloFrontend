package com.example.freeturilo.handlers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class ToastExceptionHandler implements ExceptionHandler {
    final private Context context;
    final private int messageId;

    public ToastExceptionHandler(@NonNull Context context, int messageId) {
        this.context = context;
        this.messageId = messageId;
    }

    @Override
    public void handle() {
        Toast toast = Toast.makeText(context.getApplicationContext(), messageId, Toast.LENGTH_SHORT);
        toast.show();
    }
}
