package com.example.uberproductive;


import android.app.Activity;
import android.app.Instrumentation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.DrawerActions.close;
import static androidx.test.espresso.contrib.DrawerActions.open;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertNotNull;

public class GoalTrackerTest {

    @Rule
    public ActivityTestRule<GoalTracker> mGoalTrackerTestRule = new ActivityTestRule<GoalTracker>(GoalTracker.class);
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(NoteTaker.class.getName(),null,false);
    Instrumentation.ActivityMonitor monitor1 = getInstrumentation().addMonitor(PomodoroTimer.class.getName(),null,false);

    private GoalTracker mActivity = null;
    @Before
    public void setUp() throws Exception {
        mActivity = mGoalTrackerTestRule.getActivity();
    }

    @Test
    public void testLaunch() throws InterruptedException {
        DrawerLayout drawerLayout = mActivity.findViewById(R.id.sideMenu);
        assertNotNull(drawerLayout);
        onView(withId(R.id.sideMenu)).perform(open());
        sleep(2000);
        onView(withId(R.id.sideMenu)).perform(close());
    }

    @Test
    public void testLaunchOfNoteTakerActivityFromNavigationMenu(){
        assertNotNull(mActivity.findViewById(R.id.sideMenu));

        onView(withId(R.id.sideMenu)).perform(open());

        onView(withId(R.id.notes)).perform(click());

        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);

        assertNotNull(secondActivity);
        secondActivity.finish();
    }

    @Test
    public void testLaunchOfPomodoroTimerActivityFromNavigationMenu(){
        assertNotNull(mActivity.findViewById(R.id.sideMenu));

        onView(withId(R.id.sideMenu)).perform(open());

        onView(withId(R.id.timer)).perform(click());

        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor1,5000);

        assertNotNull(secondActivity);
        secondActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}