package com.example.freeturilo.storage;

import static android.os.Looper.getMainLooper;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class StorageRunnableTest {

    private boolean actionCalled = false;
    private boolean callbackCalled = false;
    private boolean handlerCalled = false;

    @Test
    public void run_withCallback() {
        StorageRunnable<Boolean> storageRunnable = StorageRunnable
                .create(() -> { actionCalled = true; return true; })
                .setCallback( result -> callbackCalled = true)
                .setHandler( exception -> handlerCalled = true);

        storageRunnable.run();
        shadowOf(getMainLooper()).idle();

        assertTrue(actionCalled);
        assertTrue(callbackCalled);
        assertFalse(handlerCalled);
    }

    @Test
    public void run_withHandler() {
        StorageRunnable<Object> storageRunnable = StorageRunnable
                .create(() -> { actionCalled = true; throw new StorageException("Test"); })
                .setCallback( result -> callbackCalled = true)
                .setHandler( exception -> handlerCalled = true);

        storageRunnable.run();
        shadowOf(getMainLooper()).idle();

        assertTrue(actionCalled);
        assertFalse(callbackCalled);
        assertTrue(handlerCalled);
    }

    @Test
    public void threadRun_withCallback() throws InterruptedException {
        StorageRunnable<Boolean> storageRunnable = StorageRunnable
                .create(() -> { actionCalled = true; return true; })
                .setCallback( result -> callbackCalled = true)
                .setHandler( exception -> handlerCalled = true);

        Thread thread = storageRunnable.toThread();
        thread.start();
        thread.join();
        shadowOf(getMainLooper()).idle();

        assertTrue(actionCalled);
        assertTrue(callbackCalled);
        assertFalse(handlerCalled);
    }

    @Test
    public void threadRun_withHandler() throws InterruptedException {
        StorageRunnable<Object> storageRunnable = StorageRunnable
                .create(() -> { actionCalled = true; throw new StorageException("Test"); })
                .setCallback( result -> callbackCalled = true)
                .setHandler( exception -> handlerCalled = true);

        Thread thread = storageRunnable.toThread();
        thread.start();
        thread.join();
        shadowOf(getMainLooper()).idle();

        assertTrue(actionCalled);
        assertFalse(callbackCalled);
        assertTrue(handlerCalled);
    }
}