package in.sportscafe.nostragamus.utils.timeutils;

import android.text.TextUtils;

import com.jeeva.android.ExceptionTracker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import in.sportscafe.nostragamus.Constants;

/**
 * Created by Jeeva on 23/3/16.
 */
public class TimeUtils {

    public static final String[] DEFAULT_TIME_AGO_FORMAT = new String[] {
            "Just Now", "minute", "hour", "day", "week", "month", "year", "ago"
    };

    public static String getTimeAgoString(String startDate, String currentFormat, String timeZone) {
        return convertTimeAgoToDefaultString(getTimeAgo(startDate, currentFormat, timeZone));
    }

    public static String getTimeLeftString(String startDate, String currentFormat, String timeZone) {
        return convertTimeAgoToDefaultString(getTimeLeft(startDate, currentFormat, timeZone));
    }

    public static String convertTimeAgoToDefaultString(TimeAgo timeAgo) {
        return convertTimeAgoToString(timeAgo, DEFAULT_TIME_AGO_FORMAT);
    }

    public static String convertTimeAgoToString(TimeAgo timeAgo, String[] timeAgoFormat) {
        String timeUnitString = "";
        switch (timeAgo.timeUnit) {
            case MILLISECOND:
            case SECOND:
                return timeAgoFormat[0];
            case MINUTE:
                timeUnitString = timeAgoFormat[1];
                break;
            case HOUR:
                timeUnitString = timeAgoFormat[2];
                break;
            case DAY:
                timeUnitString = timeAgoFormat[3];
                break;
            case WEEK:
                timeUnitString = timeAgoFormat[4];
                break;
            case MONTH:
                timeUnitString = timeAgoFormat[5];
                break;
            case YEAR:
                timeUnitString = timeAgoFormat[6];
                break;
        }
        return timeAgo.timeDiff + " " + timeUnitString + (timeAgo.timeDiff > 1 ? "s" : "") + " " + timeAgoFormat[7];
    }

    public static TimeAgo getTimeAgo(String startDate, String currentFormat, String timeZone) {
        return calcTimeAgo(getMillisecondsFromDateString(startDate,currentFormat, timeZone),
                Calendar.getInstance().getTimeInMillis());
    }

    public static TimeAgo getTimeLeft(String startDate, String currentFormat, String timeZone) {
        return calcTimeAgo(Calendar.getInstance().getTimeInMillis(),
                getMillisecondsFromDateString(startDate, currentFormat, timeZone));
    }

    public static TimeAgo calcTimeAgo(long startTime, long endTime) {
        long milliSecondDiff = endTime - startTime;
        long daysDifference = getDaysDifference(milliSecondDiff);

        if (daysDifference <= 0) {
            long[] splitTime = getTimeFromLongValue(milliSecondDiff);
            if (splitTime[0] > 0) {
                return new TimeAgo(milliSecondDiff, splitTime[0], TimeUnit.HOUR);
            } else if (splitTime[1] > 0) {
                return new TimeAgo(milliSecondDiff, splitTime[1], TimeUnit.MINUTE);
            } else if (splitTime[2] > 0) {
                return new TimeAgo(milliSecondDiff, splitTime[2], TimeUnit.SECOND);
            } else {
                return new TimeAgo(milliSecondDiff, splitTime[3], TimeUnit.MILLISECOND);
            }
        } else {
            if (daysDifference < 7) {
                return new TimeAgo(milliSecondDiff, daysDifference, TimeUnit.DAY);
            } else if (daysDifference < 30) {
                return new TimeAgo(milliSecondDiff, daysDifference / 7, TimeUnit.WEEK);
            }

            int monthsDifference = ((int) daysDifference / 30);
            if (monthsDifference < 12) {
                return new TimeAgo(milliSecondDiff, monthsDifference, TimeUnit.MONTH);
            } else {
                return new TimeAgo(milliSecondDiff, findYearDiff(startTime, endTime), TimeUnit.YEAR);
            }
        }
    }

    public static long[] getTimeFromLongValue(long value) {
        long milliseconds = value % 1000;
        long seconds = (value / 1000) % 60;
        long minutes = (value / (1000 * 60)) % 60;
        long hours = (value / (1000 * 60 * 60)) % 24;
        return new long[]{hours, minutes, seconds, milliseconds};
    }

    public static int findYearDiff(long startTime, long endTime) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startTime);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endTime);

        int yearDiff = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);

        if (endCalendar.get(Calendar.DAY_OF_YEAR) < startCalendar.get(Calendar.DAY_OF_YEAR)) {
            yearDiff--;
        }

        return yearDiff;
    }

    public static long getDaysDifference(long diff) {
        return java.util.concurrent.TimeUnit.DAYS.convert(diff, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public static long getMillisecondsFromDateString(String dateString, String currentFormat, String timeZone) {
        Date date = getDateFromDateString(dateString, currentFormat, timeZone);
        return date.getTime();
    }

    public static String getDateStringFromDate(Date date, String neededFormat) {
        SimpleDateFormat format = new SimpleDateFormat(neededFormat);
        return format.format(date);
    }

    public static Date getDateFromDateString(String dateString, String format, String timeZone) {
        if (!TextUtils.isEmpty(dateString) && !TextUtils.isEmpty(format) && !TextUtils.isEmpty(timeZone)) {
            DateFormat simpleDateFormat = new SimpleDateFormat(format);
            if (null != timeZone) {
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            }
            try {
                return simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                ExceptionTracker.track(e);
            }
        }
        return null;
    }

    public static String getDateStringFromDate(Date date, String format, String timeZone) {
        DateFormat gmtSimpleDateFormat = new SimpleDateFormat(format);
        if (null != timeZone) {
            gmtSimpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        }
        return gmtSimpleDateFormat.format(date);
    }

    public static String getDateStringFromMs(long time, String neededFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return getDateStringFromDate(calendar.getTime(), neededFormat);
    }

    public static String formatDateString(String dateString, String currentFormat, String requiredFormat, String currentFormatTimeZone, String requredFormatTimeZone) {
        return getDateStringFromDate(getDateFromDateString(dateString, currentFormat, currentFormatTimeZone), requiredFormat, requredFormatTimeZone);
    }

    public static Date getDateFromDateString(String date, String currentFormat) {
        try {
            return (new SimpleDateFormat(currentFormat).parse(date));
        } catch (ParseException e) {
            ExceptionTracker.track(e);
        }
        return null;
    }

    public static String getFormattedDateString(String date, String neededFormat, String currentFormat,
                                                String timeZone) {
        Date formDate = getDateFromDateString(date, currentFormat, timeZone);
        String finalFormat = new SimpleDateFormat(neededFormat).format(formDate);
        return finalFormat;
    }

    public static String getCurrentTime(String neededFormat, String timeZone) {
        return getDateStringFromDate(Calendar.getInstance().getTime(), neededFormat, timeZone);
    }

    public static long getMillisecondsFromDateString(String dateString) {
        Date date = getDateFromDateString(dateString, Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE, Constants.DateFormats.GMT);
        if (date != null) {
            return date.getTime();
        }
        return 0;
    }
}