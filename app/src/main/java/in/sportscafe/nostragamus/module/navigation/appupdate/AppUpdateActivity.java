package in.sportscafe.nostragamus.module.navigation.appupdate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.getstart.GetStartActivity;
import in.sportscafe.nostragamus.module.nostraHome.NostraHomeActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

/**
 * Created by deepanshi on 6/2/17.
 */

public class AppUpdateActivity extends NostragamusActivity implements OnDismissListener {

    private static final int DISMISS_SCREEN = 58;
    private boolean mIsFirstBackPressed = false;
    public static final int DOUBLE_BACK_PRESSED_DELAY_ALLOWED = 3000;

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_app);

        initView(getIntent().getExtras());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void initView(Bundle extras) {

          /* check if it's a What's NEW Screen or a Force Update Screen or a Normal Update Screen */

        if (extras != null) {
            if (extras.containsKey(Constants.BundleKeys.SCREEN)) {
                if (extras.get(Constants.BundleKeys.SCREEN).equals(Constants.ScreenNames.WHATS_NEW)) {
                    FragmentHelper.replaceFragment(this, R.id.update_app_fl_holder, AppUpdateFragment.newInstance(Constants.ScreenNames.WHATS_NEW, getAppUpdateActionListener()));
                } else if (extras.getString(Constants.BundleKeys.SCREEN).equals(Constants.ScreenNames.APP_FORCE_UPDATE)) {
                    FragmentHelper.replaceFragment(this, R.id.update_app_fl_holder, AppUpdateFragment.newInstance(Constants.ScreenNames.APP_FORCE_UPDATE, getAppUpdateActionListener()));
                } else {
                    FragmentHelper.replaceFragment(this, R.id.update_app_fl_holder, AppUpdateFragment.newInstance(Constants.ScreenNames.APP_UPDATE, getAppUpdateActionListener()));
                }
            } else {
                FragmentHelper.replaceFragment(this, R.id.update_app_fl_holder, AppUpdateFragment.newInstance(Constants.ScreenNames.APP_UPDATE, getAppUpdateActionListener()));
            }
        } else {
            FragmentHelper.replaceFragment(this, R.id.update_app_fl_holder, AppUpdateFragment.newInstance(null, getAppUpdateActionListener()));
        }
    }

    @Override
    public void onDismiss(int requestCode, Bundle bundle) {
        switch (requestCode) {
            case DISMISS_SCREEN:
                onBackPressed();
                break;
        }
    }

    private AppUpdateFragment.AppUpdateActionListener getAppUpdateActionListener() {

        return new AppUpdateFragment.AppUpdateActionListener() {

            @Override
            public void onAppDownload(String screenType) {
                openDownloadAppScreen(screenType);
            }
        };
    }

    private void openDownloadAppScreen(String screenType) {

        DownloadingAppFragment downloadingApp = new DownloadingAppFragment();
        FragmentHelper.replaceFragment(this, R.id.update_app_fl_holder, DownloadingAppFragment.newInstance(screenType));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }


    private void navigateToHome() {
        Intent intent = new Intent(getContext(), NostraHomeActivity.class);
        intent.putExtra(Constants.BundleKeys.SCREEN, Constants.BundleKeys.LOGIN_SCREEN);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        if (getIntent().getExtras() != null) {

            if (getIntent().getExtras().getString(Constants.BundleKeys.SCREEN).equals(Constants.ScreenNames.APP_FORCE_UPDATE)) {
                handleDoubleBackPressLogicToExit();
            } else if (getIntent().getExtras().getString(Constants.BundleKeys.SCREEN).equals(Constants.ScreenNames.APP_UPDATE)) {
                if (NostragamusDataHandler.getInstance().isLoggedInUser()) {
                    navigateToHome();
                } else {
                    navigateToGetStarted();
                }
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    /**
     * Application exit happens only when user clicks back button twice within specified time interval
     */
    private void handleDoubleBackPressLogicToExit() {
        if (mIsFirstBackPressed) {
            super.onBackPressed();
        } else {

            showMessage(getString(R.string.double_back_press_msg));
            mIsFirstBackPressed = true;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIsFirstBackPressed = false;
                }
            }, DOUBLE_BACK_PRESSED_DELAY_ALLOWED);
        }
    }


    private void navigateToGetStarted() {
        startActivity(new Intent(this, GetStartActivity.class));
        finish();
    }


}
