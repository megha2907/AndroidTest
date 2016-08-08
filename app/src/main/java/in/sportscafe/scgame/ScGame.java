package in.sportscafe.scgame;


import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;

import com.jeeva.android.facebook.FacebookHandler;
import com.jeeva.android.facebook.user.FacebookPermission;
import com.jeeva.android.volley.Volley;
import com.jeeva.android.widgets.customfont.CustomFont;
import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.utils.MoEHelperConstants;
import com.moengage.push.PushManager;

import java.util.Arrays;
import java.util.List;

import in.sportscafe.scgame.module.notifications.CustomPushNotification;
import in.sportscafe.scgame.module.offline.PredictionDataHandler;
import in.sportscafe.scgame.webservice.MyWebService;

/**
 * Created by Jeeva on 14/3/16.
 */
public class ScGame extends Application {

    private static ScGame sScGame;

    public static ScGame getInstance() {
        return sScGame;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Assigning the ScGame instance
        sScGame = ScGame.this;
        initCustomFonts();
        initDataHandlers();
        // Initializing MyWebService
        MyWebService.getInstance().init();
        // Instantiating the volley
        Volley.getInstance().initVolley(getApplicationContext());
        sendInstallOrUpdateToMoEngage();
        //Moengage custom Notification
        PushManager.getInstance(getApplicationContext()).setMessageListener(new CustomPushNotification());

    }

    /**
     * Here we are helping moEngage to track install/update count
     */
    private void sendInstallOrUpdateToMoEngage() {
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * Initializing custom fonts
     */
    private void initCustomFonts() {
        CustomFont.getInstance().init(
                "fonts/montserrat/Montserrat-UltraLight.otf", "fonts/montserrat/Montserrat-Regular.otf",
                "fonts/montserrat/Montserrat-SemiBold.otf", "fonts/roboto/Roboto-Light.ttf",
                "fonts/roboto/Roboto-LightItalic.ttf", "fonts/roboto/Roboto-Regular.ttf",
                "fonts/roboto/RobotoCondensed-Regular.ttf", "fonts/roboto/RobotoCondensed-Bold.ttf",
                "fonts/roboto/Roboto-Medium.ttf",
                "fonts/georgia/Georgia.ttf", "fonts/georgia/Georgia-Bold.ttf"
        );
    }

    /**
     * Initializing all the data handlers
     */
    private void initDataHandlers() {
        Context context = getApplicationContext();
        ScGameDataHandler.getInstance().init(context);
        PredictionDataHandler.getInstance().init(context);
    }

    public int getAppVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getAppVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public TelephonyManager getTelephonyManager() {
        return (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    }

    public String getDeviceId() {
        String myAndroidDeviceId = getTelephonyManager().getDeviceId();
        if (myAndroidDeviceId != null) {
            return myAndroidDeviceId;
        } else {
            return Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }

    /**
     * Checking the internet connectivity
     *
     * @return true if the connection is available otherwise false
     */
    public boolean hasNetworkConnection() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean valid = false;

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnectedOrConnecting()) {
            valid = true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnectedOrConnecting()) {
            valid = true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            valid = true;
        }

        return valid;
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