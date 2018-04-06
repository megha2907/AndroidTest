package in.sportscafe.nostragamus.module.common;

import com.freshchat.consumer.sdk.Freshchat;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Nostragamus;

/**
 * Created by deepanshi on 1/16/18.
 */

public class NostraFireBaseInstanceIDService extends FirebaseInstanceIdService {


    private static final String TAG = NostraFireBaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        if (refreshedToken != null) {
            // Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(refreshedToken);
        }
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Send Token to Freshchat
        Freshchat.getInstance(this).setPushRegistrationToken(token);
        Nostragamus.getInstance().getServerDataManager().setGcmDeviceToken(token);
    }

}

