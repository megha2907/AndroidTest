package in.sportscafe.nostragamus.module.common;

import com.freshchat.consumer.sdk.Freshchat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by deepanshi on 1/16/18.
 */

public class NostraFireBaseMessagingService extends FirebaseMessagingService {

    // Sets an ID for the notification, so it can be updated
    public NostraFireBaseMessagingService() {
        super();
    }


    @Override
    public void onMessageReceived(RemoteMessage message) {

        // Handle FreshChat Notifications with FCM
        if (Freshchat.isFreshchatNotification(message)) {
            Freshchat.getInstance(this).handleFcmMessage(message);
        } else {
            //Handle notifications with data payload for your app
        }
    }
}