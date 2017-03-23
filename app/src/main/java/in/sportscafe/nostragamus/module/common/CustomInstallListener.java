package in.sportscafe.nostragamus.module.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.moe.pushlibrary.InstallReceiver;

import io.branch.referral.InstallListener;

/**
 * Created by Jeeva on 8/11/16.
 */

public class CustomInstallListener extends BroadcastReceiver {

    private static final String TAG = "CustomInstallListener";

    private static final String INSTALL_REFERRER = "referrer";

    @Override
    public void onReceive(Context context, Intent intent) {
        new CampaignTrackingReceiver().onReceive(context, intent);
        new InstallReceiver().onReceive(context, intent);
        new InstallListener().onReceive(context, intent);
    }
}