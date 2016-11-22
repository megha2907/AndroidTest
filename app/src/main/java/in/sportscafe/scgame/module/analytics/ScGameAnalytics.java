package in.sportscafe.scgame.module.analytics;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;

import java.util.Map;

import in.sportscafe.scgame.BuildConfig;
import in.sportscafe.scgame.Constants.AnalyticsCategory;
import in.sportscafe.scgame.R;


/**
 * Created by deepanshu on 8/8/16.
 */
public class ScGameAnalytics {

    private static String ACTION = "action";

    private static String LABEL = "label";

    private static String VALUE = "value";

    private static ScGameAnalytics sSportsCafeAnalytics = new ScGameAnalytics();

    public static ScGameAnalytics getInstance() {
        return sSportsCafeAnalytics;
    }

    private ScGameAnalytics() {
    }

    private Tracker mTracker;

    private MoEHelper mMoEHelper;

    public ScGameAnalytics init(Context context, boolean optOut) {
        GoogleAnalytics ga = GoogleAnalytics.getInstance(context);
        ga.setAppOptOut(optOut);

        if(!optOut) {

            // Initializing the google analytics
            this.mTracker = ga.newTracker(R.xml.app_tracker);

            // Initializing the MoEngage
            this.mMoEHelper = MoEHelper.getInstance(context);

            // Tracking flavor
            trackFlavor();
        }

        return this;
    }

    /**
     * track login options which the user is using
     *
     * @param loginVia - Google Plus, Facebook
     */
    public void trackLogIn(String loginVia) {
        track(AnalyticsCategory.LOGIN, loginVia, null, null);
    }

    /**
     * track logout
     *
     */
    public void trackLogOut() {
        track(AnalyticsCategory.LOGOUT, null, null, null);
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
    public void trackUserProfile(String type, String name) {
        track(AnalyticsCategory.USER_PROFILE, type, name, null);
    }

    /**
     * track new group
     *
     * @param type - Photo
     * @param name - Gallery, Camera
     */
    public void trackNewGroup(String type, String name) {
        track(AnalyticsCategory.NEW_GROUP, type, name, null);
    }

    /**
     * track new group
     *
     * @param type - Tournament
     * @param name - India vs England 2016
     */
    public void trackFeed(String type, String name) {
        track(AnalyticsCategory.FEED, type, name, null);
    }

    /**
     * track normal updates
     *
     * @param updateType - normal or force
     * @param value    - 0 or 1
     */
    public void trackUpdate(String updateType, long value) {
        track(AnalyticsCategory.APP_UPDATE, updateType, null, value);
    }

    public void startFragmentTrack(Activity activity, String fragmentName) {
        if(null != mMoEHelper) {
            mMoEHelper.onFragmentStart(activity, fragmentName);

            // It is enough to track only the fragment start through GA, no need to track fragment stop
            mTracker.setScreenName(fragmentName);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public void stopFragmentTrack(Activity activity, String fragmentName) {
        if(null != mMoEHelper) {
            mMoEHelper.onFragmentStop(activity, fragmentName);
        }
    }

    private void track(String category, String action, String label, Long value) {
        if(null == mMoEHelper) {
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

            // '_' prefix added because moEngage not supporting "action" keyword
            moeEventBuilder.putAttrString("_" + ACTION, action);
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

        mTracker.send(gaEventBuilder.build());
        mMoEHelper.trackEvent(category, moeEventBuilder.build());
    }

    public void trackOtherEvents(String category, Map<String, String> values) {
        if(null == mMoEHelper) {
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
        if(!BuildConfig.FLAVOR.equalsIgnoreCase("production")) {
            track(AnalyticsCategory.FLAVOR, null, BuildConfig.FLAVOR, null);
        }
    }

    public void autoTrack(Application application) {
        if(null != mMoEHelper) {
            mMoEHelper.autoIntegrate(application);
        }
    }
}