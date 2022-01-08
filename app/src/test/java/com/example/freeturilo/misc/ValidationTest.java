package com.example.freeturilo.misc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

@RunWith(AndroidJUnit4.class)
public class ValidationTest {

    @Test
    public void isEmpty_true() {
        Context context = RuntimeEnvironment.getApplication();
        EditText editText = new EditText(context);

        assertTrue(Validation.isEmpty(editText));
    }

    @Test
    public void isEmpty_false() {
        Context context = RuntimeEnvironment.getApplication();
        EditText editText = new EditText(context);
        editText.setText("Test");

        assertFalse(Validation.isEmpty(editText));
    }

    @Test
    public void hasInteger_true() {
        Context context = RuntimeEnvironment.getApplication();
        EditText editText = new EditText(context);
        editText.setText("14");

        assertTrue(Validation.hasPositiveInteger(editText));
    }

    @Test
    public void hasInteger_false() {
        Context context = RuntimeEnvironment.getApplication();
        EditText editText = new EditText(context);
        editText.setText("A3");

        assertFalse(Validation.hasPositiveInteger(editText));
    }

    @Test
    public void hasEmail_true() {
        Context context = RuntimeEnvironment.getApplication();
        EditText editText = new EditText(context);
        editText.setText("example@email.com");

        assertTrue(Validation.hasEmail(editText));
    }

    @Test
    public void hasEmail_false() {
        Context context = RuntimeEnvironment.getApplication();
        EditText editText = new EditText(context);
        editText.setText("example@emailcom");

        assertFalse(Validation.hasEmail(editText));
    }

    @Test
    public void setInputError() {
        Context context = RuntimeEnvironment.getApplication();
        EditText editText = new EditText(context);

        Validation.setInputError(context, editText, R.string.autocomplete_empty_error_text);

        assertEquals(context.getString(R.string.autocomplete_empty_error_text), editText.getError().toString());
    }
}