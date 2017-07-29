package in.sportscafe.nostragamus.module.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.BundleKeys;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.allchallenges.AllChallengesFragment;
import in.sportscafe.nostragamus.module.allchallenges.info.ChallengeDownloadAppFragment;
import in.sportscafe.nostragamus.module.allchallenges.info.ChallengeJoinDialogFragment;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.common.NostragamusActivity;
import in.sportscafe.nostragamus.module.common.OnDismissListener;
import in.sportscafe.nostragamus.module.navigation.NavigationFragment;
import in.sportscafe.nostragamus.module.navigation.appupdate.AppUpdateActivity;
import in.sportscafe.nostragamus.module.navigation.wallet.paytmAndBank.WalletOrBankConnectActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsActivity;
import in.sportscafe.nostragamus.module.permission.PermissionsChecker;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsFragment;
import in.sportscafe.nostragamus.module.user.lblanding.LBLandingFragment;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.ProfileFragment;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.nostragamus.module.user.myprofile.verify.VerifyProfileActivity;

/**
 * Created by Jeeva on 16/6/16.
 */
public class HomeActivity extends NostragamusActivity implements OnHomeActionListener,
        OnDismissListener, View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private static final int CODE_PROFILE_ACTIVITY = 1;
    private static final int REFRESH_CHALLENGES_CODE = 42;
    private static final int OPEN_JOINED_CHALLENGE_DIALOG_CODE = 53;
    private static final int OPEN_DOWNLOAD_APP_DIALOG = 54;
    private static final int CHALLENGE_JOINED = 55;

    public static final int DOUBLE_BACK_PRESSED_DELAY_ALLOWED = 3000;

    private int mProfileTabPosition = 0;
    private View mSelectedImage;
    private View mSelectedText;
    private boolean mIsFirstBackPressed = false;
    private int mScrollToChallengeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (null == NostragamusDataHandler.getInstance().getUserId()) {
            navigateToLogIn();
            return;
        }

        initViews();
        UserInfoModelImpl.newInstance(getUserInfoCallBackListener()).getUserInfo();
        showScreenAsRequired();

    }

    private void showScreenAsRequired() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {

            if (bundle.containsKey(BundleKeys.OPEN_PROFILE)) {
                mProfileTabPosition = Integer.parseInt(bundle.getString(BundleKeys.OPEN_PROFILE));
                showProfileScreen();

            } else if (bundle.containsKey(BundleKeys.GROUP)) {
                showGroupsScreen();

            } else if (bundle.containsKey(BundleKeys.OPEN_LEADERBOARD)) {
                showLeaderBoardScreen();

            } else {
                showChallengesScreen();
            }
        } else {
            showChallengesScreen();
        }
    }

    private void initViews() {
        findViewById(R.id.home_rl_challenges).setOnClickListener(this);
        findViewById(R.id.home_rl_group).setOnClickListener(this);
        findViewById(R.id.home_rl_leaderboard).setOnClickListener(this);
        findViewById(R.id.home_rl_profile).setOnClickListener(this);
    }

    /**
     * If user has not yet provided payment info (either paytm or bank) then he'll be asked to provide
     * <p>
     * But only once
     *
     * @param userInfo
     */
    private void checkPaymentInfoProvidedOrRequired(UserInfo userInfo) {
        // NOTE : On-boarding for collecting payout details NOT requires as wallet has been introduced.

        /*if (userInfo != null) {

            *//* If user info is null AND paymentDetails never shown to user on HomeScreen, then only (once only) *//*
            if (userInfo.getUserPaymentInfo() == null &&
                    !NostragamusDataHandler.getInstance().isPaymentDetailsShownAtHome() && BuildConfig.IS_PAID_VERSION) {

                Log.d(TAG, "[onBoard] User Payment details screen shown at home");
                launchPaymentDetailsActivity();

                // Save that payment details are shown at home
                NostragamusDataHandler.getInstance().setIsPaymentDetailsShownAtHome(true);
            } else {
                Log.d(TAG, "[onBoard] User payment details screen not required to show at home");
            }
        }*/
    }

    /**
     * Also gets invoked by wallet Transaction - updatePaymentDetails
     */
    public void launchPaymentDetailsActivity() {
        Intent intent = new Intent(this, WalletOrBankConnectActivity.class);
        startActivity(intent);
    }

    private void showProfileScreen() {
        onClickTab(Constants.Screens.PROFILE);
    }

    private void showLeaderBoardScreen() {
        onClickTab(Constants.Screens.LEADER_BOARD);
    }

    private void showGroupsScreen() {
        onClickTab(Constants.Screens.GROUP);
    }

