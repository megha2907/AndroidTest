package in.sportscafe.nostragamus.module.notifications.inApp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.receiver.NotificationAlarmReceiver;
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
    public synchronized void sendNotification(Context appContext, String title, String msg, ArrayList<String> msgList) {
        Intent targetIntent = new Intent(appContext, NostraHomeActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, 0,
                targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(title);
        for (String msgStr : msgList) {
            inboxStyle.addLine(msgStr);
        }
        if (msgList.size() > 6) {
            inboxStyle.setSummaryText((msgList.size() - 6) + " More...");
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(appContext)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.white_notification_icon)
                .setContentIntent(pendingIntent)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setNumber(msgList.size());

        NotificationManager notificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(551, notificationBuilder.build());
    }
}
