package in.sportscafe.nostragamus.module.newChallenges.helpers;

import android.text.TextUtils;

import com.jeeva.android.Log;

import java.util.Calendar;
import java.util.Timer;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.nostraHome.helper.TimerHelper;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUnit;
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

        if (startDayOfMonth == endDayOfMonth) {

            timeDurationStr = startDayOfMonth + AppSnippet.ordinalOnly(startDayOfMonth)
                    + " " + TimeUtils.getDateStringFromMs(startMillis, "MMM");

        } else if (TimeUtils.getDateStringFromMs(startMillis, "MMM")
                .equalsIgnoreCase(TimeUtils.getDateStringFromMs(endMillis, "MMM"))) {

            timeDurationStr = startDayOfMonth + AppSnippet.ordinalOnly(startDayOfMonth)
                    + " - " + endDayOfMonth + AppSnippet.ordinalOnly(endDayOfMonth)
                    + " " + TimeUtils.getDateStringFromMs(endMillis, "MMM");

        } else {
            timeDurationStr = startDayOfMonth + AppSnippet.ordinalOnly(startDayOfMonth) + " " + TimeUtils.getDateStringFromMs(startMillis, "MMM")
                    + " - " +
                    endDayOfMonth + AppSnippet.ordinalOnly(endDayOfMonth) + " " + TimeUtils.getDateStringFromMs(endMillis, "MMM");
        }

        return timeDurationStr;
    }

    public static String getStartTime(String startTime) {
        String startTimeStr = "";
        if (!TextUtils.isEmpty(startTime)) {
            long millis = TimeUtils.getMillisecondsFromDateString(startTime);
            if (millis > 0) {
                long days = TimeUtils.getDaysDifference(millis - Nostragamus.getInstance().getServerTime());
                if (days > 1) {
                    startTimeStr = String.valueOf(days + " days");
                } else {
                    startTimeStr = TimeUtils.getDateStringFromMs(millis, Constants.DateFormats.CHALLENGE_START_TIME_FORMAT);
                }
            }
        }
        return startTimeStr;
    }

    public static synchronized boolean isTimerRequired(String startTime) {
        boolean isTimerRequired = false;

        if (!TextUtils.isEmpty(startTime)) {
            long millis = TimeUtils.getMillisecondsFromDateString(startTime);
            if (millis > 0) {
                long days = TimeUtils.getDaysDifference(millis - Nostragamus.getInstance().getServerTime());
                if (days <= 1) {
                    isTimerRequired = true;
                }
            }
        }

        return isTimerRequired;
    }

    public static String getInPlayMatchTime(String time) {
        String startTimeStr = "";

        if (!TextUtils.isEmpty(time)) {
            long millis = TimeUtils.getMillisecondsFromDateString(time);
            if (millis > 0) {
                long days = TimeUtils.getDaysDifference(millis - Nostragamus.getInstance().getServerTime());
                if (days > 1) {
                    startTimeStr = String.valueOf(days + " days left");
                } else {
                    startTimeStr = TimeUtils.getDateStringFromMs(millis, Constants.DateFormats.CHALLENGE_START_TIME_FORMAT);
                }
            }
        }
        return startTimeStr;
    }

    public static boolean isMatchStarted(String matchStartTime) {

        boolean isMatchStarted = false;

        if (!TextUtils.isEmpty(matchStartTime)) {
            String startTime = matchStartTime.replace("+00:00", ".000Z");
            long startTimeMs = TimeUtils.getMillisecondsFromDateString(
                    startTime,
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE,
                    Constants.DateFormats.GMT
            );
            TimeAgo timeAgo = TimeUtils.calcTimeAgo(Nostragamus.getInstance().getServerTime(), startTimeMs);

            isMatchStarted = timeAgo.timeDiff <= 0
                    || timeAgo.timeUnit == TimeUnit.MILLISECOND
                    || timeAgo.timeUnit == TimeUnit.SECOND;
        }

        return isMatchStarted;
    }

    /**
     * Use to check that challenge / match started/completed
     * @param startTime
     * @return
     */
    public static boolean isChallengeTimeOver(String startTime) {
        boolean isStarted = false;

        if (!TextUtils.isEmpty(startTime)) {
            startTime = startTime.replace("+00:00", ".000Z");
            long startTimeMs = TimeUtils.getMillisecondsFromDateString(startTime,
                    Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE, Constants.DateFormats.GMT);
            TimeAgo timeAgo = TimeUtils.calcTimeAgo(Nostragamus.getInstance().getServerTime(), startTimeMs);

            isStarted = timeAgo.timeDiff <= 0
                    || timeAgo.timeUnit == TimeUnit.MILLISECOND
                    || timeAgo.timeUnit == TimeUnit.SECOND;
        }

        return isStarted;
    }

    /**
     * Used for : In-app notification
     * @param startTime - match start time server string
     * @param approxServerTime - saved serverTime which is calculated as current approximate server time
     *                         NOTE: Do not use Nostragamus.getInstance().getServerTime() - as this is updated only in case of app instance live...
     * @return - minutes remaining before starting match
     */
    public synchronized static long getMatchStartRemainingTimeInMinutesForInAppNoti(String startTime, long approxServerTime) {
        long minutesDifference = 0;
        if (!TextUtils.isEmpty(startTime)) {
            long millis = TimeUtils.getMillisecondsFromDateString(startTime);
            if (millis > 0) {
                minutesDifference = TimeUtils.getMinutesDifference(millis - approxServerTime);
            }
        }
        return minutesDifference;
    }

    /**
     *
     * @param startTime
     * @return Returns time string for match start to show in InApp notifications
     */
    public synchronized static String getInAppMatchStartTime(String startTime) {
        String str = "";

        long millis = 0;
        if (!TextUtils.isEmpty(startTime)) {
            try {
                millis = TimeUtils.getMillisecondsFromDateString(startTime,
                        Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE, Constants.DateFormats.GMT);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        str = TimeUtils.getDateStringFromMs(millis, Constants.DateFormats.HH_MM_AA);

        return str;
    }
}
