package in.sportscafe.nostragamus.module.analytics;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.pinpoint.analytics.monetization.AmazonMonetizationEventBuilder;
import com.amazonaws.mobileconnectors.pinpoint.analytics.monetization.GooglePlayMonetizationEventBuilder;
import com.amazonaws.mobileconnectors.pinpoint.targeting.TargetingClient;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfile;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfileUser;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.pinpoint.AmazonPinpointClient;
import com.amplitude.api.Amplitude;
import com.amplitude.api.AmplitudeClient;
import com.amplitude.api.Revenue;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.InviteEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.crashlytics.android.answers.ShareEvent;
import com.crashlytics.android.answers.SignUpEvent;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeeva.android.Log;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.AnalyticsCategory;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.Constants.UserProperties;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.NostragamusDataHandler;
import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.analytics.awsAnalytics.EMAILEndPointRequest;
import in.sportscafe.nostragamus.module.analytics.awsAnalytics.NotificationEndPointRequest;
import in.sportscafe.nostragamus.module.analytics.awsAnalytics.SMSEndPointRequest;
import in.sportscafe.nostragamus.module.common.NostraFireBaseInstanceIDService;
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;
import io.fabric.sdk.android.Fabric;

/**
 * Created by deepanshu on 8/8/16.
 */
public class NostragamusAnalytics {

    private static String ACTION = "action";

    private static String LABEL = "label";

    private static String VALUE = "value";

    private static NostragamusAnalytics sSportsCafeAnalytics = new NostragamusAnalytics();

    public static NostragamusAnalytics getInstance() {
        return sSportsCafeAnalytics;
    }

    private NostragamusAnalytics() {
    }

    private Tracker mTracker;

    private MoEHelper mMoEHelper;

    private AmplitudeClient mAmplitude;

    private FreshchatUser mFreshChatUser;

    private Freshchat mFreshChat;

    private static AppEventsLogger sFaceBookAppEventLogger;

    private boolean mAppOpeningTracked = false;

    public static PinpointManager pinpointManager;

    private IdentityManager identityManager;

    private AWSConfiguration awsConfiguration;

    private String AWS_APP_ID = "2dbeb11bf7474761afac1a7e69a61e25";

    private FirebaseAnalytics mFirebaseAnalytics;

    public IdentityManager getIdentityManager() {
        return identityManager;
    }

