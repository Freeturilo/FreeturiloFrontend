package com.example.freeturilo.core;

import androidx.annotation.NonNull;

/**
 * State of the Freeturilo system.
 * <p>
 * Values of this enum represent different states of the Freeturilo system.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see #STARTED
 * @see #DEMO
 * @see #STOPPED
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
