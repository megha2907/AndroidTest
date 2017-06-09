package in.sportscafe.nostragamus.module.appupdate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.navigation.help.HelpFragmentListener;
import in.sportscafe.nostragamus.module.onboard.OnBoardingFragment;

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
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.update_app_fl_holder, AppUpdateFragment.newInstance(Constants.ScreenNames.WHATS_NEW)).commit();
                } else if (extras.getString(Constants.BundleKeys.SCREEN).equals(Constants.ScreenNames.APP_FORCE_UPDATE)) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.update_app_fl_holder, AppUpdateFragment.newInstance(Constants.ScreenNames.APP_FORCE_UPDATE)).commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.update_app_fl_holder, AppUpdateFragment.newInstance(Constants.ScreenNames.APP_UPDATE)).commit();
                }
            }else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.update_app_fl_holder, AppUpdateFragment.newInstance(Constants.ScreenNames.APP_UPDATE)).commit();
            }
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.update_app_fl_holder, AppUpdateFragment.newInstance(null)).commit();
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

    private void navigateToHome() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.putExtra(Constants.BundleKeys.SCREEN, Constants.BundleKeys.LOGIN_SCREEN);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString(Constants.BundleKeys.SCREEN).equals(Constants.ScreenNames.APP_FORCE_UPDATE)) {
                handleDoubleBackPressLogicToExit();
            } else if (getIntent().getExtras().getString(Constants.BundleKeys.SCREEN).equals(Constants.ScreenNames.APP_UPDATE)) {
                navigateToHome();
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


}
