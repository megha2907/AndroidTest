package in.sportscafe.nostragamus;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;
import com.jeeva.android.facebook.FacebookHandler;
import com.jeeva.android.facebook.user.FacebookPermission;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.customfont.CustomFont;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.utils.MoEHelperConstants;
import com.moengage.push.PushManager;

import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import in.sportscafe.nostragamus.db.tableDto.ServerTimeDbDto;
import in.sportscafe.nostragamus.module.analytics.NostragamusAnalytics;
import in.sportscafe.nostragamus.module.crash.NostragamusUncaughtExceptionHandler;
import in.sportscafe.nostragamus.module.getstart.GetStartActivity;
import in.sportscafe.nostragamus.module.notifications.NotificationPushMessageListener;
import in.sportscafe.nostragamus.module.settings.app.AppSettingsModelImpl;
import in.sportscafe.nostragamus.module.user.login.RefreshTokenModelImpl;
import in.sportscafe.nostragamus.webservice.MyWebService;
import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Jeeva on 14/3/16.
 */
public class Nostragamus extends Application {

    private static Nostragamus sNostragamus;
    private ServerDataManager mServerDataManager;

    private static long serverTimeMillis;
    private static long systemTimeMillis;

    private void initMembers() {
        sNostragamus = Nostragamus.this;

        mServerDataManager = new ServerDataManager();
    }

    public static synchronized Nostragamus getInstance() {
        return sNostragamus;
    }

    /**
     * Used for app-session data tracking (Fetched from server)
     * <p>
     * Maintains references to all freshly/recently fetched server data.
     *
     * @return
     */
    public synchronized ServerDataManager getServerDataManager() {
        if (mServerDataManager == null) {
            mServerDataManager = new ServerDataManager();
        }
        return mServerDataManager;
    }

    private static final long ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;

    private String FRESHCHAT_APP_KEY = "982a3e04-e097-41f3-a86d-6298c6d00dc2";
    private String FRESHCHAT_APP_ID = "bace0b4a-ceca-4a37-989d-e5e1155ffc6e";


