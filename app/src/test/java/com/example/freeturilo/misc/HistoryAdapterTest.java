package com.example.freeturilo.misc;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Criterion;
import com.example.freeturilo.core.Location;
import com.example.freeturilo.core.RouteParameters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.Collections;

@RunWith(AndroidJUnit4.class)
public class HistoryAdapterTest {

    @Test
    public void getView() {
        Context context = RuntimeEnvironment.getApplication();
        Location start = new Location("Start", 49, 51);
        Location end = new Location("End", 51, 49);
        RouteParameters routeParameters = new RouteParameters(start, end, new ArrayList<>(), Criterion.TIME);
        HistoryAdapter historyAdapter = new HistoryAdapter(context, Collections.singletonList(routeParameters));

        View view = historyAdapter.getView(0, null, null);

        TextView startText = view.findViewById(R.id.start_text);
        assertEquals(start.name, startText.getText().toString());
        TextView endText = view.findViewById(R.id.end_text);
        assertEquals(end.name, endText.getText().toString());
        TextView criterionText = view.findViewById(R.id.criterion_text);
        assertEquals(Criterion.getCriterionText(context, Criterion.TIME), criterionText.getText().toString());
    }
}