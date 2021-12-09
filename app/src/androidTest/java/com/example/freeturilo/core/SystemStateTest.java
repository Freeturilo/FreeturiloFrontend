package com.example.freeturilo.core;

import static org.junit.Assert.assertEquals;

import com.example.freeturilo.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class SystemStateTest {

    @Parameterized.Parameters
    public static Iterable<Object[]> states() {
        return Arrays.asList(new Object[][] {
                {0, SystemState.STARTED, R.id.started_state_button},
                {1, SystemState.DEMO, R.id.demo_state_button},
                {2, SystemState.STOPPED, R.id.stopped_state_button}
        });
    }

    private final int stateId;
    private final SystemState systemState;
    private final int stateButtonId;

    public SystemStateTest(int stateId, SystemState systemState, int stateButtonId) {
        this.stateId = stateId;
        this.systemState = systemState;
        this.stateButtonId = stateButtonId;
    }

    @Test
    public void toInteger() {
        int result = systemState.toInteger();

        assertEquals(stateId, result);
    }

    @Test
    public void getButtonId() {
        int result = SystemState.getButtonId(systemState);

        assertEquals(stateButtonId, result);
    }

    @Test
    public void getState() {
        SystemState result = SystemState.getState(stateButtonId);

        assertEquals(systemState, result);
    }
}