package com.snowbud56.utils;

public class MathUtil {

    public static Double roundToTens(Double num, Double den) {
        return roundToTens(num/den);
    }

    public static Double roundToTens(Double amount) {
        return Math.floor((amount * 100D) / 10D) / 10D;
    }

    public static int floor(Double num, Double den) {
        return floor(num/den);
    }

    public static int floor(Double amount) {
        return (int) Math.floor(amount);
    }
}
