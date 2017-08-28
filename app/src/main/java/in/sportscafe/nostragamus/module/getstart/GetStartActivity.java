package in.sportscafe.nostragamus.module.getstart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.allchallenges.AllChallengesApiModelImpl;
import in.sportscafe.nostragamus.module.challenges.ChallengesActivity;
import in.sportscafe.nostragamus.module.navigation.appupdate.AppUpdateActivity;
import in.sportscafe.nostragamus.module.feedback.GoogleFormActivity;
import in.sportscafe.nostragamus.module.home.HomeActivity;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;

/**
 * Created by Jeeva on 27/5/16.
 */
public class GetStartActivity extends Activity {

    private static final int GOOGLE_FORM_CODE = 90;

    private static final String GOOGLE_FORM_LINK = "https://docs.google.com/a/sportscafe.in/forms/d/e/1FAIpQLScFyPxEFIeCe1Yg3xiV_BhxKTDKDCm0PuzltgLPXz7iwWexLg/viewform?c=0&w=1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (NostragamusDataHandler.newInstance().isInitialFeedbackFormShown()) {*/
        checkAnyUpdate();
        /*} else {
            navigateToForm();
        }*/
    }

    private void handleGetStart() {
        if (NostragamusDataHandler.getInstance().isLoggedInUser()) {
            if (NostragamusDataHandler.getInstance().isFirstTimeUser()) {
                navigateToEditProfile();
            } else {
                if (Nostragamus.getInstance().hasNetworkConnection()) {
                    AllChallengesApiModelImpl.newInstance().getAllCompletedChallenge();
                }
                navigateToHome();
            }
        } else {
            navigateToLogin();
        }
    }

    private void navigateToForm() {
        Intent intent = new Intent(this, GoogleFormActivity.class);
        intent.putExtra(BundleKeys.FEEDBACK_FORM_URL, GOOGLE_FORM_LINK);
        startActivityForResult(intent, GOOGLE_FORM_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (GOOGLE_FORM_CODE == requestCode) {
            handleGetStart();
        }
    }

    private void navigateToHome() {
        startActivity(new Intent(this, ChallengesActivity.class));
        finish();
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("screen", BundleKeys.LOGIN_SCREEN);
        intent.putExtra(BundleKeys.EDIT_PROFILE_LAUNCHED_FROM, EditProfileActivity.ILaunchedFrom.GET_STARTED_ACTIVITY);
        startActivity(intent);
    }

    private void navigateToLogin() {
        startActivity(new Intent(this, LogInActivity.class));
        finish();
    }

    private void checkAnyUpdate() {

        NostragamusDataHandler dataHandler = NostragamusDataHandler.getInstance();
        int lastShownAppVersion = NostragamusDataHandler.getInstance().getLastShownAppVersionCode();

        /*check App update Type */
        if (dataHandler.getAppUpdateType() != null) {

            /* check if it is a force update or a normal update &
              Check if New Update App version is greater than the last Shown App Update Version */

            if (dataHandler.getAppUpdateType().equalsIgnoreCase(Constants.AppUpdateTypes.FORCE_UPDATE)
                    && lastShownAppVersion < dataHandler.getReqUpdateVersion()) {
                showForceUpdateScreen();
                Log.d("inside", "forceUpdate");
            } else if (dataHandler.getAppUpdateType().equalsIgnoreCase(Constants.AppUpdateTypes.NORMAL_UPDATE)
                    && lastShownAppVersion < dataHandler.getReqUpdateVersion()) {
                showNormalUpdateScreen();
                NostragamusDataHandler.getInstance().setLastShownAppVersionCode(dataHandler.getReqUpdateVersion());
                Log.d("inside", "NormalUpdate");
            } else {
                Log.d("inside", "getstarted");
                handleGetStart();
            }
        } else {
            Log.d("inside", "getstarted");
            handleGetStart();
        }
    }

    private void showNormalUpdateScreen() {
        Intent intent = new Intent(this, AppUpdateActivity.class);
        intent.putExtra(Constants.BundleKeys.SCREEN, Constants.ScreenNames.APP_UPDATE);
        startActivity(intent);
        finish();
    }

    private void showForceUpdateScreen() {
        Intent intent = new Intent(this, AppUpdateActivity.class);
        intent.putExtra(Constants.BundleKeys.SCREEN, Constants.ScreenNames.APP_FORCE_UPDATE);
        startActivity(intent);
        finish();
    }
}