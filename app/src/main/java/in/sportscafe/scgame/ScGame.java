package in.sportscafe.scgame;


import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.facebook.FacebookHandler;
import com.jeeva.android.facebook.user.FacebookPermission;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.customfont.CustomFont;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.utils.MoEHelperConstants;
import com.moengage.addon.inbox.InboxManager;
import com.moengage.push.PushManager;

import java.util.Arrays;
import java.util.List;

import in.sportscafe.scgame.module.analytics.ScGameAnalytics;
import in.sportscafe.scgame.module.crash.ScGameUncaughtExceptionHandler;
import in.sportscafe.scgame.module.notifications.NotificationCustom;
import in.sportscafe.scgame.module.notifications.NotificationInboxAdapter;
import in.sportscafe.scgame.module.offline.PredictionDataHandler;
import in.sportscafe.scgame.webservice.MyWebService;
import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;

/**
 * Created by Jeeva on 14/3/16.
 */
public class ScGame extends Application {

    private static final boolean mDebuggable = BuildConfig.DEBUG;

    private static ScGame sScGame;

    public static ScGame getInstance() {
        return sScGame;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Assigning the ScGame instance
        sScGame = ScGame.this;

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
        ScGameAnalytics.getInstance().init(getApplicationContext(), mDebuggable).autoTrack(this);

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

        if(mDebuggable) {
            MultiDex.install(this);
        }
    }

    private void initCrashHandler(boolean debuggable) {

        // It is to show the crash page, If the application is crashed
        Thread.setDefaultUncaughtExceptionHandler(new ScGameUncaughtExceptionHandler());

        if(!debuggable) {
            // Initializing the Crashlytics using fabric
           // Fabric.with(this, new Kit[] {new Crashlytics()});
        }
    }

    private void initDataHandlers() {
        Context context = getApplicationContext();
        ScGameDataHandler.getInstance().init(context);
        PredictionDataHandler.getInstance().init(context);
    }

    private void initCustomFonts() {
        CustomFont.getInstance().init(
                "fonts/roboto/Roboto-Light.ttf", "fonts/roboto/Roboto-LightItalic.ttf",
                "fonts/roboto/Roboto-Regular.ttf", "fonts/roboto/RobotoCondensed-Regular.ttf",
                "fonts/roboto/RobotoCondensed-Bold.ttf", "fonts/roboto/Roboto-Medium.ttf",
                "fonts/lato/Lato-Bold.ttf", "fonts/lato/Lato-Hairline.ttf",
                "fonts/lato/Lato-Heavy.ttf", "fonts/lato/Lato-Light.ttf", "fonts/lato/Lato-Medium.ttf",
                "fonts/lato/Lato-Regular.ttf", "fonts/lato/Lato-Semibold.ttf", "fonts/lato/Lato-Thin.ttf"
        );
    }

    private void doInstallOrUpdateChanges() {
        ScGameDataHandler dataHandler = ScGameDataHandler.getInstance();

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
        sScGame = null;
    }

    public void clearAllCredentialsAndData() {
        FacebookHandler.getInstance(this).logOut(); // logout facebook

        ScGameDataHandler.getInstance().clearAll(); // clear all shared preference data

        Volley.getInstance().invalidateAll();
    }

}