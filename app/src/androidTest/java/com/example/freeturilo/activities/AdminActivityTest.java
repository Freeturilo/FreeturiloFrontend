package com.example.freeturilo.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.freeturilo.activities.EspressoExtensions.first;
import static com.example.freeturilo.activities.EspressoExtensions.setChecked;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.widget.SwitchCompat;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.freeturilo.BuildConfig;
import com.example.freeturilo.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AdminActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void login() throws InterruptedException {
        onView(withId(R.id.email)).perform(typeText(BuildConfig.ADMIN_EMAIL));
        onView(withId(R.id.password)).perform(typeText(BuildConfig.ADMIN_PASSWORD));
        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(5000);
    }

    @Test
    public void stateButtons() {
        onView(withId(R.id.started_state_button)).check(matches(allOf(
                isDisplayed(),
                withText(R.string.started_state_text),
                isClickable(),
                is(instanceOf(RadioButton.class)),
                isDescendantOfA(withId(R.id.system_state_buttons))
                )));

        onView(withId(R.id.stopped_state_button)).check(matches(allOf(
                isDisplayed(),
                withText(R.string.stopped_state_text),
                isClickable(),
                is(instanceOf(RadioButton.class)),
                isDescendantOfA(withId(R.id.system_state_buttons))
                )));

        onView(withId(R.id.demo_state_button)).check(matches(allOf(
                isDisplayed(),
                withText(R.string.demo_state_text),
                isClickable(),
                is(instanceOf(RadioButton.class)),
                isDescendantOfA(withId(R.id.system_state_buttons))
        )));

        onView(withId(R.id.system_state_buttons)).check(matches(allOf(
                isDisplayed(),
                hasChildCount(3),
                hasDescendant(isChecked())
        )));

        onView(first(allOf(withParent(withId(R.id.system_state_buttons)),
                not(isChecked())))).perform(click());
        onView(withText(R.string.yes_text)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withText(R.string.cancel_text)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void mailNotifyButton() throws InterruptedException {
        onView(withId(R.id.mail_notify_button)).check(matches(allOf(
                isDisplayed(),
                withText(R.string.mail_notify_text),
                isClickable(),
                is(instanceOf(Button.class))
        ))).perform(click());

        onView(withId(R.id.notify_switch)).check(matches(allOf(
                isDisplayed(),
                is(instanceOf(SwitchCompat.class))
        )));

        onView(withId(R.id.notify_threshold_input)).check(matches(allOf(
                isDisplayed(),
                is(instanceOf(EditText.class)),
                withInputType(InputType.TYPE_CLASS_NUMBER)
        )));

        Thread.sleep(2000);

        onView(allOf(withChild(withId(R.id.notify_switch)), withChild(withId(R.id.notify_threshold_input)))).check(matches(anyOf(
                withChild(allOf(withId(R.id.notify_switch), isChecked())),
                withChild(allOf(withId(R.id.notify_threshold_input), isNotEnabled()))
        )));

        onView(withId(R.id.notify_switch)).perform(setChecked(false));
        onView(withId(R.id.notify_threshold_input)).check(matches(isNotEnabled()));

        onView(withId(R.id.notify_switch)).perform(setChecked(true));
        onView(withId(R.id.notify_threshold_input)).check(matches(isEnabled())).perform(typeText("2"));
    }

}