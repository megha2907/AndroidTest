package in.sportscafe.nostragamus.module.analytics;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.amplitude.api.Amplitude;
import com.amplitude.api.AmplitudeClient;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jeeva.android.ExceptionTracker;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import in.sportscafe.nostragamus.BuildConfig;
import in.sportscafe.nostragamus.Constants.AnalyticsActions;
import in.sportscafe.nostragamus.Constants.AnalyticsCategory;
import in.sportscafe.nostragamus.Constants.AnalyticsLabels;
import in.sportscafe.nostragamus.Constants.UserProperties;
import in.sportscafe.nostragamus.R;


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

    private boolean mAppOpeningTracked = false;

    public NostragamusAnalytics init(Context context, boolean optOut) {
        GoogleAnalytics ga = GoogleAnalytics.getInstance(context);
        ga.setAppOptOut(optOut);

        if (!optOut) {

            // Initializing the google analytics
            this.mTracker = ga.newTracker(R.xml.app_tracker);

            // Initializing the MoEngage
            this.mMoEHelper = MoEHelper.getInstance(context);

            // Initializing the Amplitude
            this.mAmplitude = Amplitude.getInstance().initialize(context, context.getString(R.string.amplitude_api_key))
                    .enableForegroundTracking((Application) context);

            // Tracking flavor
            trackFlavor();
        }

        return this;
    }

    /**
     * track app opening
     *
     * @param openingFrom - launcher, notification
     */
    public void trackAppOpening(String openingFrom) {
        if(!mAppOpeningTracked) {
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
     *
     * @param actions - Tournament, group, challenges and sports
     */
    public void trackLeaderboard(String label) {
        track(AnalyticsCategory.LEADERBOARD, AnalyticsActions.LB_DETAIL, label, null);
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
     * @param type - tabs
     * @param name - Sports, Groups
     */
    public void trackMyProfile(String name) {
        track(AnalyticsCategory.PROFILE, AnalyticsActions.MYSELF, name, null);
    }

    /**
     * track user profile
     *
     * @param type - tabs
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
        mMoEHelper.trackEvent(category, jsonObject);

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

    public void setUserProperties(int groupCount) {
        if (null != mAmplitude) {
            try {
                JSONObject userProperties = new JSONObject();
                userProperties.put(UserProperties.NUMBER_OF_GROUPS, groupCount);
                mAmplitude.setUserProperties(userProperties);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}