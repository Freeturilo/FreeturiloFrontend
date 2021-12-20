package com.example.freeturilo.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.freeturilo.core.Criterion;
import com.google.gson.JsonElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;

import java.util.Arrays;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class CriterionSerializerTest {

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

    public CriterionSerializerTest(Criterion criterion, int ordinal) {
        this.criterion = criterion;
        this.ordinal = ordinal;
    }

    @Test
    public void serialize() {
        CriterionSerializer serializer = new CriterionSerializer();

        JsonElement element = serializer.serialize(this.criterion, null, null);

        assertTrue(element.isJsonPrimitive());
        assertEquals(this.ordinal, element.getAsJsonPrimitive().getAsNumber());
    }
}