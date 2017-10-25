package in.sportscafe.nostragamus.module.getstart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.module.navigation.appupdate.AppUpdateActivity;
import in.sportscafe.nostragamus.module.nostraHome.ui.NostraHomeActivity;
import in.sportscafe.nostragamus.module.settings.app.dto.AppSettingsResponse;
import in.sportscafe.nostragamus.module.settings.app.dto.AppUpdateInfo;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;

/**
 * Created by Jeeva on 27/5/16.
 */
public class GetStartActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAnyUpdate();
    }

    private void handleGetStart() {
        if (NostragamusDataHandler.getInstance().isLoggedInUser()) {
            if (NostragamusDataHandler.getInstance().isFirstTimeUser()) {
                navigateToEditProfile();
            } else {
                navigateToHome();
            }
        } else {
            navigateToLogin();
        }
    }

    private void navigateToHome() {
        startActivity(new Intent(this, NostraHomeActivity.class));
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

        AppSettingsResponse appSettingsResponse = Nostragamus.getInstance().getServerDataManager().getAppSettingsResponse();
        if (appSettingsResponse != null && appSettingsResponse.getAppUpdateInfo() != null) {
            AppUpdateInfo appUpdateInfo = appSettingsResponse.getAppUpdateInfo();

            String updateType = appUpdateInfo.getUpdateType();
            if (!TextUtils.isEmpty(updateType)) {
                if (updateType.equalsIgnoreCase(Constants.AppUpdateTypes.FORCE_UPDATE)) {

                    if (appUpdateInfo.getUpdateRequestVersion() > Nostragamus.getInstance().getAppVersionCode()) {
                        showForceUpdateScreen();
                    } else {
                        handleGetStart();
                    }

                } else if (updateType.equalsIgnoreCase(Constants.AppUpdateTypes.NORMAL_UPDATE)) {

                    if (appUpdateInfo.getUpdateRequestVersion() > Nostragamus.getInstance().getAppVersionCode()) {
                        showNormalUpdateScreen();
                    } else {
                        handleGetStart();
                    }

                } else {
                    handleGetStart();
                }
            } else {
                handleGetStart();
            }
        } else {
            handleGetStart();
        }

        /*NostragamusDataHandler dataHandler = NostragamusDataHandler.getInstance();
        int lastShownAppVersion = NostragamusDataHandler.getInstance().getLastShownAppVersionCode();

        *//*check App update Type *//*
        if (dataHandler.getAppUpdateType() != null) {

            *//* check if it is a force update or a normal update &
              Check if New Update App version is greater than the last Shown App Update Version *//*

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
        }*/

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