package com.snowbud56.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static String getDate() {

        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(new Date());
    }

    public static String getTime(boolean twelveHour) {
        DateFormat format;
        if (twelveHour)
            format = new SimpleDateFormat("KK:mm a");
        else {
            format = new SimpleDateFormat("kk:mm");
        }
        return format.format(new Date());
    }

    public static String getDuration(TimeUnit unit, Long milliseconds) {
        if (unit == TimeUnit.FIT) {
            if (milliseconds < 1000) {
                return milliseconds + " milliseconds";
            }
            long seconds = milliseconds/1000;
            StringBuilder builder = new StringBuilder();
            long updatedSec = seconds;
            TimeUnit curUnit = TimeUnit.DAY;
            while (curUnit != null) {

                if (updatedSec >= curUnit.getSeconds()) {
                    builder.append(getAmount(curUnit, updatedSec)).append(" ")
                            .append(curUnit.name().toLowerCase())
                            .append(getAmount(curUnit, updatedSec) == 1 ? "" : "s")
                            .append(" ");
                } else {
                    curUnit = curUnit.getNext();
                    continue;
                }

                updatedSec = getSecondsRounded(curUnit, updatedSec);
                curUnit = curUnit.getNext();
            }

            return builder.toString();
        }
        return MathUtil.roundToTens((double) milliseconds /1000, (double) unit.getSeconds()) + " " + unit.name().toLowerCase();
    }

    private static long getAmount(TimeUnit unit, long seconds) {
        return ((seconds - (seconds % unit.getSeconds())) / (long) unit.getSeconds());
    }

    private static long getSecondsRounded(TimeUnit unit, long seconds) {
        return seconds % unit.getSeconds();
    }

}
