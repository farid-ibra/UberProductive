package com.example.uberproductive;

import org.junit.Test;
import static org.junit.Assert.*;

public class PomodoroTimerTest {
    @Test
    public void unitTest() {
        long seconds = 1500000;
        assertEquals(PomodoroTimer.wTimeLeftInMillis, seconds);
    }
}