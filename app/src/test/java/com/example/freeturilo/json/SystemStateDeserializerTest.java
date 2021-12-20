package com.example.freeturilo.json;

import static org.junit.Assert.assertEquals;

import com.example.freeturilo.core.SystemState;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;

import java.util.Arrays;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class SystemStateDeserializerTest {

    @ParameterizedRobolectricTestRunner.Parameters
    public static Iterable<Object[]> states() {
        return Arrays.asList(new Object[][] {
                {0, SystemState.STARTED},
                {1, SystemState.DEMO},
                {2, SystemState.STOPPED}
        });
    }

    private final int ordinal;
    private final SystemState systemState;

    public SystemStateDeserializerTest(int ordinal, SystemState systemState) {
        this.ordinal = ordinal;
        this.systemState = systemState;
    }

    @Test
    public void deserialize() {
        SystemStateDeserializer deserializer = new SystemStateDeserializer();
        JsonElement element = new JsonPrimitive(this.ordinal);

        SystemState systemState = deserializer.deserialize(element, null, null);

        assertEquals(this.systemState, systemState);
    }
}