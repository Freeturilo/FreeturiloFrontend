package com.example.freeturilo.core;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import com.example.freeturilo.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class CriterionTest {

    @ParameterizedRobolectricTestRunner.Parameters
    public static Iterable<Object[]> criteria() {
        return Arrays.asList(new Object[][] {
                {Criterion.COST, R.string.cost_criterion_text},
                {Criterion.TIME, R.string.time_criterion_text},
                {Criterion.HYBRID, R.string.hybrid_criterion_text}
        });
    }

    private final Criterion criterion;
    private final int criterionTextId;

    public CriterionTest(Criterion criterion, int criterionTextId) {
        this.criterion = criterion;
        this.criterionTextId = criterionTextId;
    }

    @Test
    public void getCriterionText() {
        Context context = RuntimeEnvironment.getApplication();

        String result = Criterion.getCriterionText(context, criterion);

        assertEquals(context.getString(criterionTextId), result);
    }
}