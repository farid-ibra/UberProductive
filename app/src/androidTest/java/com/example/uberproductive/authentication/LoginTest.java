package com.example.uberproductive.authentication;

import android.app.Activity;
import android.app.Instrumentation;

import com.example.uberproductive.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertNotNull;

public class LoginTest {
    private String EMAIL = "farid.elte@gmail.com";
    private String PASSWORD = "Student1";

    @Rule
    public ActivityTestRule<Login> mActivityTestRule =  new ActivityTestRule<Login>(Login.class);

    private Login mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(Register.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLaunchAndLogin() throws InterruptedException {
        assertNotNull(R.id.email);
        assertNotNull(R.id.LoginPassword);
        assertNotNull(R.id.LoginButton);

        onView(withText("DELETE DATA")).perform(click());
        sleep(2000);

        onView(withId(R.id.email)).perform(typeText(EMAIL));
        closeSoftKeyboard();

        onView(withId(R.id.LoginPassword)).perform(typeText(PASSWORD));
        closeSoftKeyboard();

        onView(withId(R.id.LoginButton)).perform(click());
    }

    @Test
    public void testLaunchRegister(){
        assertNotNull(R.id.register);
        onView(withText("DELETE DATA")).perform(click());
        onView(withId(R.id.register)).perform(click());
        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNotNull(secondActivity);
        secondActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}