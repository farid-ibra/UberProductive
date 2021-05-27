package com.example.uberproductive.controller;

import android.app.Activity;
import android.app.Instrumentation;

import com.example.uberproductive.NoteTaker;
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

public class SaveNoteTest {
    private String TITLE = "Title";
    private String CONTENT = "Content";

    @Rule
    public ActivityTestRule<SaveNote> mActivityTestRule =  new ActivityTestRule<SaveNote>(SaveNote.class);

    private SaveNote mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(NoteTaker.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testAddingNoteToDatabase(){
       assertNotNull(R.id.addNoteTitle);

       onView(withId(R.id.addNoteTitle)).perform(typeText(TITLE));
       closeSoftKeyboard();

       assertNotNull(R.id.addNoteContent);

       onView(withId(R.id.addNoteContent)).perform(typeText(CONTENT));
       closeSoftKeyboard();

       onView(withId(R.id.fab)).perform(click());
       Activity secondActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
       assertNotNull(secondActivity);
       secondActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}