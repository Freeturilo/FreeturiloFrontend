package com.example.freeturilo.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;

import java.util.Arrays;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class SystemStateTest {

    @ParameterizedRobolectricTestRunner.Parameters
    public static Iterable<Object[]> states() {
        return Arrays.asList(new Object[][] {
                {0, SystemState.STARTED},
                {1, SystemState.DEMO},
                {2, SystemState.STOPPED}
        });
    }

    private final int stateId;
    private final SystemState systemState;

    public SystemStateTest(int stateId, SystemState systemState) {
        this.stateId = stateId;
        this.systemState = systemState;
    }

    @Test
    public void toInteger() {
        int result = systemState.ordinal();

        assertEquals(stateId, result);
    }
}