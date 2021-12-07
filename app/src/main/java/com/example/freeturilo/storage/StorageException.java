package com.example.freeturilo.storage;

import androidx.annotation.NonNull;

public class StorageException extends Exception {
    public final String message;

    public StorageException(@NonNull String message) {
        this.message = message;
    }
}
