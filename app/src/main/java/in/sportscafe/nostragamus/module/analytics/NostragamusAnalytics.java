package in.sportscafe.nostragamus.module.analytics;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.amplitude.api.Amplitude;
import com.amplitude.api.AmplitudeClient;
import com.amplitude.api.Revenue;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatUser;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jeeva.android.Log;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
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
import in.sportscafe.nostragamus.module.newChallenges.dataProvider.SportsDataProvider;
import in.sportscafe.nostragamus.module.newChallenges.dto.SportsTab;
import in.sportscafe.nostragamus.module.user.login.dto.UserInfo;

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

    public NostragamusAnalytics init(Context context) {
        GoogleAnalytics ga = GoogleAnalytics.getInstance(context);
        ga.setAppOptOut(BuildConfig.DEBUG);

        if (!BuildConfig.DEBUG) {

            // Initializing the google analytics
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

            // Facebook Analytics
            FacebookSdk.sdkInitialize(context);
            sFaceBookAppEventLogger = AppEventsLogger.newLogger(context);
        }

        return this;
    }


    /**
     * track app opening
     *
     * @param openingFrom - launcher, notification
     */
    public void trackAppOpening(String openingFrom) {
        if (!mAppOpeningTracked) {
            track(AnalyticsCategory.APP_OPENING, null, openingFrom, null);
            mAppOpeningTracked = true;
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
        track(AnalyticsCategory.LOGIN, actions, loginVia, null);
    }

    /**
     * track logout
     */
    public void trackLogOut() {
        track(AnalyticsCategory.LOGOUT, null, null, null);
    }

    /**
     * track dummy game start and end
     *
     * @param actions - Started, Done
     */
    public void trackDummyGame(String actions) {
        track(AnalyticsCategory.DUMMY_GAME, actions, null, null);
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
        track(AnalyticsCategory.NEW_USERS, actions, label, null);
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
        track(AnalyticsCategory.ONBOARDING_TIME, actions, label, timeSpentInS);
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
        if (null != mAmplitude) {
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
        if (null != mMoEHelper) {

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
        if (BuildConfig.IS_PAID_VERSION) {

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
        if (mMoEHelper != null) {
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
        track(category, AnalyticsActions.CLICKED, label, null);
    }

    /**
     * Tracks screen Views
     *
     * @param category
     */
    public void trackScreenShown(@NonNull String category, String label) {
        track(category, AnalyticsActions.OPENED, label, null);
    }

    public void trackReferralBenefitScreenShown() {
        track(AnalyticsCategory.REFERRAL_BENEFIT, AnalyticsActions.OPENED, AnalyticsLabels.SCREENS_SEEN, null);
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

    /**
     * Tracks Challenges Opened
     *
     * @param challengeId
     * @param sportId
     * @param category
     */
    public void trackNewChallenges(int challengeId, String challengeName, int[] sportId, String category) {

        if (BuildConfig.IS_PAID_VERSION && mAmplitude != null) {

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

        } else {
            Log.d("App", "Can't log revenue, Amplitude null!");
        }
    }


    /**
     * Tracks Contest Joined
     *
     * @param contestId
     * @param contestName
     * @param contestType
     * @param entryFee
     * @param challengeId
     * @param screenName
     */
    public void trackContestJoined(int contestId, String contestName, String contestType, int entryFee, int challengeId, String screenName) {

        if (BuildConfig.IS_PAID_VERSION && mAmplitude != null) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("contestId", contestId);
                jsonObject.put("contestName", contestName);
                jsonObject.put("contestType", contestType);
                jsonObject.put("contestEntryFee", entryFee);
                jsonObject.put("contestChallengeId", challengeId);
                jsonObject.put("screenJoinedFrom", screenName);
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
    public void trackRevenue(double price, int contestId, String contestName, String contestType) {
        if (BuildConfig.IS_PAID_VERSION && mAmplitude != null) {
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
        NostragamusAnalytics.getInstance().logFbRevenue(price, contestBundle);

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

                /*  For Later Use
                /* Set any custom metadata to give agents more context, and for segmentation for marketing or pro-active messaging
                Map<String, String> userMeta = new HashMap<String, String>();
                userMeta.put("userLoginType", "Facebook");
                userMeta.put("city", "SpringField"); */

//                //Call setUser so that the user information is synced with Freshchat's servers
//                mFreshChat.setUser(freshUser);


        }
    }


}