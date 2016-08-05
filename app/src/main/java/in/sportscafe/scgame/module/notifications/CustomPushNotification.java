package in.sportscafe.scgame.module.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.jeeva.android.Log;
import com.moengage.config.ConfigurationProvider;
import com.moengage.push.MoEngageNotificationUtils;
import com.moengage.push.PushMessageListener;

import in.sportscafe.scgame.Constants;
import in.sportscafe.scgame.R;
import in.sportscafe.scgame.module.user.group.admin.adminmembers.AdminMembersActivity;
import in.sportscafe.scgame.module.user.group.admin.approve.ApproveFragment;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupsDetailResponse;

/**
 * Created by deepanshi on 5/8/16.
 */
public class CustomPushNotification extends PushMessageListener {

    private static final String EXTRA_CUSTOM_PAYLOAD = "ex_self_silent_update";
    private static final String EXTRA_MY_NOTIFICATION = "group_request";
    public static final int NOTIFICATION_ID = 1;

    @Override
    protected void onPostNotificationReceived(Context context, Bundle extras) {
        super.onPostNotificationReceived(context, extras);
        //Update your own notification inbox
    }

    @Override
    public boolean isNotificationRequired(Context context, Bundle extras) {
        boolean result = super.isNotificationRequired(context,
                extras);//if SUPER is not not called then it will throw an exception
        if (result) {
            //your logic to check whether notification is required or not.
            //return true or false based on your logic
            Log.i("EXTRA_NOTIFICATION", extras.toString());
            if (extras.containsKey(EXTRA_MY_NOTIFICATION)) {
                return true;
            }

        }
        return result;
    }

    @Override
    public NotificationCompat.Builder onCreateNotification(Context context, Bundle extras,
                                                           ConfigurationProvider provider) {
        if (extras.containsKey(EXTRA_MY_NOTIFICATION)) {
            String title = MoEngageNotificationUtils.getNotificationTitleIfAny(extras);
            String contenttext = MoEngageNotificationUtils.getNotificationContentTextIfAny(extras);
            String groupId = extras.getString(Constants.BundleKeys.GROUP_ID);

            Intent serviceIntent = new Intent(context, AdminMembersActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.BundleKeys.GROUP_ID, Long.parseLong(groupId));
            serviceIntent.putExtras(bundle);
            Log.i("bundle", bundle.toString());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, serviceIntent, 0);

            // Use NotificationCompat.Builder to set up our notification.
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
            builder.setContentTitle(title);
            builder.setContentText(contenttext);
            builder.setContentIntent(pendingIntent);
            builder.setSubText("Notification Request.");
            builder.setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());

            return null;
        } else {
            return super.onCreateNotification(context, extras, provider);
        }
    }

    @Override
    public void onNonMoEngageMessageReceived(Context context, Bundle extras) {
        super.onNonMoEngageMessageReceived(context, extras);
        //do your own stuff here
    }

    @Override
    public void onNotificationNotRequired(Context context, Bundle extras) {
        super.onNotificationNotRequired(context, extras);
        if (extras.containsKey(EXTRA_CUSTOM_PAYLOAD)) {
            Intent serviceIntent = new Intent(context, ApproveFragment.class);
            serviceIntent.putExtras(extras);
            context.startService(serviceIntent);
        } else if (extras.containsKey(EXTRA_MY_NOTIFICATION)) {
            Intent serviceIntent = new Intent(context, GroupsDetailResponse.class);
            serviceIntent.putExtras(extras);
            context.startService(serviceIntent);
        }
    }
}