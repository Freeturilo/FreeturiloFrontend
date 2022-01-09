package com.example.freeturilo.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.hasTextColor;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.freeturilo.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class LoginActivityTest {

    private String adminEmail;
    private String adminPassword;

    public LoginActivityTest() throws IOException {
        Properties testProperties = new Properties();
        InputStream testPropertiesStream =
                Objects.requireNonNull(getClass().getClassLoader())
                        .getResourceAsStream("test.properties");
        if (testPropertiesStream != null) {
            testProperties.load(testPropertiesStream);
            adminEmail = testProperties.getProperty("ADMIN_EMAIL");
            adminPassword = testProperties.getProperty("ADMIN_PASSWORD");
        }
    }


    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void breakDown() {
        Intents.release();
    }

    @Test
    public void inputs() {
        onView(withId(R.id.email)).check(matches(allOf(
                isDisplayed(),
                isClickable(),
                withHint(R.string.login_email_hint),
                hasTextColor(R.color.black)
                )));
        onView(withId(R.id.password)).check(matches(allOf(
                isDisplayed(),
                isClickable(),
                withHint(R.string.login_password_hint),
                hasTextColor(R.color.black),
                withInputType(129)
        )));
    }

    @Test
    public void loginButton_validation() throws InterruptedException {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        onView(withId(R.id.email)).perform(typeText("adminexample.com"));
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.email))
                .check(matches(hasErrorText(context.getString(R.string.email_invalid_text))));
        onView(withId(R.id.password))
                .check(matches(hasErrorText(context.getString(R.string.password_empty_text))));
        onView(withId(R.id.email)).perform(clearText(), typeText("admin@example.com"));
        onView(withId(R.id.password)).perform(typeText("password"));
        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(1000);
        intended(hasComponent(ErrorActivity.class.getName()));
    }

    @Test
    public void loginButton() throws InterruptedException {
        if (adminEmail == null || adminPassword == null)
            fail("Admin credentials have not been specified.");
        onView(withId(R.id.email)).perform(typeText(adminEmail));
        onView(withId(R.id.password)).perform(typeText(adminPassword));
        onView(withId(R.id.login_button)).perform(click());
        Thread.sleep(2000);
        intended(hasComponent(AdminActivity.class.getName()));
    }
}