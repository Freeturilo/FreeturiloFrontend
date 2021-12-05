package com.example.freeturilo.misc;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

public class Synchronizer {
    final private AtomicInteger counter;
    final private VoidCallback callback;

    public Synchronizer(int initialCounter, @NonNull VoidCallback callback) {
        counter = new AtomicInteger(initialCounter);
        this.callback = callback;
    }

    public void decrement() {
        if (counter.decrementAndGet() == 0)
            callback.call();
    }
}
