package com.example.freeturilo.core;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;

public enum SystemState {
    STARTED(0), DEMO(1), STOPPED(2);

    private final int value;

    SystemState(int value) {
        this.value = value;
    }

    public int toInteger() {
        return value;
    }

    public static int getButtonId(SystemState state) {
        switch (state) {
            case STARTED:
                return R.id.started_state_button;
            case DEMO:
                return R.id.demo_state_button;
            default:
                return R.id.stopped_state_button;
        }
    }

    @NonNull
    public static SystemState getState(int stateButtonId) {
        final int startedStateButton = R.id.started_state_button;
        final int demoStateButton = R.id.demo_state_button;
        switch (stateButtonId) {
            case startedStateButton:
                return STARTED;
            case demoStateButton:
                return DEMO;
            default:
                return STOPPED;
        }
    }
}
