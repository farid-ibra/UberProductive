package com.example.uberproductive.controller;

import android.app.Activity;
import android.app.Instrumentation;

import com.example.uberproductive.GoalTracker;
import com.example.uberproductive.NoteTaker;
import com.example.uberproductive.PomodoroTimer;
import com.example.uberproductive.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class DashboardTest {

    @Rule
    public ActivityTestRule<Dashboard> mActivityTestRule =  new ActivityTestRule<Dashboard>(Dashboard.class);

    private Dashboard mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(NoteTaker.class.getName(),null,false);
    Instrumentation.ActivityMonitor monitor2 = getInstrumentation().addMonitor(PomodoroTimer.class.getName(),null,false);
    Instrumentation.ActivityMonitor monitor3 = getInstrumentation().addMonitor(GoalTracker.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testMovingToGoalTracker(){
        assertNotNull(R.id.goal);

        onView(withId(R.id.goal)).perform(click());

        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor3,5000);
        assertNotNull(secondActivity);
        secondActivity.finish();
    }

    @Test
    public void testMovingToNoteTaker(){
        assertNotNull(R.id.note);

        onView(withId(R.id.note)).perform(click());

        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNotNull(secondActivity);
        secondActivity.finish();
    }

    @Test
    public void testMovingToPomodoroTimer(){
        assertNotNull(R.id.pomodoro);

        onView(withId(R.id.pomodoro)).perform(click());

        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor2,5000);
        assertNotNull(secondActivity);
        secondActivity.finish();
    }

    @Test
    public void testMovingToGoalTracker2(){
        assertNotNull(R.id.textView4);

        onView(withId(R.id.textView4)).perform(click());

        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor3,5000);
        assertNotNull(secondActivity);
        secondActivity.finish();
    }

    @Test
    public void testMovingToNoteTaker2(){
        assertNotNull(R.id.textView6);

        onView(withId(R.id.textView6)).perform(click());

        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNotNull(secondActivity);
        secondActivity.finish();
    }

    @Test
    public void testMovingToPomodoroTimer2(){
        assertNotNull(R.id.textView5);

        onView(withId(R.id.textView5)).perform(click());

        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor2,5000);
        assertNotNull(secondActivity);
        secondActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}