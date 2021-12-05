package com.example.freeturilo.misc;

import java.util.concurrent.atomic.AtomicInteger;

public class Synchronizer {
    private AtomicInteger counter;
    private VoidCallback callback;

    public Synchronizer(int initialCounter, VoidCallback callback) {
        counter = new AtomicInteger(initialCounter);
        this.callback = callback;
    }

    public void decrement() {
        if (counter.decrementAndGet() == 0)
            callback.call();
    }
}
