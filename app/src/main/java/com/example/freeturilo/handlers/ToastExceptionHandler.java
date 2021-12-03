package com.example.freeturilo.handlers;

import android.content.Context;
import android.widget.Toast;

public class ToastExceptionHandler implements ExceptionHandler {
    final Context context;
    final int messageId;

    public ToastExceptionHandler(Context context, int messageId) {
        this.context = context;
        this.messageId = messageId;
    }

    @Override
    public void handle() {
        Toast toast = Toast.makeText(context.getApplicationContext(), messageId, Toast.LENGTH_SHORT);
        toast.show();
    }
}
