package com.example.freeturilo.activities;

import static org.hamcrest.CoreMatchers.isA;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class EspressoExtensions {
    public static <T> Matcher<T> first(final Matcher<T> matcher) {
        return new BaseMatcher<T>() {
            boolean isFirst = true;

            @Override
            public boolean matches(final Object item) {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false;
                    return true;
                }

                return false;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("should return first matching item");
            }
        };
    }

    public static <T> Matcher<T> hasBitmap(Bitmap bitmap) {
        return new BaseMatcher<T>() {

            @Override
            public boolean matches(Object item) {
                ImageView imageView = (ImageView) item;
                Bitmap actual = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                return actual.sameAs(bitmap);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("displayed bitmap is " + bitmap);
            }
        };
    }

    public static ViewAction setChecked(final boolean checked) {
        return new ViewAction() {
            @Override
            public BaseMatcher<View> getConstraints() {
                return new BaseMatcher<View>() {
                    @Override
                    public boolean matches(Object item) {
                        return isA(Checkable.class).matches(item);
                    }

                    @Override
                    public void describeMismatch(Object item, Description mismatchDescription) {}

                    @Override
                    public void describeTo(Description description) {}
                };
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                Checkable checkableView = (Checkable) view;
                checkableView.setChecked(checked);
            }
        };
    }
}
