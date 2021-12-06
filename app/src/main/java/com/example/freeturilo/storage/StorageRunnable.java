package com.example.freeturilo.storage;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.misc.Callback;

public class StorageRunnable<T> implements Runnable {
    private final StorageAction<T> action;
    private Callback<T> callback;
    private StorageHandler handler;

    public StorageRunnable(@NonNull StorageAction<T> action) {
        this.action = action;
    }

    @Override
    public void run() {
        try {
            T result = action.go();
            if (callback != null)
                new Handler(Looper.getMainLooper()).post(() -> callback.call(result));
        } catch (StorageException e) {
            if (handler != null)
                new Handler(Looper.getMainLooper()).post(() -> handler.handle(e));
        }
    }

    @NonNull
    public static <Q> StorageRunnable<Q> create(@NonNull StorageAction<Q> action) {
        return new StorageRunnable<>(action);
    }

    @NonNull
    public StorageRunnable<T> setCallback(@Nullable Callback<T> callback) {
        this.callback = callback;
        return this;
    }

    @NonNull
    public StorageRunnable<T> setHandler(@Nullable StorageHandler handler) {
        this.handler = handler;
        return this;
    }

    @NonNull
    public Thread toThread() {
        return new Thread(this);
    }

    public void startThread() {
        toThread().start();
    }
}
