package com.example.freeturilo.misc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class SynchronizerTest {

    private boolean callbackCalled = false;

    @Test
    public void decrement_withoutCallback_sync() {
        Synchronizer synchronizer = new Synchronizer(3, () -> callbackCalled = true);

        for (int i = 0; i < 2; i++)
            synchronizer.decrement();

        assertFalse(callbackCalled);
    }

    @Test
    public void decrement_withCallback_sync() {
        Synchronizer synchronizer = new Synchronizer(3, () -> callbackCalled = true);

        for (int i = 0; i < 3; i++)
            synchronizer.decrement();

        assertTrue(callbackCalled);
    }

    @Test
    public void decrement_withoutCallback_async() throws InterruptedException {
        Synchronizer synchronizer = new Synchronizer(10, () -> callbackCalled = true);
        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            threads.add(new Thread(synchronizer::decrement));
            threads.get(i).start();
        }

        for (int i = 0; i < 9; i++)
            threads.get(i).join();

        assertFalse(callbackCalled);
    }

    @Test
    public void decrement_withCallback_async() throws InterruptedException {
        Synchronizer synchronizer = new Synchronizer(10, () -> callbackCalled = true);
        ArrayList<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(synchronizer::decrement));
            threads.get(i).start();
        }

        for (int i = 0; i < 10; i++)
            threads.get(i).join();

        assertTrue(callbackCalled);
    }
}