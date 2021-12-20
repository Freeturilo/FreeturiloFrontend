package com.example.freeturilo.json;

import static org.junit.Assert.assertEquals;

import com.example.freeturilo.core.Criterion;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;

import java.util.Arrays;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class CriterionDeserializerTest {

    @ParameterizedRobolectricTestRunner.Parameters
    public static Iterable<Object[]> criteria() {
        return Arrays.asList(new Object[][] {
                {Criterion.COST, 0},
                {Criterion.TIME, 1},
                {Criterion.HYBRID, 2}
        });
    }

    private final Criterion criterion;
    private final int ordinal;

    public CriterionDeserializerTest(Criterion criterion, int ordinal) {
        this.criterion = criterion;
        this.ordinal = ordinal;
    }

    @Test
    public void deserialize() {
        CriterionDeserializer deserializer = new CriterionDeserializer();
        JsonElement element = new JsonPrimitive(this.ordinal);

        Criterion criterion = deserializer.deserialize(element, null, null);

        assertEquals(this.criterion, criterion);
    }
}