package com.example.freeturilo.activities;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.freeturilo.R;
import com.example.freeturilo.core.RouteParameters;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HistoryActivityTest {

    @Rule
    public ActivityScenarioRule<HistoryActivity> activityScenarioRule
            = new ActivityScenarioRule<>(HistoryActivity.class);

    @Test
    public void historyItem() {
        onData(is(instanceOf(RouteParameters.class)))
                .inAdapterView(withId(R.id.history_list)).atPosition(0).check(matches(allOf(
                isDisplayed(),
                hasDescendant(withId(R.id.start_text)),
                hasDescendant(withId(R.id.end_text)),
                hasDescendant(withId(R.id.criterion_text)),
                hasDescendant(withId(R.id.delete_history_item_button))
        )));
        Intents.init();
        onData(is(instanceOf(RouteParameters.class)))
                .inAdapterView(withId(R.id.history_list)).atPosition(0).perform(click());
        intended(hasComponent(RouteActivity.class.getName()));
        Intents.release();
    }
}