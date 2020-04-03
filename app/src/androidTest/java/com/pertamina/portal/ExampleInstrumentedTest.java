package com.pertamina.portal;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.pertamina.portal.activity.LoginActivity;
import com.pertamina.portal.activity.SplashScreenActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented CircleTransform, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {
    private String stringToBetyped;

    @Test
    public void useAppContext() {
        // Context of the app under CircleTransform.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.pertamina.iam", appContext.getPackageName());
    }

    @Rule
    public ActivityTestRule<LoginActivity> activityRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void beforeTest() {
        IdlingRegistry.getInstance().register(EspressoIdlingResouce.getIdlingResource());
    }

    @Test
    public void login() {
        // Type text and then press the button.
        onView(withId(R.id.etUserId))
                .perform(typeText("traineeptm.6010"), closeSoftKeyboard());
        onView(withId(R.id.etPin))
                .perform(typeText("rahasia"), closeSoftKeyboard());
//        onView(withId(R.id.spCountryCode))
//                .perform();
        onView(withId(R.id.btnSend)).perform(click());
//        onView(withId(R.id.btnSend)).perform(click());
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResouce.getIdlingResource());
    }
}
