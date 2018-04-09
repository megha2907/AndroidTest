package in.sportscafe.nostragamus.module.nostraHome.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeeva.android.Log;

import org.parceler.Parcels;

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
import in.sportscafe.nostragamus.module.nostraHome.helper.BottomBarCountHelper;
import in.sportscafe.nostragamus.module.notifications.NostraNotification;
import in.sportscafe.nostragamus.module.notifications.NotificationHelper;
import in.sportscafe.nostragamus.module.notifications.inApp.InAppNotificationHelper;
import in.sportscafe.nostragamus.module.recentActivity.ui.RecentActivityFragment;
import in.sportscafe.nostragamus.module.privateContest.ui.branchShare.ShareDetailsDto;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.dto.JoinPrivateContestWithInviteCodeScreenData;
import in.sportscafe.nostragamus.module.privateContest.ui.joinPrivateContest.findContest.JoinPrivateContestWithInviteCodeActivity;
import in.sportscafe.nostragamus.module.user.group.allgroups.AllGroupsFragment;
import in.sportscafe.nostragamus.module.user.login.UserInfoModelImpl;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import in.sportscafe.nostragamus.module.user.myprofile.edit.EditProfileActivity;
import in.sportscafe.nostragamus.module.user.myprofile.verify.VerifyProfileActivity;
import in.sportscafe.nostragamus.utils.FragmentHelper;

public class NostraHomeActivity extends NostraBaseActivity implements View.OnClickListener, NostraHomeActivityListener {

    private static final String TAG = NostraHomeActivity.class.getSimpleName();
    public static final int DOUBLE_BACK_PRESSED_DELAY_ALLOWED = 3000;
    private static final int NAV_BAR_COUNTER_BADGE_ANIM_TIME = 250;

    public interface LaunchedFrom {
        int SHOW_NEW_CHALLENGES = -111;
        int SHOW_IN_PLAY = -112;
        int SHOW_COMPLETED = -113;
        int SHOW_RECENT_ACTIVITY = -114;
        int SHOW_NAVIGATION = -115;
    }

    private LinearLayout mNewChallengesBottomButton;
    private LinearLayout mInPlayBottomButton;
    private LinearLayout mCompletedChallengeBottomButton;
    private LinearLayout mRecentActivityBottomButton;
    private LinearLayout mProfileBottomButton;
    private TextView mUnPlayedMatchCounterTextView;
    private TextView mUnReadRecentActivityIcon;

    private boolean mIsFirstBackPressed = false;
    private int mUnPlayedMatchCount = 0;


    @Override
    public String getScreenName() {
        return Constants.Notifications.SCREEN_HOME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nostra_home);