//    private void getunReadNotificationCount() {
//
//        Cursor cur = getContext().getContentResolver().query(
//                MoEDataContract.MessageEntity.getContentUri(getApplicationContext()),
//                MoEDataContract.MessageEntity.PROJECTION, MoEDataContract.MessageEntity.MSG_CLICKED + " = ?",
//                new String[]{"0"}, MoEDataContract.MessageEntity.DEFAULT_SORT_ORDER);
//
//        int unReadCount = 0;
//        if (null != cur) {
//            unReadCount = cur.getCount();
//            cur.close();
//        }
//
//        CustomButton notificationCount = (CustomButton) findViewById(R.id.home_ibtn_notification_count);
//
//        if (unReadCount == 0) {
//            notificationCount.setVisibility(View.GONE);
//        } else {
//            notificationCount.setVisibility(View.VISIBLE);
//            notificationCount.setText(String.valueOf(unReadCount));
//        }
//    }

    private void onClickTab(int screen) {
        switch (screen) {

            case Constants.Screens.CHALLENGES:
                setSelected(findViewById(R.id.home_ibtn_challenge), findViewById(R.id.home_tv_challenge));
                loadFragment(AllChallengesFragment.newInstance(getIntent().getExtras()));
                break;

            case Constants.Screens.GROUP:
                setSelected(findViewById(R.id.home_ibtn_group), findViewById(R.id.home_tv_group));
                loadFragment(AllGroupsFragment.newInstance());
                break;

            case Constants.Screens.LEADER_BOARD:
                setSelected(findViewById(R.id.home_ibtn_leaderboard), findViewById(R.id.home_tv_leaderboard));
                loadFragment(new LBLandingFragment());
                break;

            case Constants.Screens.PROFILE:
                setSelected(findViewById(R.id.home_ibtn_profile), findViewById(R.id.home_tv_profile));

                if (null == NostragamusDataHandler.getInstance().getUserId()) {
                    navigateToLogIn();
                    return;
                }

//                loadFragment(ProfileFragment.newInstance(mProfileTabPosition, getIntent().getExtras()));
                NavigationFragment navigationFragment = new NavigationFragment();
                if (getIntent() != null) {
                    navigationFragment.setArguments(getIntent().getExtras());
                }
                loadFragment(navigationFragment);

                NostragamusAnalytics.getInstance().trackClickEvent(Constants.AnalyticsCategory.PROFILE, Constants.AnalyticsActions.OPENED);
                break;
        }
    }

    private void setSelected(View selImg, View selText) {
        if (null != mSelectedImage) {
            mSelectedImage.setSelected(false);
        }

        if (null != mSelectedText) {
            mSelectedText.setSelected(false);
        }

        mSelectedImage = selImg;
        mSelectedImage.setSelected(true);

        mSelectedText = selText;
        mSelectedText.setSelected(true);
    }

    /*private void showJoinGroupAlert(final String groupCode, String groupName) {
        new AlertDialog.Builder(this)
                .setTitle("Group Invitation")
                .setMessage("You clicked on a group invitation link from \"" + groupName + "\" group, Would you like to proceed with joining this group?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navigateToJoinGroup(groupCode);
                    }
                })
                .setNegativeButton("No, thanks", null)
                .setCancelable(false)
                .create().show();
    }

    private void navigateToJoinGroup(String groupCode) {
        Intent intent = new Intent(this, JoinGroupActivity.class);
        intent.putExtra(BundleKeys.GROUP_CODE, groupCode);
        startActivity(intent);
    }*/

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.home_fl_holder, fragment).commit();
    }

    private void showChallengesScreen() {
        onClickTab(Constants.Screens.CHALLENGES);
    }

    private void navigateToLogIn() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (CODE_PROFILE_ACTIVITY == requestCode) {
                loadFragment(new ProfileFragment());
            }
        }

        if (Constants.RequestCodes.STORAGE_PERMISSION == requestCode && PermissionsActivity.PERMISSIONS_GRANTED == resultCode) {
            com.jeeva.android.Log.d(TAG, "Storage permission granted");
        } else {
            com.jeeva.android.Log.d(TAG, "Storage permission NOT granted");
        }
    }

    /**
     * Used to get User Info from server, which will provide payment bank / paytm details
     * so that it can be identified that to show Connect to Paytm/Bank screen to capture such data
     *
     * @return
     */
    private UserInfoModelImpl.OnGetUserInfoModelListener getUserInfoCallBackListener() {
        return new UserInfoModelImpl.OnGetUserInfoModelListener() {
            @Override
            public void onSuccessGetUpdatedUserInfo(UserInfo userInfo) {
                if (userInfo != null) {

                    if (BuildConfig.IS_PAID_VERSION) {
                        com.jeeva.android.Log.d(TAG, "[onBoard] Paid app..");
                        performOnBoardFlow(userInfo);

                    } else {
                        /**
                         * For Free version, if user is NOT new (existing) then don't show UpdateProfile
                         * else show
                         */
                        if (!NostragamusDataHandler.getInstance().isFirstTimeUser()) {
                            checkPaymentInfoProvidedOrRequired(userInfo);
                            com.jeeva.android.Log.d(TAG, "[onBoard] Free App & Not a first time user..");
                        } else {
                            performOnBoardFlow(userInfo);
                            com.jeeva.android.Log.d(TAG, "[onBoard] Free App & first time user..");
                        }
                    }
                } else {
                    Log.d(TAG, "[onBoard] User Payment info null");
                    // Do not perform any action
                }
            }

            @Override
            public void onFailedGetUpdateUserInfo(String message) {
                // Initial call, Api fails but can not be taken action
                Log.d(TAG, "Get UserInfo API failed.");
            }

            @Override
            public void onNoInternet() {
                // Initial call, Not required to show any msg
            }
        };
    }

    private void performOnBoardFlow(UserInfo userInfo) {

        if (userInfo != null) {

            if (userInfo.getInfoDetails() != null) {

                 /* check for EDIT PROFILE screen */
                Boolean disclaimerAccepted = userInfo.getInfoDetails().getDisclaimerAccepted();
                if (disclaimerAccepted == null || !disclaimerAccepted) {
                    launchEditProfile();
                } else {
//                    /* check for OTP screen */
//                    Boolean otpVerified = userInfo.getInfoDetails().getOtpVerified();
//                    if (otpVerified == null || !otpVerified) {
//                        launchVerifyOTP();
//                    }
                }
            }
        }

    }

    private void launchVerifyOTP() {
        if (getActivity() != null) {
            Intent intent = new Intent(this, VerifyProfileActivity.class);
            intent.putExtra(Constants.BundleKeys.SUCCESSFUL_REFERRAL, false);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void launchWhatsNew() {

        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), AppUpdateActivity.class);
            intent.putExtra(Constants.BundleKeys.SCREEN, Constants.ScreenNames.WHATS_NEW);
            startActivity(intent);
        }
    }

    /**
     * If disclaimer not accepted, then only show editProfile
     */
    private void launchEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("screen", BundleKeys.HOME_SCREEN);
        intent.putExtra(BundleKeys.EDIT_PROFILE_LAUNCHED_FROM, EditProfileActivity.ILaunchedFrom.HOME_ACTIVITY);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * super.onBackClicked() is called in side handleDoubleBackPressLogicToExit().
     * Here no where required to call the same method.
     */
    @Override
    public void onBackPressed() {
        Fragment visibleCurrentFragment = getSupportFragmentManager().findFragmentById(R.id.home_fl_holder);

        if (null != visibleCurrentFragment &&
                visibleCurrentFragment instanceof LBLandingFragment &&
                ((LBLandingFragment) visibleCurrentFragment).onBack()) {
            // No action here

        } else if (visibleCurrentFragment instanceof AllChallengesFragment) {
            handleDoubleBackPressLogicToExit();
        } else {
            showChallengesScreen();
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public String getScreenName() {
        return Constants.ScreenNames.HOME;
    }

    @Override
    public void onClickChallenges() {
        showChallengesScreen();
    }

    @Override
    public void onDismiss(int requestCode, Bundle bundle) {

        switch (requestCode) {
            case OPEN_JOINED_CHALLENGE_DIALOG_CODE:
                showJoinedChallenge(getContext(), bundle);
                break;

            case REFRESH_CHALLENGES_CODE:
                showChallengesScreen(); // This will refresh the screen
                break;

            case OPEN_DOWNLOAD_APP_DIALOG:
                showDownloadPaidApk(getContext());
                break;

            case CHALLENGE_JOINED:
                if (bundle != null) {
                    int challengeId = bundle.getInt(BundleKeys.CHALLENGE_ID, -1);
                    if (challengeId >= 0) {
                        mScrollToChallengeId = challengeId;
                        com.jeeva.android.Log.d(TAG, "Scroll item - challenge id : " + mScrollToChallengeId);
                    }
                }
                showChallengesScreen();
                break;
        }
    }

    private void showDownloadPaidApk(Context context) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        ChallengeDownloadAppFragment.newInstance(REFRESH_CHALLENGES_CODE)
                .show(fragmentManager, "paid_app_download");
    }

    private void showJoinedChallenge(Context context, Bundle bundle) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        ChallengeJoinDialogFragment.newInstance(CHALLENGE_JOINED, "JOINED CHALLENGE!", bundle)
                .show(fragmentManager, "challenge_info");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_rl_challenges:
                showChallengesScreen();
                break;

            case R.id.home_rl_group:
                showGroupsScreen();
                break;

            case R.id.home_rl_leaderboard:
                showLeaderBoardScreen();
                break;

            case R.id.home_rl_profile:
                showProfileScreen();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mAllChallengeDataLoaded,
                new IntentFilter(Constants.IntentActions.ACTION_ALL_CHALLENGE_DATA_LOADED));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReloadChallenges,
                new IntentFilter(Constants.IntentActions.ACTION_RELOAD_CHALLENGES));
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mAllChallengeDataLoaded);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReloadChallenges);
        super.onStop();
    }

    BroadcastReceiver mReloadChallenges = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showChallengesScreen();
        }
    };

    BroadcastReceiver mAllChallengeDataLoaded = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            /*Intent homeActivityIntent = getIntent();
            if (homeActivityIntent != null && homeActivityIntent.getExtras() != null) {
                Bundle bundle = homeActivityIntent.getExtras();
                boolean isFromNotification = bundle.getBoolean(Constants.NotificationKeys.FROM_NOTIFICATION, false);
//                bundle.putBoolean(Constants.NotificationKeys.FROM_NOTIFICATION, false);

                if (isFromNotification) {
                    String challengeIdStr = bundle.getString(Constants.NotificationKeys.NEW_CHALLENGE_ID, "");

                    if (!TextUtils.isEmpty(challengeIdStr)) {
                        final int challengeId = Integer.parseInt(challengeIdStr);
                        if (challengeId > 0) {
                            com.jeeva.android.Log.d("Temp", "New challenge Id " + challengeId);

                            Intent newBroadcastForNotification = new Intent(Constants.IntentActions.ACTION_NEW_CHALLENGE_ID);
                            newBroadcastForNotification.putExtra(BundleKeys.CHALLENGE_ID, challengeId);
                            newBroadcastForNotification.putExtra(Constants.NotificationKeys.FROM_NOTIFICATION, true);
                            LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(newBroadcastForNotification);

                            return;
                        }
                    }
                }
            }*/

            /* Once challenge has been joined, let it to be scrolled to visible position into InPlay tab  */
            com.jeeva.android.Log.d(TAG, "scroll - last joined : " + mScrollToChallengeId);
            if (mScrollToChallengeId >= 0) {
                Intent newBroadcastForNotification = new Intent(Constants.IntentActions.ACTION_SCROLL_CHALLENGE);
                newBroadcastForNotification.putExtra(BundleKeys.CHALLENGE_ID, mScrollToChallengeId);
                LocalBroadcastManager.getInstance(getContext().getApplicationContext()).sendBroadcast(newBroadcastForNotification);

                /* Reset as once it's scrolled */
                mScrollToChallengeId = -1;

                return;
            }


        }
    };

}