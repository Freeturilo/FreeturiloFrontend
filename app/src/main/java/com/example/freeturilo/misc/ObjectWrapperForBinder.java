package com.example.freeturilo.misc;

import android.os.Binder;

import androidx.annotation.NonNull;

public class ObjectWrapperForBinder extends Binder {

    private final Object data;

    public ObjectWrapperForBinder(@NonNull Object data) {
        this.data = data;
    }

    @NonNull
    public Object getData() {
        return data;
    }
}
