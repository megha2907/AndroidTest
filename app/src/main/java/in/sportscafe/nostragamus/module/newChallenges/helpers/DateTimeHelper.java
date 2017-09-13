package in.sportscafe.nostragamus.module.newChallenges.helpers;

import android.text.TextUtils;

import java.util.Calendar;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by sandip on 06/09/17.
 */

public class DateTimeHelper {

    public static String getChallengeDuration(String startTime, String endTime) {
        String timeDurationStr = "";

        long startMillis = TimeUtils.getMillisecondsFromDateString(startTime);
        long endMillis = TimeUtils.getMillisecondsFromDateString(endTime);

        int startDayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(startMillis, "d"));
        int endDayOfMonth = Integer.parseInt(TimeUtils.getDateStringFromMs(endMillis, "d"));

        timeDurationStr = startDayOfMonth + AppSnippet.ordinalOnly(startDayOfMonth) + " " + TimeUtils.getDateStringFromMs(startMillis, "MMM")
                + " - " +
                endDayOfMonth + AppSnippet.ordinalOnly(endDayOfMonth) + " " + TimeUtils.getDateStringFromMs(endMillis, "MMM");

        return timeDurationStr;
    }

    public static String getStartTime(String startTime) {
        String startTimeStr = "";
        if (!TextUtils.isEmpty(startTime)) {
            long millis = TimeUtils.getMillisecondsFromDateString(startTime);
            if (millis > 0) {
                long days = TimeUtils.getDaysDifference(millis - Calendar.getInstance().getTimeInMillis());
                if (days > 1) {
                    startTimeStr = String.valueOf(days + " days");
                } else {
                    startTimeStr = TimeUtils.getDateStringFromMs(millis, Constants.DateFormats.CHALLENGE_START_TIME_FORMAT);
                }
            }
        }
        return startTimeStr;
    }

    public static String getInPlayMatchTime(String time) {
        String startTimeStr = "";

        if (!TextUtils.isEmpty(time)) {
            long millis = TimeUtils.getMillisecondsFromDateString(time);
            if (millis > 0) {
                long days = TimeUtils.getDaysDifference(millis - Calendar.getInstance().getTimeInMillis());
                if (days > 1) {
                    startTimeStr = String.valueOf(days + " days");
                } else {
                    startTimeStr = TimeUtils.getDateStringFromMs(millis, Constants.DateFormats.CHALLENGE_START_TIME_FORMAT);
                }
            }
        }
        return startTimeStr;
    }

}
