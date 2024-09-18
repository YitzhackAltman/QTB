package org.example;

// TODO: maybe run on separate Thread
public class Time {
    private long startTime;
    private static final long FIVE_MINUTES_IN_MILLIS = 5 * 60 * 1000;

    public Time() {
        this.startTime = System.currentTimeMillis();
    }

    // Checks if 5 minutes have passed
    public boolean isFiveMinutesOver() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - startTime) >= FIVE_MINUTES_IN_MILLIS;
    }

    public void resetTimer() {
        this.startTime = System.currentTimeMillis();
    }
}
