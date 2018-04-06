package in.sportscafe.nostragamus.module.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationClient;
import com.amazonaws.regions.Regions;
import com.freshchat.consumer.sdk.Freshchat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.awsAnalytics.NostraGCMListener;

import static in.sportscafe.nostragamus.module.analytics.awsAnalytics.NostraGCMListener.LOGTAG;

/**
 * Created by deepanshi on 1/16/18.
 */

public class NostraFireBaseMessagingService extends FirebaseMessagingService {

    public static PinpointManager pinpointManager;

    public static final String LOGTAG = NostraFireBaseMessagingService.class.getSimpleName();


    // Intent action used in local broadcast
    public static final String ACTION_PUSH_NOTIFICATION = "push-notification";
    // Intent keys
    public static final String INTENT_SNS_NOTIFICATION_FROM = "from";
    public static final String INTENT_SNS_NOTIFICATION_DATA = "data";

    /**
     * Helper method to extract push message from bundle.
     *
     * @param data bundle
     * @return message string from push notification
     */
    public static String getMessage(Bundle data) {
        // If a push notification is sent as plain
        // text, then the message appears in "default".
        // Otherwise it's in the "message" for JSON format.
        return data.containsKey("default") ? data.getString("default") : data.getString(
                "message", "");
    }

    private void broadcast(final String from, RemoteMessage message) {
        Intent intent = new Intent(ACTION_PUSH_NOTIFICATION);
        intent.putExtra(INTENT_SNS_NOTIFICATION_FROM, from);
        intent.putExtra(INTENT_SNS_NOTIFICATION_DATA, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    // Sets an ID for the notification, so it can be updated
    public NostraFireBaseMessagingService() {
        super();
    }


    @Override
    public void onMessageReceived(RemoteMessage message) {

        // Handle FreshChat Notifications with FCM
        if (Freshchat.isFreshchatNotification(message)) {
            Log.d(LOGTAG, "inside:freschat");
            Freshchat.getInstance(this).handleFcmMessage(message);
        } else {
            //Handle notifications with data payload for your app
            Log.d(LOGTAG, "inside:aws");
            Log.d(LOGTAG, "From:" + message.getFrom());
            Log.d(LOGTAG, "Data:" + message.toString());

            CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(), getApplicationContext().getString(R.string.aws_pinpoint_pool_id), Regions.AP_SOUTHEAST_1);
            PinpointConfiguration config = new PinpointConfiguration(getApplicationContext(), getApplicationContext().getString(R.string.aws_app_id), Regions.US_EAST_1, cognitoCachingCredentialsProvider);
            this.pinpointManager = new PinpointManager(config);

            NotificationClient.CampaignPushResult pushResult = pinpointManager.getNotificationClient().handleFCMCampaignPush(message.getFrom(), message.getData());

            if (!NotificationClient.CampaignPushResult.NOT_HANDLED.equals(pushResult)) {
                // The push message was due to a Pinpoint campaign.
                // If the app was in the background, a local notification was added
                // in the notification center. If the app was in the foreground, an
                // event was recorded indicating the app was in the foreground,
                // for the demo, we will broadcast the notification to let the main
                // activity display it in a dialog.
                if (
                        NotificationClient.CampaignPushResult.APP_IN_FOREGROUND.equals(pushResult)) {
                    // Create a message that will display the raw
                    //data of the campaign push in a dialog.
                    //message.putString("message", String.format("Received Campaign Push:\n%s", message.toString()));
                    broadcast(message.getFrom(), message);
                }
                return;
            }

        }
    }
}