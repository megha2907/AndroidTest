package in.sportscafe.nostragamus;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.util.Base64;

import com.crashlytics.android.Crashlytics;
import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;
import com.jeeva.android.facebook.FacebookHandler;
import com.jeeva.android.facebook.user.FacebookPermission;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.customfont.CustomFont;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.utils.MoEHelperConstants;
import com.moengage.addon.inbox.InboxManager;
import com.moengage.push.PushManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.crash.NostragamusUncaughtExceptionHandler;
import in.sportscafe.nostragamus.module.getstart.GetStartActivity;
import in.sportscafe.nostragamus.module.notifications.NotificationCustom;
import in.sportscafe.nostragamus.module.notifications.NotificationInboxAdapter;
import in.sportscafe.nostragamus.module.offline.PredictionDataHandler;
import in.sportscafe.nostragamus.module.settings.app.AppSettingsModelImpl;
import in.sportscafe.nostragamus.module.user.login.RefreshTokenModelImpl;
import in.sportscafe.nostragamus.webservice.MyWebService;
import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Jeeva on 14/3/16.
 */
public class Nostragamus extends Application {

    private static final boolean mDebuggable = BuildConfig.DEBUG;

    private static Nostragamus sNostragamus;

    public static Nostragamus getInstance() {
        return sNostragamus;
    }

    private static final long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();

        // Assigning the Nostragamus instance
        sNostragamus = Nostragamus.this;

        // Initializing the SportsCafe Uncaught Exception handler
        initCrashHandler(mDebuggable);

        // Initializing all the data handlers
        initDataHandlers();

        // Initializing MyWebService
        MyWebService.getInstance().init();

        // Instantiating the volley
        Volley.getInstance().initVolley(getApplicationContext());

        // Initializing custom fonts
        initCustomFonts();

        // Initializing the SportsCafe analytics
        NostragamusAnalytics.getInstance().init(getApplicationContext(), mDebuggable).autoTrack(this);

        // Here we are helping moEngage to track install/update count
        doInstallOrUpdateChanges();

        //For customizing the notification which is received from mo-engage
        PushManager.getInstance().setMessageListener(new NotificationCustom());

        // For notification inbox
        InboxManager.getInstance().setInboxAdapter(new NotificationInboxAdapter());

        // Initializing the Branch
        Branch.getAutoInstance(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

//        if(mDebuggable) {
            MultiDex.install(this);
//        }
    }

    private void initCrashHandler(boolean debuggable) {

        // It is to show the crash page, If the application is crashed
        Thread.setDefaultUncaughtExceptionHandler(new NostragamusUncaughtExceptionHandler());

        if (!debuggable) {
            // Initializing the Crashlytics using fabric
            Fabric.with(this, new Crashlytics());
        }
    }

    private void initDataHandlers() {
        Context context = getApplicationContext();
        NostragamusDataHandler.getInstance().init(context);
        PredictionDataHandler.getInstance().init(context);
    }

    private void initCustomFonts() {
        CustomFont.getInstance().init(
                "fonts/roboto/Roboto-Light.ttf",
                "fonts/roboto/Roboto-Regular.ttf", "fonts/roboto/RobotoCondensed-Regular.ttf",
                "fonts/roboto/RobotoCondensed-Bold.ttf",
                "fonts/lato/Lato-Bold.ttf", "fonts/lato/Lato-Hairline.ttf",
                "fonts/lato/Lato-Light.ttf", "fonts/lato/Lato-Regular.ttf",
                "fonts/lato/Lato-Black.ttf"
        );
    }

    private void doInstallOrUpdateChanges() {
        NostragamusDataHandler dataHandler = NostragamusDataHandler.getInstance();

        int previousAppVersionCode = dataHandler.getPreviousAppVersionCode();
        int currentAppVersionCode = getAppVersionCode();
        if (previousAppVersionCode != currentAppVersionCode) {
            // previousAppVersionCode = 0 is new user
            MoEHelper moEHelper = MoEHelper.getInstance(getApplicationContext());
            moEHelper.setExistingUser(previousAppVersionCode != 0);
            dataHandler.setPreviousAppVersionCode(currentAppVersionCode);

        }
    }

    public void setUserEmail(String email) {
        MoEHelper.getInstance(this).setUserAttribute(MoEHelperConstants.USER_ATTRIBUTE_USER_EMAIL, email);
    }

    public List<String> getRequiredPermissionListFromFacebook() {
        return Arrays.asList(FacebookPermission.EMAIL);
    }

    public int getAppVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            ExceptionTracker.track(e);
        }
        return -1;
    }

    public String getAppVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            ExceptionTracker.track(e);
        }
        return "";
    }

    /**
     * Checking the internet connectivity
     *
     * @return true if the connection is available otherwise false
     */
    public boolean hasNetworkConnection() {
        NetworkInfo activeNetwork = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // It is to clear all the hold memory after termination
        removeAllInstances();
    }

    private void removeAllInstances() {
        sNostragamus = null;
    }

    public void clearAllCredentialsAndData() {
        FacebookHandler.getInstance(this).logOut(); // logout facebook

        NostragamusDataHandler.getInstance().clearAll(); // clear all shared preference data

        Volley.getInstance().invalidateAll();
    }

    /**
     * Periodic Api hits to get the updated data's
     */

    public void startPeriodJobs() {

        Long expiryTimeInMs = NostragamusDataHandler.getInstance().getTokenExpiry();
        if(expiryTimeInMs > 0) {
            if(Calendar.getInstance().getTimeInMillis() >= expiryTimeInMs - ONE_HOUR_IN_MILLIS) {
                RefreshTokenModelImpl.newInstance().refreshToken();
            }
        }

        // Getting app settings
        AppSettingsModelImpl.newInstance().getAppSettings();
    }

    public void logout() {
            NostragamusDataHandler.getInstance().clearAll();
            navigateToLogIn();
            NostragamusAnalytics.getInstance().trackLogOut();
            MoEHelper.getInstance(getApplicationContext()).logoutUser();
        }

    private void navigateToLogIn() {
        Intent intent = new Intent(getApplicationContext(), GetStartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}