package com.example.uberproductive;

import org.junit.Test;

import static org.junit.Assert.*;

public class PomodoroTimerTest {
    private static final long TIME_IN_MILLISECONDS = 1500000 ;

    @Test
    public void unitTest() {
        long seconds = 1500000;
        assertEquals(TIME_IN_MILLISECONDS, seconds);
    }
}