package com.example.freeturilo.connection;


import android.os.Handler;
import android.os.Looper;

import com.example.freeturilo.misc.Callback;

public class APIRunnable<T> implements Runnable{

    private final APIAction<T> action;
    private Callback<T> callback;
    private APIHandler handler;

    public APIRunnable(APIAction<T> action) {
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

    public static <Q> APIRunnable<Q> create(APIAction<Q> action) {
        return new APIRunnable<>(action);
    }

    public APIRunnable<T> setCallback(Callback<T> callback) {
        this.callback = callback;
        return this;
    }

    public APIRunnable<T> setHandler(APIHandler handler) {
        this.handler = handler;
        return this;
    }

    public Thread toThread() {
        return new Thread(this);
    }

    public void startThread() {
        toThread().start();
    }
}
