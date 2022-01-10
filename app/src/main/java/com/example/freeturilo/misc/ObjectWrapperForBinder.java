package com.example.freeturilo.misc;

import android.os.Binder;

import androidx.annotation.NonNull;

/**
 * A binder for data to be passed between application activities.
 *
 * @author Mikołaj Terzyk
 * @version 1.0.0
 * @see Binder
 */
public class ObjectWrapperForBinder extends Binder {
    /**
     * Stores data being passed between activities.
     */
    private final Object data;

    /**
     * Class constructor.
     * @param data      the data to be passed between activities
     */
    public ObjectWrapperForBinder(@NonNull Object data) {
        this.data = data;
    }

    /**
     * Gets the data passed between activities.
     * @return          an object equal to the data
     */
    @NonNull
    public Object getData() {
        return data;
    }
}
