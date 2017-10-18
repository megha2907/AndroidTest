package in.sportscafe.nostragamus.module.notifications;

import android.content.Context;
import android.os.Bundle;

import com.jeeva.android.Log;
import com.moengage.pushbase.push.PushMessageListener;

import in.sportscafe.nostragamus.Constants.Notifications;

public class NotificationPushMessageListener extends PushMessageListener {

    private static final String TAG = NotificationPushMessageListener.class.getSimpleName();

    @Override
    public boolean isNotificationRequired(Context context, Bundle extras) {
        boolean result = super.isNotificationRequired(context, extras);
        Log.i(TAG, extras.toString());
        extras.putBoolean(Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
        return result;
    }

}