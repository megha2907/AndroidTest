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
import in.sportscafe.scgame.ScGame;
import in.sportscafe.scgame.ScGameDataHandler;
import in.sportscafe.scgame.module.home.HomeActivity;
import in.sportscafe.scgame.module.play.myresults.MyResultsActivity;
import in.sportscafe.scgame.module.test.TestActivity;
import in.sportscafe.scgame.module.user.group.admin.adminmembers.AdminMembersActivity;
import in.sportscafe.scgame.module.user.group.groupinfo.GroupInfoActivity;
import in.sportscafe.scgame.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.scgame.webservice.MyWebService;

/**
 * Created by deepanshi on 5/8/16.
 */
public class NotificationCustom extends PushMessageListener {

    public static final int NOTIFICATION_ID = 1;
    private static final String EXTRA_CUSTOM_PAYLOAD = "ex_self_silent_update";
    private static final String EXTRA_JOIN_GROUP_REQUEST = Constants.NotificationKeys.JOIN_GROUP_REQUEST;
    private static final String EXTRA_APPROVED_GROUP_REQUEST = Constants.NotificationKeys.APPROVED_GROUP_REQUEST;
    private static final String EXTRA_RESULTS_LEADERBOARD = Constants.NotificationKeys.RESULTS_LEADERBOARD;
    Intent serviceIntent;

    @Override
    protected void onPostNotificationReceived(Context context, Bundle extras) {
        super.onPostNotificationReceived(context, extras);
        //Update your own notification inbox
        Log.i("extras in notification",extras.toString());
    }

    @Override
    public boolean isNotificationRequired(Context context, Bundle extras) {
        boolean result = super.isNotificationRequired(context,
                extras);//if SUPER is not not called then it will throw an exception
        if (result) {
            //your logic to check whether notification is required or not.
            //return true or false based on your logic
            Log.i("EXTRA_NOTIFICATION", extras.toString());
            if (extras.containsKey(EXTRA_JOIN_GROUP_REQUEST) || extras.containsKey(EXTRA_APPROVED_GROUP_REQUEST) || extras.containsKey(EXTRA_RESULTS_LEADERBOARD)) {
                return true;
            }

        }
        return result;
    }

    @Override
    public NotificationCompat.Builder onCreateNotification(Context context, Bundle extras,
                                                           ConfigurationProvider provider) {
        if (extras.containsKey(EXTRA_JOIN_GROUP_REQUEST) || extras.containsKey(EXTRA_APPROVED_GROUP_REQUEST) || extras.containsKey(EXTRA_RESULTS_LEADERBOARD)) {

            String title = MoEngageNotificationUtils.getNotificationTitleIfAny(extras);
            String contenttext = MoEngageNotificationUtils.getNotificationContentTextIfAny(extras);
            String groupId = extras.getString(Constants.NotificationKeys.GROUP_ID);


            if (extras.containsKey(EXTRA_JOIN_GROUP_REQUEST)) {

                String groupInfoStr = extras.getString("group_info");
                Log.i("newGroupInfo", groupInfoStr);
                GroupInfo groupInfo = MyWebService.getInstance().getObjectFromJson(groupInfoStr, GroupInfo.class);
                if(null != groupInfo)
                {    Log.i("goiing inside", "scdata");
                    ScGameDataHandler.getInstance().addNewGroup(groupInfo);
                }
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.BundleKeys.GROUP_ID, Long.parseLong(groupId));
                serviceIntent = new Intent(context, TestActivity.class);
                serviceIntent.putExtras(bundle);
                Log.i("groupId",groupId);

            } else if (extras.containsKey(EXTRA_APPROVED_GROUP_REQUEST)) {

                String groupInfoStr = extras.getString("group_info");
                Log.i("newGroupInfo2", groupInfoStr);
                GroupInfo groupInfo = MyWebService.getInstance().getObjectFromJson(groupInfoStr, GroupInfo.class);
                if(null != groupInfo) {
                    ScGameDataHandler.getInstance().addNewGroup(groupInfo);
                }

                serviceIntent = new Intent(context, GroupInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.BundleKeys.GROUP_ID, Long.parseLong(groupId));
                serviceIntent.putExtras(bundle);

            }
            else if (extras.containsKey(EXTRA_RESULTS_LEADERBOARD)) {
                serviceIntent = new Intent(context, MyResultsActivity.class);
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, serviceIntent, 0);

            // Use NotificationCompat.Builder to set up our notification.
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.white_notification_icon);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.white_notification_icon));
            builder.setContentTitle(title);
            builder.setContentText(contenttext);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);
            builder.build();


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
//TODO check why is it not giving groupinfo with return builder
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
            Intent serviceIntent = new Intent(context, GroupInfoActivity.class);
            serviceIntent.putExtras(extras);
            context.startService(serviceIntent);
        } else if (extras.containsKey(EXTRA_JOIN_GROUP_REQUEST)) {
            Intent serviceIntent = new Intent(context, GroupInfoActivity.class);
            serviceIntent.putExtras(extras);
            context.startService(serviceIntent);
        }
    }
}