package com.jeeva.android;

import com.crashlytics.android.Crashlytics;

/**
 * Created by Jeeva on 7/1/15.
 */
public class ExceptionTracker {

    public static void track(Throwable throwable) {
        Crashlytics.logException(throwable);
        throwable.printStackTrace();
    }

    public static void track(String message) {
        Crashlytics.log(message);
    }
}