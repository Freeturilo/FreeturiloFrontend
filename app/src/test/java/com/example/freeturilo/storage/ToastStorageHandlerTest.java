package com.example.freeturilo.storage;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.Toast;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowToast;

@RunWith(AndroidJUnit4.class)
public class ToastStorageHandlerTest {

    @Test
    public void handle() {
        Context context = RuntimeEnvironment.getApplication();
        ToastStorageHandler handler = new ToastStorageHandler(context);

        handler.handle(new StorageException("Test message for a storage exception."));

        assertEquals("Test message for a storage exception.", ShadowToast.getTextOfLatestToast());
        assertEquals(Toast.LENGTH_SHORT, ShadowToast.getLatestToast().getDuration());
    }
}