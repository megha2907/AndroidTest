package in.sportscafe.nostragamus.module.common;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;

import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.InAppActivity;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;

/**
 * Created by Jeeva on 6/4/16.
 */
public class NostragamusActivity extends InAppActivity {

    private static final String NORMAL_UPDATE = "Normal";

    private static final String FORCE_UPDATE = "Force";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking the version code to request update or force update the application
//        checkAnyUpdate(); // Todo enable it later
    }

    private void checkAnyUpdate() {
        NostragamusDataHandler dataHandler = NostragamusDataHandler.getInstance();

        int currentAppVersion = Nostragamus.getInstance().getAppVersionCode();
        if(currentAppVersion < dataHandler.getForceUpdateVersion()) {
            showForceUpdateDialog(dataHandler.getForceUpdateMessage());
        } else if(dataHandler.isNormalUpdateEnabled()
                && currentAppVersion < dataHandler.getNormalUpdateVersion()) {
            showNormalUpdateDialog(dataHandler.getNormalUpdateMessage());
        }
    }

    private AlertDialog mUpdateDialog;

    private void showNormalUpdateDialog(String message) {
        mUpdateDialog = AppSnippet.getAlertDialog(this, getString(R.string.new_version_title),
                message,
                getString(R.string.update), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToPlayStore();

                        mUpdateDialog.dismiss();
                        mUpdateDialog = null;

                        NostragamusAnalytics.getInstance().trackUpdate(NORMAL_UPDATE, 1);
                    }
                },
                getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mUpdateDialog.dismiss();
                        mUpdateDialog = null;

                        NostragamusAnalytics.getInstance().trackUpdate(NORMAL_UPDATE, 0);
                    }
                }, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        NostragamusDataHandler.getInstance().setNormalUpdateEnabled(!isChecked);
                    }
                });

        mUpdateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(null != mUpdateDialog) {
                    mUpdateDialog = null;

                    NostragamusAnalytics.getInstance().trackUpdate(NORMAL_UPDATE, 0);
                }
            }
        });

        mUpdateDialog.show();
    }

    private void showForceUpdateDialog(String message) {
        AlertDialog alertDialog = AppSnippet.getAlertDialog(this, getString(R.string.new_version_title),
                message,
                getString(R.string.update), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToPlayStore();

                        NostragamusAnalytics.getInstance().trackUpdate(FORCE_UPDATE, 1);
                    }
                });
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                AppSnippet.closeApp();

                NostragamusAnalytics.getInstance().trackUpdate(FORCE_UPDATE, 0);
            }
        });
        alertDialog.show();
    }

    private void navigateToPlayStore() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_store_url))));
        } catch (ActivityNotFoundException e) {
            ExceptionTracker.track(e);
        }
    }
}