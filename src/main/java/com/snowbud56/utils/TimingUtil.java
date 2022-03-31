package com.snowbud56.utils;

import java.util.HashMap;

public class TimingUtil {

    private static final HashMap<String, Timing> timings = new HashMap<>();

    public static void startTiming(String name) {
        timings.put(name, new Timing());
    }

    public static Timing stopTiming(String name) {
        if (!timings.containsKey(name)) return new Timing();
        Timing timing = timings.get(name);
        timings.remove(name);
        return timing;
    }

    public static class Timing {

        private final long nanoStart;
        private final long milliStart;

        Timing() {
            this.nanoStart = System.nanoTime();
            this.milliStart = System.currentTimeMillis();
        }

        public long getNanoDuration() {
            return System.nanoTime() - nanoStart;
        }

        public long getMilliDuration() {
            return System.currentTimeMillis() - milliStart;
        }
    }
}
