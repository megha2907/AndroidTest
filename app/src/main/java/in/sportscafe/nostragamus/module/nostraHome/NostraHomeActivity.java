package in.sportscafe.nostragamus.module.nostraHome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.challengeCompleted.ui.CompletedChallengeHistoryFragment;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;
import in.sportscafe.nostragamus.module.inPlay.ui.InPlayFragment;
import in.sportscafe.nostragamus.module.navigation.NavigationFragment;
import in.sportscafe.nostragamus.module.navigation.appupdate.AppUpdateActivity;
import in.sportscafe.nostragamus.module.newChallenges.ui.NewChallengesFragment;
import in.sportscafe.nostragamus.module.nostraHome.dataProviders.NostraHomeApiImpl;
import in.sportscafe.nostragamus.module.notifications.NostraNotification;
import in.sportscafe.nostragamus.module.notifications.NotificationHelper;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsFragment;
import in.sportscafe.nostragamus.module.user.login.LogInActivity;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.nostragamus.module.user.myprofile.verify.VerifyProfileActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class NostraHomeActivity extends NostraBaseActivity implements View.OnClickListener {

    private static final String TAG = NostraHomeActivity.class.getSimpleName();
    public static final int DOUBLE_BACK_PRESSED_DELAY_ALLOWED = 3000;

    public interface LaunchedFrom {
        int SHOW_NEW_CHALLENGES = -111;
        int SHOW_IN_PLAY = -112;
        int SHOW_COMPLETED = -113;
        int SHOW_GROUPS = -114;
        int SHOW_NAVIGATION = -115;
    }

    private LinearLayout mNewChallengesBottomButton;
    private LinearLayout mInPlayBottomButton;
    private LinearLayout mCompletedChallengeBottomButton;
    private LinearLayout mGroupBottomButton;
    private LinearLayout mProfileBottomButton;

    private boolean mIsFirstBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nostra_home);

        initMembers();
        initViews();
        getServerTime();
        onNewChallengesClicked(getIntent().getExtras());
        handleNotifications();
    }

    private void getServerTime() {
        new NostraHomeApiImpl().fetchServerTimeFromServer(new NostraHomeApiImpl.NostraHomeApiListener() {
            @Override
            public void noInternet() {
                Log.d(TAG, "No internet for fetching ServerTime");
            }

            @Override
            public void onTimerFailure() {
                Log.d(TAG, "ServerTimer response failed");
            }

            @Override
            public void onTimerSuccess() {
                Log.d(TAG, "ServerTimer Response success");
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null) {
            /* Either a notification flow can be handled or launching any specific screen
             * If SCREEN_LAUNCH_REQUEST is found in args then launch those requested screens
             * Else handle notification scenario */

            int launchFrom = intent.getIntExtra(Constants.BundleKeys.SCREEN_LAUNCH_REQUEST, -1);
            if (launchFrom == -1) {
                handleNotifications();

            } else {
                switch (launchFrom) {
                    case LaunchedFrom.SHOW_NEW_CHALLENGES:
                        onNewChallengesClicked(intent.getExtras());
                        break;

                    case LaunchedFrom.SHOW_IN_PLAY:
                        onInPlayClicked(intent.getExtras());
                        break;

                    case LaunchedFrom.SHOW_COMPLETED:
                        onHistoryClicked(intent.getExtras());
                        break;

                    case LaunchedFrom.SHOW_GROUPS:
                        onGroupClicked(intent.getExtras());
                        break;

                    case LaunchedFrom.SHOW_NAVIGATION:
                        onNavigationClicked(intent.getExtras());
                        break;
                }
            }
        }
    }

    private void handleNotifications() {
        NotificationHelper notificationHelper = new NotificationHelper();
        NostraNotification notification = notificationHelper.getNotificationIfToBeSent(getIntent());

        if (notification != null && !TextUtils.isEmpty(notification.getScreenName())) {
            if (NostragamusDataHandler.getInstance().isLoggedInUser()) {
                String screenName = notification.getScreenName();
                Log.d("Notification", "ScreenName : " + screenName);

                if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_NEW_CHALLENGE)) {
                    onNewChallengesClicked(notificationHelper.getBundleAddedNotificationDetailsIntoArgs(getIntent(), notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_NEW_CHALLENGE_SPORT)) {
                    onNewChallengesClicked(notificationHelper.getBundleAddedNotificationDetailsIntoArgs(getIntent(), notification));

                }  else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_NEW_CHALLENGE_MATCHES)) {
                    startActivity(notificationHelper.getNewChallengeMatchesScreenIntent(this, notification));

                }  else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_IN_PLAY_MATCHES)) {
                    onInPlayClicked(notificationHelper.getBundleAddedNotificationDetailsIntoArgs(getIntent(), notification));

                }  else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_RESULTS)) {
                    startActivity(notificationHelper.getResultsScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_CHALLENGE_HISTORY)) {
                    onHistoryClicked(notificationHelper.getBundleAddedNotificationDetailsIntoArgs(getIntent(), notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_REFER_FRIEND)) {
                    startActivity(notificationHelper.getReferFriendScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_REFERRAL_CREDITS)) {
                    startActivity(notificationHelper.getReferralCreditsScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_STORE)) {
                    startActivity(notificationHelper.getStoreScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_APP_UPDATE)) {
                    startActivity(notificationHelper.getAppUpdateScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_WALLET_HISTORY)) {
                    startActivity(notificationHelper.getWalletHistoryScreenIntent(this, notification));
                }

            } else {
                Log.d("Notification", "User Logged out, can not launch Home!");
                Intent intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
            }
        }
    }

    private void initMembers() {
        UserInfoModelImpl.newInstance(getUserInfoCallBackListener()).getUserInfo();
        NostragamusAnalytics.getInstance().setMoEngageUserProperties();
    }

    private void initViews() {
        mNewChallengesBottomButton = (LinearLayout) findViewById(R.id.home_challenges_tab_layout);
        mInPlayBottomButton = (LinearLayout) findViewById(R.id.home_inPlay_tab_layout);
        mCompletedChallengeBottomButton = (LinearLayout) findViewById(R.id.home_completed_tab_layout);
        mGroupBottomButton = (LinearLayout) findViewById(R.id.home_group_tab_layout);
        mProfileBottomButton = (LinearLayout) findViewById(R.id.home_profile_tab_layout);

        mNewChallengesBottomButton.setOnClickListener(this);
        mInPlayBottomButton.setOnClickListener(this);
        mCompletedChallengeBottomButton.setOnClickListener(this);
        mGroupBottomButton.setOnClickListener(this);
        mProfileBottomButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_challenges_tab_layout:
                onNewChallengesClicked(getIntent().getExtras());
                break;

            case R.id.home_inPlay_tab_layout:
                onInPlayClicked(getIntent().getExtras());
                break;

            case R.id.home_completed_tab_layout:
                onHistoryClicked(getIntent().getExtras());
                break;

            case R.id.home_group_tab_layout:
                onGroupClicked(getIntent().getExtras());
                break;

            case R.id.home_profile_tab_layout:
                onNavigationClicked(getIntent().getExtras());
                break;
        }
    }

    protected void setNewChallengesSelected() {
        mNewChallengesBottomButton.setSelected(true);
        mInPlayBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(false);
        mGroupBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setInPlaySelected() {
        mInPlayBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(false);
        mGroupBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setHistorySelected() {
        mInPlayBottomButton.setSelected(false);
        mNewChallengesBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(true);
        mGroupBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setGroupSelected() {
        mGroupBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(false);
        mInPlayBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setProfileSelected() {
        mProfileBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(false);
        mInPlayBottomButton.setSelected(false);
        mGroupBottomButton.setSelected(false);
    }

    private void onNavigationClicked(Bundle args) {
        setProfileSelected();
        loadNavigationFragment(args);
    }

    private void onGroupClicked(Bundle args) {
        setGroupSelected();
        loadGroupFragment(args);
    }

    private void onInPlayClicked(Bundle args) {
        setInPlaySelected();
        loadInPlayFragment(args);
    }

    private void onHistoryClicked(Bundle args) {
        setHistorySelected();
        loadHistoryFragment(args);
    }

    private void onNewChallengesClicked(Bundle args) {
        setNewChallengesSelected();
        loadNewChallengeFragment(args);
    }

    private void loadNavigationFragment(Bundle args) {
        NavigationFragment navigationFragment = new NavigationFragment();
        if (args != null) {
            navigationFragment.setArguments(args);
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, navigationFragment);
    }

    private void loadInPlayFragment(Bundle args) {
        InPlayFragment inPlayFragment = new InPlayFragment();
        if (args != null) {
            inPlayFragment.setArguments(args);
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, inPlayFragment);

    }

    private void loadNewChallengeFragment(Bundle args) {
        NewChallengesFragment newChallengesFragment = new NewChallengesFragment();
        if (args != null) {
            newChallengesFragment.setArguments(args);
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, newChallengesFragment);
    }

    private void loadHistoryFragment(Bundle args) {
        CompletedChallengeHistoryFragment completedChallengeHistoryFragment = new CompletedChallengeHistoryFragment();
        if (args != null) {
            completedChallengeHistoryFragment.setArguments(args);
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, completedChallengeHistoryFragment);
    }

    private void loadGroupFragment(Bundle args) {
        AllGroupsFragment allGroupsFragment = new AllGroupsFragment().newInstance();
        if (args != null) {
            allGroupsFragment.setArguments(args);
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, allGroupsFragment);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment != null && !(fragment instanceof NewChallengesFragment)) {
            onNewChallengesClicked(getIntent().getExtras());
        } else {
            handleDoubleBackPressToExitApp();
        }
    }

    /**
     * Application exit happens only when user clicks back button twice within specified time interval
     */
    private void handleDoubleBackPressToExitApp() {
        if (mIsFirstBackPressed) {
            super.onBackPressed();
        } else {

            Toast.makeText(this, getString(R.string.double_back_press_msg), Toast.LENGTH_SHORT).show();
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
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mNetworkStateChangedReceiver,
                new IntentFilter(Constants.IntentActions.ACTION_INTERNET_STATE_CHANGED));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNetworkStateChangedReceiver);
        super.onStop();
    }

    /* Local broadcast receiver when Internet connected, if app is in Foreground, will trigger action */
    BroadcastReceiver mNetworkStateChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onInternetConnected();
        }
    };

    private void onInternetConnected() {
        if (!this.isFinishing()) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                if (fragment instanceof NewChallengesFragment) {
                    ((NewChallengesFragment) fragment).onInternetConnected();
                }
                if (fragment instanceof InPlayFragment) {
                    ((InPlayFragment) fragment).onInternetConnected();
                }
            }
        }
    }

    /*----------- TODO : On boarding flow ------------ */

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
                if (userInfo != null && BuildConfig.IS_PAID_VERSION) {

                    com.jeeva.android.Log.d(TAG, "[onBoard] Paid app..");
                    performOnBoardFlow(userInfo);
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
                    /* check for OTP screen */
                    Boolean otpVerified = userInfo.getInfoDetails().getOtpVerified();
                    if (otpVerified == null || !otpVerified) {
                        launchVerifyOTP();
                    }
                }
            }
        }

    }

    private void launchVerifyOTP() {
        if (!isFinishing()) {
            Intent intent = new Intent(this, VerifyProfileActivity.class);
            intent.putExtra(Constants.BundleKeys.SUCCESSFUL_REFERRAL, false);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    /**
     * If disclaimer not accepted, then only show editProfile
     */
    private void launchEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("screen", Constants.BundleKeys.HOME_SCREEN);
        intent.putExtra(Constants.BundleKeys.EDIT_PROFILE_LAUNCHED_FROM, EditProfileActivity.ILaunchedFrom.HOME_ACTIVITY);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void launchWhatsNew() {

        if (!isFinishing()) {
            Intent intent = new Intent(this, AppUpdateActivity.class);
            intent.putExtra(Constants.BundleKeys.SCREEN, Constants.ScreenNames.WHATS_NEW);
            startActivity(intent);
        }
    }
}
