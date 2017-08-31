package in.sportscafe.nostragamus.module.notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.jeeva.android.Log;
import com.moe.pushlibrary.utils.MoEHelperConstants;
import com.moengage.config.ConfigurationProvider;
import com.moengage.pushbase.push.PushMessageListener;

import in.sportscafe.nostragamus.Constants.NotificationKeys;
import in.sportscafe.nostragamus.module.user.myprofile.dto.GroupInfo;
import in.sportscafe.nostragamus.webservice.MyWebService;

/**
 * Created by deepanshi on 5/8/16.
 */
public class NotificationCustom extends PushMessageListener {

    @Override
    public boolean isNotificationRequired(Context context, Bundle extras) {
        boolean result = super.isNotificationRequired(context, extras);
        //if SUPER is not not called then it will throw an exception
        Log.i("EXTRA_NOTIFICATION", extras.toString());

        extras.putBoolean(NotificationKeys.FROM_NOTIFICATION, true);

        if (result) {
            //your logic to check whether notification is required or not.
            //return true or false based on your logic

            if (extras.containsKey(NotificationKeys.RESULTS_LEADERBOARD)) {
                return true;
            }
        }
        return result;
    }

    @Override
    public NotificationCompat.Builder onCreateNotification(Context context, Bundle extras, ConfigurationProvider provider) {

        if (extras.containsKey(NotificationKeys.RESULTS_LEADERBOARD)) { // sending Match id
            extras.putString(MoEHelperConstants.GCM_EXTRA_ACTIVITY_NAME,
                    "in.sportscafe.nostragamus.module.play.myresults.MyResultsActivity");
        }

        return super.onCreateNotification(context, extras, provider);
    }
}