    public NostragamusAnalytics init(final Context context) {
        GoogleAnalytics ga = GoogleAnalytics.getInstance(context);
        ga.setAppOptOut(BuildConfig.DEBUG);

        if (!BuildConfig.DEBUG) {

            // Initializing the google analyticsReceived
            this.mTracker = ga.newTracker(R.xml.app_tracker);
            this.mTracker.enableAdvertisingIdCollection(true);

            // Initializing the MoEngage
            this.mMoEHelper = MoEHelper.getInstance(context);


            // Initializing the Amplitude
            this.mAmplitude = Amplitude.getInstance().initialize(context, context.getString(R.string.amplitude_api_key))
                    .enableForegroundTracking((Application) context);


            // Initializing the FreshChat
            mFreshChat = Freshchat.getInstance(context);
            mFreshChatUser = Freshchat.getInstance(context).getUser();

            // Tracking flavor
            //trackFlavor();

            //Tracking events on fabric
            Fabric.with(context, new Answers());
            Fabric.with(context, new Crashlytics());

            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

            // Facebook Analytics
            FacebookSdk.sdkInitialize(context);
            sFaceBookAppEventLogger = AppEventsLogger.newLogger(context);

            try {

                // initialize AWS Mobile client
                AWSMobileClient.getInstance().initialize(context, new AWSStartupHandler() {
                    @Override
                    public void onComplete(AWSStartupResult awsStartupResult) {
                        Log.i("inside", "successAWS");
                    }
                });

            /* initialize Pinpoint Manager & CognitoCachingCredentialsProvider
             *  aws app id and aws pool id is linked to aws account
             *  Please don't change it until it's change in aws configuration file */
                CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider = new CognitoCachingCredentialsProvider(context, context.getString(R.string.aws_pinpoint_pool_id), Regions.AP_SOUTHEAST_1);
                PinpointConfiguration config = new PinpointConfiguration(context, context.getString(R.string.aws_app_id), Regions.US_EAST_1, cognitoCachingCredentialsProvider);
                this.pinpointManager = new PinpointManager(config);

                this.awsConfiguration = new AWSConfiguration(context);
                identityManager = new IdentityManager(context, awsConfiguration);
                identityManager.setDefaultIdentityManager(identityManager);

            } catch (final AmazonClientException ex) {

            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        if (!TextUtils.isEmpty(Nostragamus.getInstance().getServerDataManager().getGcmDeviceToken())) {
                            Log.e("NotError", Nostragamus.getInstance().getServerDataManager().getGcmDeviceToken());
                            pinpointManager.getNotificationClient().registerDeviceToken(Nostragamus.getInstance().getServerDataManager().getGcmDeviceToken());

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

        return this;
    }


    /**
     * track app opening
     *
     * @param openingFrom - launcher, notification
     */
    public void trackAppOpening(String openingFrom) {
        if (!mAppOpeningTracked && !BuildConfig.DEBUG) {
            track(AnalyticsCategory.APP_OPENING, null, openingFrom, null);
            mAppOpeningTracked = true;

            // Start a session with Pinpoint
            if (pinpointManager != null && pinpointManager.getSessionClient() != null) {
                pinpointManager.getSessionClient().startSession();
            }
        }
    }

    /**
     * track app closing
     * <p>
     * closingFrom - home screen on back pressed
     */
    public void trackAppClosing() {
        // Stop the session with Pinpoint
        if (pinpointManager != null && pinpointManager.getSessionClient() != null && !BuildConfig.DEBUG) {
            pinpointManager.getSessionClient().stopSession();
        }
    }

    /**
     * track app sharing
     *
     * @param sharingFrom - Group, Profile
     */
    public void trackReferralAction(String sharingFrom) {
        track(AnalyticsCategory.REFERRAL_ACTION, null, sharingFrom, null);
    }

    /**
     * track login options which the user is using
     *
     * @param loginVia - Google Plus, Facebook
     */
    public void trackLogIn(String actions, String loginVia) {
        if (!BuildConfig.DEBUG) {
            track(AnalyticsCategory.LOGIN, actions, loginVia, null);
            trackEvent(AnalyticsCategory.LOGIN, actions, loginVia);
        }
    }

    /**
     * track logout
     */
    public void trackLogOut() {
        if (!BuildConfig.DEBUG) {
            trackEvent(AnalyticsCategory.LOGOUT, null, null);
        }
    }

    /**
     * track dummy game start and end
     *
     * @param actions - Started, Done
     */
    public void trackDummyGame(String actions) {
        if (!BuildConfig.DEBUG) {
            track(AnalyticsCategory.DUMMY_GAME, actions, null, null);
            trackEvent(AnalyticsCategory.DUMMY_GAME, "Action", actions);
        }
    }

    /**
     * track dummy game skip
     *
     * @param actions - Started, Done
     */
    public void trackDummyGame(String actions, long numberOfScreens) {
        track(AnalyticsCategory.DUMMY_GAME, actions, AnalyticsLabels.SCREENS_SEEN, numberOfScreens);
    }

    /**
     * track bank transfer start and end
     *
     * @param actions - Started, Successful
     */
    public void trackPowerBank(String actions) {
        track(AnalyticsCategory.POWER_BANK, actions, null, null);
    }

    /**
     * track groups
     *
     * @param actions - join and leave
     */
    public void trackGroups(String actions) {
        track(AnalyticsCategory.GROUP, actions, null, null);
    }

    /**
     * track groups
     *
     * @param actions     - new
     * @param tournaments - Gallery, Camera
     */
    public void trackGroups(String actions, String tournaments) {
        track(AnalyticsCategory.GROUP, actions, tournaments, null);
    }

    /**
     * track new users
     *
     * @param actions - referral or organic
     * @param label   - channels like facebook, twitter
     */
    public void trackNewUsers(String actions, String label) {
        if (!BuildConfig.DEBUG) {
            track(AnalyticsCategory.NEW_USERS, actions, label, null);
            trackSignUpFabric(AnalyticsCategory.NEW_USERS + "-" + actions);
        }
    }

    /**
     * track powerups
     *
     * @param actions - applied
     * @param label   - 2x, no_negs
     */
    public void trackPowerups(String actions, String label) {
        track(AnalyticsCategory.POWERUP, actions, label, null);
    }

    /**
     * track badges
     *
     * @param actions - received
     * @param label   - Baby Step
     */
    public void trackBadges(String actions, String label) {
        track(AnalyticsCategory.BADGE, actions, label, null);
    }

    /**
     * track No. of times challenge Config popup opened
     *
     * @param actions - config popup opened
     */
    public void trackConfigs(String actions) {
        track(AnalyticsCategory.CONFIGS, actions, null, null);
    }


    /**
     * track No. of times Rewards popup opened
     *
     * @param actions - Rewards popup opened
     */
    public void trackRewards(String actions) {
        track(AnalyticsCategory.CASH_REWARDS, actions, null, null);
    }

    /**
     * track No. of times What's New opened
     *
     * @param actions - What's New Screen button click
     */
    public void trackWhatsNew(String actions) {
        track(AnalyticsCategory.WHATS_NEW, actions, null, null);
    }

    /**
     * track No. of times Update App opened
     *
     * @param actions - Update App Screen opened
     */
    public void trackUpdateApp(String actions) {
        track(AnalyticsCategory.APP_UPDATE, actions, null, null);
    }

    /**
     * track No. of times Update Later clicked
     *
     * @param actions - Update Later Clicked
     */
    public void trackUpdateLater(String actions) {
        track(AnalyticsCategory.UPDATE_LATER, actions, null, null);
    }


    /**
     * track timeline
     *
     * @param actions - Tournament
     */
    public void trackTimeline(String actions) {
        track(AnalyticsCategory.TIMELINE, actions, null, null);
    }

    /**
     * track timeline
     *
     * @param actions - Tournament
     * @param label   - India vs England 2016
     */
    public void trackTimeline(String actions, String label, long unplayedMatches) {
        track(AnalyticsCategory.TIMELINE, actions, label, unplayedMatches);
    }

    /**
     * track leaderboards
     */
    public void trackLeaderboard(String label) {
        track(AnalyticsCategory.LEADERBOARD, AnalyticsActions.LB_DETAIL, label, null);
    }

    /**
     * track Don't want cash rewards
     *
     * @param actions - Paytm , cash rewards
     */
    public void trackNoCashRewards(String actions) {
        track(AnalyticsCategory.CASH_REWARDS, actions, null, null);
    }

    /**
     * track play games
     *
     * @param actions - Started, completed
     */
    public void trackPlay(String actions) {
        track(AnalyticsCategory.PLAY, actions, null, null);
    }

    /**
     * track play games
     *
     * @param actions       - Answered, Shuffled
     * @param label         - Swipe direction like left, right, top or bottom
     * @param timeSpentInMs - Actual milliseconds spent before swiping the questions
     */
    public void trackPlay(String actions, String label, long timeSpentInMs) {
        track(AnalyticsCategory.PLAY, actions, label, timeSpentInMs);
    }


    /**
     * track onBoarding Time
     *
     * @param actions - OnBoardingTime
     * @param label   - Actual time spent in OnBoarding from Edit Profile to Home
     */
    public void trackOnBoarding(String actions, String label, long timeSpentInS) {
        if (!BuildConfig.DEBUG) {
            track(AnalyticsCategory.ONBOARDING_TIME, actions, label, timeSpentInS);
            trackEvent(AnalyticsCategory.ONBOARDING, AnalyticsCategory.ONBOARDING_TIME, String.valueOf(timeSpentInS));
        }
    }


    /**
     * track edit profile
     *
     * @param type - Photo, Others
     * @param name - Gallery, Camera, UPDATE
     */
    public void trackEditProfile(String type, String name) {
        track(AnalyticsCategory.EDIT_PROFILE, type, name, null);
    }

    /**
     * track user profile
     *
     * @param name - Sports, Groups
     */
    public void trackMyProfile(String name) {
        track(AnalyticsCategory.PROFILE, AnalyticsActions.MYSELF, name, null);
    }

    /**
     * track user profile
     *
     * @param name - Sports, Groups
     */
    public void trackOtherProfile(String name) {
        track(AnalyticsCategory.PROFILE, AnalyticsActions.OTHER_USER, name, null);
    }

    /**
     * track normal updates
     *
     * @param updateType - normal or force
     * @param value      - 0 or 1
     */
    public void trackUpdate(String updateType, long value) {
        track(AnalyticsCategory.APP_UPDATE, updateType, null, value);
    }

    public void startFragmentTrack(Activity activity, String fragmentName) {
        if (null != mMoEHelper) {
            mMoEHelper.onFragmentStart(activity, fragmentName);

            // It is enough to track only the fragment start through GA, no need to track fragment stop
            mTracker.setScreenName(fragmentName);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public void stopFragmentTrack(Activity activity, String fragmentName) {
        if (null != mMoEHelper) {
            mMoEHelper.onFragmentStop(activity, fragmentName);
        }
    }

    private void track(String category, String action, String label, Long value) {

        if (!BuildConfig.DEBUG) {

            if (null == mMoEHelper) {
                return;
            }

            // Google Analytics
            HitBuilders.EventBuilder gaEventBuilder = new HitBuilders.EventBuilder();

            // MoEngage Analytics
            PayloadBuilder moeEventBuilder = new PayloadBuilder();

            // category
            gaEventBuilder.setCategory(category);

            // action
            if (null != action) {
                gaEventBuilder.setAction(action);
                moeEventBuilder.putAttrString(ACTION, action);
            }

            // label
            if (null != label) {
                gaEventBuilder.setLabel(label);
                moeEventBuilder.putAttrString(LABEL, label);
            }

            // value
            if (null != value) {
                gaEventBuilder.setValue(value);
                moeEventBuilder.putAttrLong(VALUE, value);
            }

            JSONObject jsonObject = moeEventBuilder.build();

          /* Stopped tracking events on MoEngage
        mMoEHelper.trackEvent(category, jsonObject); */

        /*try {
            if (jsonObject.has(ACTION)) {
                jsonObject.put("_" + ACTION, jsonObject.get(ACTION));
                jsonObject.remove(ACTION);
            }
        } catch (JSONException e) {
            ExceptionTracker.track(e);
        }*/

            mAmplitude.logEvent(category, jsonObject);

            mTracker.send(gaEventBuilder.build());

        }
    }

    public void trackOtherEvents(String category, Map<String, String> values) {
        if (null == mMoEHelper) {
            return;
        }

        // Google Analytics
        HitBuilders.EventBuilder gaEventBuilder = new HitBuilders.EventBuilder();
        gaEventBuilder.setAll(values);
        gaEventBuilder.setCategory(category);

        mTracker.send(gaEventBuilder.build());
        mMoEHelper.trackEvent(category, values);
    }

    private void trackFlavor() {
        if (!BuildConfig.FLAVOR.equalsIgnoreCase("production")) {
            track(AnalyticsCategory.FLAVOR, null, BuildConfig.FLAVOR, null);
        }
    }

    public void autoTrack(Application application) {
        if (null != mMoEHelper) {
            mMoEHelper.autoIntegrate(application);

            mAmplitude.trackSessionEvents(true);
        }
    }

    public void setUserId(String userId) {
        if (null != mAmplitude && null != userId) {
            mAmplitude.setUserId(userId);
        }
    }

    /**
     * Every time UserProperties are set, the recent one will be used to send with next tracking logs
     * If Null / empty value is set for property, then it won't be sent
     */
    public void setUserProperties() {
        if (null != mAmplitude && !BuildConfig.DEBUG) {
            try {
                JSONObject userProperties = new JSONObject();

                if (BuildConfig.IS_PAID_VERSION) {
                    userProperties.put(UserProperties.PRO_APP, "yes");
                } else {
                    userProperties.put(UserProperties.PRO_APP, "No");
                }

                if (BuildConfig.IS_ACL_VERSION) {
                    userProperties.put(UserProperties.ACL_APP, "yes");
                } else {
                    userProperties.put(UserProperties.ACL_APP, "No");
                }

                String channel = NostragamusDataHandler.getInstance().getInstallChannel();
                if (!TextUtils.isEmpty(channel)) {
                    userProperties.put(UserProperties.REFERRAL_CHANNEL, channel);
                }
                String campaign = NostragamusDataHandler.getInstance().getInstallReferralCampaign();
                if (!TextUtils.isEmpty(campaign)) {
                    userProperties.put(UserProperties.REFERRAL_CAMPAIGN, campaign);
                }

                String walletInit = String.valueOf(NostragamusDataHandler.getInstance().getWalletInitialAmount());
                if (!TextUtils.isEmpty(walletInit)) {
                    userProperties.put(UserProperties.WALLET_INIT, walletInit);
                }

                String linkName = NostragamusDataHandler.getInstance().getInstallLinkName();
                if (!TextUtils.isEmpty(linkName)) {
                    userProperties.put(UserProperties.LINK_NAME, linkName);
                }

                mAmplitude.setUserProperties(userProperties);
                Log.d("userProperties--", userProperties.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void setMoEngageUserProperties(Context context) {
        if (null != mMoEHelper && !BuildConfig.DEBUG) {

            if (BuildConfig.IS_PAID_VERSION) {
                mMoEHelper.setUserAttribute(UserProperties.PRO_APP, true);
            } else {
                mMoEHelper.setUserAttribute(UserProperties.PRO_APP, false);
            }

            if (BuildConfig.IS_ACL_VERSION) {
                mMoEHelper.setUserAttribute(UserProperties.ACL_APP, true);
            } else {
                mMoEHelper.setUserAttribute(UserProperties.ACL_APP, false);
            }

            String channel = NostragamusDataHandler.getInstance().getInstallChannel();
            if (!TextUtils.isEmpty(channel)) {
                mMoEHelper.setUserAttribute(UserProperties.REFERRAL_CHANNEL, channel);
            }
            String campaign = NostragamusDataHandler.getInstance().getInstallReferralCampaign();
            if (!TextUtils.isEmpty(campaign)) {
                mMoEHelper.setUserAttribute(UserProperties.REFERRAL_CAMPAIGN, campaign);
            }

            String walletInit = String.valueOf(NostragamusDataHandler.getInstance().getWalletInitialAmount());
            if (!TextUtils.isEmpty(walletInit)) {
                mMoEHelper.setUserAttribute(UserProperties.WALLET_INIT, walletInit);
            }

            String linkName = NostragamusDataHandler.getInstance().getInstallLinkName();
            if (!TextUtils.isEmpty(linkName)) {
                mMoEHelper.setUserAttribute(UserProperties.LINK_NAME, linkName);
            }

            PackageInfo pInfo = null;
            try {
                pInfo = context.getPackageManager().
                        getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String versionName = pInfo.versionName;
            if (!TextUtils.isEmpty(versionName)) {
                mMoEHelper.setUserAttribute(UserProperties.APP_VERSION, versionName);
            }

            /* setEmail() mandatory to get notification - DO NOT REMOVE  */
            UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
            if (userInfo != null) {
                String email = userInfo.getEmail();
                if (!TextUtils.isEmpty(email)) {
                    mMoEHelper.setEmail(email);
                }
            }

        }
    }


    /**
     * @param isAddMoney - if true, tracks as ADD-MONEY else WITHDRAW-MONEY
     * @param amount     - amount of transaction
     */
    public void trackWalletTransaction(boolean isAddMoney, double amount) {
        if (BuildConfig.IS_PAID_VERSION && !BuildConfig.DEBUG) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Amount", amount);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            String category = AnalyticsCategory.WALLET_WITHDRAW_MONEY;
            if (isAddMoney) {
                category = AnalyticsCategory.WALLET_ADD_MONEY;
            }

            if (mAmplitude != null) {
                mAmplitude.logEvent(category, jsonObject);
            }
            if (mMoEHelper != null) {
                mMoEHelper.trackEvent(category, jsonObject);
            }

        }
    }

    /**
     * NOTE: Must be called only on successful Login
     * <p>
     * This method used as Login method for moengage to identify user.
     * https://docs.moengage.com/docs/identifying-user
     *
     * @param userId
     */
    public void setMoengageUniqueId(String userId) {
        if (mMoEHelper != null && !BuildConfig.DEBUG) {
            mMoEHelper.setUniqueId(userId);
        } else {
            Log.e("Analytics", "Could not set UniqueId for MoEngage");
        }
    }

    /**
     * Tracks clicks on different event actions
     *
     * @param category
     */
    public void trackClickEvent(@NonNull String category, String label) {
        if (!BuildConfig.DEBUG) {
            track(category, AnalyticsActions.CLICKED, label, null);
            trackEvent(category, AnalyticsActions.CLICKED, label);
        }
    }

    /**
     * Tracks screen Views
     *
     * @param category
     */
    public void trackScreenShown(@NonNull String category, String label) {
        if (!BuildConfig.DEBUG) {
            track(category, AnalyticsActions.OPENED, label, null);
            trackEvent(category, AnalyticsActions.OPENED, label);
        }
    }

    /**
     * Tracks source
     *
     * @param category
     */
    public void trackSource(@NonNull String category, String label) {
        if (!BuildConfig.DEBUG) {
            track(category, AnalyticsActions.SOURCE, label, null);
            trackEvent(category, AnalyticsActions.SOURCE, label);
        }
    }


    /**
     * Tracks tab clicked
     *
     * @param category
     */
    public void trackTabClicked(@NonNull String category, String label) {
        if (!BuildConfig.DEBUG) {
            trackEvent(category, AnalyticsActions.TAB_CLICKED, label);
        }
    }

    public void trackReferralBenefitScreenShown() {
        track(AnalyticsCategory.REFERRAL_BENEFIT, AnalyticsActions.OPENED, AnalyticsLabels.SCREENS_SEEN, null);
    }

    /**
     * Tracks Challenges Opened
     *
     * @param challengeId
     * @param sportId
     * @param prizes
     * @param challengeStartTime
     * @param category
     */
    public void trackNewChallenges(int challengeId, String challengeName, int[] sportId, int prizes, String challengeStartTime, String category) {

        if (BuildConfig.IS_PAID_VERSION && mAmplitude != null && !BuildConfig.DEBUG) {

            String sportsName = "";
            JSONObject sportsJson = null;
            if (sportId != null) {
                SportsDataProvider sportsDataProvider = new SportsDataProvider();
                List<SportsTab> sportsTabList = sportsDataProvider.getSportsList();
                sportsJson = new JSONObject();
                for (SportsTab sportsTab : sportsTabList) {
                    for (int temp = 0; temp < sportId.length; temp++) {

                        if (sportId[temp] == sportsTab.getSportsId()) {

                            try {
                                sportsJson.put("sportsName", sportsTab.getSportsName());
                                sportsName = sportsTab.getSportsName();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("challengeId", challengeId);
                jsonObject.put("challengeName", challengeName);
                jsonObject.put("sports", sportsJson);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            if (mAmplitude != null) {
                mAmplitude.logEvent(category, jsonObject);
            }
            if (mMoEHelper != null) {
                mMoEHelper.trackEvent(category, jsonObject);
            }


              /* Send New challenges Data to aws pinpoint */
            if (pinpointManager != null && pinpointManager.getAnalyticsClient() != null) {

                final AnalyticsEvent event = pinpointManager.getAnalyticsClient().createEvent(AnalyticsCategory.NEW_CHALLENGES)
                        .withAttribute("challengeName", challengeName)
                        .withAttribute("sportName", sportsName);

                pinpointManager.getAnalyticsClient().recordEvent(event);
                pinpointManager.getAnalyticsClient().submitEvents();

            }

              /* Send New challenges Data to Fabric */
            Answers.getInstance().logCustom(new CustomEvent(AnalyticsCategory.NEW_CHALLENGES)
                    .putCustomAttribute("challengeName", challengeName)
                    .putCustomAttribute("sportName", sportsName));


            /* Send New challenges Data to Fb Analytics */
            Bundle contestBundle = new Bundle();
            contestBundle.putString("challengeName", challengeName);
            contestBundle.putString("sportName", sportsName);

            if (sFaceBookAppEventLogger != null) {
                sFaceBookAppEventLogger.logEvent(AnalyticsCategory.NEW_CHALLENGES, contestBundle);
            }

             /* Send New challenges Data to Firebase
               NOTE : Event Name with space not allowed */
            if (mFirebaseAnalytics != null) {
                mFirebaseAnalytics.logEvent("NEW_CHALLENGES", contestBundle);
            }

        } else {
            Log.d("App", "Can't log revenue, Amplitude null!");
        }
    }


    /**
     * Tracks Contest Joined
     *
     * @param contestName
     * @param contestType
     * @param entryFee
     * @param challengeName
     * @param screenName
     */
    public void trackContestJoined(String contestName, String contestType, int entryFee, String screenName, String challengeName, int prizeMoney) {

        if (BuildConfig.IS_PAID_VERSION && mAmplitude != null && !BuildConfig.DEBUG) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("contestName", contestName);
                jsonObject.put("contestType", contestType);
                jsonObject.put("contestEntryFee", entryFee);
                jsonObject.put("contestChallengeName", challengeName);
                jsonObject.put("screenJoinedFrom", screenName);
                jsonObject.put("prizeMoney", prizeMoney);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            String category = AnalyticsCategory.CONTEST_JOINED;

            if (mAmplitude != null) {
                mAmplitude.logEvent(category, jsonObject);
            }
            if (mMoEHelper != null) {
                mMoEHelper.trackEvent(category, jsonObject);
            }

             /* Send Contest Joining Data to Aws Pinpoint */
            if (pinpointManager != null && pinpointManager.getAnalyticsClient() != null) {

                final AnalyticsEvent event = pinpointManager.getAnalyticsClient().createEvent(AnalyticsCategory.CONTEST_JOINED)
                        .withAttribute("contestName", contestName)
                        .withAttribute("contestType", contestType)
                        .withAttribute("contestEntryFee", String.valueOf(entryFee))
                        .withAttribute("contestChallengeName", String.valueOf(challengeName))
                        .withAttribute("screenJoinedFrom", screenName)
                        .withAttribute("prizeMoney", String.valueOf(prizeMoney));

                pinpointManager.getAnalyticsClient().recordEvent(event);
                pinpointManager.getAnalyticsClient().submitEvents();

            }

             /* Send Contest Joining Data to Fabric */
            Answers.getInstance().logCustom(new CustomEvent(AnalyticsCategory.CONTEST_JOINED)
                    .putCustomAttribute("contestName", contestName)
                    .putCustomAttribute("contestType", contestType)
                    .putCustomAttribute("contestEntryFee", String.valueOf(entryFee))
                    .putCustomAttribute("contestChallengeName", String.valueOf(challengeName))
                    .putCustomAttribute("screenJoinedFrom", screenName)
                    .putCustomAttribute("prizeMoney", String.valueOf(prizeMoney)));

            /* Send Contest Joining Data to Fb Analytics */
            Bundle contestBundle = new Bundle();
            contestBundle.putString("contestName", contestName);
            contestBundle.putString("contestType", contestType);
            contestBundle.putInt("contestEntryFee", entryFee);
            contestBundle.putString("contestChallengeName", challengeName);
            contestBundle.putString("screenJoinedFrom", screenName);
            contestBundle.putInt("prizeMoney", prizeMoney);

            if (sFaceBookAppEventLogger != null) {
                sFaceBookAppEventLogger.logEvent(AnalyticsCategory.CONTEST_JOINED, contestBundle);
            }

             /* Send Contest Joining Data to Firebase
               NOTE : Event Name with space not allowed */
            if (mFirebaseAnalytics != null) {
                mFirebaseAnalytics.logEvent("CONTEST_JOINED", contestBundle);
            }

        } else {
            Log.d("App", "Can't log revenue, Amplitude null!");
        }
    }

    /**
     * Tracks revenue
     * Once User Joins a contest
     *
     * @param price
     */
    public void trackRevenue(double price, int contestId, String contestName, String contestType, String orderId) {
        if (BuildConfig.IS_PAID_VERSION && mAmplitude != null && !BuildConfig.DEBUG) {
            JSONObject eventPropertiesJson = new JSONObject();
            try {
                eventPropertiesJson.put("contestId", contestId);
                eventPropertiesJson.put("contestName", contestName);
                eventPropertiesJson.put("contestType", contestType);
            } catch (JSONException jEx) {
                jEx.printStackTrace();
            }

            Revenue revenue = new Revenue();
            revenue.setQuantity(1);     // Aways one
            revenue.setPrice(price);
            revenue.setEventProperties(eventPropertiesJson);

            mAmplitude.logRevenueV2(revenue);
        } else {
            Log.d("App", "Can't log revenue, Amplitude null!");
        }

         /* Send Revenue Data to Google Analytics */
        double priceValue = price;
        long priceValue1 = (long) priceValue;
        track(AnalyticsCategory.REVENUE, AnalyticsActions.CONTEST_JOINED, contestName, priceValue1);

        /* Send Revenue Data to Fb Analytics */
        Bundle contestBundle = new Bundle();
        contestBundle.putInt("contestId", contestId);
        contestBundle.putString("contestName", contestName);
        contestBundle.putString("contestType", contestType);
        logFbRevenue(price, contestBundle);

        /* Send Revenue Data to Aws Pinpoint */
        trackAWSMonetizationEvent(price, AnalyticsActions.CONTEST_JOINED, orderId);

        /* Send Revenue Data to Fabric */
        trackFabricPurchaseEvent(price, AnalyticsActions.CONTEST_JOINED, orderId);

        /* Send Revenue Data to FireBase */
        trackFireBasePurchaseEvent(price, contestName, orderId);

    }

    /**
     * Tracks Banners
     *
     * @param bannerKey
     * @param bannerName
     */
    public void trackBanners(String bannerKey, String bannerName) {
        if (!BuildConfig.DEBUG) {
            trackEvent(AnalyticsCategory.BANNERS, bannerKey, bannerName);
        }
    }

    /**
     * Tracks In App Notifications for match results
     *
     * @param notificationAction
     * @param contestName
     */
    public void trackInAppNotifications(String notificationAction, String contestName) {
        if (!BuildConfig.DEBUG) {
            trackEvent(AnalyticsCategory.MATCH_REMINDER, notificationAction, contestName);
        }
    }

    /**
     * Tracks POST SIGN UP
     *
     * @param screenName
     *
     */
    public void trackPostSignUp(String screenName) {
        if (!BuildConfig.DEBUG) {
            trackEvent(AnalyticsCategory.POST_SIGN_UP, "Stage", screenName);
        }
    }

    /**
     * Set FreshChat User Properties
     */
    public void setFreshChatUserProperties(Context context) {

        /* Set UserInfo  */
        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        if (userInfo != null) {
            FreshchatUser freshUser = Freshchat.getInstance(context).getUser();
            freshUser.setFirstName(userInfo.getUserName());
            String email = userInfo.getEmail();
            if (!TextUtils.isEmpty(email)) {
                freshUser.setEmail(userInfo.getEmail());
            }

            //Call setUser so that the user information is synced with Freshchat's servers
            Freshchat.getInstance(context).setUser(freshUser);

             /* To set external id : This should be an unique identifier for the user
                   This cannot be changed once set for the user */
            Freshchat.getInstance(context).identifyUser(String.valueOf(userInfo.getId()), null);

        }
    }

    /**
     * Set Crashlytics User Properties
     */
    public void setCrashlyticsUserProperties(Context context) {

        // Set up Crashlytics, disabled for debug builds
        if (!BuildConfig.DEBUG) {
            Crashlytics crashlyticsKit = new Crashlytics.Builder()
                    .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                    .build();

            if (crashlyticsKit != null && context != null) {
                try {
                    Fabric.with(context, crashlyticsKit);
                    /* Set UserInfo  */
                    UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
                    if (userInfo != null) {
                        Crashlytics.setUserIdentifier(String.valueOf(userInfo.getId()));
                        Crashlytics.setUserEmail(userInfo.getEmail());
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /******* METHODS TO TRACK EVENTS ON AWS PINPOINT ***************/

    public void trackAWSEvent(String eventName, String eventAttribute, String eventAttributeValue) {

        if (pinpointManager != null && pinpointManager.getAnalyticsClient() != null) {

            final AnalyticsEvent event = pinpointManager.getAnalyticsClient().createEvent(eventName)
                    .withAttribute(eventAttribute, eventAttributeValue);

            pinpointManager.getAnalyticsClient().recordEvent(event);
            pinpointManager.getAnalyticsClient().submitEvents();

        }
    }

    public void trackAWSMonetizationEvent(Double price, String productId, String transactionID) {

        if (pinpointManager != null && pinpointManager.getAnalyticsClient() != null) {

            final AnalyticsEvent event =
                    GooglePlayMonetizationEventBuilder.create(pinpointManager.getAnalyticsClient())
                            .withItemPrice(price)
                            .withProductId(productId)
                            .withQuantity(1.0)
                            .withProductId(transactionID).build();


            pinpointManager.getAnalyticsClient().recordEvent(event);
            pinpointManager.getAnalyticsClient().submitEvents();

        }
    }

    public void setAwsPinPointUserProperties() {
        UserInfo userInfo = Nostragamus.getInstance().getServerDataManager().getUserInfo();
        if (userInfo != null && pinpointManager != null && pinpointManager.getTargetingClient() != null) {

            EndpointProfile profile = this.pinpointManager.getTargetingClient().currentEndpoint();
            EndpointProfileUser user = new EndpointProfileUser();
            user.setUserId(String.valueOf(userInfo.getId()));
            profile.setUser(user);
            this.pinpointManager.getTargetingClient().updateEndpointProfile(profile);

        }
    }

    /******* END METHODS TO TRACK EVENTS ON AWS PINPOINT ***************/


    /******* METHODS TO TRACK EVENTS ON FABRIC ***************/

    public void trackFabricEvent(String eventName, String eventAttribute, String eventAttributeValue) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logCustom(new CustomEvent(eventName)
                    .putCustomAttribute(eventAttribute, eventAttributeValue));
        }
    }

    public void trackFabricPurchaseEvent(Double price, String productId, String transactionID) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logPurchase(new PurchaseEvent()
                    .putItemPrice(BigDecimal.valueOf(price))
                    .putCurrency(Currency.getInstance("INR"))
                    .putItemName(productId)
                    .putItemType("Contest")
                    .putItemId(transactionID)
                    .putSuccess(true));
        }
    }


    public void trackFabricContentRealTimeEvent(String contentName, String contentType, String contentId) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(contentName)
                    .putContentType(contentType)
                    .putContentId(contentId));
        }
    }


    public void trackFabricShareEvent(String method, String contentName, String contentType, String contentId) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logShare(new ShareEvent()
                    .putMethod(method)
                    .putContentName(contentName)
                    .putContentType(contentType)
                    .putContentId(contentId));
        }
    }

    public void trackInviteInFabric() {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logInvite(new InviteEvent()
                    .putMethod("AppInvite"));
        }
    }

    public void trackSignUpFabric(String source) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logSignUp(new SignUpEvent()
                    .putMethod(source)
                    .putSuccess(true));
        }
    }

    /******* END METHODS TO TRACK EVENTS ON FABRIC ***************/


    /******* METHODS TO TRACK EVENTS ON FACEBOOK ***************/

    /**
     * Logs Facebook events
     *
     * @param eventName
     * @param eventAttribute
     * @param eventAttributeValue
     */
    public void logFbEvents(String eventName, String eventAttribute, String eventAttributeValue) {
        Bundle args = new Bundle();
        args.putString(eventAttribute, eventAttributeValue);
        if (sFaceBookAppEventLogger != null) {
            sFaceBookAppEventLogger.logEvent(eventName, args);
        }
    }

    public void logFbUserProperties(UserInfo userInfo) {

        if (userInfo != null && userInfo.getUserPropertyInfo() != null) {

            Bundle user_props = new Bundle();

            Gson gson = new Gson();
            String json = gson.toJson(userInfo.getUserPropertyInfo());
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> map = gson.fromJson(json, type);
            for (String key : map.keySet()) {
                user_props.putString(key, String.valueOf(map.get(key)));
            }

            if (sFaceBookAppEventLogger != null) {

                sFaceBookAppEventLogger.setUserID(String.valueOf(userInfo.getId()));

                sFaceBookAppEventLogger.updateUserProperties(user_props, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                    }
                });
            }
        }

    }

    /**
     * Logs Facebook revenue event
     *
     * @param amount
     * @param args
     */
    public void logFbRevenue(double amount, @NonNull Bundle args) {
        if (sFaceBookAppEventLogger != null) {
            try {
                BigDecimal values = BigDecimal.valueOf(amount);
                sFaceBookAppEventLogger.logPurchase(values, Currency.getInstance(Constants.INDIAN_CURRENCY_CODE), args);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Logs Facebook Play completed event
     *
     * @param args
     */
    public void logFbPlayCompleted(@Nullable Bundle args) {
        if (sFaceBookAppEventLogger != null) {
            sFaceBookAppEventLogger.logEvent(Constants.FaceBookAnalyticsEvents.MATCH_PLAY_COMPLETED, args);
        }
    }


    /******* END METHODS TO TRACK EVENTS ON FACEBOOK **************/


    public void logFireBaseEvents(String eventName, String eventAttribute, String eventAttributeValue) {
        Bundle args = new Bundle();
        args.putString(eventAttribute, eventAttributeValue);
        if (mFirebaseAnalytics != null) {
            mFirebaseAnalytics.logEvent(eventName.replaceAll("\\s+", ""), args);
        }
    }


    public void logFireBaseUserProperties(UserInfo userInfo) {

        if (userInfo != null && mFirebaseAnalytics != null) {
            mFirebaseAnalytics.setUserProperty(UserProperties.COUNT_REFERRALS, String.valueOf(userInfo.getReferralCount()));
            mFirebaseAnalytics.setUserProperty(UserProperties.HAS_REFERRED, String.valueOf(userInfo.isHasReferred()));
            mFirebaseAnalytics.setUserProperty(UserProperties.HAS_DEPOSITED, String.valueOf(userInfo.isHasDeposited()));
            mFirebaseAnalytics.setUserProperty(UserProperties.COUNT_DEPOSITS, String.valueOf(userInfo.getDepositCount()));
            mFirebaseAnalytics.setUserProperty(UserProperties.COUNT_CONTESTS_JOINED, String.valueOf(userInfo.getContestJoinedCount()));
            mFirebaseAnalytics.setUserProperty(UserProperties.PAID_CONTESTS_JOINED, String.valueOf(userInfo.getPaidContestJoinedCount()));
            mFirebaseAnalytics.setUserProperty(UserProperties.MOST_PLAYED_SPORT, userInfo.getMostPlayedSport());
        }
    }


    private void trackFireBasePurchaseEvent(double price, String contestName, String orderId) {
        Bundle args = new Bundle();
        args.putString("currency", "INR");
        args.putDouble("value", price);
        args.putString("transaction_id", orderId);

        if (mFirebaseAnalytics != null) {
            mFirebaseAnalytics.logEvent("ecommerce_purchase", args);
        }
    }


    /******* tracking events on AWS , FABRIC , FACEBOOK , FIREBASE ***************/
    public void trackEvent(String eventName, String eventAttribute, String eventAttributeValue) {
        trackAWSEvent(eventName, eventAttribute, eventAttributeValue);
        trackFabricEvent(eventName, eventAttribute, eventAttributeValue);
        logFbEvents(eventName, eventAttribute, eventAttributeValue);
        logFireBaseEvents(eventName, eventAttribute, eventAttributeValue);
    }

    public void logUserProperties(UserInfo userInfo) {

        /* log FB User Properties */
        logFbUserProperties(userInfo);

        /* log FireBase User Properties */
        logFireBaseUserProperties(userInfo);

        /* log Aws Pinpoint Endpoints */
        if (getIdentityManager() != null) {

            final AWSCredentialsProvider cp = getIdentityManager().getCredentialsProvider();
            AmazonPinpointClient client = new AmazonPinpointClient(cp);
            SMSEndPointRequest.getInstance().createEndpoint(client, AWS_APP_ID);
            EMAILEndPointRequest.getInstance().createEndpoint(client, AWS_APP_ID);

            if (TextUtils.isEmpty(Nostragamus.getInstance().getServerDataManager().getGcmDeviceToken())) {
                NostraFireBaseInstanceIDService.getInstance().onTokenRefresh();
                NotificationEndPointRequest.getInstance().createEndpoint(client, AWS_APP_ID);
            } else {
                NotificationEndPointRequest.getInstance().createEndpoint(client, AWS_APP_ID);
            }
        }

    }

}