package com.example.freeturilo.activities;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.hasTextColor;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;

import android.content.Context;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.freeturilo.R;
import com.example.freeturilo.core.Location;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RouteCreateActivityTest {

    @Rule
    public ActivityScenarioRule<RouteCreateActivity> activityScenarioRule
            = new ActivityScenarioRule<>(RouteCreateActivity.class);

    @Test
    public void startInput() {
        onView(withId(R.id.start_input)).check(matches(allOf(
                isDisplayed(),
                isDescendantOfA(withId(R.id.autocomplete_inputs)),
                withHint(R.string.start_input_hint),
                hasTextColor(R.color.black),
                isClickable()
        ))).perform(typeText("D"));
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).check(matches(isDisplayed()));
        onView(allOf(withParent(withParentIndex(0)), withId(R.id.text_primary)))
                .inRoot(isPlatformPopup()).check(matches(withText(startsWith("D"))));
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.start_input)).check(matches(allOf(
                hasTextColor(R.color.purple_first),
                withTagValue(notNullValue()),
                withTagValue(is(instanceOf(Location.class)))
        ))).perform(
                clearText(),
                typeText("P"),
                pressImeActionButton()
        ).check(matches(allOf(
                hasTextColor(R.color.black),
                withTagValue(nullValue())
        )));
    }

    @Test
    public void endInput() {
        onView(withId(R.id.end_input)).check(matches(allOf(
                isDisplayed(),
                isDescendantOfA(withId(R.id.autocomplete_inputs)),
                withHint(R.string.end_input_hint),
                hasTextColor(R.color.black),
                isClickable()
        ))).perform(typeText("G"));
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).check(matches(isDisplayed()));
        onView(allOf(withParent(withParentIndex(0)), withId(R.id.text_primary)))
                .inRoot(isPlatformPopup()).check(matches(withText(startsWith("G"))));
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.end_input)).check(matches(allOf(
                hasTextColor(R.color.purple_first),
                withTagValue(notNullValue()),
                withTagValue(is(instanceOf(Location.class)))
        ))).perform(
                clearText(),
                typeText("H"),
                pressImeActionButton()
        ).check(matches(allOf(
                hasTextColor(R.color.black),
                withTagValue(nullValue())
        )));
    }

    @Test
    public void addStopButton() {
        onView(withId(R.id.add_stop_button)).check(matches(allOf(
                isDisplayed(),
                withText(R.string.add_stop_text),
                isClickable()
        ))).perform(click());
        onView(withId(R.id.autocomplete_inputs)).check(matches(hasChildCount(3)));
        onView(allOf(withParent(withId(R.id.autocomplete_inputs)), withParentIndex(1)))
                .check(matches(allOf(
                        isDisplayed(),
                        withHint(R.string.stop_hint))));
        onView(withId(R.id.add_stop_button)).perform(
                click(),
                click()
        ).check(matches(not(isDisplayed())));
        onView(withId(R.id.autocomplete_inputs)).check(matches(hasChildCount(5)));
    }

    @Test
    public void criterionButtons() {
        onView(withId(R.id.criterion_cost_button)).check(matches(isChecked()));
        onView(withId(R.id.criterion_hybrid_button)).check(matches(not(isChecked())));
        onView(withId(R.id.criterion_time_button)).check(matches(not(isChecked())));

        onView(withId(R.id.criterion_time_button)).perform(click());

        onView(withId(R.id.criterion_cost_button)).check(matches(not(isChecked())));
        onView(withId(R.id.criterion_hybrid_button)).check(matches(not(isChecked())));
        onView(withId(R.id.criterion_time_button)).check(matches(isChecked()));
    }

    @Test
    public void submitButton_validation() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.start_input)).perform(typeText("Dalanowska"));
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.add_stop_button)).perform(click());
        onView(withHint(R.string.stop_hint)).perform(
                typeText("9273f4u3r2ji52v"),
                pressImeActionButton()
        );
        onView(withId(R.id.submit_button)).perform(click());
        onView(withId(R.id.end_input)).check(matches(hasErrorText(context.getString(R.string.autocomplete_empty_error_text))));
        onView(withId(R.id.end_input)).perform(typeText("Kochanowskiego"));
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.submit_button)).perform(click());
        onView(withHint(R.string.stop_hint)).check(matches(hasErrorText(context.getString(R.string.autocomplete_not_found_error_text))));
        onView(withHint(R.string.stop_hint)).perform(
                clearText(),
                typeText("rondo ONZ")
        );
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click());
        Intents.init();
        onView(withId(R.id.submit_button)).perform(click());
        intended(hasComponent(RouteActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void submitButton_specify() throws InterruptedException {
        onView(withId(R.id.start_input)).perform(typeText("Dalanowska"));
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.end_input)).perform(
                typeText("Rondo"),
                pressImeActionButton());
        Thread.sleep(100);
        onView(withId(R.id.submit_button)).perform(click());
        onView(withText(R.string.specify_address_title)).check(matches(isDisplayed()));
        Intents.init();
        onData(anything()).atPosition(0).inRoot(isDialog()).perform(click());
        intended(hasComponent(RouteActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void submitButton() {
        onView(withId(R.id.start_input)).perform(typeText("Kochanowskiego"));
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.end_input)).perform(typeText("Koszykowa"));
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.criterion_time_button)).perform(click());
        Intents.init();
        onView(withId(R.id.submit_button)).perform(click());
        intended(hasComponent(RouteActivity.class.getName()));
        Intents.release();
    }
}