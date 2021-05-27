package com.example.uberproductive;

import android.app.Activity;
import android.app.Instrumentation;

import com.example.uberproductive.authentication.Login;
import com.example.uberproductive.controller.SaveNote;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.DrawerActions.open;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class NoteTakerTest {

    @Rule
    public ActivityTestRule<NoteTaker> mActivityTestRule =  new ActivityTestRule<NoteTaker>(NoteTaker.class);

    private NoteTaker mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(SaveNote.class.getName(),null,false);
    Instrumentation.ActivityMonitor monitor2 = getInstrumentation().addMonitor(Login.class.getName(),null,false);
    Instrumentation.ActivityMonitor monitor3 = getInstrumentation().addMonitor(GoalTracker.class.getName(),null,false);
    Instrumentation.ActivityMonitor monitor4 = getInstrumentation().addMonitor(PomodoroTimer.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLaunchOfAddNoteActivityOnFloatingButtonClick(){
        assertNotNull(mActivity.findViewById(R.id.AddButton));

        onView(withId(R.id.AddButton)).perform(click());

        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);

        assertNotNull(secondActivity);

        secondActivity.finish();
    }

    @Test
    public void testLaunchOfLoginActivityFromNavigationMenu(){
       assertNotNull(mActivity.findViewById(R.id.sideMenu));

       onView(withId(R.id.sideMenu)).perform(open());

       onView(withId(R.id.login)).perform(click());

       Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor2,5000);

        //in case of user is temporary
        /*
        assertNotNull(secondActivity);
        secondActivity.finish();
        */
        // in case user is logged in
        /*
        assertNull(secondActivity);
         */
    }

    @Test
    public void testLaunchOfGoalTrackerActivityFromNavigationMenu(){
        assertNotNull(mActivity.findViewById(R.id.sideMenu));

        onView(withId(R.id.sideMenu)).perform(open());

        onView(withId(R.id.goalTracker)).perform(click());

        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor3,5000);

        assertNotNull(secondActivity);
        secondActivity.finish();
    }
    @Test
    public void testLaunchOfPomodoroTimerActivityFromNavigationMenu(){
        assertNotNull(mActivity.findViewById(R.id.sideMenu));

        onView(withId(R.id.sideMenu)).perform(open());

        onView(withId(R.id.timer)).perform(click());

        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor4,5000);

        assertNotNull(secondActivity);
        secondActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}