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
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class RegisterTest {

    private String FULLNAME = "Farid Ibrahimli";
    private String EMAIL = "farid.elte@gmail.com";
    private String PASSWORD = "Student1";
    private String CONFIRMPASSWORD = "Student1";

    @Rule
    public ActivityTestRule<Register> mActivityTestRule =  new ActivityTestRule<Register>(Register.class);

    private Register mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(Login.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLaunchAndRegistration(){

        assertNotNull(R.id.userName);
        assertNotNull(R.id.userEmail);
        assertNotNull(R.id.password);
        assertNotNull(R.id.passwordConfirm);
        assertNotNull(R.id.register);

        onView(withId(R.id.userName)).perform(typeText(FULLNAME));
        closeSoftKeyboard();

        onView(withId(R.id.userEmail)).perform(typeText(EMAIL));
        closeSoftKeyboard();

        onView(withId(R.id.password)).perform(typeText(PASSWORD));
        closeSoftKeyboard();

        onView(withId(R.id.passwordConfirm)).perform(typeText(CONFIRMPASSWORD));
        closeSoftKeyboard();

        onView(withId(R.id.register)).perform(click());
    }

    @Test
    public void testLaunchLogin(){
        assertNotNull(R.id.login);
        onView(withId(R.id.login)).perform(click());
        Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNotNull(secondActivity);
        secondActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}