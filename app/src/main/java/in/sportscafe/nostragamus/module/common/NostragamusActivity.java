package in.sportscafe.nostragamus.module.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jeeva.android.InAppActivity;

import org.parceler.Parcels;

import java.util.List;

import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.popups.PopUp;
import in.sportscafe.nostragamus.module.popups.PopUpActivity;
import in.sportscafe.nostragamus.module.popups.PopUpModelImpl;

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
     *
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
        int visibility = View.SYSTEM_UI_FLAG_IMMERSIVE |
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
     *
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