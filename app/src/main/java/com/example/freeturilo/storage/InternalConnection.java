package com.example.freeturilo.storage;

import android.content.Context;

import java.io.InputStream;
import java.io.OutputStream;

public interface InternalConnection {

    InputStream openFileInput(String filename) throws StorageException;

    OutputStream openFileOutput(String filename) throws StorageException;

    boolean checkFileAbsent(String filename);

    interface Builder {
        Builder setContext(Context context);

        InternalConnection create();
    }
}
