package in.sportscafe.nostragamus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sc on 5/1/18.
 */

public class NotificationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null &&
                (intent.getAction().equals(Intent.ACTION_TIME_CHANGED) ||
                        intent.getAction().equals(Intent.ACTION_TIME_TICK))) {
            Log.d("Time", "Time changed");
        }
    }
}
