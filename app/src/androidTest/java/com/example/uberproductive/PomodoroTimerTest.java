package com.example.uberproductive;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertNotNull;

public class PomodoroTimerTest {

    @Rule
    public ActivityTestRule<PomodoroTimer> mActivityTestRule =  new ActivityTestRule<PomodoroTimer>(PomodoroTimer.class);

    private PomodoroTimer mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLaunchAndButtonClicks() throws InterruptedException {
        assertNotNull(R.id.button_start_pause);
        assertNotNull(R.id.button_reset);
        assertNotNull(R.id.switch1);
        assertNotNull(R.id.text_view_countdown);
        assertNotNull(R.id.AppLogo);

        onView(withId(R.id.button_start_pause)).perform(click());
        sleep(3000);
        onView(withId(R.id.button_start_pause)).perform(click());
        sleep(3000);
        onView(withId(R.id.button_reset)).perform(click());
        sleep(3000);
        onView(withId(R.id.switch1)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}