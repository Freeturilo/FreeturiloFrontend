package com.example.freeturilo.connection;

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
public class APIRunnableTest {

    private boolean actionCalled = false;
    private boolean callbackCalled = false;
    private boolean handlerCalled = false;


    @Test
    public void run_withCallback() {
        APIRunnable<Boolean> apiRunnable = APIRunnable
                .create(() -> { actionCalled = true; return true; })
                .setCallback( result -> callbackCalled = true)
                .setHandler( exception -> handlerCalled = true);

        apiRunnable.run();
        shadowOf(getMainLooper()).idle();

        assertTrue(actionCalled);
        assertTrue(callbackCalled);
        assertFalse(handlerCalled);
    }

    @Test
    public void run_withHandler() {
        APIRunnable<Object> apiRunnable = APIRunnable
                .create(() -> { actionCalled = true; throw new APIException(-1); })
                .setCallback( result -> callbackCalled = true)
                .setHandler( exception -> handlerCalled = true);

        apiRunnable.run();
        shadowOf(getMainLooper()).idle();

        assertTrue(actionCalled);
        assertFalse(callbackCalled);
        assertTrue(handlerCalled);
    }

    @Test
    public void threadRun_withCallback() throws InterruptedException {
        APIRunnable<Boolean> apiRunnable = APIRunnable
                .create(() -> { actionCalled = true; return true; })
                .setCallback( result -> callbackCalled = true)
                .setHandler( exception -> handlerCalled = true);

        Thread thread = apiRunnable.toThread();
        thread.start();
        thread.join();
        shadowOf(getMainLooper()).idle();

        assertTrue(actionCalled);
        assertTrue(callbackCalled);
        assertFalse(handlerCalled);
    }

    @Test
    public void threadRun_withHandler() throws InterruptedException {
        APIRunnable<Object> apiRunnable = APIRunnable
                .create(() -> { actionCalled = true; throw new APIException(-1); })
                .setCallback( result -> callbackCalled = true)
                .setHandler( exception -> handlerCalled = true);

        Thread thread = apiRunnable.toThread();
        thread.start();
        thread.join();
        shadowOf(getMainLooper()).idle();

        assertTrue(actionCalled);
        assertFalse(callbackCalled);
        assertTrue(handlerCalled);
    }
}