package com.snowbud56.utils;

public enum TimeUnit {

    SECOND(1),
    MINUTE(60),
    HOUR(60*60),
    DAY(60*60*24),
    FIT(0);

    private final int seconds;
    TimeUnit(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    public TimeUnit getNext() {
        if (this == DAY) return HOUR;
        else if (this == HOUR) return MINUTE;
        else if (this == MINUTE) return SECOND;
        else return null;
    }
}