    @Override
    public void onCreate() {
        super.onCreate();

        initMembers();

        // Initializing the SportsCafe Uncaught Exception handler
        initCrashHandler();

        // Initializing all the data handlers
        initDataHandlers();

        // Initializing MyWebService
        MyWebService.getInstance().init();

        // Instantiating the volley
        Volley.getInstance().initVolley(getApplicationContext());

        // Initializing custom fonts
        initCustomFonts();

        // Here we are helping moEngage to track install/update count
        doInstallOrUpdateChanges();

        //For customizing the notification which is received from mo-engage
        PushManager.getInstance().setMessageListener(new NotificationPushMessageListener());

        //Enable cookie matching
        Branch.enableCookieBasedMatching("http://nostragamus.in/");

        // Initializing the Branch
        Branch.getAutoInstance(this);

        // Initializing FireBase
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId(getString(R.string.firebase_app_id)) // Required for Analytics.
                .setApiKey(getString(R.string.firebase_api_key)) // Required for Auth.
                .setGcmSenderId(getString(R.string.firebase_gcm_sender_id))
                .setDatabaseUrl(getString(R.string.firebase_url)) // Required for RTDB.
                .build();
        FirebaseApp.initializeApp(this, options, "in.sportscafe.nostragamus${buildTypeSuffix}");

        // Initializing the FreshChat
        FreshchatConfig freshchatConfig=new FreshchatConfig(FRESHCHAT_APP_ID,FRESHCHAT_APP_KEY);
        freshchatConfig.setCameraCaptureEnabled(true);
        freshchatConfig.setGallerySelectionEnabled(true);
        Freshchat.getInstance(getApplicationContext()).init(freshchatConfig);

        // Moengage - Opt-out from Location Tracking and GeoFence
        MoEHelper.getInstance(getApplicationContext()).optOutOfLocationTracking(true);
        MoEHelper.getInstance(getApplicationContext()).optOutOfGeoFences(true);

        // Initializing the SportsCafe analytics
        NostragamusAnalytics.getInstance().init(getApplicationContext()).autoTrack(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (BuildConfig.DEBUG) {
            MultiDex.install(this);
        }
    }

    private void initCrashHandler() {

        // It is to show the crash page, If the application is crashed
        Thread.setDefaultUncaughtExceptionHandler(new NostragamusUncaughtExceptionHandler());

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
    }

    private void initDataHandlers() {
        Context context = getApplicationContext();
        NostragamusDataHandler.getInstance().init(context);
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

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/lato/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    private void doInstallOrUpdateChanges() {
        MoEHelper moEHelper = MoEHelper.getInstance(getApplicationContext());
        moEHelper.setExistingUser(!NostragamusDataHandler.getInstance().isFirstTimeUser());

        /*NostragamusDataHandler dataHandler = NostragamusDataHandler.getInstance();

        int previousAppVersionCode = dataHandler.getPreviousAppVersionCode();
        int currentAppVersionCode = getAppVersionCode();
        if (previousAppVersionCode != currentAppVersionCode) {
            // previousAppVersionCode = 0 is new user
            MoEHelper moEHelper = MoEHelper.getInstance(getApplicationContext());
            moEHelper.setExistingUser(dataHandler.isFirstTimeUser());
            dataHandler.setPreviousAppVersionCode(currentAppVersionCode);

        }*/
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
        // This method is only called when running on Emulated env, not on real device
        removeAllInstances();
        super.onTerminate();
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
        if (expiryTimeInMs > 0) {
            if (Calendar.getInstance().getTimeInMillis() >= expiryTimeInMs - ONE_HOUR_IN_MILLIS) {
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
        Freshchat.resetUser(getApplicationContext());
    }

    private void navigateToLogIn() {
        Intent intent = new Intent(getApplicationContext(), GetStartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * @return IMEI of device or empty
     */
    public String getDeviceImeI() {
        String imeiStr = "";
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            /*
             * getDeviceId() function Returns the unique device ID.
             * for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
             */
            imeiStr = telephonyManager.getDeviceId();

           /*
            * getSubscriberId() function Returns the unique subscriber ID,
            * for example, the IMSI for a GSM phone.
            *
            * imsiStr = telephonyManager.getSubscriberId();
            */

            Log.d("Device-Id", "IMEI : " + imeiStr);
        }
        return imeiStr;
    }

    /**
     * NOTE: Android Id can be regenerated ove Factory data-reset
     *
     * @return Android-Id for Device/Os OR empty
     */
    public String getAndroidId() {
        String androidId = "";
        if (getContentResolver() != null) {
            androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.d("Device-Id", "Android-Id : " + androidId);
        }
        return androidId;
    }

    public String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }

    public float getScreenDetails(int screenDetails) {
        float value = 0;
        try {
            switch (screenDetails) {
                case Constants.ScreenDetails.DPI:
                    value = getResources().getDisplayMetrics().densityDpi;
                    break;

                case Constants.ScreenDetails.HEIGHT:
                    value = getResources().getDisplayMetrics().heightPixels;
                    break;

                case Constants.ScreenDetails.WIDTH:
                    value = getResources().getDisplayMetrics().widthPixels;
                    break;
            }
        } catch (Exception e) {
        }
        return value;
    }

    public long getDeviceTotalRam() {
        long totalRam = 0;
        try {
            ActivityManager actManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            actManager.getMemoryInfo(memInfo);
            totalRam = memInfo.totalMem;
        } catch (Exception e) {
        }
        return totalRam;
    }

    public String getUserAccounts() {
        String accountStr = "";
        try {
            AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
            Account[] list = manager.getAccounts();
            for (Account account : list) {
                accountStr = accountStr + account.name + ", ";
            }
        } catch (Exception e) {
        }
        return accountStr;
    }

    @NonNull
    public String getAppTypeFlavor() {
        String flavor;
        if (BuildConfig.IS_ACL_VERSION) {
            flavor = Constants.AppType.ACL;
        } else if (BuildConfig.IS_PAID_VERSION) {
            flavor = Constants.AppType.PRO;
        } else {
            flavor = Constants.AppType.PLAYSTORE;
        }
        return flavor;
    }

    /**
     * Keeps reference for serverTime & localTime when system received it.
     *
     * @param serverTime
     */
    public synchronized void setServerTime(long serverTime) {
        serverTimeMillis = serverTime;
        systemTimeMillis = System.currentTimeMillis();
    }

    /**
     * NOTE: DO NOT MAKE ANY KIND OF CHANGE IN THIS FUNCTION. IT CAN RESULT INTO IMPROPER OUTPUT
     *
     * @return servertime - updated locally after set once
     */
    public synchronized long getServerTime() {
        long elapsedSystemTime = System.currentTimeMillis() - systemTimeMillis;     // get elapsed time from current local time to last operation
        systemTimeMillis = System.currentTimeMillis();                              // Update system time (local time updated ultimately)
        serverTimeMillis = serverTimeMillis + elapsedSystemTime;                    // add elapsed local time to serverTime (serverTime updated ultimately)
        return serverTimeMillis;
    }

    /**
     * Server time is CACHED, SAVED in DB. It's used for InApp-Notification ;
     * to send notification offline, to identify approx server time
     * @param serverTimeDbDto
     * @return
     */
    public synchronized long getApproxTimeBasedOnSavedServerTime(ServerTimeDbDto serverTimeDbDto) {
        long approxServerTime = 0;

        if (serverTimeDbDto != null) {
            long timeGone = SystemClock.elapsedRealtime() - serverTimeDbDto.getSystemElapsedRealTime();
            approxServerTime = serverTimeDbDto.getServerTime() + timeGone;
        }

        return approxServerTime;
    }
}