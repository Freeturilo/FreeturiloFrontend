package com.example.freeturilo.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.freeturilo.activities.EspressoExtensions.hasBitmap;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.freeturilo.R;
import com.example.freeturilo.core.ErrorType;

import org.junit.Rule;
import org.junit.Test;

public class ErrorActivityTest {

    @Rule
    public ActivityScenarioRule<MenuActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MenuActivity.class);

    private void startErrorActivity(ErrorType errorType) {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ErrorActivity.class);
        intent.putExtra(ErrorActivity.ERROR_TYPE_INTENT, errorType);
        activityScenarioRule.getScenario().onActivity((activity) -> activity.startActivity(intent));
    }

    @Test
    public void networkError() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        startErrorActivity(ErrorType.NETWORK);
        onView(withId(R.id.error_image)).check(matches(allOf(
                isDisplayed(),
                is(instanceOf(ImageView.class)),
                hasBitmap(ErrorType.getTypeImage(context, ErrorType.NETWORK))
        )));
        onView(withId(R.id.error_primary_text)).check(matches(withText(R.string.error_primary_text)));
        onView(withId(R.id.error_secondary_text)).check(matches(withText(R.string.error_network_text)));
    }

    @Test
    public void authError() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        startErrorActivity(ErrorType.AUTH);
        onView(withId(R.id.error_image)).check(matches(allOf(
                isDisplayed(),
                is(instanceOf(ImageView.class)),
                hasBitmap(ErrorType.getTypeImage(context, ErrorType.AUTH))
        )));
        onView(withId(R.id.error_primary_text)).check(matches(withText(R.string.error_primary_text)));
        onView(withId(R.id.error_secondary_text)).check(matches(withText(R.string.error_auth_text)));
    }

    @Test
    public void stoppedError() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        startErrorActivity(ErrorType.STOPPED);
        onView(withId(R.id.error_image)).check(matches(allOf(
                isDisplayed(),
                is(instanceOf(ImageView.class)),
                hasBitmap(ErrorType.getTypeImage(context, ErrorType.STOPPED))
        )));
        onView(withId(R.id.error_primary_text)).check(matches(withText(R.string.error_primary_text)));
        onView(withId(R.id.error_secondary_text)).check(matches(withText(R.string.error_stopped_text)));
    }

    @Test
    public void serverError() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        startErrorActivity(ErrorType.SERVER);
        onView(withId(R.id.error_image)).check(matches(allOf(
                isDisplayed(),
                is(instanceOf(ImageView.class)),
                hasBitmap(ErrorType.getTypeImage(context, ErrorType.SERVER))
        )));
        onView(withId(R.id.error_primary_text)).check(matches(withText(R.string.error_primary_text)));
        onView(withId(R.id.error_secondary_text)).check(matches(withText(R.string.error_server_text)));
    }

}