package com.zugara.atproj.lampsplus.ui.fragments;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zugara.atproj.lampsplus.R;
import com.zugara.atproj.lampsplus.ui.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;

/**
 * Created by andre on 04-Jan-19.
 */


@RunWith(AndroidJUnit4.class)
public class CreateSessionFragmentTest {

    @Rule
    public final ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testIllegalSession() throws Exception {
        onView(withId(R.id.sessionNameText))
                .perform(typeText("Session?"));
        onView(withId(R.id.createButton)).perform(click());
    }

}
