package com.example.freeturilo.storage;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class ToastStorageHandler implements StorageHandler {
    final private Context context;

    public ToastStorageHandler(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void handle(StorageException e) {
        Toast.makeText(context.getApplicationContext(), e.message, Toast.LENGTH_SHORT).show();
    }
}
