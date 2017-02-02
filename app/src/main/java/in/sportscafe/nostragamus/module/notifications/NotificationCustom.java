package in.sportscafe.nostragamus.module.notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.jeeva.android.Log;
import com.moe.pushlibrary.utils.MoEHelperConstants;
import com.moengage.config.ConfigurationProvider;
import com.moengage.pushbase.push.PushMessageListener;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.webservice.MyWebService;

/**
 * Created by deepanshi on 5/8/16.
 */
public class NotificationCustom extends PushMessageListener {

    public static final int NOTIFICATION_ID = 1;
    private static final String EXTRA_BADGE_REQUEST = Constants.NotificationKeys.BADGE_REQUEST;
    private static final String EXTRA_JOIN_GROUP_REQUEST = Constants.NotificationKeys.JOIN_GROUP_REQUEST;
    private static final String EXTRA_APPROVED_GROUP_REQUEST = Constants.NotificationKeys.APPROVED_GROUP_REQUEST;
    private static final String EXTRA_RESULTS_LEADERBOARD = Constants.NotificationKeys.RESULTS_LEADERBOARD;
    private static final String EXTRA_DAILY_NOTIFICATION_REQUEST = Constants.NotificationKeys.DAILY_NOTIFICATION_REQUEST;
    private static final String EXTRA_HOURLY_NOTIFICATION_REQUEST = Constants.NotificationKeys.HOURLY_NOTIFICATION_REQUEST;

    @Override
    public boolean isNotificationRequired(Context context, Bundle extras) {
        boolean result = super.isNotificationRequired(context, extras);
        //if SUPER is not not called then it will throw an exception
        Log.i("EXTRA_NOTIFICATION", extras.toString());
        if (result) {
            //your logic to check whether notification is required or not.
            //return true or false based on your logic

            if (extras.containsKey(EXTRA_JOIN_GROUP_REQUEST)
                    || extras.containsKey(EXTRA_APPROVED_GROUP_REQUEST)
                    || extras.containsKey(EXTRA_RESULTS_LEADERBOARD)) {
                return true;
            }
        }
        return result;
    }

    @Override
    public NotificationCompat.Builder onCreateNotification(Context context, Bundle extras, ConfigurationProvider provider) {
        if (extras.containsKey(EXTRA_JOIN_GROUP_REQUEST)) { // sending group id

            extras.putString(MoEHelperConstants.GCM_EXTRA_ACTIVITY_NAME,
                    "in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity");
            String groupInfoStr = extras.getString("group_info");
            GroupInfo groupInfo = MyWebService.getInstance().getObjectFromJson(groupInfoStr, GroupInfo.class);
            if (null != groupInfo) {
                NostragamusDataHandler.getInstance().addNewGroup(groupInfo);
            }
        } else if (extras.containsKey(EXTRA_APPROVED_GROUP_REQUEST)) { // sending group id
            extras.putString(MoEHelperConstants.GCM_EXTRA_ACTIVITY_NAME,
                    "in.sportscafe.nostragamus.module.user.group.groupinfo.GroupInfoActivity");

            String groupInfoStr = extras.getString("group_info");
            GroupInfo groupInfo = MyWebService.getInstance().getObjectFromJson(groupInfoStr, GroupInfo.class);
            if (null != groupInfo) {
                NostragamusDataHandler.getInstance().addNewGroup(groupInfo);
            }
        } else if (extras.containsKey(EXTRA_RESULTS_LEADERBOARD)) { // sending match id
            extras.putString(MoEHelperConstants.GCM_EXTRA_ACTIVITY_NAME,
                    "in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity");
        }
        else if (extras.containsKey(EXTRA_BADGE_REQUEST)) {
            extras.putString(MoEHelperConstants.GCM_EXTRA_ACTIVITY_NAME,
                    "in.sportscafe.nostragamus.module.home.HomeActivity");
        }


        return super.onCreateNotification(context, extras, provider);
    }
}