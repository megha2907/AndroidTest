package in.sportscafe.nostragamus.module.notifications.inApp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import org.parceler.Parcels;

import java.util.Random;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.inPlay.dto.InPlayContestDto;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.notifications.NostraNotification;
import in.sportscafe.nostragamus.module.notifications.NostraNotificationData;
import in.sportscafe.nostragamus.service.InAppNotificationService;

/**
 * Created by sc on 5/1/18.
 */

public class InAppNotificationHelper {
    private final String TAG = InAppNotificationHelper.class.getSimpleName();
    private final int ALARM_REQUEST_ID = 1999;

    /**
     * Checks OR Sets repeating alarm for InApp Notifications
     * @param appContext
     */
    public synchronized void setAlarmForInAppNotifications(Context appContext) {
        AlarmManager alarmMgr = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(appContext, InAppNotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(appContext, ALARM_REQUEST_ID, intent, 0);

        boolean isAlarmSet = (PendingIntent.getService(appContext, ALARM_REQUEST_ID, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (!isAlarmSet) {
            Log.d(TAG, "Alarm ALREADY set for In-app notifications");

        } else {
            alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(),
                    /*TODO AlarmManager.INTERVAL_HOUR*/60000,
                    pendingIntent);
            Log.d(TAG, "Set alarm for In-app notifications at : " + SystemClock.elapsedRealtime());
        }
    }

    /**
     * sends InApp Notifications
     * @param appContext
     * @param title
     * @param msg
     */
    public synchronized void sendNotification(final Context appContext, final String title,
                                              final String msg, String timeRemainingSubText,
                                              final InPlayContestDto inPlayContestDto) {
        int requestCode = new Random().nextInt();

        Intent notificationIntent = new Intent(appContext, NostraHomeActivity.class);
        notificationIntent.putExtras(getNotificationArgs(inPlayContestDto));

        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, requestCode /* For every notification, create a separate instance */,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(appContext)
                .setContentTitle(title)
                .setContentText(msg)
                .setSubText(timeRemainingSubText)
                .setSmallIcon(R.drawable.white_notification_icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setWhen(0);

        NotificationManager notificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, notificationBuilder.build());

    }

    private Bundle getNotificationArgs(InPlayContestDto inPlayContestDto) {
        NostraNotificationData nostraNotificationData = new NostraNotificationData();
        nostraNotificationData.setInPlayContestDto(inPlayContestDto);

        NostraNotification notificationDetails = new NostraNotification();
        notificationDetails.setScreenName(Constants.Notifications.SCREEN_IN_PLAY_MATCHES);
        notificationDetails.setData(nostraNotificationData);

        Bundle args = new Bundle();
        args.putBoolean(Constants.Notifications.IS_LAUNCHED_FROM_NOTIFICATION, true);
        args.putBoolean(Constants.Notifications.IS_IN_APP_NOTIFICATION, true);
        args.putParcelable(Constants.BundleKeys.IN_APP_NOSTRA_NOTIFICATION_DETAILS, Parcels.wrap(notificationDetails));

        return args;
    }
}
