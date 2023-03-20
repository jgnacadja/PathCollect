package org.odk.collect.android.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeAgo {
    public static final long SECOND_MILLIS = 1000;
    public static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final long DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");

    public static String formatTimestamp(long timestamp) {
        return formatter.format(new Date(timestamp));
    }

    public static String getTimeAgo(long time) {
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "maintenant";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "il y a 1 min.";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return "il y a " + diff / MINUTE_MILLIS + " min.";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "il y a 1h";
        } else if (diff < 24 * HOUR_MILLIS) {
            return "il y a " + diff / HOUR_MILLIS + "h";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "hier";
        } else {
            return "il y a " + diff / DAY_MILLIS + "j";
        }
    }
}