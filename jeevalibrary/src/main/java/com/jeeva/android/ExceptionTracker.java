package com.jeeva.android;

import com.crashlytics.android.Crashlytics;

/**
 * Created by Jeeva on 7/1/15.
 */
public class ExceptionTracker {

    public static void track(Throwable throwable) {
        try {
            Crashlytics.logException(throwable);
        } catch (Exception e) {
        }
        throwable.printStackTrace();
    }

    public static void track(String message) {
        try {
            Crashlytics.log(message);
        } catch (Exception e) {
        }
    }
}