package com.example.freeturilo.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MenuActivityTest {

    @Rule public ActivityScenarioRule<MenuActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MenuActivity.class);

    @Test
    public void mapButton() {
        onView(withId(R.id.map_button)).check(matches(allOf(
                isDisplayed(),
                withText(R.string.map_button_text),
                isClickable()
        )));

        Intents.init();
        onView(withId(R.id.map_button)).perform(click());
        intended(hasComponent(MapActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void routeButton() {
        onView(withId(R.id.route_button)).check(matches(allOf(
                isDisplayed(),
                withText(R.string.route_button_text),
                isClickable()
        )));

        Intents.init();
        onView(withId(R.id.route_button)).perform(click());
        intended(hasComponent(RouteCreateActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void historyButton() {
        onView(withId(R.id.history_button)).check(matches(allOf(
                isDisplayed(),
                withText(R.string.history_button_text),
                isClickable()
        )));

        Intents.init();
        onView(withId(R.id.history_button)).perform(click());
        intended(hasComponent(HistoryActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void toolbar() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.logo)).check(matches(isDisplayed()));

        Intents.init();
        onView(withId(R.id.logo)).perform(longClick());
        intended(hasComponent(LoginActivity.class.getName()));
        Intents.release();
    }
}