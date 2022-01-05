package com.example.freeturilo.core;

import androidx.annotation.NonNull;

import com.example.freeturilo.R;

/**
 * State of the Freeturilo system.
 * <p>
 * Values of this enum represent different states of the Freeturilo system.
 * There are static methods declared within the enum that help use it.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #STARTED
 * @see #DEMO
 * @see #STOPPED
 * @see #getButtonId
 * @see #getState
 */
public enum SystemState {
    /**
     * Represents the normal working system state.
     */
    STARTED,
    /**
     * Represents the state in which the Freeturilo System uses static NextBike
     * data instead of dynamically updating it.
     */
    DEMO,
    /**
     * Represents the state in which the Freeturilo API refuses to properly
     * respond to requests sent by unauthorized (non-admin) users.
     */
    STOPPED;

    /**
     * Gets the resource identifier of the button representing
     * a {@code SystemState}. Reverse of {@code getState}.
     * @param state     a system state
     * @return          an integer identifying the button representing
     *                  the system state
     */
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

    /**
     * Gets the {@code SystemState} that is represented by a button of
     * a resource identifier. Reverse of {@code getButtonId}.
     * @param stateButtonId     an integer identifying a button
     * @return                  a system state represented by the button
     *                          of the button identifier
     */
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

    /**
     * Generates numerical text representation of a system state.
     * @return          a string of the integer representing this system state
     * @see #ordinal
     */
    @NonNull
    @Override
    public String toString() {
        return String.valueOf(this.ordinal());
    }
}
