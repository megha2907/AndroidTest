package in.sportscafe.nostragamus.module.nostraHome.helper;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.utils.timeutils.TimeAgo;
import in.sportscafe.nostragamus.utils.timeutils.TimeUtils;

/**
 * Created by sandip on 04/10/17.
 */

public class TimerHelper {

    public synchronized static long getCountDownFutureTime(@NonNull String startTime) {
        long countDownTime = -1;

        if (!TextUtils.isEmpty(startTime)) {
            try {
                long startTimeMs = TimeUtils.getMillisecondsFromDateString(startTime,
                        Constants.DateFormats.FORMAT_DATE_T_TIME_ZONE, Constants.DateFormats.GMT);
                TimeAgo timeAgo = TimeUtils.calcTimeAgo(Nostragamus.getInstance().getServerTime(), startTimeMs);
                if (timeAgo != null) {
                    countDownTime = timeAgo.totalDiff;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        return countDownTime;
    }

    public synchronized static String getTimerFormatFromMillis(long millis) {
        String str = "";

        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60));   //(millisUntilFinished / (1000 * 60 * 60)) % 60; (If 24 hrs based result required)

        str = String.format("%02dh %02dm %02ds", hour, minute, second);

        return str;
    }

}
