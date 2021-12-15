package com.example.freeturilo.misc;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Favourite;
import com.example.freeturilo.core.FavouriteType;
import com.example.freeturilo.core.Location;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import java.util.Collections;

@RunWith(AndroidJUnit4.class)
public class AutoCompleteAdapterTest {

    @Test
    public void getView() {
        Context context = RuntimeEnvironment.getApplication();
        Location location = new Favourite("Test", 49, 51, FavouriteType.SCHOOL);
        AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(context, Collections.singletonList(location));

        View view = autoCompleteAdapter.getView(0, null, null);

        TextView textPrimary = view.findViewById(R.id.text_primary);
        assertEquals(location.getPrimaryText(), textPrimary.getText().toString());
        TextView textSecondary = view.findViewById(R.id.text_secondary);
        assertEquals(", " + location.getInlineSecondaryText(context), textSecondary.getText().toString());
    }
}