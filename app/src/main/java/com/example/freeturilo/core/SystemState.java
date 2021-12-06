package com.example.freeturilo.core;

public enum SystemState {
    STARTED(0), DEMO(1), STOPPED(2);

    private final int value;

    SystemState(int value) {
        this.value = value;
    }

    public int toInteger() {
        return value;
    }
}