        initMembers();
        initViews();
        getServerTime();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        onNewChallengesClicked(getIntent().getExtras());
        handleNotifications();

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUnPlayedMatchCount();
    }

    private void updateUnPlayedMatchCount() {
        new BottomBarCountHelper().getInplayCounter(getApplicationContext(),
                new BottomBarCountHelper.BottomBarCountHelperListener() {
                    @Override
                    public void onData(int status, final int unPlayedMatchCount) {
                        if (unPlayedMatchCount > 0) {
                            int setTextDelayTime = 0;
                            if (mUnPlayedMatchCounterTextView.getVisibility() != View.VISIBLE) {
                                Animation anim = new ScaleAnimation(0f, 1f, 0f, 1f,
                                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                anim.setFillAfter(true);
                                anim.setDuration(NAV_BAR_COUNTER_BADGE_ANIM_TIME);
                                mUnPlayedMatchCounterTextView.startAnimation(anim);
                                mUnPlayedMatchCounterTextView.setVisibility(View.VISIBLE);
                                setTextDelayTime = 0;

                            } else {
                                if (mUnPlayedMatchCount != unPlayedMatchCount) {
                                    setTextDelayTime = 500;
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Animation anim = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
                                                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                            anim.setFillAfter(false);
                                            anim.setRepeatMode(Animation.REVERSE);
                                            anim.setRepeatCount(1);
                                            anim.setDuration(NAV_BAR_COUNTER_BADGE_ANIM_TIME);
                                            mUnPlayedMatchCounterTextView.startAnimation(anim);
                                        }
                                    }, setTextDelayTime);
                                }
                            }

                            mUnPlayedMatchCount = unPlayedMatchCount;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (unPlayedMatchCount > 9) {
                                        mUnPlayedMatchCounterTextView.setPadding(getResources().getDimensionPixelOffset(R.dimen.dim_3),
                                                getResources().getDimensionPixelOffset(R.dimen.dim_1),
                                                getResources().getDimensionPixelOffset(R.dimen.dim_4),
                                                getResources().getDimensionPixelOffset(R.dimen.dim_2));
                                    }
                                    mUnPlayedMatchCounterTextView.setText(String.valueOf(unPlayedMatchCount));
                                }
                            }, setTextDelayTime + NAV_BAR_COUNTER_BADGE_ANIM_TIME);

                        } else {
                            android.util.Log.d(TAG, "Bottom bar counter is 0");
                            if (mUnPlayedMatchCounterTextView.getVisibility() == View.VISIBLE) {
                                Animation anim = new ScaleAnimation(1f, 0f, 1f, 0f,
                                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                anim.setDuration(NAV_BAR_COUNTER_BADGE_ANIM_TIME);
                                mUnPlayedMatchCounterTextView.startAnimation(anim);
                            }

                            mUnPlayedMatchCounterTextView.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError(int status) {
                        android.util.Log.d(TAG, "Error in updating bottombar counter");
                    }
                });
    }

    private void setAlarmForInAppNotification() {
        InAppNotificationHelper inAppNotificationHelper = new InAppNotificationHelper();
        inAppNotificationHelper.setAlarmForInAppNotifications(getApplicationContext());
    }

    private void getServerTime() {
        new NostraHomeApiImpl().fetchServerTimeFromServer(getApplicationContext(),
                new NostraHomeApiImpl.NostraHomeApiListener() {
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
                        setAlarmForInAppNotification();
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

                    case LaunchedFrom.SHOW_RECENT_ACTIVITY:
                        onRecentActivityClicked(intent.getExtras());
                        break;

                    case LaunchedFrom.SHOW_NAVIGATION:
                        onNavigationClicked(intent.getExtras());
                        break;
                }
            }
        }
    }

    private void handleNotifications() {
        if (NostragamusDataHandler.getInstance().isLoggedInUser()) {

            NotificationHelper notificationHelper = new NotificationHelper();
            NostraNotification notification = notificationHelper.getNotificationIfToBeSent(getIntent());
            if (notification != null && !TextUtils.isEmpty(notification.getScreenName())) {

                String screenName = notification.getScreenName();
                Log.d("Notification", "ScreenName : " + screenName);

                if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_NEW_CHALLENGE)) {
                    onNewChallengesClicked(notificationHelper.getBundleAddedNotificationDetailsIntoArgs(getIntent(), notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_NEW_CHALLENGE_SPORT)) {
                    onNewChallengesClicked(notificationHelper.getBundleAddedNotificationDetailsIntoArgs(getIntent(), notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_NEW_CHALLENGES_GAMES)) {
                    startActivity(notificationHelper.getNewChallengeMatchesScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_IN_PLAY_MATCHES)) {
                    onInPlayClicked(notificationHelper.getBundleAddedNotificationDetailsIntoArgs(getIntent(), notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_RESULTS)) {
                    startActivity(notificationHelper.getResultsScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_CHALLENGE_HISTORY_WINNINGS)) {
                    startActivity(notificationHelper.getChallengeHistoryWinningsScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_REFER_FRIEND)) {
                    startActivity(notificationHelper.getReferFriendScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_REFERRAL_CREDITS)) {
                    startActivity(notificationHelper.getReferralCreditsScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_STORE)) {
                    startActivity(notificationHelper.getStoreScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_APP_UPDATE)) {
                    startActivity(notificationHelper.getAppUpdateScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_WHATS_NEW)) {
                    startActivity(notificationHelper.getWhatsNewScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_WALLET_HISTORY)) {
                    startActivity(notificationHelper.getWalletHistoryScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_WALLET_ADD_MONEY)) {
                    startActivity(notificationHelper.getAddWalletMoneyScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_CHALLENGE_HISTORY)) {
                    onHistoryClicked(notificationHelper.getBundleAddedNotificationDetailsIntoArgs(getIntent(), notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_CHALLENGE_HISTORY_GAMES)) {
                    startActivity(notificationHelper.getChallengeHistoryMatchesScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_CHALLENGE_HISTORY_LEADERBOARDS)) {
                    startActivity(notificationHelper.getChallengeHistoryLeaderBoardsScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_WEB_VIEW)) {
                    startActivity(notificationHelper.getWebViewScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_SLIDES)) {
                    startActivity(notificationHelper.getSlidesScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_ANNOUNCEMENT)) {
                    startActivity(notificationHelper.getAnnouncementScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_WALLET_HOME)) {
                    startActivity(notificationHelper.getWalletHomeScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_KYC_DETAILS)) {
                    startActivity(notificationHelper.getKYCScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_INPLAY_CONTEST)) {
                    startActivity(notificationHelper.getInPlayContestScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_INPLAY_CONTEST_LEADERBOARDS)) {
                    startActivity(notificationHelper.getInPlayContestLeaderBoardScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.SCREEN_POWERUP_TRANSACTION)) {
                    startActivity(notificationHelper.getPowerUpTransactionScreenIntent(this, notification));

                } else if (screenName.equalsIgnoreCase(Constants.Notifications.NONE)) {
                            /* NO CLICK EVENT SHOULD HAPPEN */
                }

            } else {    // Keep this always into else part
                checkForPrivateContest();
            }
        } else {
            Log.d("Notification", "User Logged out, can not launch Home!");
        }
    }


    private void checkForPrivateContest() {
        if (!TextUtils.isEmpty(NostragamusDataHandler.getInstance().getPrivateContestInvitationCode())) {

            /* Get dataDto  */
            ShareDetailsDto shareDetailsDto = new Gson().fromJson(
                    NostragamusDataHandler.getInstance().getPrivateContestInvitationCode(), ShareDetailsDto.class);

            if (shareDetailsDto != null && !TextUtils.isEmpty(shareDetailsDto.getPrivateCode())) {

                Log.d(TAG, "Code Private contest Invitation code : " + shareDetailsDto.getPrivateCode());

                JoinPrivateContestWithInviteCodeScreenData privateCodeScreenData = new JoinPrivateContestWithInviteCodeScreenData();
                privateCodeScreenData.setPrivateCode(shareDetailsDto.getPrivateCode());
                privateCodeScreenData.setShareDetails(shareDetailsDto);

                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.BundleKeys.JOIN_PRIVATE_CONTEST_WITH_INVITATION_CODE_SCREEN_DATA,
                        Parcels.wrap(privateCodeScreenData));

                Intent intent = new Intent(this, JoinPrivateContestWithInviteCodeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                /* As private contest action is going to be taken now, remove this data ;
                    so that next time the same action not be taken */
                NostragamusDataHandler.getInstance().setPrivateContestInvitationCode("");
            }
        }
    }

    private void initMembers() {
        UserInfoModelImpl.newInstance(getUserInfoCallBackListener()).getUserInfo();
        NostragamusAnalytics.getInstance().setMoEngageUserProperties(getApplicationContext());
        NostragamusAnalytics.getInstance().setFreshChatUserProperties(getApplicationContext());
        NostragamusAnalytics.getInstance().setCrashlyticsUserProperties(getApplicationContext());
    }

    private void initViews() {
        mNewChallengesBottomButton = (LinearLayout) findViewById(R.id.home_challenges_tab_layout);
        mInPlayBottomButton = (LinearLayout) findViewById(R.id.home_inPlay_tab_layout);
        mCompletedChallengeBottomButton = (LinearLayout) findViewById(R.id.home_completed_tab_layout);
        mRecentActivityBottomButton = (LinearLayout) findViewById(R.id.home_recent_activity_tab_layout);
        mProfileBottomButton = (LinearLayout) findViewById(R.id.home_profile_tab_layout);
        mUnPlayedMatchCounterTextView = (TextView) findViewById(R.id.home_inPlay_matches_count);
        mUnReadRecentActivityIcon = (TextView) findViewById(R.id.home_unready_recent_activity);

        mNewChallengesBottomButton.setOnClickListener(this);
        mInPlayBottomButton.setOnClickListener(this);
        mCompletedChallengeBottomButton.setOnClickListener(this);
        mRecentActivityBottomButton.setOnClickListener(this);
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

            case R.id.home_recent_activity_tab_layout:
                onRecentActivityClicked(getIntent().getExtras());
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
        mRecentActivityBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setInPlaySelected() {
        mInPlayBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(false);
        mRecentActivityBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setHistorySelected() {
        mInPlayBottomButton.setSelected(false);
        mNewChallengesBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(true);
        mRecentActivityBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
    }

    protected void setGroupSelected() {
        mRecentActivityBottomButton.setSelected(true);
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
        mRecentActivityBottomButton.setSelected(false);
    }

    private void setRecentActivitySelected() {
        mRecentActivityBottomButton.setSelected(true);
        mNewChallengesBottomButton.setSelected(false);
        mCompletedChallengeBottomButton.setSelected(false);
        mInPlayBottomButton.setSelected(false);
        mProfileBottomButton.setSelected(false);
        hideRecentActivityUnreadIcon();
    }


    private void onNavigationClicked(Bundle args) {
        setProfileSelected();
        loadNavigationFragment(args);
        NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.HOME_SCREEN, Constants.AnalyticsClickLabels.NAVIGATION);
    }

    private void onRecentActivityClicked(Bundle args) {
        setRecentActivitySelected();
        loadRecentActivityFragment(args);
        NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.HOME_SCREEN, Constants.AnalyticsClickLabels.RECENT_ACTIVITY);
    }


    private void onInPlayClicked(Bundle args) {
        setInPlaySelected();
        loadInPlayFragment(args);
        NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.HOME_SCREEN, Constants.AnalyticsClickLabels.IN_PLAY);
    }

    private void onHistoryClicked(Bundle args) {
        setHistorySelected();
        loadHistoryFragment(args);
        NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.HOME_SCREEN, Constants.AnalyticsClickLabels.HISTORY);
    }

    private void onNewChallengesClicked(Bundle args) {
        setNewChallengesSelected();
        loadNewChallengeFragment(args);
        NostragamusAnalytics.getInstance().trackScreenShown(Constants.AnalyticsCategory.HOME_SCREEN, Constants.AnalyticsClickLabels.NEW_CHALLENGES);
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

    private void loadRecentActivityFragment(Bundle args) {
        RecentActivityFragment recentActivityFragment = new RecentActivityFragment();
        if (args != null) {
            recentActivityFragment.setArguments(args);
        }
        FragmentHelper.replaceFragment(this, R.id.fragment_container, recentActivityFragment);
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
                    updateRecentActivityCounter(userInfo);

                    /* Get Moengage details JsonObject from server and Send Key/Value to Moengage  */
                    /*  Gson gson = new Gson();
                    String json = gson.toJson(userInfo.getUserPaymentInfo().getBank());

                    // Convert JSON string back to Map.
                    Type type = new TypeToken<Map<String, String>>(){}.getType();
                    Map<String, String> map = gson.fromJson(json, type);
                    for (String key : map.keySet()) {
                        System.out.println(key + " = " + map.get(key));
                    } */


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

    @Override
    public void showNewChallenges(Bundle args) {
        if (args == null) {
            args = getIntent().getExtras();
        }
        onNewChallengesClicked(args);
    }

    @Override
    public void showInPlayChallenges(Bundle args) {
        if (args == null) {
            args = getIntent().getExtras();
        }
        onInPlayClicked(args);
    }

    @Override
    public void showHistoryChallenges(Bundle args) {
        if (args == null) {
            args = getIntent().getExtras();
        }
        onHistoryClicked(args);
    }


    public void updateInplayCounter() {
        updateUnPlayedMatchCount();
    }

    @Override
    public void updateRecentActivityUnReadCounter(UserInfo updatedUserInfo) {
        updateRecentActivityCounter(updatedUserInfo);
    }

    private void updateRecentActivityCounter(UserInfo userInfo) {
        if (userInfo != null) {
            if (userInfo.isHasUnreadActivities()) {
                Animation anim = new ScaleAnimation(0f, 1f, 0f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setFillAfter(true);
                anim.setDuration(NAV_BAR_COUNTER_BADGE_ANIM_TIME);
                mUnReadRecentActivityIcon.startAnimation(anim);
                mUnReadRecentActivityIcon.setVisibility(View.VISIBLE);
            } else {
                if (mUnReadRecentActivityIcon.getVisibility() == View.VISIBLE) {
                    Animation anim = new ScaleAnimation(1f, 0f, 1f, 0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    anim.setDuration(NAV_BAR_COUNTER_BADGE_ANIM_TIME);
                    mUnReadRecentActivityIcon.startAnimation(anim);
                }

                mUnReadRecentActivityIcon.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void hideRecentActivityUnreadIcon() {

        if (mUnReadRecentActivityIcon.getVisibility() == View.VISIBLE) {
            Animation anim = new ScaleAnimation(1f, 0f, 1f, 0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(NAV_BAR_COUNTER_BADGE_ANIM_TIME);
            mUnReadRecentActivityIcon.startAnimation(anim);
        }

        mUnReadRecentActivityIcon.setVisibility(View.INVISIBLE);
    }

}
