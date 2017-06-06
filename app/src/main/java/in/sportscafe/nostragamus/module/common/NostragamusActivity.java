package in.sportscafe.nostragamus.module.common;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;

import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.InAppActivity;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.AppSnippet;
import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.popups.PopUp;
import in.sportscafe.nostragamus.module.popups.PopUpActivity;
import in.sportscafe.nostragamus.module.popups.PopUpModelImpl;
import in.sportscafe.nostragamus.module.settings.SettingActivity;

/**
 * Created by Jeeva on 6/4/16.
 */
public abstract class NostragamusActivity extends InAppActivity implements PopUpModelImpl.OnGetPopUpModelListener {

    public abstract String getScreenName();
    private String mScreenName;
    private boolean mShouldAnimateActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performActivityEntryAnimation();

        // Checking the version code to request update or force update the application
        checkAnyUpdate();

        String screenName = getScreenName();
        if (!TextUtils.isEmpty(screenName)) {
            PopUpModelImpl.newInstance(this).getPopUps(screenName);
        }

        NostragamusAnalytics.getInstance().trackAppOpening(AnalyticsLabels.NOTIFICATION);
    }

    @Override
    public void finish() {
        super.finish();
        performActivityExitAnimation();
    }

    /**
     * Set to true for activity if animation for entry/exit required
     * @param shouldAnimate
     */
    protected void setShouldAnimateActivity(boolean shouldAnimate) {
        mShouldAnimateActivity = shouldAnimate;
    }

    private void performActivityExitAnimation() {
        if (mShouldAnimateActivity) {
            overridePendingTransition(R.anim.activity_anim_stay, R.anim.slide_right_from_right);
        }
    }

    private void performActivityEntryAnimation() {
        if (mShouldAnimateActivity) {
            overridePendingTransition(R.anim.slide_left_from_right, R.anim.activity_anim_stay);
        }
    }

    private void checkAnyUpdate() {

        NostragamusDataHandler dataHandler = NostragamusDataHandler.getInstance();

        if (BuildConfig.IS_PAID_VERSION) {
            int currentAppVersion = Nostragamus.getInstance().getAppVersionCode();
            if (currentAppVersion < dataHandler.getForcePaidUpdateVersion()) {
                showForceUpdateDialog(dataHandler.getForcePaidUpdateMessage(), true, dataHandler.getPaidForceApkLink());
            } else if (dataHandler.isNormalPaidUpdateEnabled()
                    && currentAppVersion < dataHandler.getNormalPaidUpdateVersion()) {
                showNormalUpdateDialog(dataHandler.getNormalPaidUpdateMessage(), true, dataHandler.getPaidNormalApkLink());
            }

        } else {

            int currentAppVersion = Nostragamus.getInstance().getAppVersionCode();
            if (currentAppVersion < dataHandler.getForceUpdateVersion()) {
                showForceUpdateDialog(dataHandler.getForceUpdateMessage(), false, "");
            } else if (dataHandler.isNormalUpdateEnabled()
                    && currentAppVersion < dataHandler.getNormalUpdateVersion()) {
                showNormalUpdateDialog(dataHandler.getNormalUpdateMessage(), false, "");
            }
        }
    }

    private AlertDialog mUpdateDialog;

    private void showNormalUpdateDialog(String message, final boolean isPaidVersion, final String paidApkLink) {
        mUpdateDialog = AppSnippet.getAlertDialog(this, getString(R.string.new_version_title),
                message,
                getString(R.string.update), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isPaidVersion) {
                            NostragamusAnalytics.getInstance().trackUpdate(Constants.AppUpdateTypes.NORMAL_PAID_UPDATE, 1);
                            navigateAppHostedUrl(paidApkLink);
                        } else {
                            NostragamusAnalytics.getInstance().trackUpdate(Constants.AppUpdateTypes.NORMAL_UPDATE, 1);
                            navigateToPlayStore();
                        }

                        mUpdateDialog.dismiss();
                        mUpdateDialog = null;
                    }
                },
                getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isPaidVersion) {
                            NostragamusAnalytics.getInstance().trackUpdate(Constants.AppUpdateTypes.NORMAL_PAID_UPDATE, 0);
                        } else {
                            NostragamusAnalytics.getInstance().trackUpdate(Constants.AppUpdateTypes.NORMAL_UPDATE, 0);
                        }

                        mUpdateDialog.dismiss();
                        mUpdateDialog = null;
                    }
                }, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isPaidVersion) {
                            NostragamusDataHandler.getInstance().setNormalPaidUpdateEnabled(!isChecked);
                        } else {
                            NostragamusDataHandler.getInstance().setNormalUpdateEnabled(!isChecked);
                        }
                    }
                });

        mUpdateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(null != mUpdateDialog) {
                    mUpdateDialog = null;

                    if (isPaidVersion) {
                        NostragamusAnalytics.getInstance().trackUpdate(Constants.AppUpdateTypes.NORMAL_PAID_UPDATE, 0);
                    } else {
                        NostragamusAnalytics.getInstance().trackUpdate(Constants.AppUpdateTypes.NORMAL_UPDATE, 0);
                    }
                }
            }
        });

        mUpdateDialog.show();
    }

    private void showForceUpdateDialog(String message, final boolean isPaidVersion, final String paidApkLink) {
        AlertDialog alertDialog = AppSnippet.getAlertDialog(this, getString(R.string.new_version_title),
                message,
                getString(R.string.update), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPaidVersion) {
                            navigateAppHostedUrl(paidApkLink);
                            NostragamusAnalytics.getInstance().trackUpdate(Constants.AppUpdateTypes.FORCE_PAID_UPDATE, 1);

                        } else {
                            navigateToPlayStore();
                            NostragamusAnalytics.getInstance().trackUpdate(Constants.AppUpdateTypes.FORCE_UPDATE, 1);
                        }
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isPaidVersion) {
                    NostragamusAnalytics.getInstance().trackUpdate(Constants.AppUpdateTypes.FORCE_PAID_UPDATE, 0);
                }  else {
                    NostragamusAnalytics.getInstance().trackUpdate(Constants.AppUpdateTypes.FORCE_UPDATE, 0);
                }

                AppSnippet.closeApp();
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

    private void navigateAppHostedUrl(String paidApkLink) {
        String apkLink = "http://sportscafe.in/app";

        if (!TextUtils.isEmpty(paidApkLink)) {
            apkLink = paidApkLink;
        }

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(apkLink)));
        } catch (ActivityNotFoundException e) {
            ExceptionTracker.track(e);
        }
    }

    private void openPopup(List<PopUp> popUps) {
        Intent intent = new Intent(this, PopUpActivity.class);
        intent.putExtra(BundleKeys.POPUP_DATA, Parcels.wrap(popUps));
        startActivity(intent);
    }

    @Override
    public void onSuccessGetUpdatedPopUps(List<PopUp> popUps) {
        openPopup(popUps);
    }

    @Override
    public void onFailedGetUpdatePopUps(String message) {

    }

    /**
     * Sets Immersive mode
     * Status bar and navigation bar gets hidden
     */
    protected void setImmersiveFullScreenMode() {
        int visibility =   View.SYSTEM_UI_FLAG_IMMERSIVE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(visibility);

        /* set transparent system bars */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setSystemUiChangeListenerForImmersiveMode(visibility);
    }

    private void setSystemUiChangeListenerForImmersiveMode(final int visibilityApplied) {
        final Runnable hideSystemUiCallback = new Runnable() {
            @Override
            public void run() {
                setImmersiveFullScreenMode();
            }
        };

        final Handler mSystemUiHandler = new Handler();

        getWindow().getDecorView().
                setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & visibilityApplied) == 0) {
                            mSystemUiHandler.removeCallbacks(hideSystemUiCallback);
                            mSystemUiHandler.postDelayed(hideSystemUiCallback, 1000);
                        }
                    }
                });
    }

    /**
     * Launching Webview screen
     * @param url
     * @param heading
     */
    protected void navigateToWebView(String url, String heading) {
        Intent intent = new Intent(NostragamusActivity.this, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("heading", heading);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}