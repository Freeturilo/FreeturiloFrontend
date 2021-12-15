package com.example.freeturilo.connection;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.freeturilo.misc.Callback;

public class APIRunnable<T> implements Runnable {
    private final APIAction<T> action;
    private Callback<T> callback;
    private APIHandler handler;

    private APIRunnable(@NonNull APIAction<T> action) {
        this.action = action;
    }

    @Override
    public void run() {
        try {
            T result = action.go();
            if (callback != null)
                new Handler(Looper.getMainLooper()).post(() -> callback.call(result));
        } catch (APIException e) {
            if (handler != null)
                new Handler(Looper.getMainLooper()).post(() -> handler.handle(e));
        }
    }

    @NonNull
    public static <Q> APIRunnable<Q> create(@NonNull APIAction<Q> action) {
        return new APIRunnable<>(action);
    }

    @NonNull
    public APIRunnable<T> setCallback(@Nullable Callback<T> callback) {
        this.callback = callback;
        return this;
    }

    @NonNull
    public APIRunnable<T> setHandler(@Nullable APIHandler handler) {
        this.handler = handler;
        return this;
    }

    @NonNull
    public Thread toThread() {
        return new Thread(this);
    }
}
