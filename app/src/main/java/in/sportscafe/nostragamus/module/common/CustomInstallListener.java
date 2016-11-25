package in.sportscafe.nostragamus.module.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.analytics.CampaignTrackingReceiver;
import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;
import com.moe.pushlibrary.InstallReceiver;

import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
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

        try {
            Bundle bundle = intent.getExtras();

            if(bundle.containsKey(INSTALL_REFERRER)) {
                Log.i(TAG, "Referrer String --> " + bundle.getString(INSTALL_REFERRER));
            }

            if(bundle.containsKey(BundleKeys.GROUP_CODE)) {
                NostragamusDataHandler.getInstance().setInstallGroupCode(bundle.getString(BundleKeys.GROUP_CODE));
                Log.i(TAG, "Referrer String --> " + bundle.getString(BundleKeys.GROUP_CODE));
            }

            Log.i(TAG, "Install Bundle --> " + bundle.toString());

            Log.i(TAG, "Install Data --> " + intent.getDataString());
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }
    }
}