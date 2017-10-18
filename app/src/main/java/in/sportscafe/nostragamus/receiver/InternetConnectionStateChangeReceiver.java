package in.sportscafe.nostragamus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Nostragamus;

/**
 * Created by sandip on 11/09/17.
 */

public class InternetConnectionStateChangeReceiver extends BroadcastReceiver {
    private static final String TAG = InternetConnectionStateChangeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Nostragamus.getInstance().hasNetworkConnection()) {
            Log.d(TAG, "Internet Connected!");
            Intent internetIntent = new Intent(Constants.IntentActions.ACTION_INTERNET_STATE_CHANGED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(internetIntent);
        } else {
            Log.d(TAG, "Internet Disconnected!");
        }
    }
}
